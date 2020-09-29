package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderMirror extends GlFilter {
    private static final String SHADERNOICE2_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "\n" +
                    "uniform float var1; // ## 0, 1.0, 0.5\n" +
                    "\n" +
                    "\n" +
                    "const float PI = 3.1415926535897932384626433832795;\n" +
                    "const float PI_4 = 0.785398163397448;\n" +
                    "\n" +
                    "\n" +
                    "vec4 MirroringByYAxis(vec2 uv, float s, in sampler2D top, in sampler2D bottom)\n" +
                    "{\n" +
                    "if(uv.x < var1)\n" +
                    "{\n" +
                    "vec2 st = vec2( ((uv.x - 0.5) * s) + 0.5, ((uv.y - 0.5) * s) + 0.5 );\n" +
                    "return texture2D(top, st, -10.0);\n" +
                    "}\n" +
                    "else\n" +
                    "{\n" +
                    "vec2 st = vec2( ((uv.x - 0.5) * s) + 0.5, ((uv.y - 0.5) * s) + 0.5 );\n" +
                    "st.x = 1.0 - st.x;\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "}\n" +
                    "\n" +
                    "vec4 MirroringByXAxis(vec2 uv, float s, in sampler2D top, in sampler2D bottom)\n" +
                    "{\n" +
                    "if(uv.y < var1)\n" +
                    "{\n" +
                    "vec2 st = vec2( ((uv.x - 0.5) * s) + 0.5, ((uv.y - 0.5) * s) + 0.5 );\n" +
                    "return texture2D(top, st, -10.0);\n" +
                    "}\n" +
                    "else\n" +
                    "{\n" +
                    "vec2 st = vec2( ((uv.x - 0.5) * s) + 0.5, ((uv.y - 0.5) * s) + 0.5 );\n" +
                    "st.x = 1.0 - st.x;\n" +
                    "return texture2D(bottom, st, -10.0);\n" +
                    "}\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "if (!enabled) {\n" +
                    "\n" +
                    "gl_FragColor = MirroringByYAxis(vTextureCoord, 1.0, sTexture, sTexture);\n" +
                    "// MirroringByXAxis(vTextureCoord, 1.0, sTexture, sTexture);\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageShaderMirror() {
        super(DEFAULT_VERTEX_SHADER, SHADERNOICE2_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("var1"), 0.5f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
