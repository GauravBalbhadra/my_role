package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderbillBoard extends GlFilter {
    private static final String SHADERBILLBOARD_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform bool enabled;\n" +
                    "uniform float amplitude;\n" +
                    "\n" +
                    "// Created by Stephane Cuillerdier - Aiekick/2015\n" +
                    "// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.\n" +
                    "\n" +
                    "// Splitter framework\n" +
                    "// put tranform effect in effect function\n" +
                    "// put source to tranform in bg function\n" +
                    "// getUV is the func for the define of the coord system\n" +
                    "\n" +
                    "// global yVar is the var controled by y mouse axis from range 0. to 1.\n" +
                    "// s => iResolution.xy\n" +
                    "// g => fragCoord.xy\n" +
                    "// m => iMouse.xy\n" +
                    "/////VARS//////////////\n" +
                    "float yVar;\n" +
                    "vec2 s,g,m;\n" +
                    "///////////////////////\n" +
                    "\n" +
                    "//your funcs here if you want\n" +
                    "\n" +
                    "///////////////////////\n" +
                    "// source to transform\n" +
                    "vec3 bg(vec2 uv)\n" +
                    "{\n" +
                    "return texture2D(sTexture, uv).rgg;\n" +
                    "}\n" +
                    "\n" +
                    "///////////////////////\n" +
                    "// transform effect\n" +
                    "vec3 effect(vec2 uv, vec3 col)\n" +
                    "{\n" +
                    "float grid = yVar * 10.+5.;\n" +
                    "float step_x = 0.0015625;\n" +
                    "float step_y = step_x * s.x / s.y;\n" +
                    "float offx = floor(uv.x  / (grid * step_x));\n" +
                    "float offy = floor(uv.y  / (grid * step_y));\n" +
                    "vec3 res = bg(vec2(offx * grid * step_x , offy * grid * step_y));\n" +
                    "vec2 prc = fract(uv / vec2(grid * step_x, grid * step_y));\n" +
                    "vec2 pw = pow(abs(prc - 0.5), vec2(2.0));\n" +
                    "float  rs = pow(0.45, 2.0);\n" +
                    "float gr = smoothstep(rs - 0.1, rs + 0.1, pw.x + pw.y);\n" +
                    "float y = (res.r + res.g + res.b) / 3.0;\n" +
                    "vec3 ra = res / y;\n" +
                    "float ls = 0.3;\n" +
                    "float lb = ceil(y / ls);\n" +
                    "float lf = ls * lb + 0.3;\n" +
                    "res = lf * res;\n" +
                    "col = mix(res, vec3(0.1, 0.1, 0.1), gr);\n" +
                    "return col;\n" +
                    "}\n" +
                    "\n" +
                    "///////////////////////\n" +
                    "// screen coord system\n" +
                    "vec2 getUV()\n" +
                    "{\n" +
                    "return g / s;\n" +
                    "}\n" +
                    "\n" +
                    "///////////////////////\n" +
                    "/////do not modify////\n" +
                    "///////////////////////\n" +
                    "void main ()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "s = resolution.xy;\n" +
                    "g = gl_FragCoord.xy;\n" +
                    "\n" +
                    "m = s / (10. / (amplitude * 10.));\n" +
                    "yVar = 0.5;\n" +
                    "\n" +
                    "vec2 uv = getUV();\n" +
                    "vec3 tex = bg(uv);\n" +
                    "vec3 col = g.x<m.x?effect(uv,tex):tex;\n" +
                    "\n" +
                    "col = mix( col, vec3(0.), 1.-smoothstep( 1., 2., abs(m.x-g.x) ) );\n" +
                    "gl_FragColor = vec4(col,1.);\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageShaderbillBoard() {
        super(DEFAULT_VERTEX_SHADER, SHADERBILLBOARD_FRAGMENT_SHADER);

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
        // GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("amplitude"), 1.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
