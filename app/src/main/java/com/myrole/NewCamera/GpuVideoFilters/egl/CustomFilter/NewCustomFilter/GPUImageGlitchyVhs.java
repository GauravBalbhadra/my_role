package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageGlitchyVhs extends GlFilter {
    private static final String GLITCHVHS_FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "//filter:mipmap, wrap:repeat\n" +
                    "uniform sampler2D lutTexture; // ## noise_gray.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform float strength;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float glitchy; // ## 0.0, 1.0, 1.0\n" +
                    "uniform float color; // ## 0.0, 1.0, 0.5\n" +
                    "uniform float speed; // ## 1.0, 5.0, 2.0\n" +
                    "uniform float ySpread; // ## 0.01, 0.5, 0.1\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    \n" +
                    "    vec4 moviecol;\n" +
                    "    \n" +
                    "    vec2 uvOffset = texture2D(lutTexture, vec2(iTime*0.0)).rg;\n" +
                    "    uvOffset.x *= (0.02 * glitchy);\n" +
                    "    uvOffset.y *= (0.052 * glitchy);\n" +
                    "    \n" +
                    "    vec2 uvr = uv + uvOffset + vec2(-0.02*texture2D(lutTexture, vec2(uv.x,uv.y/20.0)).r, (tan(sin(iTime * speed)) * 0.6 ) * ySpread * strength);\n" +
                    "    vec2 uvg = uv + uvOffset;\n" +
                    "    vec2 uvb = uv / uvOffset + vec2(-0.01*texture2D(lutTexture, vec2(uv.x,uv.y + iTime*5.0)).r, -0.2);\n" +
                    "    uvr = mix(uv, uvr, glitchy);\n" +
                    "    uvg = mix(uv, uvg, glitchy);\n" +
                    "    uvb = mix(uv, uvb, glitchy);\n" +
                    "    moviecol.r = texture2D(sTexture, uvr).r;\n" +
                    "    moviecol.g = vec4(texture2D(sTexture, uvg)).g;\n" +
                    "    moviecol.b = texture2D(sTexture, uvb).b;\n" +
                    "    \n" +
                    "    moviecol.rgb = mix(moviecol.rgb, vec3(dot(moviecol.rgb, vec3(.43))), color);\n" +
                    "    \n" +
                    "    gl_FragColor = vec4(moviecol.rgb, 1.0);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    private float glitchy = 1.0f;
    private float color = 1.0f;
    private float speed = 1.0f;
    private float ySpread = 1.0f;
    private float strength = 1.0f;
    private int hTex;
    private Bitmap lutTexture;
    public GPUImageGlitchyVhs(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, GLITCHVHS_FRAGMENT_SHADER);
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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("strength"), strength);
        GLES20.glUniform1f(getHandle("glitchy"), glitchy);
        GLES20.glUniform1f(getHandle("color"), color);
        GLES20.glUniform1f(getHandle("speed"), speed);
        GLES20.glUniform1f(getHandle("ySpread"), ySpread);


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
