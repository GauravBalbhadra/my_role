package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageRainbow extends GlFilter {

    private static final String RAINBOW_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "//uniform float fractionalWidthOfPixel;\n" +
                    "//uniform float aspectRatio;\n" +
                    "\n" +
                    "uniform highp float iTime ;\n" +
                    "//uniform highp float frameWidth ;\n" +
                    "//uniform highp float frameHeight ;\n" +
                    "uniform highp float sliderValue ;\n" +
                    "uniform highp vec2 iMouse;\n" +
                    "\n" +
                    "\n" +
                    "highp float over_than=0.1999;\n" +
                    "\n" +
                    "// Smooth HSV to RGB conversion\n" +
                    "//highp vec3 hsv2rgb_smooth( in vec3 c )\n" +
                    "highp vec3 hsv2rgb_smooth( highp vec3 c )\n" +
                    "{\n" +
                    "    highp vec3 rgb = clamp( abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),6.0)-3.0)-1.0, 0.0, 1.0 );\n" +
                    "    rgb = rgb*rgb*(3.0-2.0*rgb); // cubic smoothing\n" +
                    "    return c.z * mix( vec3(1.0), rgb, c.y);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    \n" +
                    "    /// iResolution.xy;\n" +
                    "\n" +
                    "    highp vec2 uv = vTextureCoord.xy ;\n" +
                    "    \n" +
                    "    // highp float clr =1.0*uv.y-(iTime/2.0);\n" +
                    "    highp float clr =1.0*uv.y-(iTime/2.0);\n" +
                    "\n" +
                    "    \n" +
                    "    highp vec3 tx=texture2D(sTexture,uv).rgb;\n" +
                    "    \n" +
                    "    highp vec3 hsvColor = hsv2rgb_smooth(vec3(clr, 1.0, 1.0));\n" +
                    "    \n" +
                    "//    over_than=max(0.1999,0.1999+sin(iTime/10.0));\n" +
                    "      over_than = max(0.1999,0.1999 + sin((10.0 - (sliderValue*10.0 ))/10.0));\n" +
                    "\n" +
                    "    \n" +
                    "    // ignore certain colors ! alien  cond ! if xx ? then zz  : then ww\n" +
                    "    \n" +
                    "    highp vec4 fragColor;\n" +
                    "\n" +
                    "    \n" +
                    "    (tx.r>over_than)  ?  fragColor = vec4(hsvColor, 1.0) : fragColor=vec4(tx,1.0);\n" +
                    "    // avec if !!!\n" +
                    "    if(tx.g>over_than) fragColor = vec4(hsvColor, 1.0);else fragColor=vec4(tx,1.0);\n" +
                    "    if(tx.b>over_than) fragColor = vec4(hsvColor, 1.0);else fragColor=vec4(tx,1.0);\n" +
                    "    \n" +
                    "    highp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                    "\n" +
                    "    if (vTextureCoord.x < iMouse.x)\n" +
                    "    {\n" +
                    "        fragColor = vec4(texColor, 1.0);\n" +
                    "    }\n" +
                    "    \n" +
                    "    \n" +
                    "    gl_FragColor = fragColor;\n" +
                    "    \n" +
                    "   // gl_FragColor = texture2D(sTexture,vTextureCoord);\n" +
                    "    \n" +
                    "    }";
    final long START_TIME = System.currentTimeMillis();
    float heights;
    float widths;
    float constant = 1.0f;

    public GPUImageRainbow() {
        super(DEFAULT_VERTEX_SHADER, RAINBOW_FRAGMENT_SHADER);

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

    }

}
