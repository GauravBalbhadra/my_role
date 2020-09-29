package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageHSVRainbow extends GlFilter {


    private static final String HSVRAINBOW_FRAGMENT_SHADER =
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
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float color; // ## 0.0, 1.0, 0.0\n" +
                    "\n" +
                    "\n" +
                    "vec4 hsvtorgb(in float h, in float s, in float v){\n" +
                    "    float f = h - floor(h);\n" +
                    "#define p v * (1.0 - s)\n" +
                    "#define q v * (1.0 - (s * f))\n" +
                    "#define t v * (1.0 - (s * (1.0 - f)))\n" +
                    "    if(h < 0.0 || h >= 6.0){\n" +
                    "        return vec4(v, p, 0.0, 1.0);\n" +
                    "    }else if(h >= 5.0){\n" +
                    "        return vec4(v, p, q, 1.0);\n" +
                    "    }else if(h >= 4.0){\n" +
                    "        return vec4(t, p, v, 1.0);\n" +
                    "    }else if(h >= 3.0){\n" +
                    "        return vec4(p, q, v, 1.0);\n" +
                    "    }else if(h >= 2.0){\n" +
                    "        return vec4(p, v, t, 1.0);\n" +
                    "    }else if(h >= 1.0){\n" +
                    "        return vec4(q, v, p, 1.0);\n" +
                    "    }else{\n" +
                    "        return vec4(v, t, p, 1.0);\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "void main(){\n" +
                    "    if (!enabled) {\n" +
                    "        vec4 src = texture2D(sTexture, gl_FragCoord.xy / resolution.xy);\n" +
                    "        float hue = (gl_FragCoord.xy / resolution.x).x * 6.0;\n" +
                    "        float sat = max(src.r, max(src.g, src.b)) - min(src.r, max(src.g, src.b));\n" +
                    "        float luma = src.r * 0.3 + src.g * 0.6 + src.b * 0.1;\n" +
                    "        gl_FragColor = hsvtorgb(hue, min(1.0, sat + 0.1 + (0.3*color)), luma);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "}\n" +
                    "\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageHSVRainbow() {
        super(DEFAULT_VERTEX_SHADER, HSVRAINBOW_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("color"), 0.75f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
