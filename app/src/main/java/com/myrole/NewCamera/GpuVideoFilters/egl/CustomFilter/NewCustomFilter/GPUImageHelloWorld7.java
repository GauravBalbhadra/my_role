package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageHelloWorld7 extends GlFilter {
    private static final String HELLOWORLD_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "// Normalized pixel coordinates (from 0 to 1)\n" +
                    "vec2 uv = vTextureCoord;\n" +
                    "vec4 tc = texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "// Time varying pixel color\n" +
                    "vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "fragColor *= vec4(tc);\n" +
                    "\n" +
                    "gl_FragColor = fragColor;\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    // private boolean enabled = true;


    public GPUImageHelloWorld7() {
        super(DEFAULT_VERTEX_SHADER, HELLOWORLD_FRAGMENT_SHADER);

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

    }


}
