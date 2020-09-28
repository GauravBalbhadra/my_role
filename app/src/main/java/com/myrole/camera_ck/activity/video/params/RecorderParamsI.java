package com.myrole.camera_ck.activity.video.params;


import java.io.Serializable;

/**
 * Parameters for recording video.
 */
public interface RecorderParamsI extends
        CameraParamsI,
        VideoFrameRateParamsI,
        VideoScaleParamsI,
        VideoSizeParamsI,
        VideoTransformerParamsI,
        Serializable {

    interface BuilderI<T extends BuilderI<T>> extends
            CameraParamsI.BuilderI<T>,
            VideoFrameRateParamsI.BuilderI<T>,
            VideoScaleParamsI.BuilderI<T>,
            VideoSizeParamsI.BuilderI<T>,
            VideoTransformerParamsI.BuilderI<T> {

        @Override
        RecorderParamsI build();
    }
}