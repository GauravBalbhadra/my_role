package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;


import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageHotline_Miami extends GlFilter {

    public static final String HOTLINE_MIMI_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "// Textures\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "// ## noise_64.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "\n" +
                    "//varying vec2 textureCoordinate3;\n" +
                    "//uniform sampler2D inputImageTexture3;   // ## 72Favicon.png\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform bool enabled;           // ## 1\n" +
                    "//uniform float progress;\n" +
                    "//uniform float strength;\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "uniform float size_width; // ## 0, 200, 50\n" +
                    "uniform float size_height; // ## 0, 200, 50\n" +
                    "\n" +
                    "uniform float distortion_width; // ## 0, 50, 20\n" +
                    "uniform float distortion_height; // ## 0, 50, 20\n" +
                    "uniform float speed; // ## 0, 10.0, 0.75\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "uniform bool enable_color; // ## 1\n" +
                    "uniform float color_r_attr_1; // ## 0, 10.0, 4.0\n" +
                    "uniform float color_r_attr_2; // ## 0, 5.0, 1.0\n" +
                    "uniform float color_r_attr_3; // ## 0, 10.0, 2.0\n" +
                    "\n" +
                    "uniform float color_g_attr_1; // ## 0, 10.0, 2.0\n" +
                    "\n" +
                    "uniform float color_b_attr_1; // ## 0, 5.0, 1.0\n" +
                    "uniform float color_b_attr_2; // ## 0, 10.0, 2.0\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "vec2 size = vec2(size_width, size_height);\n" +
                    "vec2 distortion = vec2(distortion_width, distortion_height);\n" +
                    "\n" +
                    "vec2 transformed = vec2(\n" +
                    "gl_FragCoord.x + sin(gl_FragCoord.y / size.x + iTime * speed) * distortion.x,\n" +
                    "gl_FragCoord.y + cos(gl_FragCoord.x / size.y + iTime * speed) * distortion.y\n" +
                    ");\n" +
                    "vec2 relCoord = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "if (!enable_color) {\n" +
                    "gl_FragColor = texture2D(sTexture, transformed / resolution.xy) + vec4(\n" +
                    "(cos(relCoord.x + iTime * speed * color_r_attr_1) + color_r_attr_2) / color_r_attr_3,\n" +
                    "(relCoord.x + relCoord.y) / color_g_attr_1,\n" +
                    "(sin(relCoord.y + iTime * speed) + color_b_attr_1) / color_b_attr_2,\n" +
                    "0\n" +
                    ");\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, transformed / resolution.xy);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";


    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageHotline_Miami() {
        super(DEFAULT_VERTEX_SHADER, HOTLINE_MIMI_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;


    }

    @Override
    public void onDraw() {

        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("speed"), 0.75f);
        GLES20.glUniform1f(getHandle("color_r_attr_1"), 4.0f);
        GLES20.glUniform1f(getHandle("color_r_attr_2"), 1.0f);
        GLES20.glUniform1f(getHandle("color_r_attr_3"), 2.0f);
        GLES20.glUniform1f(getHandle("color_g_attr_1"), 2.0f);
        GLES20.glUniform1f(getHandle("color_b_attr_1"), 1.0f);
        GLES20.glUniform1f(getHandle("color_b_attr_2"), 2.0f);
        GLES20.glUniform1f(getHandle("size_width"), 50.0f);
        GLES20.glUniform1f(getHandle("size_height"), 50.0f);
    }


}
