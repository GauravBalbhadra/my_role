package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageDisco extends GlFilter {


    private static final String DISCO_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "// Textures\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "// ## noise_64.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform bool enabled;           // ## 1\n" +
                    "//uniform float progress;\n" +
                    "//uniform float strength;\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "uniform float var_1; // ## 0, 5, 2.0\n" +
                    "uniform float var_2; // ## 0, 10.0, 2.0\n" +
                    "\n" +
                    "uniform float var_3; // ## 0, 1000, 200.0\n" +
                    "uniform float var_4; // ## 0, 1000, 200.0\n" +
                    "uniform float var_5; // ## 0, 1000, 200.0\n" +
                    "\n" +
                    "uniform float var_6; // ## 0, 1000, 500.0\n" +
                    "uniform float var_7; // ## 0, 1000, 500.0\n" +
                    "uniform float var_8; // ## 0, 1000, 500.0\n" +
                    "\n" +
                    "uniform float var_9; // ## 0, 5.0, 2.0\n" +
                    "uniform float var_10; // ## 0, 5.0, 2.0\n" +
                    "uniform float var_11; // ## 0, 5.0, 2.0\n" +
                    "\n" +
                    "uniform float var_12; // ## 0, 1.0, 0.25\n" +
                    "\n" +
                    "uniform float var_13; // ## 0, 1.0, 1.0\n" +
                    "uniform float var_14; // ## 0, 1.0, 1.0\n" +
                    "uniform float var_15; // ## 0, 1.0, 1.0\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "#define M_PI 3.14159265358979323846\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    vec4 orig = texture2D(sTexture, uv);\n" +
                    "    \n" +
                    "    float frame = mod(floor(60.0 * iTime), 1.0);\n" +
                    "    float t = iTime * (var_1 + 0.05 * frame * sin(iTime));\n" +
                    "    \n" +
                    "    vec2 center = vec2(0.5, 0.5);\n" +
                    "    \n" +
                    "    uv = uv - center;\n" +
                    "    uv = (var_2 + sin(t)) * uv;\n" +
                    "    uv = mod(uv, 2.0);\n" +
                    "    uv = abs(uv - vec2(1.0, 1.0));\n" +
                    "    \n" +
                    "    vec2 rot;\n" +
                    "    rot.x = 0.5 + (uv.x - 0.5) * cos(t) - (uv.y - 0.5) * sin(t);\n" +
                    "    rot.y = 0.5 + (uv.y - 0.5) * cos(t) + (uv.x - 0.5) * sin(t);\n" +
                    "    uv = rot;\n" +
                    "    \n" +
                    "    vec4 tex =  texture2D(sTexture, uv);\n" +
                    "    vec4 tex0, tex1, tex2, tex3;\n" +
                    "    \n" +
                    "    if(orig.r >= 0.01){\n" +
                    "        tex0 = texture2D(sTexture, vec2(uv.x + 1.0 / resolution.x, uv.y + 1.0 / resolution.y));\n" +
                    "        tex1 = texture2D(sTexture, vec2(uv.x - 1.0 / resolution.x, uv.y + 1.0 / resolution.y));\n" +
                    "        tex2 = texture2D(sTexture, vec2(uv.x - 1.0 / resolution.x, uv.y - 1.0 / resolution.y));\n" +
                    "        tex3 = texture2D(sTexture, vec2(uv.x + 1.0 / resolution.x, uv.y - 1.0 / resolution.y));\n" +
                    "    }\n" +
                    "    else{\n" +
                    "        tex0 = texture2D(sTexture, vec2(uv.x + 1.0 / resolution.x, uv.y + 1.0 / resolution.y));\n" +
                    "        tex1 = texture2D(sTexture, vec2(uv.x - 1.0 / resolution.x, uv.y + 1.0 / resolution.y));\n" +
                    "        tex2 = texture2D(sTexture, vec2(uv.x - 1.0 / resolution.x, uv.y - 1.0 / resolution.y));\n" +
                    "        tex3 = texture2D(sTexture, vec2(uv.x + 1.0 / resolution.x, uv.y - 1.0 / resolution.y));\n" +
                    "    }\n" +
                    "    \n" +
                    "    vec4 d = vec4(length(tex - tex0), length(tex - tex1), length(tex - tex2), 1.0);\n" +
                    "    \n" +
                    "    float r1 = pow(0.5 + 0.5 * sin(t * 1.1 + 0.3 * uv.x), var_3);\n" +
                    "    float r2 = pow(0.5 + 0.5 * sin(t * 1.4 - 0.3 * uv.x), var_4);\n" +
                    "    float r3 = pow(0.5 + 0.5 * sin(t * 1.3 + 0.3 * uv.y), var_5);\n" +
                    "    \n" +
                    "    float r4 = pow(0.5 + 0.5 * sin(t * 21.0), var_6);\n" +
                    "    float r5 = pow(0.5 + 0.5 * sin(t * 33.4), var_7);\n" +
                    "    float r6 = pow(0.5 + 0.5 * sin(t * 47.4), var_8);\n" +
                    "    \n" +
                    "    float r00 = var_9 + sin(t * 2.0);\n" +
                    "    float r01 = var_10 + sin(t * 3.4);\n" +
                    "    float r02 = var_11 + sin(t * 5.4);\n" +
                    "    \n" +
                    "\n" +
                    "    gl_FragColor = var_12 * tex + vec4(r00, r01, r02, var_13) * d + vec4(r1, r2, r3, var_14) * orig + vec4(r4, r5, r6, var_15) * orig;\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "//    gl_FragColor = vec4(mix(video.rgb, overlay.rgb, overlay.a * opacity), video.a);\n" +
                    "//    gl_FragColor = var_12 * tex + vec4(r00, r01, r02, var_13) * d;// + vec4(r1, r2, r3, var_14) * orig + vec4(r4, r5, r6, var_15) * orig;\n" +
                    "//    gl_FragColor = vec4(r1, r2, r3, var_14) * orig + orig;\n" +
                    "//    gl_FragColor = var_12 * tex + vec4(r00, r01, r02, var_13) * d + vec4(r1, r2, r3, var_14) * orig + orig;\n" +
                    "    \n" +
                    "}\n";


    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageDisco() {
        super(DEFAULT_VERTEX_SHADER, DISCO_FRAGMENT_SHADER);

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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("var_1"), 2.0f);
        GLES20.glUniform1f(getHandle("var_2"), 2.0f);
        GLES20.glUniform1f(getHandle("var_3"), 200.0f);
        GLES20.glUniform1f(getHandle("var_4"), 200.0f);
        GLES20.glUniform1f(getHandle("var_5"), 200.0f);
        GLES20.glUniform1f(getHandle("var_6"), 500.0f);
        GLES20.glUniform1f(getHandle("var_7"), 500.0f);
        GLES20.glUniform1f(getHandle("var_8"), 500.0f);
        GLES20.glUniform1f(getHandle("var_9"), 2.0f);
        GLES20.glUniform1f(getHandle("var_10"), 2.0f);
        GLES20.glUniform1f(getHandle("var_11"), 2.0f);
        GLES20.glUniform1f(getHandle("var_12"), 0.25f);
        GLES20.glUniform1f(getHandle("var_13"), 1.0f);
        GLES20.glUniform1f(getHandle("var_14"), 1.0f);
        GLES20.glUniform1f(getHandle("var_15"), 1.0f);


    }


}
