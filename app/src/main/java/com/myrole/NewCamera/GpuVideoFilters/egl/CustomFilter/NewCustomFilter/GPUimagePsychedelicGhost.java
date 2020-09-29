package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUimagePsychedelicGhost extends GlFilter {
    private static final String PHSCHEDELIC_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "uniform float strength;\n" +
                    "\n" +
                    "\n" +
                    "vec3 rainbow(float h) {\n" +
                    "    h = mod(mod(h, 1.0) + 1.0, 1.0);\n" +
                    "    float h6 = h * 6.0;\n" +
                    "    float r = clamp(h6 - 4.0, 0.0, 1.0) +\n" +
                    "    clamp(2.0 - h6, 0.0, 1.0);\n" +
                    "    float g = h6 < 2.0\n" +
                    "    ? clamp(h6, 0.0, 1.0)\n" +
                    "    : clamp(4.0 - h6, 0.0, 1.0);\n" +
                    "    float b = h6 < 4.0\n" +
                    "    ? clamp(h6 - 2.0, 0.0, 1.0)\n" +
                    "    : clamp(6.0 - h6, 0.0, 1.0);\n" +
                    "    return vec3(r, g, b);\n" +
                    "}\n" +
                    "\n" +
                    "vec3 plasma(vec2 fragCoord)\n" +
                    "{\n" +
                    "    const float speed = 12.0;\n" +
                    "    \n" +
                    "    const float scale = 2.5;\n" +
                    "    \n" +
                    "    const float startA = 563.0 / 512.0;\n" +
                    "    const float startB = 233.0 / 512.0;\n" +
                    "    const float startC = 4325.0 / 512.0;\n" +
                    "    const float startD = 312556.0 / 512.0;\n" +
                    "    \n" +
                    "    const float advanceA = 6.34 / 512.0 * 18.2 * speed;\n" +
                    "    const float advanceB = 4.98 / 512.0 * 18.2 * speed;\n" +
                    "    const float advanceC = 4.46 / 512.0 * 18.2 * speed;\n" +
                    "    const float advanceD = 5.72 / 512.0 * 18.2 * speed;\n" +
                    "    \n" +
                    "    vec2 uv = fragCoord * scale / resolution.xy;\n" +
                    "    \n" +
                    "    float a = startA + iTime * advanceA;\n" +
                    "    float b = startB + iTime * advanceB;\n" +
                    "    float c = startC + iTime * advanceC;\n" +
                    "    float d = startD + iTime * advanceD;\n" +
                    "    \n" +
                    "    float n = sin(a + 3.0 * uv.x) +\n" +
                    "    sin(b - 4.0 * uv.x) +\n" +
                    "    sin(c + 2.0 * uv.y) +\n" +
                    "    sin(d + 5.0 * uv.y);\n" +
                    "    \n" +
                    "    n = mod(((4.0 + n) / 4.0), 1.0);\n" +
                    "    \n" +
                    "    vec2 tuv = fragCoord.xy / resolution.xy;\n" +
                    "    n += texture2D(sTexture, tuv).r;\n" +
                    "    \n" +
                    "    return rainbow(n);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec3 green = vec3(0.173, 0.5, 0.106);\n" +
                    "        vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "        vec3 britney = texture2D(sTexture, uv).rgb;\n" +
                    "        float greenness = 1.0 - (length(britney - green) / length(vec3(1, 1, 1)));\n" +
                    "        float britneyAlpha = clamp(((greenness - 0.7) / 0.2)* strength * 2.0, 0.0, 1.0);\n" +
                    "        gl_FragColor = vec4(britney * (1.0 - britneyAlpha), 1.0) + vec4(plasma(gl_FragCoord.xy) * britneyAlpha, 1.0);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    float strength = 1.0f;

    public GPUimagePsychedelicGhost() {
        super(DEFAULT_VERTEX_SHADER, PHSCHEDELIC_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("strength"), strength);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
