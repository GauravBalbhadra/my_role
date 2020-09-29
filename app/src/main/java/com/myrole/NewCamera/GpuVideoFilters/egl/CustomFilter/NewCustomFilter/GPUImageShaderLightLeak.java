package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageShaderLightLeak extends GlFilter {


    private static final String SHADDERLIGHTLEAK_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float progress;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "const vec3 red = vec3(1.0, 0.0, 0.0);\n" +
                    "const vec3 yellow = vec3(1.0, 1.0, 0.0);\n" +
                    "\n" +
                    "\n" +
                    "float Circle( vec2 p, float r )\n" +
                    "{\n" +
                    "    return length(p)-r;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "float random(vec2 co)\n" +
                    "{\n" +
                    "    float a = 12.9898;\n" +
                    "    float b = 78.233;\n" +
                    "    float c = 43758.5453;\n" +
                    "    float dt= dot(co.xy ,vec2(a,b));\n" +
                    "    float sn= mod(dt,3.14);\n" +
                    "    return fract(sin(sn) * c);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    \n" +
                    "    vec3 base = texture2D(sTexture, uv).xyz;\n" +
                    "    \n" +
                    "    float t = 1.0 - progress;\n" +
                    "    \n" +
                    "    uv.x -= t - 0.3;\n" +
                    "    \n" +
                    "    \n" +
                    "    if(!enabled)\n" +
                    "    {\n" +
                    "        vec3 noise = vec3(random(uv * 1.5), random(uv * 2.5), random(uv));\n" +
                    "        \n" +
                    "        uv.x = clamp(0.0, 1.0, uv.x);\n" +
                    "        \n" +
                    "        float weight = 0.2 + (smoothstep(0.99, 1.0, uv.x + random(uv) * 0.1) * 0.8);\n" +
                    "        \n" +
                    "        vec3 src = mix(red, yellow, weight * 0.2);\n" +
                    "        \n" +
                    "        src = mix(src, noise, 0.15);\n" +
                    "        \n" +
                    "        vec3 color = mix(base, src, uv.x);\n" +
                    "        \n" +
                    "        gl_FragColor = vec4(color, 1.0);\n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "        gl_FragColor = vec4(base, 1.0);\n" +
                    "    }\n" +
                    "}";
    int heights;
    int widths;
    public GPUImageShaderLightLeak() {
        super(DEFAULT_VERTEX_SHADER, SHADDERLIGHTLEAK_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;

    }

    @Override
    public void onDraw() {
//        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
//        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("progress"), 0.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
