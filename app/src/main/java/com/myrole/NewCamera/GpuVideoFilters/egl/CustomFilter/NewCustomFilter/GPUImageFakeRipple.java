package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageFakeRipple extends GlFilter {
    private static final String FAKERIPPLE_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp vec2 iMouse ;\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "// ~ ~ ~ ~ ~  ~ Fake Ripple Effect  ~ ~ ~ ~ ~\n" +
                    "lowp float tempTime = mod(iTime,10.0) ;\n" +
                    "//    tempTime=iTime;\n" +
                    "\n" +
                    "//    lowp float w = (0.5 - (uv.x)) * (iResolution.x / iResolution.y);\n" +
                    "//    lowp float h = 0.5 - uv.y;\n" +
                    "\n" +
                    "//    lowp float w = (0.5 - (uv.x)) * (iResolution.x / iResolution.y);\n" +
                    "//    lowp float w = (0.5 - uv.x);\n" +
                    "//    lowp float h = 0.5 - uv.y;\n" +
                    "lowp float w = (iMouse.x - uv.x);\n" +
                    "lowp float h = iMouse.y - uv.y;\n" +
                    "\n" +
                    "\n" +
                    "lowp float distanceFromCenter = sqrt(w * w + h * h);\n" +
                    "\n" +
                    "lowp float sinArg = distanceFromCenter * 10.0 - tempTime * 10.0;\n" +
                    "\n" +
                    "//    lowp float sinArg = distanceFromCenter * 10.0 - tempTime * (10.0 * sliderValue);\n" +
                    "lowp float slope = cos(sinArg) ;\n" +
                    "\n" +
                    "slope *= sliderValue;\n" +
                    "\n" +
                    "//slope = 2.0;\n" +
                    "\n" +
                    "\n" +
                    "//  lowp vec4 color = texture2D(sTexture, uv + normalize(vec2(w, h)) * slope * 0.05);\n" +
                    "//    lowp vec4 color = texture2D(sTexture, uv + normalize(vec2(w, h)) * slope *0.09);\n" +
                    "//lowp vec4 color = texture2D(sTexture, vTextureCoord + normalize(vec2(w, h)) * slope *0.09);\n" +
                    "\n" +
                    "\n" +
                    "//    lowp vec2 ripple = normalize(vec2(w, h)) * slope *0.09 ;\n" +
                    "lowp vec2 ripple = vec2(w, h) * slope *0.2 ;\n" +
                    "\n" +
                    "lowp vec4 color = texture2D(sTexture, vTextureCoord + ripple);\n" +
                    "\n" +
                    "//    smoothstep\n" +
                    "\n" +
                    "gl_FragColor = color;\n" +
                    "//    gl_FragColor = texture2D(sTexture, vTextureCoord );\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "/*\n" +
                    "\n" +
                    "float w = (0.5 - (uv.x)) * (iResolution.x / iResolution.y);\n" +
                    "float h = 0.5 - uv.y;\n" +
                    "float distanceFromCenter = sqrt(w * w + h * h);\n" +
                    "\n" +
                    "float sinArg = distanceFromCenter * 10.0 - iTime * 10.0;\n" +
                    "float slope = cos(sinArg) ;\n" +
                    "vec4 color = texture(iChannel0, uv + normalize(vec2(w, h)) * slope * 0.05);\n" +
                    "\n" +
                    "*/\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageFakeRipple() {
        super(DEFAULT_VERTEX_SHADER, FAKERIPPLE_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), 0.08f);
        // GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
