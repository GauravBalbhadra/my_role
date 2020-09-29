package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


//refrence:-https://www.shadertoy.com/view/4lXGzS VideoPixelate_GL.fsh

public class GPUImageVideoPixelate extends GlFilter {

    private static final String VIDEOPIXELATE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float progress;\n" +
                    "uniform float size;\n" +
                    "uniform float color;\n" +
                    "float yVar;\n" +
                    "vec2 s,g,m;\n" +
                    "\n" +
                    "vec3 bg(vec2 uv)\n" +
                    "{\n" +
                    "return mix(texture2D(sTexture,uv).rgb, texture2D(sTexture, uv).rgg, color);\n" +
                    "}\n" +
                    "\n" +
                    "vec3 effect(vec2 uv, vec3 col)\n" +
                    "{\n" +
                    " float granularity = yVar*20.+10.;\n" +
                    "if (granularity > 0.0)\n" +
                    " {\n" +
                    "float dx = granularity / s.x;\n" +
                    "float dy = granularity / s.y;\n" +
                    " uv = vec2(dx*(floor(uv.x/dx) + 0.5),\n" +
                    "dy*(floor(uv.y/dy) + 0.5));\n" +
                    "return bg(uv);\n" +
                    "}\n" +
                    "return col;\n" +
                    "}\n" +
                    "\n" +
                    "vec2 getUV()\n" +
                    "{\n" +
                    "    return g / s;\n" +
                    "}\n" +
                    "\n" +
                    "void main(){\n" +
                    " vec2 iMouse = vec2(progress * resolution.x, size * resolution.y);\n" +
                    " s = resolution.xy;\n" +
                    " g = gl_FragCoord.xy;\n" +
                    "m = iMouse.x==0.?m = s/2.:iMouse.xy;\n" +
                    "yVar = m.y/s.y;\n" +
                    "vec2 uv = getUV();\n" +
                    "vec3 tex = bg(uv);\n" +
                    "vec3 col = g.x<m.x?effect(uv,tex):tex;\n" +
                    "col = mix( col, vec3(0.), 1.-smoothstep( 1., 2., abs(m.x-g.x) ) );\n" +
                    "gl_FragColor = vec4(col, 1.0);\n" +
                    "}\n ";


    int heights;
    int widths;

    public GPUImageVideoPixelate() {
        super(DEFAULT_VERTEX_SHADER, VIDEOPIXELATE_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;


    }

    @Override
    protected void onDraw() {
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        float size = 0.3f;
        GLES20.glUniform1f(getHandle("color"), size);
        float color = 1.0f;
        GLES20.glUniform1f(getHandle("size"), color);
        float progress = 1.0f;
        GLES20.glUniform1f(getHandle("progress"), progress);

    }
}
