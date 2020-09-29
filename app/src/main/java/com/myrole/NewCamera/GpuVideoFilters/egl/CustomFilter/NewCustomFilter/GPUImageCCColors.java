package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


//Refrence:-https://www.shadertoy.com/view/4l2GWy CCColors_GL.fsh

public class GPUImageCCColors extends GlFilter {
    public static final String CCCOLORS_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float progress;\n" +
                    "\n" +
                    "#define R_THRESHOLD 0.5\n" +
                    "#define G_THRESHOLD 0.5\n" +
                    "#define B_THRESHOLD 0.5\n" +
                    "#define I_THRESHOLD 0.5\n" +
                    "\n" +
                    "vec4 palette[16];\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    palette[ 0] = vec4(0., 0., 0., 1.);\n" +
                    "    palette[ 1] = vec4(76./255., 76./255., 76./255., 1.);\n" +
                    "    palette[ 2] = vec4(37./255., 49./255., 146./255., 1.);\n" +
                    "    palette[ 3] = vec4(37./255., 49./255., 146./255., 1.);\n" +
                    "    palette[ 4] = vec4(87./255., 166./255., 78./255., 1.);\n" +
                    "    palette[ 5] = vec4(127./255., 204./255., 25./255., 1.);\n" +
                    "    palette[ 6] = vec4(76./255., 153./255., 178./255., 1.);\n" +
                    "    palette[ 7] = vec4(153./255., 178./255., 242./255., 1.);\n" +
                    "    palette[ 8] = vec4(204./255., 76./255., 76./255., 1.);\n" +
                    "    palette[ 9] = vec4(204./255., 76./255., 76./255., 1.);\n" +
                    "    palette[10] = vec4(178./255., 102./255., 229./255., 1.);\n" +
                    "    palette[11] = vec4(229./255., 127./255., 216./255., 1.);\n" +
                    "    palette[12] = vec4(127./255., 102./255., 76./255., 1.);\n" +
                    "    palette[13] = vec4(222./255., 222./255., 108./255., 1.);\n" +
                    "    palette[14] = vec4(153./255., 153./255., 153./255., 1.);\n" +
                    "    palette[15] = vec4(240./255., 240./255., 240./255., 1.);\n" +
                    "    \n" +
                    "    vec2 iMouse = vec2(progress * resolution.x, progress * resolution.y);\n" +
                    "    \n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    float mousex = iMouse.x != 0. ? iMouse.x/resolution.x : 0.5;\n" +
                    "    if(uv.x > mousex) {\n" +
                    "        gl_FragColor = texture2D(sTexture, uv);\n" +
                    "        return;\n" +
                    "    }\n" +
                    "    vec3 RGB = texture2D(sTexture, uv).xyz;\n" +
                    "    vec4 rgbi = vec4(\n" +
                    "                     RGB.r > R_THRESHOLD,\n" +
                    "                     RGB.g > G_THRESHOLD,\n" +
                    "                     RGB.b > B_THRESHOLD,\n" +
                    "                     (RGB.r + RGB.g + RGB.b) > I_THRESHOLD);\n" +
                    "    int palIdx = int(rgbi.r * 8. + rgbi.g * 4. + rgbi.b * 2. + rgbi.a);\n" +
                    "    if(palIdx==0)\n" +
                    "        gl_FragColor = palette[0];\n" +
                    "    else if(palIdx==1)\n" +
                    "        gl_FragColor = palette[1];\n" +
                    "    else if(palIdx==2)\n" +
                    "        gl_FragColor = palette[2];\n" +
                    "    else if(palIdx==3)\n" +
                    "        gl_FragColor = palette[3];\n" +
                    "    else if(palIdx==4)\n" +
                    "        gl_FragColor = palette[4];\n" +
                    "    else if(palIdx==5)\n" +
                    "        gl_FragColor = palette[5];\n" +
                    "    else if(palIdx==6)\n" +
                    "        gl_FragColor = palette[6];\n" +
                    "    else if(palIdx==7)\n" +
                    "        gl_FragColor = palette[7];\n" +
                    "    else if(palIdx==8)\n" +
                    "        gl_FragColor = palette[8];\n" +
                    "    else if(palIdx==9)\n" +
                    "        gl_FragColor = palette[9];\n" +
                    "    else if(palIdx==10)\n" +
                    "        gl_FragColor = palette[10];\n" +
                    "    else if(palIdx==11)\n" +
                    "        gl_FragColor = palette[11];\n" +
                    "    else if(palIdx==12)\n" +
                    "        gl_FragColor = palette[12];\n" +
                    "    else if(palIdx==13)\n" +
                    "        gl_FragColor = palette[13];\n" +
                    "    else if(palIdx==14)\n" +
                    "        gl_FragColor = palette[14];\n" +
                    "    else if(palIdx==15)\n" +
                    "        gl_FragColor = palette[15];\n" +
                    "    \n" +
                    "}\n";


    int heights;
    int widths;

    public GPUImageCCColors() {
        super(DEFAULT_VERTEX_SHADER, CCCOLORS_FRAGMENT_SHADER);
    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;
    }

    @Override
    protected void onDraw() {
        float progress = 0.5f;
        GLES20.glUniform1f(getHandle("progress"), progress);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }
}
