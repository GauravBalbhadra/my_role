package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


//refrence:-// https://www.shadertoy.com/view/4ltSRN ColorGlow.fsh

public class GPUImageColorGlow extends GlFilter {
    private static final String COLORGLOW_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform int glowType;\n" +
                    "void main()\n" +
                    "{\n" +
                    " gl_FragColor = texture2D(sTexture,gl_FragCoord.xy/resolution.xy);\n" +
                    "vec4 f = vec4(1.,1.,1.,1.);\n" +
                    "if (glowType > 0) {\n" +
                    " gl_FragColor = .5*(1.0+sin(6.28318*gl_FragColor+iTime*f));\n" +
                    " } else {\n" +
                    "gl_FragColor = abs(mod(gl_FragColor+iTime*f,2.)-1.);\n" +
                    " }\n" +
                    "}";
    float iTime = 0.5f;
    int heights;
    int widths;
    private int mglowType = 1;

    public GPUImageColorGlow() {
        super(DEFAULT_VERTEX_SHADER, COLORGLOW_FRAGMENT_SHADER);


    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;


    }

    @Override
    public void onDraw() {
        GLES20.glUniform1f(getHandle("glowType"), mglowType);
        GLES20.glUniform1f(getHandle("iTime"), iTime);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);

    }


}
