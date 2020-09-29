package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageFlashLight extends GlFilter {
    private static final String FLASHLIGHT_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "\n" +
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
                    "uniform lowp float sliderValue3 ;\n" +
                    "uniform lowp vec2 iMouse;\n" +
                    "\n" +
                    "lowp float speed = 5.;  //5.0\n" +
                    "\n" +
                    "lowp float blob(lowp float x,lowp float y,lowp float fx,lowp float fy){\n" +
                    "\n" +
                    "lowp float time = iTime /1.0 ;\n" +
                    "\n" +
                    "lowp float xx = x+sin(time*fx/speed)*.9;\n" +
                    "\n" +
                    "//lowp float yy = y+cos(time*fy/speed)*99.9;\n" +
                    "lowp float yy = y+cos(time*fy/speed)*99.9;\n" +
                    "\n" +
                    "//    return 25.0/sqrt(xx*xx+yy*yy);\n" +
                    "\n" +
                    "return 25.0/sqrt(xx*xx+yy*yy);\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 uv =  vTextureCoord.xy; ///iResolution.xy;\n" +
                    "lowp vec4 orgClr = texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "lowp float x = uv.x*2.0;\n" +
                    "lowp float y = uv.y*2.0;\n" +
                    "\n" +
                    "//    lowp float a = blob(x,y,3.3,3.2) + blob(x,y,3.9,3.0);\n" +
                    "//    lowp float b = blob(x,y,3.2,2.9) + blob(x,y,2.7,2.7);\n" +
                    "//    lowp float c = blob(x,y,2.4,3.3) + blob(x,y,2.8,2.3);\n" +
                    "\n" +
                    "lowp float a = blob(x,y,9.0,7.0) + blob(x,y,9.9,9.0);\n" +
                    "lowp float b = blob(x,y,7.6,8.6) + blob(x,y,7.5,7.2);\n" +
                    "lowp float c = blob(x,y,8.6,9.4) + blob(x,y,7.6,8.9);\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//lowp vec3 d = vec3(a,b,c)/50.0;\n" +
                    "lowp vec3 d = vec3(a,b,c)/50.0;\n" +
                    "\n" +
                    "lowp vec4 fragColor ;\n" +
                    "fragColor = vec4(d.x,d.y,d.z,1.0);\n" +
                    "\n" +
                    "//ScreenBlendFilter.\n" +
                    "//    fragColor = 1.0 - ((1.0 - fragColor) * (1.0 - orgClr));\n" +
                    "fragColor = ((1.0 - fragColor) * (orgClr));\n" +
                    "\n" +
                    "//    fragColor = fragColor * orgClr + fragColor * (1.0 - orgClr.a) + orgClr * (1.0 - fragColor.a);\n" +
                    "\n" +
                    "\n" +
                    "//fragColor *= vec4(orgClr) ;\n" +
                    "\n" +
                    "//     orgClr *= vec4(fragColor)  ;\n" +
                    "\n" +
                    "//\n" +
                    "lowp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                    "if (vTextureCoord.x < iMouse.x)\n" +
                    "{\n" +
                    "fragColor = vec4(texColor, 1.0);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = fragColor;\n" +
                    "\n" +
                    "\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageFlashLight() {
        super(DEFAULT_VERTEX_SHADER, FLASHLIGHT_FRAGMENT_SHADER);

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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
