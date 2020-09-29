package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageHueRotation extends GlFilter {

    public static final String HUEROTATION_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "uniform float sliderValue;\n" +
                    "\n" +
                    "#define SPEED 10.0\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv =  vTextureCoord.xy;\n" +
                    "    float c = cos(iTime*SPEED*1.0);\n" +
                    "    float s = sin(iTime*SPEED*1.0);\n" +
                    "    \n" +
                    "    \n" +
                    "    mat4 hueRotation =\n" +
                    "    mat4(     0.299,  0.587,  0.114, 0.0,\n" +
                    "         0.299,  0.587,  0.114, 0.0,\n" +
                    "         0.299,  0.587,  0.114, 0.0,\n" +
                    "         0.000,  0.000,  0.000, 1.0) +\n" +
                    "    \n" +
                    "    mat4(     0.701, -0.587, -0.114, 0.0,\n" +
                    "         -0.299,  0.413, -0.114, 0.0,\n" +
                    "         -0.300, -0.588,  0.886, 0.0,\n" +
                    "         0.000,  0.000,  0.000, 0.0) * c +\n" +
                    "    \n" +
                    "    mat4(     0.168,  0.330, -0.497, 0.0,\n" +
                    "         -0.328,  0.035,  0.292, 0.0,\n" +
                    "         1.250, -1.050, -0.203, 0.0,\n" +
                    "         0.000,  0.000,  0.000, 0.0) * s;\n" +
                    "    \n" +
                    "    \n" +
                    "    vec4 pixel = texture2D(sTexture, uv);\n" +
                    "    \n" +
                    "    vec4 fragColor = pixel * hueRotation;\n" +
                    "    \n" +
                    "    gl_FragColor = fragColor;\n" +
                    "}\n";


    final long START_TIME = System.currentTimeMillis();

    public GPUImageHueRotation() {
        super(DEFAULT_VERTEX_SHADER, HUEROTATION_FRAGMENT_SHADER);
    }


    @Override
    protected void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("sliderValue"), 0.5f);
    }
}
