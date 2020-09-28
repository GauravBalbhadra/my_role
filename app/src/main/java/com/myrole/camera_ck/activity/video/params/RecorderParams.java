package com.myrole.camera_ck.activity.video.params;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.myrole.camera_ck.activity.video.common.ImageFit;
import com.myrole.camera_ck.activity.video.common.ImageScale;
import com.myrole.camera_ck.activity.video.common.ImageSize;
import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;

/**
 * Parameters for recording video.
 */
@AutoValue
public  abstract class RecorderParams implements RecorderParamsI {

    protected RecorderParams() {}

    @Override
    public abstract CameraControllerI.Facing getVideoCameraFacing();

    @Override
    public abstract Optional<Integer> getVideoFrameRate();

    @Override
    public abstract ImageSize getVideoSize();

    @Override
    public abstract ImageScale getVideoImageScale();

    @Override
    public abstract ImageFit getVideoImageFit();

    @Override
    public abstract boolean getShouldCropVideo();

    @Override
    public abstract boolean getShouldPadVideo();

    public abstract Builder toBuilder();

  /*  public static Builder builder() {
        return Builder.<Builder>setDefaults(new AutoValue.Builder(builder()));
    }
*/
    @AutoValue.Builder
    public abstract static class Builder implements RecorderParamsI.BuilderI<Builder> {

        public static <T extends RecorderParamsI.BuilderI<T>> T setOnlyClassDefaults(T builder) {
            return builder;
        }

        public static <T extends RecorderParamsI.BuilderI<T>> T setDefaults(T builder) {
            CameraParams.Builder.setOnlyClassDefaults(builder);
            VideoFrameRateParams.Builder.setOnlyClassDefaults(builder);
            VideoScaleParams.Builder.setOnlyClassDefaults(builder);
            VideoSizeParams.Builder.setOnlyClassDefaults(builder);
            VideoTransformerParams.Builder.setOnlyClassDefaults(builder);
            return setOnlyClassDefaults(builder);
        }

        public static <T extends RecorderParamsI.BuilderI<T>> T mergeOnlyClass(
                T builder, RecorderParamsI params) {
            return builder;
        }

        public static <T extends RecorderParamsI.BuilderI<T>> T merge(T builder, RecorderParamsI params) {
            CameraParams.Builder.mergeOnlyClass(builder, params);
            VideoFrameRateParams.Builder.mergeOnlyClass(builder, params);
            VideoScaleParams.Builder.mergeOnlyClass(builder, params);
            VideoSizeParams.Builder.mergeOnlyClass(builder, params);
            VideoTransformerParams.Builder.mergeOnlyClass(builder, params);
            return mergeOnlyClass(builder, params);
        }

        public static <T extends RecorderParamsI> T validateOnlyClass(T params) {
            return params;
        }

        public static <T extends RecorderParamsI> T validate(T params) {
            VideoFrameRateParams.Builder.validateOnlyClass(params);
            validateOnlyClass(params);
            return params;
        }

        protected Builder() {}

        @Override
        public abstract Builder setVideoCameraFacing(CameraControllerI.Facing val);

        @Override
        public Builder setVideoFrameRate(int val) {
            return setVideoFrameRate(Optional.of(val));
        }
        @Override
        public abstract Builder setVideoFrameRate(Optional<Integer> val);

        @Override
        public abstract Builder setVideoSize(ImageSize imageSize);

        @Override
        public abstract Builder setVideoImageScale(ImageScale val);

        @Override
        public abstract Builder setVideoImageFit(ImageFit val);

        abstract RecorderParams autoBuild();

        @Override
        public RecorderParams build() {
            return validate(autoBuild());
        }
    }
}
