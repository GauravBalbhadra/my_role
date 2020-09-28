package com.myrole.camera_ck.internal.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 18-Jul-17.
 */

public enum CameraOption {
    CAMERA_IMAGE("Take Photo"),
    CAMERA_VIDEO("Take Video"),
    GALLERY_IMAGE("Pick Photo"),
    GALLERY_VIDEO("Pick Video"),
    NONE("NONE");

    private final String value;

    CameraOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CameraOption getOptionType(String value) {
        switch (value) {
            case "Take Photo":
                return CAMERA_IMAGE;
            case "Take Video":
                return CAMERA_VIDEO;
            case "Pick Photo":
                return GALLERY_IMAGE;
            case "Pick Video":
                return GALLERY_VIDEO;
            default:
                return NONE;
        }
    }

    public static List<CameraOption> getOptions()
    {
        List<CameraOption> options = new ArrayList<>();
        options.add(CAMERA_IMAGE);
        options.add(CAMERA_VIDEO);
        options.add(GALLERY_IMAGE);
        options.add(GALLERY_VIDEO);
        return options;
    }
}
