package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderOldTvIOS extends GlFilter {
    private static final String SHADEROLDTVIOS_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform bool enabled;\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "\n" +
                    "uniform float INTERLACING_SEVERITY;     // ## 0, 0.01, 0.001\n" +
                    "uniform float TRACKING_HEIGHT;          // ## 0, 1.0, 0.15\n" +
                    "uniform float TRACKING_SEVERITY;        // ## 0, 0.1, 0.025\n" +
                    "uniform float TRACKING_SPEED;           // ## 0, 2.0, 0.2\n" +
                    "uniform float SHIMMER_SPEED;            // ## 0, 60, 30.0\n" +
                    "//uniform float RGB_MASK_SIZE;            // ## 0, 5, 2.0\n" +
                    "\n" +
                    "\n" +
                    "//#define INTERLACING_SEVERITY 0.001\n" +
                    "//\n" +
                    "//#define TRACKING_HEIGHT 0.15\n" +
                    "//#define TRACKING_SEVERITY 0.025\n" +
                    "//#define TRACKING_SPEED 0.2\n" +
                    "//\n" +
                    "//#define SHIMMER_SPEED 30.0\n" +
                    "//\n" +
                    "//#define RGB_MASK_SIZE 2.0\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "// x wigglies (sampling error)\n" +
                    "uv.x -= sin(uv.y * 500.0 + iTime) * INTERLACING_SEVERITY;\n" +
                    "\n" +
                    "float scan = mod(gl_FragCoord.y, 3.0);\n" +
                    "\n" +
                    "// Convert our xy coordinates into a linear index we can use in\n" +
                    "// the next step\n" +
                    "// periodically offset y by 1 pixel to get that shimmer\n" +
                    "float yOffset = floor(sin(iTime * SHIMMER_SPEED));\n" +
                    "float pix = (gl_FragCoord.y+yOffset) * resolution.x + gl_FragCoord.x;\n" +
                    "pix = floor(pix);\n" +
                    "\n" +
                    "// Simulate pixel layout by using a repeating RGB mask\n" +
                    "//    vec4 colMask = vec4(mod(pix, RGB_MASK_SIZE), mod((pix+1.0), RGB_MASK_SIZE), mod((pix+2.0), RGB_MASK_SIZE), 1.0);\n" +
                    "//    colMask = colMask / (RGB_MASK_SIZE - 1.0) + 0.5;\n" +
                    "\n" +
                    "// Tracking\n" +
                    "float t = -iTime * TRACKING_SPEED;\n" +
                    "float fractionalTime = (t - floor(t)) * 1.3 - TRACKING_HEIGHT;\n" +
                    "if(fractionalTime + TRACKING_HEIGHT >= uv.y && fractionalTime <= uv.y)\n" +
                    "{\n" +
                    "uv.x -= fractionalTime * TRACKING_SEVERITY;\n" +
                    "}\n" +
                    "\n" +
                    "// removed green mask\n" +
                    "//    gl_FragColor = texture2D(sTexture, uv) * colMask*scan;\n" +
                    "\n" +
                    "gl_FragColor = texture2D(sTexture, uv) *scan;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
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
    public GPUImageShaderOldTvIOS() {
        super(DEFAULT_VERTEX_SHADER, SHADEROLDTVIOS_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("INTERLACING_SEVERITY"), 0.001f);
        GLES20.glUniform1f(getHandle("TRACKING_HEIGHT"), 0.15f);
        GLES20.glUniform1f(getHandle("TRACKING_SEVERITY"), 0.025f);
        GLES20.glUniform1f(getHandle("TRACKING_SPEED"), 0.2f);
        GLES20.glUniform1f(getHandle("SHIMMER_SPEED"), 30.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
