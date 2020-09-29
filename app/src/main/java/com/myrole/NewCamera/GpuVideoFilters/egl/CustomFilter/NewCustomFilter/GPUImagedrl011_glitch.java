package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImagedrl011_glitch extends GlFilter {
    private static final String DR011GLITCH_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "uniform lowp float sliderValue3 ;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control\n" +
                    "//const lowp float speed = 1.0 ;   // Need Control\n" +
                    "\n" +
                    "//#define SIN01(a) (sin(a)*0.5 + 0.5)\n" +
                    "\n" +
                    "lowp float SIN01 (lowp float a)\n" +
                    "{\n" +
                    "//    return (sin(a)*0.5 + 0.5);\n" +
                    "\n" +
                    "lowp float Dist = (sliderValue2 *5.0 );\n" +
                    "\n" +
                    "return (sin(a)*Dist + Dist);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "lowp vec3 rgb2hsv(lowp vec3 c)\n" +
                    "{\n" +
                    "lowp vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
                    "lowp vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n" +
                    "lowp vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
                    "\n" +
                    "lowp float d = q.x - min(q.w, q.y);\n" +
                    "lowp float e = 1.0e-10;\n" +
                    "return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "lowp vec3 hsv = rgb2hsv(texture2D(sTexture, uv).rgb);\n" +
                    "\n" +
                    "\n" +
                    "lowp float tempTime = iTime * (sliderValue * 20.0 ) ;\n" +
                    "\n" +
                    "lowp float angle = hsv.x + atan(uv.y, uv.x) + tempTime*0.1;\n" +
                    "\n" +
                    "\n" +
                    "lowp mat2 RotationMatrix = mat2( cos( angle ), -sin( angle ), sin( angle ),  cos( angle ));\n" +
                    "\n" +
                    "\n" +
                    "//    lowp vec3 col = texture2D(sTexture, uv + RotationMatrix * vec2(log(max(SIN01(iTime*0.7)-0.2, 0.)*0.2 + 1.  ),0) * hsv.y).rgb;\n" +
                    "\n" +
                    "lowp vec3 col = texture2D(sTexture, uv + RotationMatrix * vec2(log(max(SIN01(tempTime*0.7)-0.2, 0.)*0.2 + 1.0),0.0) * hsv.y).rgb;\n" +
                    "\n" +
                    "gl_FragColor = vec4(col,1.0);\n" +
                    "\n" +
                    "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImagedrl011_glitch() {
        super(DEFAULT_VERTEX_SHADER, DR011GLITCH_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue2"), 0.1f);

        // GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
