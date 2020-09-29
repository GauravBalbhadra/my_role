package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageShader_Vibration extends GlFilter {

    private static final String VIBRATION_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "uniform bool enableGlitch; // ## 0\n" +
                    "uniform float var1; // ## 0.0, 1.0, 0.02\n" +
                    "uniform float var2; // ## 0.0, 1.0, 0.02\n" +
                    "uniform float var3; // ## 0.0, 3.0, 0.8\n" +
                    "\n" +
                    "\n" +
                    "float rand (float value) {\n" +
                    "return fract(sin(value)*1e4);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "\n" +
                    "if (!enabled) {\n" +
                    "\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "\n" +
                    "if (!enableGlitch) {\n" +
                    "\n" +
                    "vec2 uvR = uv;\n" +
                    "vec2 uvB = uv;\n" +
                    "\n" +
                    "uvR.x = uv.x * 1.0 - rand(iTime) * var1 * var3;\n" +
                    "uvB.y = uv.y * 1.0 + rand(iTime) * var2 * var3;\n" +
                    "\n" +
                    "//\n" +
                    "if(uv.y < rand(iTime) && uv.y > rand(iTime) -0.1 && sin(iTime) < 0.0)\n" +
                    "{\n" +
                    "uv.x = (uv + 0.02 * rand(iTime)).x;\n" +
                    "}\n" +
                    "\n" +
                    "//\n" +
                    "vec4 c;\n" +
                    "c.r = texture2D(sTexture, uvR).r;\n" +
                    "c.g = texture2D(sTexture, uv).g;\n" +
                    "c.b = texture2D(sTexture, uvB).b;\n" +
                    "\n" +
                    "//\n" +
                    "float scanline = sin( uv.y * 800.0 * rand(iTime))/30.0;\n" +
                    "c *= 1.0 - scanline;\n" +
                    "\n" +
                    "//vignette\n" +
                    "float vegDist = length(( 0.5 , 0.5 ) - uv);\n" +
                    "c *= 1.0 - vegDist * 0.6;\n" +
                    "\n" +
                    "gl_FragColor = c;\n" +
                    "\n" +
                    "} else {\n" +
                    "\n" +
                    "uv.x = uv.x * 1.0 - rand(iTime) * var1 * var3;\n" +
                    "uv.y = uv.y * 1.0 - rand(iTime + 1.3456) * var2 * var3;\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = texture2D(sTexture, uv);\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageShader_Vibration() {
        super(DEFAULT_VERTEX_SHADER, VIBRATION_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("var1"), 0.02f);
        GLES20.glUniform1f(getHandle("var2"), 0.02f);
        GLES20.glUniform1f(getHandle("var3"), 0.08f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
