package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageEndless extends GlFilter {

    private static final String ENDLESS_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "//uniform float fractionalWidthOfPixel;\n" +
                    "//uniform float aspectRatio;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "//uniform lowp float frameWidth ;\n" +
                    "//uniform lowp float frameHeight ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "\n" +
                    "uniform lowp vec2 iMouse;\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "uniform lowp int isSpiral ;\n" +
                    "\n" +
                    "//bool DROSTE_MODE = false;\n" +
                    "//bool DROSTE_MODE = false;\n" +
                    "#define PI 3.141592653589793238462643383279502884197169\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    //inputColor *= dot(vec2(0.707),normalize(vTextureCoordToUse-adjustedSamplePos))*.5+1.;\n" +
                    "\n" +
                    "    lowp vec4 orgImage = texture2D(sTexture,vTextureCoord);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy ;\n" +
                    "\n" +
                    "       lowp float atans = (atan(uv.x-0.5,uv.y-0.5)+PI)/(PI*2.);\n" +
                    "       lowp float time = iTime * (sliderValue*4.0);\n" +
                    "        \n" +
                    "        //Zooming\n" +
                    "        //uv -= .8;\n" +
                    "    \n" +
                    "        uv.x -= iMouse.x;\n" +
                    "        uv.y -= iMouse.y;\n" +
                    "\n" +
                    "        // if (DROSTE_MODE)\n" +
                    "        if (isSpiral == 1)\n" +
                    "        {\n" +
                    "            uv *= (1./pow(4.,fract((time+atans)/2.)));\n" +
                    "        } else {\n" +
                    "            uv *= (1./pow(4.,fract(time/2.)));\n" +
                    "        }\n" +
                    "        uv += .5;\n" +
                    "        //-------\n" +
                    "        \n" +
                    "        lowp vec2 tri = abs(1.-(uv*2.));\n" +
                    "    \n" +
                    "        //if (DROSTE_MODE)\n" +
                    "        if (isSpiral == 1)\n" +
                    "        {\n" +
                    "            tri = (vec2(length(uv-.5)))*2.;\n" +
                    "        }\n" +
                    "    \n" +
                    "        lowp float zoom = min(pow(2.,floor(-log2(tri.x))),pow(2.,floor(-log2(tri.y))));\n" +
                    "        //lowp float zoom_id = log2(zoom)+1.;\n" +
                    "        lowp float zoom_id = log2(zoom)+1.;\n" +
                    "\n" +
                    "        lowp float div = ((pow(2.,((-zoom_id)-1.))*((-2.)+pow(2.,zoom_id))));\n" +
                    "    \n" +
                    "    \n" +
                    "        lowp vec2 uv2 = (((uv)-(div))*zoom);\n" +
                    "        //lowp vec2 uv2 = (((uv)-(div)) );\n" +
                    "        //lowp vec2 uv2 = (((uv)-(div))*zoom );\n" +
                    "        //uv2 = uv;\n" +
                    "    \n" +
                    "        lowp vec4 fragColor = vec4(texture2D(sTexture,uv2).rgb,1.0);\n" +
                    "    \n" +
                    "        gl_FragColor = fragColor;\n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    float heights;
    float widths;
    float constant = -0.5f;

    public GPUImageEndless() {
        super(DEFAULT_VERTEX_SHADER, ENDLESS_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), constant);//0.5f
        GLES20.glUniform1f(getHandle("frameWidth"), widths);
        GLES20.glUniform1f(getHandle("frameHeight"), heights);
        GLES20.glUniform1f(getHandle("isSpiral"), 0);
        GLES20.glUniform2f(getHandle("iMouse"), 0.5f, 0.5f);


    }

}
