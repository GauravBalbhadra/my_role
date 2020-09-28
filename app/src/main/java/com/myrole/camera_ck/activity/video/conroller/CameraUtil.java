package com.myrole.camera_ck.activity.video.conroller;


import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

import com.google.common.base.Preconditions;
import com.myrole.camera_ck.activity.video.common.ImageFit;
import com.myrole.camera_ck.activity.video.common.ImageScale;
import com.myrole.camera_ck.activity.video.common.ImageSize;

import java.util.List;

/**
 * Utils for camera controllers.
 */
@SuppressWarnings("deprecation")
public final class CameraUtil {


    /**
     * Iterate over supported camera preview sizes to see which one best fits the
     * dimensions of the given view while maintaining the aspect ratio. If none can,
     * be lenient with the aspect ratio.
     *
     * @param sizes Supported camera preview sizes.
     * @param w     The width of the view.
     * @param h     The height of the view.
     * @return Best match camera preview size to fit in the view.
     */
    public static ImageSize getOptimalSize(List<Camera.Size> sizes, int w, int h) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;

        // Start with max value and refine as we iterate over available preview sizes. This is the
        // minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

        // Target view height
        int targetHeight = h;

        // Try to find a preview size that matches aspect ratio and the target view size.
        // Iterate over all available sizes and pick the largest size that can fit in the view and
        // still maintain the aspect ratio.
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find preview size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return new ImageSize(optimalSize.width,optimalSize.height);
    }

    public static int getContextRotation(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_0:
            default:
                return 0;
        }
    }

    public static boolean isLandscapeAngle(int orientationDegrees) {
        return (orientationDegrees > 45 && orientationDegrees < 135)
                || (orientationDegrees > 225 && orientationDegrees < 315);
    }

    public static boolean isContextConfigurationLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     *  Finds the best supported camera preview size for the target size.
     *  Target size must have at least one dimension specified and should be rotated into the same
     *  orientation as the preview sizes.
     */
    public static ImageSize getBestResolution(List<ImageSize> previewSizes,
                                              ImageSize targetSize, ImageFit imageFit, ImageScale imageScale) {
        Preconditions.checkState(targetSize.isAtLeastOneDimensionDefined());
        ImageSize bestSize = null;
        float bestScore = 0;
        for (ImageSize size : previewSizes) {
            float score = calculateCameraSizeScore(size, targetSize, imageFit, imageScale);
            if (bestSize == null || score > bestScore) {
                bestSize = size;
                bestScore = score;
            }
        }
        return bestSize;
    }

    /**
     * Calculate a score for how good a camera size is for recording the desired target size based
     * on these goals:
     * <ul>
     * <li> Increase recorded image pixels (that aren't scaled up) </li>
     * <li> Decrease camera pixels recorded that are wasted </li>
     * </ul>
     */
    public static float calculateCameraSizeScore(
            ImageSize cameraSize, ImageSize targetSize,
            ImageFit imageFit, ImageScale imageScale) {
        if (!targetSize.areBothDimensionsDefined()) {
            targetSize = targetSize.toBuilder().calculateUndefinedDimensions(cameraSize).build();
        }
        // Find the intersection of these sizes to find the recorded non upscaled pixels:
        // 1) Camera size scaled to target size
        // 2) Target size
        // 3) Camera size
        long recordedNonUpscaledPixels = cameraSize.toBuilder()
                .scale(targetSize, imageFit, imageScale)
                .min(targetSize)
                .min(cameraSize)
                .getArea();
        long pixelsWasted = Math.max(0, cameraSize.getArea() - recordedNonUpscaledPixels);
        return 100f * recordedNonUpscaledPixels - pixelsWasted;
    }

    /**
     *  Choose the best fps range for the target fps. Tries to choose an fps range with a max
     *  greater than the target FPS. Also tries to choose an FPS range with the largest range to
     *  allow the most flexibility with exposure.
     */
    public static int[] getBestFpsRange(List<int[]> fpsRanges, float targetFps) {
        int targetFpsInt = (int)(1000f * targetFps);
        int[] bestFpsRange = null;
        int bestFpsRangeSize = -1;
        // Choose the range whose max fps is greater than the target FPS and has the greatest range.
        for (int[] fpsRange : fpsRanges) {
            if (fpsRange[1] >= targetFpsInt) {
                int fpsRangeSize = fpsRange[1] - fpsRange[0];
                if (fpsRangeSize > bestFpsRangeSize) {
                    bestFpsRange = fpsRange;
                    bestFpsRangeSize = fpsRangeSize;
                }
            }
        }
        // If no range was found then choose the range with the max closest to the target FPS.
        if (bestFpsRange == null) {
            for (int[] fpsRange : fpsRanges) {
                int fpsRangeSize = fpsRange[1] - fpsRange[0];
                if (bestFpsRange == null || fpsRange[1] > bestFpsRange[1]
                        || fpsRangeSize > bestFpsRangeSize) {
                    bestFpsRange = fpsRange;
                    bestFpsRangeSize = fpsRangeSize;
                }
            }
        }
        return bestFpsRange;
    }

    /**
     * Determine the orientation to display the camera surface.
     */
    public static int determineCameraDisplayRotation(
            int surfaceOrientationDegrees,
            int cameraOrientationDegrees,
            CameraControllerI.Facing cameraFacing) {
        if (cameraFacing == CameraControllerI.Facing.FRONT) {
            int displayOrientation = (cameraOrientationDegrees + surfaceOrientationDegrees) % 360;
            return (360 - displayOrientation) % 360;
        } else {
            return (cameraOrientationDegrees - surfaceOrientationDegrees + 360) % 360;
        }
    }

    private CameraUtil() {}
}
