package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUimageVCRdistortion extends GlFilter {
    private static final String VCRADIATION_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "//filter:mipmap, wrap:repeat\n" +
                    "uniform sampler2D lutTexture; // ## noise_gray.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float shift; // ## 0.0, 1.0, 1.0\n" +
                    "uniform bool enableStripes; // ## 1\n" +
                    "uniform bool enableNoise; // ## 1\n" +
                    "uniform bool enableVignette; // ## 1\n" +
                    "\n" +
                    "uniform float opacity; // ## 0, 1, 1\n" +
                    "\n" +
                    "float noise(vec2 p)\n" +
                    "{\n" +
                    "float s = texture2D(lutTexture,vec2(1.,2.*cos(iTime))*iTime*8. + p*opacity).x;\n" +
                    "s *= s;\n" +
                    "return s;\n" +
                    "}\n" +
                    "\n" +
                    "float onOff(float a, float b, float c)\n" +
                    "{\n" +
                    "return step(c, sin(iTime + a*cos(iTime*b)));\n" +
                    "}\n" +
                    "\n" +
                    "float ramp(float y, float start, float end)\n" +
                    "{\n" +
                    "float inside = step(start,y) - step(end,y);\n" +
                    "float fact = (y-start)/(end-start)*inside;\n" +
                    "return (1.-fact) * inside;\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "float stripes(vec2 uv)\n" +
                    "{\n" +
                    "\n" +
                    "float noi = noise(uv*vec2(0.5,1.) + vec2(1.,3.));\n" +
                    "return ramp(mod(uv.y*4. + iTime/2.+sin(iTime + sin(iTime*0.63)),1.),0.5,0.6)*noi;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 getVideo(vec2 uv)\n" +
                    "{\n" +
                    "vec2 look = uv;\n" +
                    "float window = 1./(1.+20.*(look.y-mod(iTime/4.,1.))*(look.y-mod(iTime/4.,1.)));\n" +
                    "look.x = look.x + sin(look.y*10. + iTime)/50.*onOff(4.,4.,.3)*(1.+cos(iTime*80.))*window;\n" +
                    "float vShift = 0.4*onOff(2.,3.,.9)*(sin(iTime)*sin(iTime*20.) +\n" +
                    "(0.5 + 0.1*sin(iTime*200.)*cos(iTime)));\n" +
                    "look.y = mod(look.y + vShift, 1.);\n" +
                    "\n" +
                    "look = mix(uv, look, shift);\n" +
                    "\n" +
                    "vec3 video = vec3(texture2D(sTexture,look));\n" +
                    "return video;\n" +
                    "}\n" +
                    "\n" +
                    "vec2 screenDistort(vec2 uv)\n" +
                    "{\n" +
                    "uv -= vec2(.5,.5);\n" +
                    "uv = uv*1.2*(1./1.2+2.*uv.x*uv.x*uv.y*uv.y);\n" +
                    "uv += vec2(.5,.5);\n" +
                    "return uv;\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "if (!enableVignette) uv = screenDistort(uv);\n" +
                    "vec3 video = getVideo(uv);\n" +
                    "float vigAmt = 3.+.3*sin(iTime + 5.*cos(iTime*5.));\n" +
                    "float vignette = (1.-vigAmt*(uv.y-.5)*(uv.y-.5))*(1.-vigAmt*(uv.x-.5)*(uv.x-.5));\n" +
                    "\n" +
                    "if (!enableStripes) video += stripes(uv);\n" +
                    "if (!enableNoise) video += noise(uv*2.)/2.;\n" +
                    "if (!enableVignette) video *= vignette;\n" +
                    "if (!enableStripes) video *= (12.+mod(uv.y*30.+iTime,1.))/13.;\n" +
                    "\n" +
                    "gl_FragColor = vec4(video, 1.0);\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    float strength = 1.0f;
    private int hTex;
    private Bitmap lutTexture;

    public GPUimageVCRdistortion(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, VCRADIATION_FRAGMENT_SHADER);
        this.lutTexture = bitmap;
        hTex = EglUtil.NO_TEXTURE;
    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;

    }

    @Override
    public void onDraw() {
        int offsetDepthMapTextureUniform = getHandle("lutTexture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, hTex);
        GLES20.glUniform1i(offsetDepthMapTextureUniform, 3);
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("shift"), strength);
        GLES20.glUniform1f(getHandle("opacity"), 1.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }

    @Override
    public void setup() {
        super.setup();
        loadTexture();
    }

    private void loadTexture() {
        if (hTex == EglUtil.NO_TEXTURE) {
            hTex = EglUtil.loadTexture(lutTexture, EglUtil.NO_TEXTURE, false);
        }
    }

    public void releaseLutBitmap() {
        if (lutTexture != null && !lutTexture.isRecycled()) {
            lutTexture.recycle();
            lutTexture = null;
        }
    }

    public void reset() {
        hTex = EglUtil.NO_TEXTURE;
        hTex = EglUtil.loadTexture(lutTexture, EglUtil.NO_TEXTURE, false);
    }


}
