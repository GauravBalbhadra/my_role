package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageSwGlitch extends GlFilter {

    private static final String SWGLITCH_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "uniform lowp float sliderValue3 ;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "lowp vec3 rgb2hsv(lowp vec3 c){\n" +
                    "    lowp vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
                    "    lowp vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n" +
                    "    lowp vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
                    "    \n" +
                    "    lowp float d = q.x - min(q.w, q.y);\n" +
                    "    lowp float e = 1.0e-10;\n" +
                    "    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n" +
                    "}\n" +
                    "\n" +
                    "lowp vec3 hsv2rgb(lowp vec3 c){\n" +
                    "    lowp vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" +
                    "    lowp vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n" +
                    "    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "lowp float rand(lowp float n)\n" +
                    "{\n" +
                    "    return fract(sin(n) * 43758.5453123);\n" +
                    "}\n" +
                    "\n" +
                    "lowp float rand2(lowp vec2 n) {\n" +
                    "    return fract(sin(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    lowp vec2 U = vTextureCoord;\n" +
                    "    lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "   \n" +
                    "    lowp vec4 color = texture2D(sTexture, uv);\n" +
                    "    \n" +
                    "    lowp vec2 tempFragCoord = vec2(vTextureCoord.x*frameWidth,vTextureCoord.y*frameHeight);\n" +
                    "    \n" +
                    "    lowp float  tempTime = mod(iTime,5.0);\n" +
                    "    \n" +
                    "    // blue\n" +
                    "    lowp vec3 hsvColor = rgb2hsv(color.rgb);\n" +
                    "    hsvColor.x = .59;\n" +
                    "    hsvColor.y = clamp(hsvColor.y, .4, .6 );\n" +
                    "    \n" +
                    "//    lowp float noise = rand(1. + vTextureCoord.y + vTextureCoord.x * (iTime*1.0));\n" +
                    "//    lowp float noise = rand(1. + tempFragCoord.y + tempFragCoord.x * 300.0);\n" +
                    "//    hsvColor.z += noise * .2;\n" +
                    "    \n" +
                    "    // vertical lines\n" +
                    "    //hsvColor.z += .2 * step(mod(vTextureCoord.x, 3.), 1.);\n" +
                    "    hsvColor.z += .2 * step(mod(tempFragCoord.x, 100. * sliderValue), 1.);\n" +
                    "\n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "    // horizontal lines\n" +
                    "   /* lowp float offset = rand(tempFragCoord.y * tempFragCoord.x + tempFragCoord.x * iTime) * 10.;\n" +
                    "    lowp float ratio = step(rand(50. + tempFragCoord.y + tempFragCoord.x * iTime), .2);\n" +
                    "    lowp float timeShift = cos(iTime) * 200.;\n" +
                    "    hsvColor.z += .4 * step(mod(tempFragCoord.y + offset + iTime * 400. + timeShift, 200.), 10.) * ratio;\n" +
                    "   */\n" +
                    "    \n" +
                    "    lowp float offset = rand(tempFragCoord.y * tempFragCoord.x + tempFragCoord.x * tempTime) * 10.;\n" +
                    "    lowp float ratio = step(rand(50. + tempFragCoord.y + tempFragCoord.x * tempTime), .2);\n" +
                    "    lowp float timeShift = cos(tempTime) * 200.;\n" +
                    "    hsvColor.z += .4 * step(mod(tempFragCoord.y + offset + tempTime * 400. + timeShift, 600.), 30.) *( ratio*sliderValue2);\n" +
                    "\n" +
                    "    \n" +
                    "    \n" +
                    "    // random rectangles\n" +
                    "//    lowp float offset2 = rand(ceil(tempFragCoord.y / 100.) * floor(iTime * 20.));\n" +
                    "//    hs\u0010vColor.z += .15 * step(mod(tempFragCoord.y + iTime + offset2 * 100. + 100., 200.), 100.);\n" +
                    "    \n" +
                    "    \n" +
                    "    // horizontal rectangles\n" +
                    "    lowp float offset3 = rand(ceil(tempFragCoord.y * 0.01));\n" +
                    "    hsvColor.z += (.15 * step(mod(tempFragCoord.y + tempTime * 15000., 2000. + 2000. * offset3), 400. + 800. * offset3))*sliderValue3;\n" +
                    "\n" +
                    "    color.rgb = hsv2rgb(hsvColor);\n" +
                    "    \n" +
                    "    gl_FragColor = color;\n" +
                    "    \n" +
                    "\n" +
                    "}\n" +
                    "\n";


    final long START_TIME = System.currentTimeMillis();
    float h, w;


    public GPUImageSwGlitch() {
        super(DEFAULT_VERTEX_SHADER, SWGLITCH_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        h = height;
        w = width;

    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("frameWidth"), w);
        GLES20.glUniform1f(getHandle("frameHeight"), h);
        float sliderValue = 1.0f;
        GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
        GLES20.glUniform1f(getHandle("sliderValue2"), 0.5f);
        GLES20.glUniform1f(getHandle("sliderValue3"), 0.5f);
    }
}
