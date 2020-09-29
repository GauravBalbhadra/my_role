package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageStrechDistoration extends GlFilter {


    private static final String FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform highp vec2 center;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    highp vec2 normCoord = 2.0 * vTextureCoord - 1.0;\n" +
                    "    highp vec2 normCenter = 2.0 * center - 1.0;\n" +
                    "    \n" +
                    "    normCoord -= normCenter;\n" +
                    "    mediump vec2 s = sign(normCoord);\n" +
                    "    normCoord = abs(normCoord);\n" +
                    "    normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n" +
                    "    normCoord = s * normCoord;\n" +
                    "    \n" +
                    "    normCoord += normCenter;\n" +
                    "       \n" +
                    "    mediump vec2 vTextureCoordToUse = normCoord / 2.0 + 0.5;\n" +
                    "    \n" +
                    "    \n" +
                    "    gl_FragColor = texture2D(sTexture, vTextureCoordToUse );\n" +
                    "    \n" +
                    "}";

    public GPUImageStrechDistoration() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }


}
