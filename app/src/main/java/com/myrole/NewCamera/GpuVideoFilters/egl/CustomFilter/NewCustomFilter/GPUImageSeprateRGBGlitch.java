package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageSeprateRGBGlitch extends GlFilter {


    private static final String SEPRATERGBGLITCH_FRAGMENT_SHADER =
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
                    "float hash( float n )\n" +
                    "{\n" +
                    "    return fract(sin(n)*43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "float noise( in vec3 x )\n" +
                    "{\n" +
                    "    vec3 p = floor(x);\n" +
                    "    vec3 f = fract(x);\n" +
                    "    \n" +
                    "    f = f*f*(3.0-2.0*f);\n" +
                    "    \n" +
                    "    float n = p.x + p.y*57.0 + 113.0*p.z;\n" +
                    "    \n" +
                    "    float res = mix(mix(mix( hash(n+  0.0), hash(n+  1.0),f.x),\n" +
                    "                        mix( hash(n+ 57.0), hash(n+ 58.0),f.x),f.y),\n" +
                    "                    mix(mix( hash(n+113.0), hash(n+114.0),f.x),\n" +
                    "                        mix( hash(n+170.0), hash(n+171.0),f.x),f.y),f.z);\n" +
                    "    return res;\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "        \n" +
                    "        float blurx = noise(vec3(iTime * 10.0, 0.0, 0.0)) * 2.0 - 1.0;\n" +
                    "        float offsetx = blurx * 0.025 * strength * 2.0;\n" +
                    "        \n" +
                    "        float blury = noise(vec3(iTime * 10.0, 1.0, 0.0)) * 2.0 - 1.0;\n" +
                    "        float offsety = blury * 0.01 * strength * 2.0;\n" +
                    "        \n" +
                    "        \n" +
                    "        vec2 ruv = uv + vec2(offsetx, offsety);\n" +
                    "        vec2 guv = uv + vec2(-offsetx, -offsety);\n" +
                    "        vec2 buv = uv + vec2(0.00, 0.0);\n" +
                    "        \n" +
                    "        float r = texture2D(sTexture, ruv).r;\n" +
                    "        float g = texture2D(sTexture, guv).g;\n" +
                    "        float b = texture2D(sTexture, buv).b;\n" +
                    "        \n" +
                    "        gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    float strength = 1.0f;

    public GPUImageSeprateRGBGlitch() {
        super(DEFAULT_VERTEX_SHADER, SEPRATERGBGLITCH_FRAGMENT_SHADER);

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
