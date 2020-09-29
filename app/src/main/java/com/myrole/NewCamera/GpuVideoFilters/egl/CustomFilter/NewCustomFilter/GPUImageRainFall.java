package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageRainFall extends GlFilter {

    private static final String FIREWORKS_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "float hash (float x)\n" +
                    "{\n" +
                    "return fract (sin (x * 133.3 ) * 13.13 );\n" +
                    "}\n" +
                    "\n" +
                    "void main(void)\n" +
                    "{\n" +
                    "vec2 resolution = vec2(500.0);\n" +
                    "\n" +
                    "vec2 uv = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);\n" +
                    "\n" +
                    "vec3 c = vec3(0.6, 0.7, 0.8);\n" +
                    "\n" +
                    "float a = 0.5;\n" +
                    "\n" +
                    "float si = sin(a);\n" +
                    "\n" +
                    "float co = cos(a);\n" +
                    "\n" +
                    "uv *= mat2(co, si, si, co);\n" + //  "uv *= mat2(co, -si, si, -co);\n" +
                    "\n" +
                    "uv *= length(uv + vec2(0.0, 4.9)) * 0.3 + 1.0;\n" +
                    "\n" +
                    "float v = 1.0 - sin(hash(floor(uv.x * 100.0)) * 2.0);\n" +
                    "\n" +
                    "float b = clamp(abs(sin(20.0 * iTime *.75 * v + uv.y * (5.0 / (2.0 + v)))) - 0.95, 0.0, 1.0) * 20.0;\n" +
                    "\n" +
                    "c *= v * b;\n" +
                    "\n" +
                    "//gl_FragColor = vec4(c, 1.0);\n" +
                    "//}\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = vec4(c, 1.0 ) + texture2D(sTexture,vTextureCoord);\n" +
                    "\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageRainFall() {
        super(DEFAULT_VERTEX_SHADER, FIREWORKS_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
//        widths = width;
//        heights = height;

    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        // GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
