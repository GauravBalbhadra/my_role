package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageFireWorks extends GlFilter {

    private static final String FIREWORKS_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "#define N(h) fract(sin(vec4(6,9,1,0)*h))\n" +
                    "\n" +
                    "void main(void)\n" +
                    "{\n" +
                    "lowp vec4 org = texture2D(sTexture,vTextureCoord);\n" +
                    "//vec4 o = org;\n" +
                    "vec2 uv = gl_FragCoord.xy/resolution.y;\n" +
                    "\n" +
                    "float e, d, i=0.2;\n" +
                    "vec4 p;\n" +
                    "\n" +
                    "for(float i=1.0; i<9.9; i++) {\n" +
                    "d = floor(e = i*9.1+iTime);\n" +
                    "p = N(d)+.13;\n" +
                    "e -= d;\n" +
                    "for(float d=0.; d<15.;d++)\n" +
                    "org += p*(2.9-e)/1e3/length(uv-(p-e*(N(d*i)-.5)).xy);\n" +
                    "}\n" +
                    "\n" +
                    "gl_FragColor = vec4(org.rgb, 1);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageFireWorks() {
        super(DEFAULT_VERTEX_SHADER, FIREWORKS_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;

    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
