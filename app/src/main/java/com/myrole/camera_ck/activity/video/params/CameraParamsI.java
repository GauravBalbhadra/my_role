package com.myrole.camera_ck.activity.video.params;






import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;

import java.io.Serializable;

/**
 * Params for the camera.
 */
public interface CameraParamsI
        extends VideoFrameRateParamsI, VideoScaleParamsI, VideoSizeParamsI, Serializable {

    /**
     * Get the initial video camera facing.
     */
    CameraControllerI.Facing getVideoCameraFacing();

    interface BuilderI<T extends BuilderI<T>> extends
            VideoFrameRateParamsI.BuilderI<T>,
            VideoScaleParamsI.BuilderI<T>,
            VideoSizeParamsI.BuilderI<T> {

        /**
         * Set the initial video camera facing. Default value is the back camera.
         */
        T setVideoCameraFacing(CameraControllerI.Facing val);

        @Override
        CameraParamsI build();
    }
}