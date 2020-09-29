package com.myrole.NewCamera.GpuVideoFilters.composer;


interface IAudioComposer {

    void setup();

    boolean stepPipeline();

    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();
}
