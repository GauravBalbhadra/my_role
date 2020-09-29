package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageGrayScaleRGB extends GlFilter {

    private static final String GRAYSCALERGB_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control\n" +
                    "\n" +
                    "uniform lowp vec2 iMouse; // = vec2(0.5,0.5);   // Need Control\n" +
                    "\n" +
                    "const bool darkIsHot = true;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    lowp vec2 U = vTextureCoord;\n" +
                    "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "    \n" +
                    "    lowp vec4 fragColor;\n" +
                    "\n" +
                    "    \n" +
                    "    lowp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                    "    \n" +
                    "    if (vTextureCoord.x < iMouse.x)\n" +
                    "    {\n" +
                    "        fragColor = vec4(texColor, 1.0);\n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "        lowp float a = texColor.r;\n" +
                    "        if(darkIsHot)\n" +
                    "        {\n" +
                    "            a = 1.0 - a;\n" +
                    "        }\n" +
                    "        //fast shader version\n" +
                    "        fragColor.r = 1.0 - clamp(step(0.166, a)*a, 0.0, 0.333) - 0.667*step(0.333, a) + step(0.666, a)*a + step(0.833, a)*1.0;\n" +
                    "        fragColor.b = clamp(step(0.333, a)*a, 0.0, 0.5) + step(0.5, a)*0.5;\n" +
                    "        fragColor.g = clamp(a, 0.0, 0.166) + 0.834*step(0.166, a) - step(0.5, a)*a - step(0.666, a)*1.0;\n" +
                    "        \n" +
                    "        \n" +
                    "        //human readable version\n" +
                    "//        if(a<0.166)\n" +
                    "//        {\n" +
                    "//            mappedColor.r=1.0;\n" +
                    "//            mappedColor.g=a;\n" +
                    "//           mappedColor.b=0.0;\n" +
                    "//        }\n" +
                    "        //else if(a>=0.166 && a < 0.333)\n" +
                    "        //{\n" +
                    "        //mappedColor.r=1.0-a;\n" +
                    "        //mappedColor.g=1.0;\n" +
                    "        //mappedColor.b=0.0;\n" +
                    "        //}\n" +
                    "        //else if(a>=0.333 && a < 0.5)\n" +
                    "        //{\n" +
                    "        //mappedColor.r=0.0;\n" +
                    "        //mappedColor.g=1.0;\n" +
                    "        //mappedColor.b=a;\n" +
                    "        //}\n" +
                    "        //else if(a>=0.5 && a < 0.666)\n" +
                    "        //{\n" +
                    "        //mappedColor.r=0.0;\n" +
                    "        //mappedColor.g=1.0-a;\n" +
                    "        //mappedColor.b=1.0;\n" +
                    "        //}\n" +
                    "        //else if(a>=0.666 && a < 0.833)\n" +
                    "        //{\n" +
                    "        //mappedColor.r=a;\n" +
                    "        //mappedColor.g=0.0;\n" +
                    "        //mappedColor.b=1.0;\n" +
                    "        //}\n" +
                    "        //else\n" +
                    "        //{\n" +
                    "        //mappedColor.r=1.0;\n" +
                    "        //mappedColor.g=0.0;\n" +
                    "        //mappedColor.b=1.0;\n" +
                    "        //}\n" +
                    "    }\n" +
                    "    \n" +
                    "//    gl_FragColor = fragColor;\n" +
                    "      gl_FragColor = mix(texture2D(sTexture,vTextureCoord),fragColor,sliderValue);\n" +
                    "\n" +
                    "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "    \n" +
                    "//    gl_FragColor = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a * mixturePercent), textureColor.a);\n" +
                    "\n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    float heights;
    float widths;
    float constant = 0.5f;

    public GPUImageGrayScaleRGB() {
        super(DEFAULT_VERTEX_SHADER, GRAYSCALERGB_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), constant);//1.0f
        GLES20.glUniform1f(getHandle("frameWidth"), widths);
        GLES20.glUniform1f(getHandle("frameHeight"), heights);
        //GLES20.glUniform2f(getHandle("iMouse"), 0.5f, 0.5f);


    }

}
