package com.myrole.NewCamera.Filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageFourINOne;
import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageOutInBlue;
import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageRainFall;
import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageThreeINOne;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CustomFilterType {//set custom effects for video
    DEFAULT,
    SEPRATEDRGB,
    FLASHLIGHT,
    HOTLINEMIMI,
    OLDTV,
    SHADERMIRROR,
    FOURINONE,
    DISCO,
    COLORPIXEL,
    HELLOWORLD7,
    OLDVHS,
    THREEINONE,
    CHROMATICGLTCH,
    RAINFALL,
    SHDEROLDTVIOS,
    EMIX,
    CHROMATICABREVATION,
    VCRADIATIOBN,
    HSVRAINBOW,
    WARMER,
    RAINBOW,
    SHADERVIBRATION,
    GLITCHSHADER,
    SHADERSNOW,
    VIDEOGLITCH,
    OUTINBLUE,
    DRL11GLITCH,
    ASCII,
    ELECTRONICGLOW,
    ENDLESS,
    PSYCHEDELIC,
    GRAYSCALERGB,
    EVI,
    SWGLITCH,
    BEAMBLEND,
    DIRTYOLDCART,
    VIDEOPIXEL,
    SINMOV,
    GLTCHYVHS,
    HUEROTATION,
    RAINMOOD,
    COLORGLOW,
    CCCOLORS,
    SHADEROLDTV,
    BITOFGLITCH,
    FAKERIPPLE,
    GLITCH2,
    GLITCHPIXEL;


    public static List<CustomFilterType> createVideoFilterList() {
        return Arrays.asList(CustomFilterType.values());
    }


    public static ArrayList<String> createGlFilterName(Context context) {//        strings.add("Sparkling Stars");
        ArrayList<String> strings = new ArrayList<>();
        strings.add(" ");
        strings.add("Seperate RGB");

        strings.add("Flashlight");
        strings.add("Miami");
        strings.add("Old Video");
        strings.add("Mirror");
        strings.add("Color Popart");
        strings.add("Disco");
        strings.add("Posterize");
        strings.add("Flare");
        strings.add("OLD VHS");
        strings.add("Threefold");
        strings.add("Chromatic");
        strings.add("Rainfall");
        strings.add("TV Glitch");
        strings.add("Sketchy");
        strings.add("Shadow Rays");
        strings.add("Distortion");
        strings.add("Splash of neo");
        strings.add("Warmer");
        strings.add("Rainbow");
        strings.add("Vibration");
        strings.add("Shader Glitch");
        strings.add("Snowflakes");
        strings.add("Video Glitch");
        strings.add("Out In Blue");
        strings.add("Hue");
        strings.add("Punctuation");
        strings.add("Electrify");
        strings.add("EndLess");
        strings.add("Neo Pop");
        strings.add("Colour Game");
        strings.add("Evil");
        strings.add("SWGlitch");
        strings.add("Red Beam");
        strings.add("Raw");
        strings.add("Pixelate");
        strings.add("Sin Movement");
        strings.add("Sliding");
        strings.add("Hue Rotation");
        strings.add("Wavey");
        strings.add("Negative Reel");
        strings.add("Mask");
        strings.add("Grizzled");
        strings.add("Dark Room");
        strings.add("Wave Motion");
        strings.add("Rush");
        strings.add("Pixel Glitch");
        return strings;

    }


    public static GlFilter createGlFilter(CustomFilterType filterType, Context context) {
        switch (filterType) {
            case DEFAULT:
                return new GlFilter();
            case THREEINONE:
                return new GPUImageThreeINOne();
            case FOURINONE:
                return new GPUImageFourINOne();
            case RAINFALL:
                return new GPUImageRainFall();
            case OUTINBLUE:
                return new GPUImageOutInBlue();
//            case SHADERVIBRATION:
//                return new GPUImageShader_Vibration();
//            case SHDEROLDTVIOS:
//                return new GPUImageShaderOldTvIOS();
//            case GLITCHPIXEL:
//                return new GPUImageGlitchPixel();
//            case GLITCH2:
//                return new GPUImageGlitch2();
//            case GLITCHSHADER:
//                return new GPUImageGlitch_Shader_A();
//            case EMIX:
//                Bitmap bitmapf = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_256);
//                return new GPUImageEmix_es(bitmapf);
//            case FAKERIPPLE:
//                return new GPUImageFakeRipple();
//            case DRL11GLITCH:
//                return new GPUImagedrl011_glitch();
//            case CHROMATICABREVATION:
//                return new GPUImageChromatic_Aberration();
//            case ASCII:
//                return new GpuImageASCII();
//            case OLDTV:
//                return new GPUImageOldTv();
//            case VCRADIATIOBN:
//                Bitmap bitmavc = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_gray);
//                return new GPUimageVCRdistortion(bitmavc);
//            case SHADERMIRROR:
//                return new GPUImageShaderMirror();
//            case FLASHLIGHT:
//                return new GPUImageFlashLight();
//            case ENDLESS:
//                return new GPUImageEndless();
//            case GRAYSCALERGB:
//                return new GPUImageGrayScaleRGB();
//            case WARMER:
//                return new GPUImageWarmer();
//            case RAINBOW:
//                return new GPUImageRainbow();
//            case SINMOV:
//                return new GPUImageSinMovment();
//            case EVI:
//                return new GPUImageEvii_Britney();
//            case SWGLITCH:
//                return new GPUImageSwGlitch();
//            case HELLOWORLD7:
//                return new GPUImageHelloWorld7();
//            case DISCO:
//                return new GPUImageDisco();
//            case HSVRAINBOW:
//                return new GPUImageHSVRainbow();
//            case HOTLINEMIMI:
//                return new GPUImageHotline_Miami();
//            case PSYCHEDELIC:
//                return new GPUimagePsychedelicGhost();
//            case ELECTRONICGLOW:
//                return new GPUImageElectronicGlow();
//            case SHADERSNOW:
//                return new GPUImageShaderSnow();
//            case BITOFGLITCH:
//                return new GPUImageBitOfGlitch();
//            case COLORPIXEL:
//                return new GPUImageColorizePixerize();
//            case BEAMBLEND:
//                return new GPUImageBeamBlend();
//            case COLORGLOW:
//                return new GPUImageColorGlow();
//            case RAINMOOD:
//                return new GPUImageRainMood();
//            case VIDEOGLITCH:
//                return new GPUImageVideoGlitch();
//            case CCCOLORS:
//                return new GPUImageCCColors();
//            case VIDEOPIXEL:
//                return new GPUImageVideoPixelate();
//            case HUEROTATION:
//                return new GPUImageHueRotation();
//            case CHROMATICGLTCH:
//                return new GPUImageChromaticGlitch();
//            case SEPRATEDRGB:
//                return new GPUImageSeperateRGB_es();
//            case SHADEROLDTV:
//                Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_rgb);
//                return new GPUImageShaderOldTv2(bitmap1);
//            case OLDVHS:
//                Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_256);
//                return new GPUImageOldVhs(bitmap3);
//            case DIRTYOLDCART:
//                Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_64);
//                return new GPUImageDirtyOldCart(bitmap5);
//            case GLTCHYVHS:
//                Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.noise_gray);
//                return new GPUImageGlitchyVhs(bitmap6);
            default:
                return new GlFilter();
        }
    }
}
