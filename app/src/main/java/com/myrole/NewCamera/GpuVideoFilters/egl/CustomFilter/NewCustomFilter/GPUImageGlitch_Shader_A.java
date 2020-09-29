package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageGlitch_Shader_A extends GlFilter {
    private static final String GLITCHSHADERA_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp vec2 iMouse ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "//uniform lowp float sliderValue3 ;\n" +
                    "\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.0,0.5);   // Need Control\n" +
                    "//const lowp float speed = 1.0 ;   // Need Control\n" +
                    "\n" +
                    "const lowp float GlitchAmount = 1.0; //1.0\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "lowp vec4 posterize(lowp vec4 color,lowp float numColors)\n" +
                    "{\n" +
                    "return floor(color * numColors - 0.5) / numColors;\n" +
                    "}\n" +
                    "\n" +
                    "lowp vec2 quantize(lowp vec2 v,lowp float steps)\n" +
                    "{\n" +
                    "return floor(v * steps) / steps;\n" +
                    "}\n" +
                    "\n" +
                    "lowp float dist(lowp vec2 a,lowp vec2 b)\n" +
                    "{\n" +
                    "return sqrt(pow(b.x - a.x, 2.0) + pow(b.y - a.y, 2.0));\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "lowp vec4 fragColor;\n" +
                    "\n" +
                    "//lowp float amount = pow(GlitchAmount, 2.0);\n" +
                    "lowp float amount = pow((GlitchAmount * (sliderValue+0.01)), 2.0);\n" +
                    "\n" +
                    "lowp vec2 pixel = 1.0 / iResolution.xy;\n" +
                    "\n" +
                    "lowp vec4 color = texture2D(sTexture, uv);\n" +
                    "\n" +
                    "\n" +
                    "//    gl_FragColor =  color ;\n" +
                    "\n" +
                    "//    if (vTextureCoord.x < iMouse.x)\n" +
                    "if (vTextureCoord.x < (1.0 - sliderValue2))\n" +
                    "{\n" +
                    "gl_FragColor =  color ;\n" +
                    "return;\n" +
                    "}\n" +
                    "\n" +
                    "lowp float t = mod(mod(iTime, amount * 100.0 * (amount - 0.5)) * 109.0, 1.0);\n" +
                    "lowp vec4 postColor = posterize(color, 16.0);\n" +
                    "//    lowp vec4 postColor = posterize(color, (sliderValue3 * 32.0));\n" +
                    "\n" +
                    "lowp vec4 a = posterize(texture2D(sTexture, quantize(uv, 64.0 * t) + pixel * (postColor.rb - vec2(.5)) * 100.0), 5.0).rbga;\n" +
                    "lowp vec4 b = posterize(texture2D(sTexture, quantize(uv, 32.0 - t) + pixel * (postColor.rg - vec2(.5)) * 1000.0), 4.0).gbra;\n" +
                    "lowp vec4 c = posterize(texture2D(sTexture, quantize(uv, 16.0 + t) + pixel * (postColor.rg - vec2(.5)) * 20.0), 16.0).bgra;\n" +
                    "\n" +
                    "\n" +
                    "fragColor = mix(\n" +
                    "texture2D(sTexture,\n" +
                    "uv + amount * (quantize((a * t - b + c - (t + t / 2.0) / 10.0).rg, 16.0) - vec2(.5)) * pixel * 100.0),\n" +
                    "(a + b + c) / 3.0,\n" +
                    "(0.5 - (dot(color, postColor) - 1.5)) * amount);\n" +
                    "\n" +
                    "gl_FragColor =  fragColor ;\n" +
                    "\n" +
                    "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageGlitch_Shader_A() {
        super(DEFAULT_VERTEX_SHADER, GLITCHSHADERA_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), 1.0f);
        GLES20.glUniform1f(getHandle("sliderValue2"), 1.0f);
    }


}
