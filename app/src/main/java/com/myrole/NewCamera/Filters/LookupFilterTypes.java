package com.myrole.NewCamera.Filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageLookupFilter;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;
import com.myrole.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LookupFilterTypes {//set filters for video
    DEFAULT, SP38, SP33, SP39, SP45, SP46, SP1,
    SP2, SP3, SP4, SP5, SP6, SP7, SP8, SP9, SP10, SP11,
    SP12, SP13, SP14, SP15, SP16, SP17, SP18,
    SP19, SP20, SP21, SP22, SP23, SP24, SP25, SP26, SP27, SP28,
    SP29, SP30, SP31, SP32, SP34, SP35, SP36, SP37,
    SP40, SP47, SP48, SP41, SP43, SP42, SP44;


    public static List<LookupFilterTypes> createVideoEffectList() {
        return Arrays.asList(LookupFilterTypes.values());
    }


    public static ArrayList<String> createFilterName(Context context) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Beauty");
        strings.add("Vignette");
        strings.add("Monochrome");
        strings.add("Hue");
        strings.add("Posterize");
        strings.add("Warm");
        strings.add("Cold");
        strings.add("Ivory");
        strings.add("Stark");
        strings.add("Charm");
        strings.add("Mellow");
        strings.add("Burned");
        strings.add("Red hood");
        strings.add("Bright");
        strings.add("Greener");
        strings.add("Moonlight");
        strings.add("1981");
        strings.add("Seafoam");
        strings.add("Dodger");
        strings.add("1982");
        strings.add("Vivid");
        strings.add("Vivid cool");
        strings.add("Dramatic");
        strings.add("Mono");
        strings.add("1983");
        strings.add("Monitor");
        strings.add("Duo tone");
        strings.add("Glossy");
        strings.add("Brighter");
        strings.add("Dramatic warm");
        strings.add("Silvertone");
        strings.add("Glossy cool");
        strings.add("Disco light");
        strings.add("Dramatic sharp");
        strings.add("Spring");
        strings.add("Sweet");
        strings.add("Natural");
        strings.add("Fair");
        strings.add("Soft");
        strings.add("Rosy");
        strings.add("Poshue");
        strings.add("Sharpen");
        strings.add("Zoom Blur");
        strings.add("Stretch Distortion");
        strings.add("B/W Sketch");
        strings.add("Pixellate");
        strings.add("Crosshatch");
        strings.add("Toon Filter");
        return strings;

    }


    public static GlFilter createGlFilter(LookupFilterTypes filterType, Context context) {
        switch (filterType) {
            case SP1:
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fresh_5);
                return new GPUImageLookupFilter(bitmap);
            case SP2:
                Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_phaselis);
                return new GPUImageLookupFilter(bitmap1);
            case SP3:
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_magnesia);
                return new GPUImageLookupFilter(bitmap2);
            case SP4:
                Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.beautylookup);
                return new GPUImageLookupFilter(bitmap3);
            default:
                return new GlFilter();
        }
    }
}
