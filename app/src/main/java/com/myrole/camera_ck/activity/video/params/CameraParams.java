package com.myrole.camera_ck.activity.video.params;



import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.myrole.camera_ck.activity.video.common.ImageFit;
import com.myrole.camera_ck.activity.video.common.ImageScale;
import com.myrole.camera_ck.activity.video.common.ImageSize;
import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;

/**
 * Params for the camera.
 */
@AutoValue
public abstract class CameraParams implements CameraParamsI {

    protected CameraParams() {}

    @Override
    public abstract CameraControllerI.Facing getVideoCameraFacing();

    @Override
    public abstract Optional<Integer> getVideoFrameRate();

    @Override
    public abstract ImageSize getVideoSize();

    @Override
    public abstract ImageFit getVideoImageFit();

    @Override
    public abstract ImageScale getVideoImageScale();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.setDefaults(new Builder() {
            @Override
            public Builder setVideoCameraFacing(CameraControllerI.Facing val) {
                return null;
            }

            @Override
            public Builder setVideoFrameRate(Optional<Integer> val) {
                setVideoFrameRate(val);
                return this;
            }

            @Override
            public Builder setVideoSize(ImageSize imageSize) {
                return null;
            }

            @Override
            public Builder setVideoImageScale(ImageScale val) {
                return null;
            }

            @Override
            public Builder setVideoImageFit(ImageFit val) {
                return null;
            }

            @Override
            public CameraParams build() {
                return null;
            }
        });//(new AutoValue_CameraParams.Builder());
    }

    @AutoValue.Builder
    public abstract static class Builder implements CameraParamsI.BuilderI<Builder> {

        public static <T extends CameraParamsI.BuilderI<T>> T setOnlyClassDefaults(T builder) {
            return builder.setVideoCameraFacing(CameraControllerI.Facing.BACK);
        }

        public static <T extends CameraParamsI.BuilderI<T>> T setDefaults(T builder) {
            VideoFrameRateParams.Builder.setOnlyClassDefaults(builder);
            VideoScaleParams.Builder.setOnlyClassDefaults(builder);
            VideoSizeParams.Builder.setOnlyClassDefaults(builder);
            return setOnlyClassDefaults(builder);
        }

        public static <T extends CameraParamsI.BuilderI<T>> T mergeOnlyClass(
                T builder, CameraParamsI params) {
            return builder.setVideoCameraFacing(params.getVideoCameraFacing());
        }

        public static <T extends CameraParamsI.BuilderI<T>> T merge(
                T builder, CameraParamsI params) {
            VideoFrameRateParams.Builder.mergeOnlyClass(builder, params);
            VideoScaleParams.Builder.mergeOnlyClass(builder, params);
            VideoSizeParams.Builder.mergeOnlyClass(builder, params);
            return mergeOnlyClass(builder, params);
        }

        protected Builder() {}

        @Override
        public abstract Builder setVideoCameraFacing(CameraControllerI.Facing val);

        @Override
        public Builder setVideoFrameRate(int val) {
            setVideoFrameRate(Optional.of(val));
            return this;
        }
        @Override
        public abstract Builder setVideoFrameRate(Optional<Integer> val);

        @Override
        public abstract Builder setVideoSize(ImageSize imageSize);

        @Override
        public abstract Builder setVideoImageScale(ImageScale val);

        @Override
        public abstract Builder setVideoImageFit(ImageFit val);

        @Override
        public abstract CameraParams build();
    }
}
