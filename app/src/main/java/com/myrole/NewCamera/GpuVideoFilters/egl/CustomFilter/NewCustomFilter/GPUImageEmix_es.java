package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageEmix_es extends GlFilter {
    private static final String EMIX_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "//filter:mipmap, wrap:repeat\n" +
                    "uniform sampler2D lutTexture; // ## noise_256.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "uniform float strength;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float blockSize; // ## 15.0, 45.0, 20.0\n" +
                    "uniform float blockCtrl; // ## 0.0, 0.1, 0.05\n" +
                    "\n" +
                    "// license: \"public domain, but I appreciate credits mentions if you use this :)\"\n" +
                    "#define BLOCK_SIZE blockSize\n" +
                    "#define BLOCK_CTRL blockCtrl\n" +
                    "\n" +
                    "float sat( float t ) {\n" +
                    "return clamp( t, 0.0, 1.0 );\n" +
                    "}\n" +
                    "vec2 sat( vec2 t ) {\n" +
                    "return clamp( t, 0.0, 1.0 );\n" +
                    "}\n" +
                    "float remap  ( float t, float a, float b ) {\n" +
                    "return sat( (t - a) / (b - a) );\n" +
                    "}\n" +
                    "float linterp( float t ) {\n" +
                    "return sat( 1.0 - abs( 2.0*t - 1.0 ) );\n" +
                    "}\n" +
                    "// this function and the ones above aren't mine\n" +
                    "// they aren't really needed but I don't feel like\n" +
                    "// removing them either\n" +
                    "// I don't remember where they were from...\n" +
                    "vec3 spectrum_offset( float t ) {\n" +
                    "vec3 ret;\n" +
                    "float lo = step(t,0.5);\n" +
                    "float hi = 1.0-lo;\n" +
                    "float w = linterp( remap( t, 1.0/6.0, 5.0/6.0 ) );\n" +
                    "float neg_w = 1.0-w;\n" +
                    "ret = vec3(lo,1.0,hi) * vec3(neg_w, w, neg_w);\n" +
                    "return pow( ret, vec3(1.0/2.2) );\n" +
                    "}\n" +
                    "\n" +
                    "float rand(vec2 co){\n" +
                    "return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "uv.y *= resolution.y/resolution.x;\n" +
                    "//    float time = iGlobalTime;\n" +
                    "vec4 sum = texture2D(sTexture, vec2(1.0,resolution.x/resolution.y)*uv);\n" +
                    "\n" +
                    "float amount = 6.0 * strength * 1.5;\n" +
                    "for(float i = 0.0; i < amount; i++){\n" +
                    "uv /= pow(mix(vec2(1.0), fract(BLOCK_SIZE*uv)+0.5, clamp(pow(length(texture2D(lutTexture, vec2(0.06*iTime)).xyz ), 15.0),0.0, 1.0)), vec2(BLOCK_CTRL));\n" +
                    "sum = clamp(sum, 0.15, 1.0);\n" +
                    "sum /= 0.1+0.9*clamp(texture2D(sTexture, vec2(1.0,resolution.x/resolution.y)*uv+vec2(1.0/resolution.x,i/resolution.y)),0.0,2.0);\n" +
                    "sum *= 0.1+0.9*clamp(texture2D(sTexture, vec2(1.0,resolution.x/resolution.y)*uv+vec2(1.0/resolution.x,-i/resolution.y)),0.0,2.0);\n" +
                    "sum *= 0.1+0.9*clamp(texture2D(sTexture, vec2(1.0,resolution.x/resolution.y)*uv+vec2(-i/resolution.x,i/resolution.y)),0.0,2.0);\n" +
                    "sum /= 0.1+0.9*clamp(texture2D(sTexture, vec2(1.0,resolution.x/resolution.y)*uv+vec2(-i/resolution.x,-i/resolution.y)),0.0,2.0);\n" +
                    "sum.xyz /= 1.01-0.025*spectrum_offset( 1.0-length(sum.xyz) );\n" +
                    "sum.xyz *= 1.0+0.01*spectrum_offset( length(sum.xyz) );\n" +
                    "\n" +
                    "}\n" +
                    "sum = 0.1+0.9*sum;\n" +
                    "float chromaf = pow(length(texture2D(lutTexture, vec2(0.0213*iTime)).xyz ), 2.0);\n" +
                    "sum /= length(sum);\n" +
                    "sum = (-0.2+2.0*sum)*0.9;\n" +
                    "gl_FragColor = sum;\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    private int hTex;
    private Bitmap lutTexture;
    public GPUImageEmix_es(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, EMIX_FRAGMENT_SHADER);
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
        GLES20.glUniform1f(getHandle("strength"), 0.6f);
        GLES20.glUniform1f(getHandle("blockSize"), 20.0f);
        GLES20.glUniform1f(getHandle("blockCtrl"), 0.05f);
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
