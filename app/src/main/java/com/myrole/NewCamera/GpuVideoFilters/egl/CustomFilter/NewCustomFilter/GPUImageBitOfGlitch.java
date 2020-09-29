package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageBitOfGlitch extends GlFilter {

    private static final String BITOFGLITCH_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "uniform lowp float constant ;\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control\n" +
                    "//const lowp float speed = 1.0 ;   // Need Control\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    lowp vec2 U = vTextureCoord;\n" +
                    "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "    lowp vec4 fragColor;\n" +
                    "\n" +
                    "    \n" +
                    "//        lowp vec3 video1 = vec3(texture2D(sTexture, uv));\n" +
                    "//        lowp vec3 video1 = vec3(texture2D(sTexture, uv.yx));\n" +
                    "    \n" +
                    "    lowp vec3 video1 = vec3(texture2D(sTexture, vec2(uv.x+1.0,uv.y+1.0)));\n" +
                    "    \n" +
                    "    lowp vec3 video2 = vec3(texture2D(sTexture, uv));\n" +
                    "    \n" +
                    "    lowp vec3 img = vec3(uv.x, uv.y, 0.1);\n" +
                    "    \n" +
                    "//    video2 = sin(sqrt(iTime) * (mod(video1, video2) * 0.3));\n" +
                    "    \n" +
                    "    video2 = sin(sqrt(1.0) * (mod(video1, video2) * 1.3));\n" +
                    "\n" +
                    "\n" +
                    "//    video2 = sin(sqrt(1.0) * (mod(video1, video2) * 1.0));\n" +
                    "    \n" +
                    "   // video2 = sin(sqrt(1.0) * (mod(video1, video2) * 1.0));\n" +
                    "\n" +
                    "    /*\n" +
                    "    if( video2.g > 0.01 )\n" +
                    "    {\n" +
                    "        video2.g = smoothstep(video2.g, 0.01, 0.85);\n" +
                    "    }\n" +
                    "    if( video2.r > 0.02 )\n" +
                    "    {\n" +
                    "        mod(video2.r, sin(iTime));\n" +
                    "    }\n" +
                    "    if( video2.b > 0.03 )\n" +
                    "    {\n" +
                    "        mod(video2.b, cos(iTime));\n" +
                    "    }\n" +
                    "    */\n" +
                    "    \n" +
                    "    img = video2;\n" +
                    "    \n" +
                    "    fragColor = vec4(img, 1.0);\n" +
                    "    \n" +
                    "    gl_FragColor =  fragColor ;\n" +
                    "\n" +
                    "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "    \n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    private float sliderValue = 1.0f;


    public GPUImageBitOfGlitch() {
        super(DEFAULT_VERTEX_SHADER, BITOFGLITCH_FRAGMENT_SHADER);

    }


    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);


    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
        //GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
    }
}
