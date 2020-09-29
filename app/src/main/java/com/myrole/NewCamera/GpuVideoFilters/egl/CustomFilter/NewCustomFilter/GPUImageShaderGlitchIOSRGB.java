package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderGlitchIOSRGB extends GlFilter {
    private static final String SHADERGLITCH_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform float shiftR; // ## -1, 1, 0\n" +
                    "uniform float shiftG; // ## -1, 1, 0\n" +
                    "uniform float shiftB; // ## -1, 1, 0\n" +
                    "uniform bool manualMode; // ## 0\n" +
                    "\n" +
                    "uniform bool enabled;\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "\n" +
                    "uniform float offsetStrength; // ## -2, 2, 0.5\n" +
                    "\n" +
                    "uniform float var1; // ## 0, 0.5, 0.1\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "float rand () {\n" +
                    "return fract(sin(iTime)*1e4);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "\n" +
                    "if (!enabled) {\n" +
                    "if (manualMode) {\n" +
                    "vec2 rCoord = vec2(vTextureCoord.x + shiftR, vTextureCoord.y + shiftR);\n" +
                    "vec4 rColor = texture2D(sTexture, rCoord);\n" +
                    "\n" +
                    "vec2 gCoord = vec2(vTextureCoord.x + shiftG, vTextureCoord.y + shiftG);\n" +
                    "vec4 gColor = texture2D(sTexture, gCoord);\n" +
                    "\n" +
                    "vec2 bCoord = vec2(vTextureCoord.x + shiftB, vTextureCoord.y + shiftB);\n" +
                    "vec4 bColor = texture2D(sTexture, bCoord);\n" +
                    "\n" +
                    "\n" +
                    "vec4 r = vec4(rColor.r, 0, 0, 1);\n" +
                    "vec4 g = vec4(0, gColor.g, 0, 1);\n" +
                    "vec4 b = vec4(0, 0, bColor.b, 1);\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = r + g + b;\n" +
                    "\n" +
                    "} else {\n" +
                    "\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "vec2 uvR = uv;\n" +
                    "vec2 uvB = uv;\n" +
                    "\n" +
                    "if (offsetStrength < 0.0) {\n" +
                    "//                uvR.x = uv.x * 1.0 - rand() * 0.02 * 0.8;\n" +
                    "//                uvB.y = uv.y * 1.0 + rand() * 0.02 * 0.8;\n" +
                    "uvR.x = uv.x * 1.0 - (rand() * var1 * offsetStrength);\n" +
                    "uvB.y = uv.y * 1.0 + (rand() * var1 * offsetStrength);\n" +
                    "\n" +
                    "} else {\n" +
                    "uvR.x = uv.x * 1.0 - (rand() * var1 * offsetStrength);\n" +
                    "uvB.y = uv.y * 1.0 + (rand() * var1 * offsetStrength);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "//\n" +
                    "vec4 c;\n" +
                    "c.r = texture2D(sTexture, uvR).r;\n" +
                    "c.g = texture2D(sTexture, uv).g;\n" +
                    "c.b = texture2D(sTexture, uvB).b;\n" +
                    "\n" +
                    "gl_FragColor = c;\n" +
                    "}\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageShaderGlitchIOSRGB() {
        super(DEFAULT_VERTEX_SHADER, SHADERGLITCH_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("shiftR"), 1.0f);
        GLES20.glUniform1f(getHandle("shiftG"), 1.0f);
        GLES20.glUniform1f(getHandle("shiftB"), 1.0f);
        GLES20.glUniform1f(getHandle("offsetStrength"), 0.5f);
        GLES20.glUniform1f(getHandle("var1"), 0.1f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
