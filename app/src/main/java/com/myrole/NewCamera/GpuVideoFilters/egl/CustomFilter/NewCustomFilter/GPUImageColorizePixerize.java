package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageColorizePixerize extends GlFilter {


    private static final String COLORIZEPIXE_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "uniform lowp vec2 iMouse ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "uniform lowp float sliderValue3 ;\n" +
                    "uniform lowp int type ;\n" +
                    "\n" +
                    "\n" +
                    "lowp float posterSteps = 16.0;  //16.0\n" +
                    "lowp vec3 color1 = vec3(1.0, 1., 0.0);\n" +
                    "lowp vec3 color2 = vec3(1., 0., 1.);\n" +
                    "\n" +
                    "//lowp vec3 color1 = vec3(0.5, 0.5, 0.0);\n" +
                    "//lowp vec3 color2 = vec3(0.5, 0., 1.);\n" +
                    "\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "lowp float rgbToGray(lowp vec4 rgba) {\n" +
                    "    \n" +
                    "    const lowp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
                    "    return dot(rgba.xyz, W);\n" +
                    "    \n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    \n" +
                    "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "    \n" +
                    "    lowp vec4 orgColor = texture2D(sTexture, uv);\n" +
                    "    \n" +
                    "    lowp float luma = rgbToGray(orgColor);\n" +
                    "    luma = floor(luma * posterSteps) / posterSteps;\t// posterize\n" +
                    "    \n" +
                    "    if (type < 9)\n" +
                    "    {\n" +
                    "        if (type == 0)   {\n" +
                    "            color1 = vec3(1.0, 1., 0.0);\n" +
                    "            color2 = vec3(1., 0., 1.);        }\n" +
                    "        \n" +
                    "        if (type == 1)   {\n" +
                    "            color1 = vec3(1.0, 1., 1.0);\n" +
                    "            color2 = vec3(1., 1.0, 1.);        }\n" +
                    "        \n" +
                    "        if (type == 2)   {\n" +
                    "            color1 = vec3(0.5, 1., 0.0);\n" +
                    "            color2 = vec3(0.5, 0., 1.);        }\n" +
                    "\n" +
                    "        if (type == 3)   {\n" +
                    "            color1 = vec3(1.0, 0.5, 0.0);\n" +
                    "            color2 = vec3(1.0, 0.5, 1.);        }\n" +
                    "        \n" +
                    "        if (type == 4)   {\n" +
                    "            color1 = vec3(1.0, 1.0, 0.5);\n" +
                    "            color2 = vec3(1.0, 0., 0.5);        }\n" +
                    "\n" +
                    "        if (type == 5)   {\n" +
                    "            color1 = vec3(1.0, 0.5, 0.75);\n" +
                    "            color2 = vec3(0.5, 1.0, 1.0);        }\n" +
                    "\n" +
                    "        if (type == 6)   {\n" +
                    "            color1 = vec3(0.0, 1.0, 0.0);\n" +
                    "            color2 = vec3(0.0, 0.0, 1.0);        }\n" +
                    "\n" +
                    "        if (type == 7)   {\n" +
                    "            color1 = vec3(1.0, 0.0, 0.0);\n" +
                    "            color2 = vec3(0.0, 0.0, 1.0);        }\n" +
                    "\n" +
                    "        if (type == 8)   {\n" +
                    "            color1 = vec3(1.0, 0.0, 0.0);\n" +
                    "            color2 = vec3(0.0, 1.0, 0.0);        }\n" +
                    "\n" +
                    "        \n" +
                    "        // fade from black at 0.5 to full color at 0 & 1\n" +
                    "        lowp vec4 fragClr = texture2D(sTexture,uv) ;\n" +
                    "        if(luma < 0.5) {\n" +
                    "            //        gl_FragColor = vec4(color2 * (1. - luma * 2.), 1.);\n" +
                    "            fragClr = vec4(mix(orgColor.rgb,color2 * (1. - luma * 2.),sliderValue), 1.);\n" +
                    "\n" +
                    "        } else {\n" +
                    "            //gl_FragColor = color ;\n" +
                    "             fragClr = vec4(mix(orgColor.rgb,color1 * (-0.5 + luma * 2.),sliderValue2) , 1.0);\n" +
                    "        }\n" +
                    "        //\n" +
                    "        lowp vec3 textColor = texture2D(sTexture,uv).rgb;\n" +
                    "        if (vTextureCoord.x < iMouse.x)\n" +
                    "        {\n" +
                    "            fragClr = vec4(textColor, 1.0);\n" +
                    "        }\n" +
                    "        gl_FragColor = fragClr;\n" +
                    "        \n" +
                    "    }\n" +
                    "     else\n" +
                    "    {\n" +
                    "    \n" +
                    "        \n" +
                    "//        luma = floor(luma * posterSteps) / posterSteps;\n" +
                    "        luma = floor(luma * 6.0) / 6.0;\n" +
                    "        \n" +
                    "        // draw input color mapped to luma\n" +
                    "//        gl_FragColor = vec4(vec3(luma * sliderValue, luma * sliderValue2, luma * sliderValue3), 1.0);\n" +
                    "    \n" +
                    "        gl_FragColor = vec4(mix(orgColor.rgb,vec3(luma * sliderValue, luma * sliderValue2, luma * sliderValue3),iMouse.x) , 1.0);\n" +
                    "\n" +
                    "        \n" +
                    "    }\n" +
                    "\n" +
                    "    \n" +
                    "   // gl_FragColor = texture2D(sTexture, vTextureCoord );\n" +
                    "    \n" +
                    "\n" +
                    "\n" +
                    "    \n" +
                    "}\n";


    final long START_TIME = System.currentTimeMillis();
    float w, h;

    public GPUImageColorizePixerize() {
        super(DEFAULT_VERTEX_SHADER, COLORIZEPIXE_FRAGMENT_SHADER);

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

        GLES20.glUniform1f(getHandle("type"), 0);
        float sliderValue = 0.3f;
        GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
        GLES20.glUniform1f(getHandle("sliderValue2"), 0.6f);
        GLES20.glUniform1f(getHandle("sliderValue3"), sliderValue);
        GLES20.glUniform1f(getHandle("frameWidth"), w);
        GLES20.glUniform1f(getHandle("frameHeight"), h);
    }
}
