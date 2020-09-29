package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageThreeINOne extends GlFilter {

    private static final String THREEINIONE_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "const float PI = 3.1415926535897932384626433832795;\n" +
                    "const float PI_4 = 0.785398163397448;\n" +
                    "\n" +
                    "\n" +
                    "vec4 ThreeInOne(vec2 uv, float s, in sampler2D top, in sampler2D bottom)\n" +
                    "{\n" +
                    "if(uv.x < 0.33)\n" +
                    "{\n" +
                    "vec2 st = vec2( (uv.x), (uv.y));\n" +
                    "return texture2D(top, st, -10.0);\n" +
                    "}\n" +
                    "\n" +
                    "else if (0.33 < uv.x )\n" +
                    "{\n" +
                    "if (0.66 < uv.x){\n" +
                    "vec2 st = vec2( (uv.x) - 0.66 , (uv.y));\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 st = vec2( (uv.x) - 0.33 , (uv.y));\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "gl_FragColor = ThreeInOne(vTextureCoord, 1.0, sTexture, sTexture);\n" +
                    "\n" +
                    "\n" +
                    "if(uv.x < 0.33)\n" +
                    "{\n" +
                    "vec3 col = vec3(0.8980392156862745, 0.8980392156862745, 0.8980392156862745);\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "\n" +
                    "}\n" +
                    "else if (0.33 < uv.x )\n" +
                    "{\n" +
                    "if (0.66 < uv.x)\n" +
                    "{\n" +
                    "vec3 col =  vec3(1.0, 0.30196078431372547, 0.30196078431372547);\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "}\n" +
                    "else\n" +
                    "{\n" +
                    "vec3 col = vec3(0.49019607843137253, 1.0, 0.30196078431372547);\n" +
                    "\n" +
                    "// Output to screen\n" +
                    "vec4 fragColor = vec4(col,1.0);\n" +
                    "gl_FragColor = gl_FragColor * fragColor;\n" +
                    "}\n" +
                    "}\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageThreeINOne() {
        super(DEFAULT_VERTEX_SHADER, THREEINIONE_FRAGMENT_SHADER);

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
//        GLES20.glUniform1f(getHandle("wobble"), 1.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }
}
