package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageSmoky extends GlFilter {

    private static final String SMOKY_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "float rand(vec2 n) {\n" +
                    "return fract(cos(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "float noise(vec2 n) {\n" +
                    "const vec2 d = vec2(0.0, 1.0);\n" +
                    "vec2 b = floor(n), f = smoothstep(vec2(0.0), vec2(1.0), fract(n));\n" +
                    "return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);\n" +
                    "}\n" +
                    "\n" +
                    "float fbm(vec2 n) {\n" +
                    "float total = 0.0, amplitude = 1.0;\n" +
                    "for (int i = 0; i < 4; i++) {\n" +
                    "total += noise(n) * amplitude;\n" +
                    "n += n;\n" +
                    "amplitude *= 0.5;\n" +
                    "}\n" +
                    "return total;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "const vec3 c1 = vec3( 255.0/255.0,  255.0/255.0, 255.0/255.0);\n" +
                    "const vec3 c2 = vec3( 000.0/255.0,  000.0/255.0, 000.0/255.0);\n" +
                    "const vec3 c3 = vec3( 000.0/255.0,  000.0/255.0, 000.0/255.0);\n" +
                    "const vec3 c4 = vec3( 000.0/255.0,  000.0/255.0, 000.0/255.0);\n" +
                    "const vec3 c5 = vec3( 055.0/255.0,  055.0/255.0, 055.0/255.0);\n" +
                    "const vec3 c6 = vec3( 000.0/255.0,  000.0/255.0, 000.0/255.0);\n" +
                    "\n" +
                    "\n" +
                    "vec2 speed = vec2(0.1, 0.2);\n" +
                    "float shift = 3.0;\n" +
                    "\n" +
                    "vec2 p = gl_FragCoord.xy * 6.0 / resolution.xx;\n" +
                    "float q = fbm(p - iTime * 0.9);\n" +
                    "vec2 r = vec2(fbm(p + q + iTime * speed.x - p.x - p.y), fbm(p + q - iTime * speed.y));\n" +
                    "vec3 c = -mix(c1, c2, fbm(p + r)) - mix(c3, c4, r.x) - mix(c5, c6, r.y);\n" +
                    "\n" +
                    "//CHANGING COS TO SIN WILL FADE THE EFFECT\n" +
                    "gl_FragColor = vec4(c * cos(shift * gl_FragCoord.y / resolution.y), 1.0) + texture2D(sTexture,vTextureCoord);\n" +
                    "\n" +
                    "float grad = gl_FragCoord.y / resolution.y;\n" +
                    "gl_FragColor.xyz *= 1.0-grad;\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageSmoky() {
        super(DEFAULT_VERTEX_SHADER, SMOKY_FRAGMENT_SHADER);

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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
