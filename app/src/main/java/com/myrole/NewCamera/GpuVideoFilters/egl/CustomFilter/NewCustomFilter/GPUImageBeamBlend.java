package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageBeamBlend extends GlFilter {

    private static final String BEAMBLEND_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform  sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "uniform float sliderValue;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv =  vTextureCoord.xy;\n" +
                    "    vec4 tc = texture2D(sTexture, vTextureCoord);\n" +
                    "    float t = iTime;\n" +
                    "    \n" +
                    "    vec3 cl = vec3 ( 1.0, 0.5, 0.5 );\n" +
                    "    cl *= (4.1 * sliderValue)  *sqrt(abs( 1.0 / (sin(0.5- uv.x + sin(uv.y+t)* 0.20 ) * 2.0)));\n" +
                    "    \n" +
                    "    vec4 fragColor = vec4(cl-1.8, 1.0 );\n" +
                    "    fragColor *= vec4(tc) ;\n" +
                    "        \n" +
                    "    gl_FragColor = fragColor;\n" +
                    "}\n";


    final long START_TIME = System.currentTimeMillis();

    public GPUImageBeamBlend() {
        super(DEFAULT_VERTEX_SHADER, BEAMBLEND_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);


    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        float sliderValue = 0.45f;
        GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
    }
}
