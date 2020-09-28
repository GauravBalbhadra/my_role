package com.myrole.utilities;

public class MergeMediaEvent {

    private String mergedPath;

    public MergeMediaEvent(String messagePath) {
        mergedPath = messagePath;
    }

    public String getResponsePath() {
        return mergedPath;
    }

}
