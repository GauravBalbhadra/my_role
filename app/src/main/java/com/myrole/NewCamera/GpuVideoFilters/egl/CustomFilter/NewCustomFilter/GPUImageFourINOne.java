package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageFourINOne extends GlFilter {

    private static final String FOURINIONE_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform bool enabled;\n" +
                    "uniform float coordinate; // 0.5\n" +
                    "\n" +
                    "\n" +
                    "const float PI = 3.1415926535897932384626433832795;\n" +
                    "const float PI_4 = 0.785398163397448;\n" +
                    "\n" +
                    "\n" +
                    "vec4 SplitInFour(vec2 uv, float s, in sampler2D top, in sampler2D bottom)\n" +
                    "{\n" +
                    "if(uv.x < coordinate)\n" +
                    "{\n" +
                    "if (uv.y > coordinate)\n" +
                    "{\n" +
                    "vec2 st = vec2( (uv.x * 2.0), (uv.y * 2.0) - 1.0);\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 st = vec2( (uv.x * 2.0), (uv.y * 2.0));\n" +
                    "return texture2D(top, st, -10.0);\n" +
                    "}\n" +
                    "\n" +
                    "else if (uv.x > coordinate){\n" +
                    "if (uv.y < coordinate)\n" +
                    "{\n" +
                    "vec2 st = vec2( (uv.x * 2.0) - 1.0, (uv.y * 2.0));\n" +
                    "return texture2D(top, st, -10.0);\n" +
                    "}\n" +
                    "vec2 st = vec2( (uv.x * 2.0) - 1.0, (uv.y * 2.0) - 1.0);\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "vec4 f = vec4(1.,1.,1.,1.);\n" +
                    "\n" +
                    "if (!enabled) {\n" +
                    "gl_FragColor = SplitInFour(vTextureCoord, 1.0, sTexture, sTexture);\n" +
                    "if (uv.x < coordinate){\n" +
                    "if (uv.y > coordinate)\n" +
                    "{\n" +
                    "vec3 col = vec3(0.6627450980392157, 0.19215686274509805, 0.6);\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "\n" +
                    "}else{\n" +
                    "vec3 col = vec3(0.9176470588235294, 0.9254901960784314, 0.5882352941176471);\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "}\n" +
                    "}else if (uv.x > coordinate){\n" +
                    "if (uv.y < coordinate)\n" +
                    "{\n" +
                    "vec3 col =  vec3(0.2627450980392157, 0.7529411764705882, 0.6745098039215687);\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "}else{\n" +
                    "vec3 col = vec3(0.9803921568627451, 0.0196078431372549, 0.34901960784313724);\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "}\n" +
                    "}\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageFourINOne() {
        super(DEFAULT_VERTEX_SHADER, FOURINIONE_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;

    }

    @Override
    public void onDraw() {
        //  float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
//        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("coordinate"), 0.5f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
