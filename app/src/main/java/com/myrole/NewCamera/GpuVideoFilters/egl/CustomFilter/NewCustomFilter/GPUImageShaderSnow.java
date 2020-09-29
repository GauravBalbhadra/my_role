package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageShaderSnow extends GlFilter {


    private static final String SHADDERSNOW_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "uniform bool snowMode; // ## 1\n" +
                    "uniform float snowDepth; // ## 0, 50, 20\n" +
                    "uniform float snowSize; // ## 0, 10, 5\n" +
                    "\n" +
                    "uniform float n; // ## 0, 5, 4\n" +
                    "uniform float attr_1; // ## 0, 1.0, 0.5\n" +
                    "uniform float attr_2; // ## 0, 1.0, 0.5\n" +
                    "uniform float attr_3; // ## 0, 1, 0.1\n" +
                    "uniform float attr_4; // ## 0, 1.0, 0.5\n" +
                    "uniform float attr_5; // ## 0, 1.0, 0.5\n" +
                    "\n" +
                    "uniform float attr_6; // ## 0, 5, 2.0\n" +
                    "uniform float attr_7; // ## 0, 1.0, 0\n" +
                    "uniform float attr_8; // ## 0, 5.0, 1.0\n" +
                    "\n" +
                    "uniform float attr_9; // ## 0, 1.0, 0.5\n" +
                    "\n" +
                    "#define pi 3.1415926\n" +
                    "\n" +
                    "float T;\n" +
                    "\n" +
                    "// iq's hash function from https://www.shadertoy.com/view/MslGD8\n" +
                    "vec2 hash( vec2 p ) { p=vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))); return fract(sin(p)*18.5453); }\n" +
                    "\n" +
                    "float simplegridnoise(vec2 v)\n" +
                    "{\n" +
                    "    float s = 1. / 256.;\n" +
                    "    vec2 fl = floor(v), fr = fract(v);\n" +
                    "    float mindist = 1e9;\n" +
                    "    for(int y = -1; y <= 1; y++)\n" +
                    "        for(int x = -1; x <= 1; x++)\n" +
                    "        {\n" +
                    "            vec2 offset = vec2(x, y);\n" +
                    "            vec2 pos = attr_4 + attr_5 * cos(2. * pi * (T*attr_3 + hash(fl+offset)) + vec2(0,1.6));\n" +
                    "            mindist = min(mindist, length(pos+offset -fr));\n" +
                    "        }\n" +
                    "    \n" +
                    "    return mindist;\n" +
                    "}\n" +
                    "\n" +
                    "float blobnoise(vec2 v, float s)\n" +
                    "{\n" +
                    "    return pow(attr_1 + attr_2 * cos(pi * clamp(simplegridnoise(v)*attr_6, attr_7, attr_8)), s);\n" +
                    "}\n" +
                    "\n" +
                    "float fractalblobnoise(vec2 v, float s)\n" +
                    "{\n" +
                    "    float val = 0.;\n" +
                    "    //    const float n = 4.;\n" +
                    "    for(float i = 0.; i < n; i++)\n" +
                    "        //val += 1.0 / (i + 1.0) * blobnoise((i + 1.0) * v + vec2(0.0, iGlobalTime * 1.0), s);\n" +
                    "        val += pow(attr_9, i+1.) * blobnoise(exp2(i) * v + vec2(0, T), s);\n" +
                    "    \n" +
                    "    return val;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        T = iTime;\n" +
                    "        \n" +
                    "        vec2 r = vec2(1.0, resolution.y / resolution.x);\n" +
                    "        //    vec2 uv = gl_FragCoord.xy / resolution.xy * vec2(1.0, -1.0);\n" +
                    "        vec2 uv1 = gl_FragCoord.xy / resolution.xy;\n" +
                    "        vec2 uv = vec2(uv1.x, 1.0-uv1.y);\n" +
                    "        \n" +
                    "        //vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "        float val = 0.0;\n" +
                    "        if (snowMode) {\n" +
                    "            val = fractalblobnoise(r * uv * snowDepth, snowSize);\n" +
                    "        } else {\n" +
                    "            val = blobnoise(r * uv * snowDepth, snowSize);\n" +
                    "        }\n" +
                    "        //float val = blobnoise(r * uv * 10.0, 5.0);\n" +
                    "        //fragColor = vec4(vec3(val), 1.0);\n" +
                    "        \n" +
                    "        gl_FragColor = mix(texture2D(sTexture, uv1), vec4(1.0), vec4(val));\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageShaderSnow() {
        super(DEFAULT_VERTEX_SHADER, SHADDERSNOW_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("snowDepth"), 20);
        GLES20.glUniform1f(getHandle("snowSize"), 5.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        GLES20.glUniform1f(getHandle("n"), 4);
        GLES20.glUniform1f(getHandle("attr_1"), 0.5f);
        GLES20.glUniform1f(getHandle("attr_2"), 0.5f);
        GLES20.glUniform1f(getHandle("attr_3"), 0.1f);
        GLES20.glUniform1f(getHandle("attr_4"), 0.5f);
        GLES20.glUniform1f(getHandle("attr_5"), 0.5f);
        GLES20.glUniform1f(getHandle("attr_6"), 2.0f);
        GLES20.glUniform1f(getHandle("attr_7"), 0);
        GLES20.glUniform1f(getHandle("attr_8"), 1.0f);
        GLES20.glUniform1f(getHandle("attr_9"), 0.5f);
        //GLES20.glUniform1f(getHandle("snowMode"), 0);

    }
}
