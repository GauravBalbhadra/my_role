package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderNoice2 extends GlFilter {
    private static final String SHADERNOICE2_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "\n" +
                    "uniform float var1; // ## 0, 1.0, 0.5\n" +
                    "uniform float var2; // ## 0, 1.0, 0.3\n" +
                    "uniform float var3; // ## 0, 5.0, 0.8\n" +
                    "\n" +
                    "uniform int hash22Mode; // ## 1, 3, 1\n" +
                    "\n" +
                    "\n" +
                    "// Standard 2x2 hash algorithm.\n" +
                    "vec2 hash22(vec2 p) {\n" +
                    "\n" +
                    "\n" +
                    "// Faster, but probaly doesn't disperse things as nicely as other methods.\n" +
                    "if (hash22Mode == 1) {\n" +
                    "float n = sin(dot(p, vec2(41, 2890)));\n" +
                    "p = fract(vec2(2097152, 262144)*n);\n" +
                    "return cos(p*6.283 + iTime*2.);\n" +
                    "\n" +
                    "} else if (hash22Mode == 2) {\n" +
                    "return abs(fract(p+ iTime*.5)-.5)*4.-1.; // Snooker.\n" +
                    "\n" +
                    "} else if (hash22Mode == 3) {\n" +
                    "return abs(cos(p*6.283 + iTime*2.))*2.-1.; // Bounce.\n" +
                    "}\n" +
                    "\n" +
                    "//return abs(fract(p+ iGlobalTime*.5)-.5)*4.-1.; // Snooker.\n" +
                    "//return abs(cos(p*6.283 + iGlobalTime*2.))*2.-1.; // Bounce.\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "vec4 tex = texture2D(sTexture, uv);\n" +
                    "vec2 noiseUV = hash22(tex.xy);\n" +
                    "\n" +
                    "\n" +
                    "//float r = uv.x + 0.2 * uv.y;\n" +
                    "//        float r = (tex.x + tex.y + noiseUV.x + noiseUV.y)  * 0.5 - 0.3;\n" +
                    "//        gl_FragColor = vec4(r,r, r, 0.8);\n" +
                    "\n" +
                    "float r = (tex.x + tex.y + noiseUV.x + noiseUV.y)  * var1 - var2;\n" +
                    "gl_FragColor = vec4(r,r, r, var3);\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageShaderNoice2() {
        super(DEFAULT_VERTEX_SHADER, SHADERNOICE2_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("var1"), 0.5f);
        GLES20.glUniform1f(getHandle("var2"), 0.3f);
        GLES20.glUniform1f(getHandle("var3"), 0.8f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
