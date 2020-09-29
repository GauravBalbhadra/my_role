package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GpuImageASCII extends GlFilter {
    private static final String ASCII_FRAGMENT_SHADER =
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
                    "uniform float zoom; // ## 2.0, 5.0, 3.0\n" +
                    "\n" +
                    "\n" +
                    "// referenced the method of bitmap of iq : https://www.shadertoy.com/view/4dfXWj\n" +
                    "\n" +
                    "#define r resolution.xy\n" +
                    "#define t time\n" +
                    "\n" +
                    "#define P(id,a,b,c,d,e,f,g,h) if( id == int(pos.y) ){ int pa = a+2*(b+2*(c+2*(d+2*(e+2*(f+2*(g+2*(h))))))); cha = floor(mod(float(pa)/pow(2.,float(pos.x)-1.),2.)); }\n" +
                    "\n" +
                    "float gray(vec3 _i)\n" +
                    "{\n" +
                    "    return _i.x*0.299+_i.y*0.587+_i.z*0.114;\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec2 uv = vec2(floor(gl_FragCoord.x/8./zoom)*8.*zoom,floor(gl_FragCoord.y/12./zoom)*12.*zoom)/r;\n" +
                    "        ivec2 pos = ivec2(mod(gl_FragCoord.x/zoom,8.),mod(gl_FragCoord.y/zoom,12.));\n" +
                    "        vec4 tex = texture2D(sTexture,uv);\n" +
                    "        float cha = 0.;\n" +
                    "        \n" +
                    "        float g = gray(tex.xyz);\n" +
                    "        if( g < .125 )\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,0,0,0,0,0);\n" +
                    "            P(9,0,0,0,0,0,0,0,0);\n" +
                    "            P(8,0,0,0,0,0,0,0,0);\n" +
                    "            P(7,0,0,0,0,0,0,0,0);\n" +
                    "            P(6,0,0,0,0,0,0,0,0);\n" +
                    "            P(5,0,0,0,0,0,0,0,0);\n" +
                    "            P(4,0,0,0,0,0,0,0,0);\n" +
                    "            P(3,0,0,0,0,0,0,0,0);\n" +
                    "            P(2,0,0,0,0,0,0,0,0);\n" +
                    "            P(1,0,0,0,0,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if( g < .25 ) // .\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,0,0,0,0,0);\n" +
                    "            P(9,0,0,0,0,0,0,0,0);\n" +
                    "            P(8,0,0,0,0,0,0,0,0);\n" +
                    "            P(7,0,0,0,0,0,0,0,0);\n" +
                    "            P(6,0,0,0,0,0,0,0,0);\n" +
                    "            P(5,0,0,0,0,0,0,0,0);\n" +
                    "            P(4,0,0,0,1,1,0,0,0);\n" +
                    "            P(3,0,0,0,1,1,0,0,0);\n" +
                    "            P(2,0,0,0,0,0,0,0,0);\n" +
                    "            P(1,0,0,0,0,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if( g < .375 ) // ,\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,0,0,0,0,0);\n" +
                    "            P(9,0,0,0,0,0,0,0,0);\n" +
                    "            P(8,0,0,0,0,0,0,0,0);\n" +
                    "            P(7,0,0,0,0,0,0,0,0);\n" +
                    "            P(6,0,0,0,0,0,0,0,0);\n" +
                    "            P(5,0,0,0,0,0,0,0,0);\n" +
                    "            P(4,0,0,0,1,1,0,0,0);\n" +
                    "            P(3,0,0,0,1,1,0,0,0);\n" +
                    "            P(2,0,0,0,0,1,0,0,0);\n" +
                    "            P(1,0,0,0,1,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if( g < .5 ) // -\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,0,0,0,0,0);\n" +
                    "            P(9,0,0,0,0,0,0,0,0);\n" +
                    "            P(8,0,0,0,0,0,0,0,0);\n" +
                    "            P(7,0,0,0,0,0,0,0,0);\n" +
                    "            P(6,1,1,1,1,1,1,1,0);\n" +
                    "            P(5,0,0,0,0,0,0,0,0);\n" +
                    "            P(4,0,0,0,0,0,0,0,0);\n" +
                    "            P(3,0,0,0,0,0,0,0,0);\n" +
                    "            P(2,0,0,0,0,0,0,0,0);\n" +
                    "            P(1,0,0,0,0,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if(g < .625 ) // +\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,0,0,0,0,0);\n" +
                    "            P(9,0,0,0,1,0,0,0,0);\n" +
                    "            P(8,0,0,0,1,0,0,0,0);\n" +
                    "            P(7,0,0,0,1,0,0,0,0);\n" +
                    "            P(6,1,1,1,1,1,1,1,0);\n" +
                    "            P(5,0,0,0,1,0,0,0,0);\n" +
                    "            P(4,0,0,0,1,0,0,0,0);\n" +
                    "            P(3,0,0,0,1,0,0,0,0);\n" +
                    "            P(2,0,0,0,0,0,0,0,0);\n" +
                    "            P(1,0,0,0,0,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if(g < .75 ) // *\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,0,1,0,0,0,0);\n" +
                    "            P(9,1,0,0,1,0,0,1,0);\n" +
                    "            P(8,0,1,0,1,0,1,0,0);\n" +
                    "            P(7,0,0,1,1,1,0,0,0);\n" +
                    "            P(6,0,0,0,1,0,0,0,0);\n" +
                    "            P(5,0,0,1,1,1,0,0,0);\n" +
                    "            P(4,0,1,0,1,0,1,0,0);\n" +
                    "            P(3,1,0,0,1,0,0,1,0);\n" +
                    "            P(2,0,0,0,1,0,0,0,0);\n" +
                    "            P(1,0,0,0,0,0,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else if(g < .875 ) // #\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,1,0,0,1,0,0);\n" +
                    "            P(9,0,0,1,0,0,1,0,0);\n" +
                    "            P(8,1,1,1,1,1,1,1,0);\n" +
                    "            P(7,0,0,1,0,0,1,0,0);\n" +
                    "            P(6,0,0,1,0,0,1,0,0);\n" +
                    "            P(5,0,1,0,0,1,0,0,0);\n" +
                    "            P(4,0,1,0,0,1,0,0,0);\n" +
                    "            P(3,1,1,1,1,1,1,1,0);\n" +
                    "            P(2,0,1,0,0,1,0,0,0);\n" +
                    "            P(1,0,1,0,0,1,0,0,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        else // @\n" +
                    "        {\n" +
                    "            P(11,0,0,0,0,0,0,0,0);\n" +
                    "            P(10,0,0,1,1,1,1,0,0);\n" +
                    "            P(9,0,1,0,0,0,0,1,0);\n" +
                    "            P(8,1,0,0,0,1,1,1,0);\n" +
                    "            P(7,1,0,0,1,0,0,1,0);\n" +
                    "            P(6,1,0,0,1,0,0,1,0);\n" +
                    "            P(5,1,0,0,1,0,0,1,0);\n" +
                    "            P(4,1,0,0,1,0,0,1,0);\n" +
                    "            P(3,1,0,0,1,1,1,1,0);\n" +
                    "            P(2,0,1,0,0,0,0,0,0);\n" +
                    "            P(1,0,0,1,1,1,1,1,0);\n" +
                    "            P(0,0,0,0,0,0,0,0,0);\n" +
                    "        }\n" +
                    "        \n" +
                    "        vec3 col = tex.xyz/max(tex.x,max(tex.y,tex.z));\n" +
                    "        gl_FragColor = vec4(cha*col, 1);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int w, h;


    public GpuImageASCII() {
        super(DEFAULT_VERTEX_SHADER, ASCII_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        w = width;
        h = height;

    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("zoom"), 2.0f);
        GLES20.glUniform2f(getHandle("resolution"), w, h);
    }
}
