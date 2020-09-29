package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageWarmer extends GlFilter {

    private static final String WARMER_FRAGMENT_SHADER =
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
                    "uniform lowp vec2 iMouse; \n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    \n" +
                    "    //lowp float factor = 2.0;\n" +
                    "    lowp float factor = 2.0  ;\n" +
                    "\n" +
                    "    lowp vec2 uv =  vTextureCoord.xy; ///iResolution.xy;\n" +
                    "    lowp float t = iTime * (10.0 * sliderValue) ;\n" +
                    "    \n" +
                    "    //t= t/10.0;\n" +
                    "    \n" +
                    "    lowp float freq = texture2D(sTexture,uv).x;\n" +
                    "    \n" +
                    "//    uv.x/=1.04;\n" +
                    "    uv.x/=1.0;\n" +
                    "\n" +
                    "    \n" +
                    "    lowp vec3 tx = texture2D(sTexture,uv).rgb * (factor*factor*freq)/factor;\n" +
                    "    \n" +
                    "    //tx.r+=mod(0.+t/20.0,0.1+sin(t));\n" +
                    "    tx.r+=mod(0.+t/10.0,0.1+sin(t));\n" +
                    "\n" +
                    "    \n" +
                    "    lowp vec4 fragColor;\n" +
                    "    lowp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                    "    if (vTextureCoord.x < iMouse.x)\n" +
                    "    {\n" +
                    "        fragColor = vec4(texColor, 1.0);\n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "        fragColor = vec4(vec3(tx),1.0);\n" +
                    "        \n" +
                    "    }\n" +
                    "    \n" +
                    "    gl_FragColor = fragColor;\n" +
                    "    \n" +
                    "   \n" +
                    "    \n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    float heights;
    float widths;
    float constant = 1.0f;

    public GPUImageWarmer() {
        super(DEFAULT_VERTEX_SHADER, WARMER_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), constant);

    }

}
