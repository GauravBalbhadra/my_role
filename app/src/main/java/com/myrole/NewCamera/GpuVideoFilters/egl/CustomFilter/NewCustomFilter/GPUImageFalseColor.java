package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageFalseColor extends GlFilter {

    private static final String FRAGMENT_SHADER =
            "precision lowp float;\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "uniform float intensity;\n" +
                    "uniform vec3 firstColor;\n" +
                    "uniform vec3 secondColor;\n" +
                    "\n" +
                    "const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    float luminance = dot(textureColor.rgb, luminanceWeighting);\n" +
                    "    \n" +
                    "    gl_FragColor = vec4( mix(firstColor.rgb, secondColor.rgb, luminance), textureColor.a);\n" +
                    "}";


    public GPUImageFalseColor() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT_SHADER);
    }


}
