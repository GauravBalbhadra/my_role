package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageSinMovment extends GlFilter {
    private static final String SINMOVMENT_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "uniform lowp int   isVertical ;\n" +
                    "\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    lowp vec2 U = vTextureCoord;\n" +
                    "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "    \n" +
                    "    lowp vec2 uv1 = uv;\n" +
                    "    lowp vec2 uv2 = uv;\n" +
                    "    \n" +
                    "    lowp vec4 finalColor  = texture2D(sTexture ,vTextureCoord );\n" +
                    "    \n" +
                    "    if (isVertical == 0)\n" +
                    "    {\n" +
                    "    \n" +
                    "//        lowp float y  = mod(floor(uv1.y * frameHeight / 2.0), 2.0);\n" +
                    "//        lowp float y1 = mod(floor(0.2-uv2.y * frameHeight / 2.0), 2.0);\n" +
                    "\n" +
                    "        lowp float temDiv = (30.0  *(sliderValue+0.05))+ 0.2;  //2.0\n" +
                    "        \n" +
                    "        lowp float y  = mod(floor(uv1.y * frameHeight / temDiv), 2.0);\n" +
                    "        lowp float y1 = mod(floor(0.2-uv2.y * frameHeight / temDiv), 2.0);\n" +
                    "        \n" +
                    "        \n" +
                    "        lowp float timeTemp  = mod(iTime,20.0)* (sliderValue2*30.0);\n" +
                    "        \n" +
                    "      //  tempTime = mod((iTime+0.05)*10.0 , floor((iTime+1.0)*10.0));\n" +
                    "\n" +
                    "        \n" +
                    "        \n" +
                    "    //    lowp float x1 =abs(sin((uv1.y-uv1.x + iTime))) * 0.04;\n" +
                    "        lowp float x1 =abs(sin((uv1.y-uv1.x + timeTemp))) * 0.04;\n" +
                    "        \n" +
                    "        \n" +
                    "        \n" +
                    "        if (y1 == 0.0 )\n" +
                    "        {\n" +
                    "    //        gl_FragColor =  texture2D(sTexture, 0.80*(uv2 + vec2(-x1+0.10, 0.)));\n" +
                    "            finalColor =  texture2D(sTexture, 0.80*(uv2 + vec2(-x1 , 0.)));\n" +
                    "        }\n" +
                    "        else //if(y==0.0)\n" +
                    "        {\n" +
                    "    //        gl_FragColor =  texture2D(sTexture, 0.80*(uv1 + vec2(x1+0.10, 0.)));\n" +
                    "            finalColor =  texture2D(sTexture, 0.80*(uv1 + vec2(x1 , 0.)));\n" +
                    "        }\n" +
                    "        \n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "    \n" +
                    "        lowp float temDiv = (30.0  *(sliderValue+0.05))+ 0.2;  //2.0\n" +
                    "        lowp float timeTemp  = mod(iTime,20.0) * (sliderValue2*30.0);\n" +
                    "\n" +
                    "\n" +
                    "        lowp float x  = mod(floor(uv1.x * frameWidth / temDiv), 2.0);\n" +
                    "        lowp float x1 = mod(floor(0.2-uv2.x * frameWidth / temDiv), 2.0);\n" +
                    "        \n" +
                    "        //    lowp float x1 =abs(sin((uv1.y-uv1.x + iTime))) * 0.04;\n" +
                    "        lowp float y1 =abs(sin((uv1.x-uv1.y + timeTemp))) * 0.04;\n" +
                    "        \n" +
                    "        \n" +
                    "        if (x1 == 0.0 )\n" +
                    "        {\n" +
                    "            //        gl_FragColor =  texture2D(sTexture, 0.80*(uv2 + vec2(-x1+0.10, 0.)));\n" +
                    "            finalColor =  texture2D(sTexture, 0.80*(uv2 + vec2(0. , -y1)));\n" +
                    "        }\n" +
                    "        else //if(y==0.0)\n" +
                    "        {\n" +
                    "            //        gl_FragColor =  texture2D(sTexture, 0.80*(uv1 + vec2(x1+0.10, 0.)));\n" +
                    "            finalColor =  texture2D(sTexture, 0.80*(uv1 + vec2(0. , y1)));\n" +
                    "        }\n" +
                    "\n" +
                    "    }\n" +
                    "    \n" +
                    "    gl_FragColor = finalColor;\n" +
                    "    }";
    final long START_TIME = System.currentTimeMillis();
    float heights;
    float widths;

    public GPUImageSinMovment() {
        super(DEFAULT_VERTEX_SHADER, SINMOVMENT_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), 0.0f);
        GLES20.glUniform1f(getHandle("sliderValue2"), 0.15f);
        GLES20.glUniform1f(getHandle("frameWidth"), widths);
        GLES20.glUniform1f(getHandle("frameHeight"), heights);
        GLES20.glUniform1f(getHandle("isVertical"), 0);


    }
}
