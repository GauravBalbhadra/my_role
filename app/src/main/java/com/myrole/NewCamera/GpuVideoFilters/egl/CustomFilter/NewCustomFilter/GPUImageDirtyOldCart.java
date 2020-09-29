package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageDirtyOldCart extends GlFilter {
    private static final String DIRTYOLDCART_FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying vec2 vTexture" +
                    "oord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "//filter:mipmap, wrap:repeat\n" +
                    "uniform sampler2D lutTexture; // ## noise_64.png, GL_LINEAR_MIPMAP_NEAREST, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float scanLine; // ## 0.0, 1.0, 0.01\n" +
                    "uniform float dirty; // ## 0.0, 1.0, 0.0\n" +
                    "uniform float uVignette; // ## 0.0, 1.0, 0.0\n" +
                    "uniform float scanGap; // ## 0.0, 10.0, 2.0\n" +
                    "uniform float scanSpeed; // ## 1.0, 10.0, 1.0\n" +
                    "uniform float scanParam; // ## 0.0, 40.0, 4.0\n" +
                    "\n" +
                    "float scanline(vec2 uv) {\n" +
                    "    return sin(resolution.y * uv.y * 0.7 - iTime * 10.0);\n" +
                    "}\n" +
                    "\n" +
                    "float slowscan(vec2 uv) {\n" +
                    "    return sin(resolution.y * uv.y * 0.02 + iTime * 6.0);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 colorShift(vec2 uv) {\n" +
                    "    return vec2(\n" +
                    "                uv.x,\n" +
                    "                uv.y + sin(iTime)*0.02\n" +
                    "                );\n" +
                    "}\n" +
                    "\n" +
                    "float noise(vec2 uv) {\n" +
                    "    return clamp(texture2D(lutTexture, uv.xy + iTime*6.0).r +\n" +
                    "                 texture2D(lutTexture, uv.xy - iTime*4.0).g, 0.96, 1.0);\n" +
                    "}\n" +
                    "\n" +
                    "// from https://www.shadertoy.com/view/4sf3Dr\n" +
                    "// Thanks, Jasper\n" +
                    "vec2 crt(vec2 coord, float bend)\n" +
                    "{\n" +
                    "    // put in symmetrical coords\n" +
                    "    coord = (coord - 0.5) * 2.0;\n" +
                    "    \n" +
                    "    coord *= 0.5;\n" +
                    "    \n" +
                    "    // deform coords\n" +
                    "    coord.x *= 1.0 + pow((abs(coord.y) / bend), 2.0);\n" +
                    "    coord.y *= 1.0 + pow((abs(coord.x) / bend), 2.0);\n" +
                    "    \n" +
                    "    // transform back to 0.0 - 1.0 space\n" +
                    "    coord  = (coord / 1.0) + 0.5;\n" +
                    "    \n" +
                    "    return coord;\n" +
                    "}\n" +
                    "\n" +
                    "vec2 colorshift(vec2 uv, float amount, float rand) {\n" +
                    "    \n" +
                    "    return vec2(\n" +
                    "                uv.x,\n" +
                    "                uv.y + amount * rand // * sin(uv.y * resolution.y * 0.12 + iTime)\n" +
                    "                );\n" +
                    "}\n" +
                    "\n" +
                    "vec2 scandistort(vec2 uv) {\n" +
                    "    float scan1 = clamp(cos(uv.y * scanGap + iTime * scanSpeed), 0.0, 1.0);\n" +
                    "    float scan2 = clamp(cos(uv.y * scanGap + iTime * scanSpeed + scanParam) * 10.0, 0.0, 1.0) ;\n" +
                    "    float amount = scan1 * scan2 * uv.x;\n" +
                    "    \n" +
                    "    uv.x -= 0.05 * mix(texture2D(lutTexture, vec2(uv.x, amount)).r * amount, amount, 0.9);\n" +
                    "    \n" +
                    "    return uv;\n" +
                    "    \n" +
                    "}\n" +
                    "\n" +
                    "float vignette(vec2 uv) {\n" +
                    "    uv = (uv - 0.5) * 0.98;\n" +
                    "    float v = clamp(pow(cos(uv.x * 3.1415), 1.2) * pow(cos(uv.y * 3.1415), 1.2) * 50.0, 0.0, 1.0);\n" +
                    "    return mix(1.0, v, uVignette);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    vec2 sd_uv = scandistort(uv);\n" +
                    "    vec2 crt_uv = crt(sd_uv, 2.0);\n" +
                    "    \n" +
                    "    vec4 color;\n" +
                    "    \n" +
                    "    //float rand_r = sin(iTime * 3.0 + sin(iTime)) * sin(iTime * 0.2);\n" +
                    "    //float rand_g = clamp(sin(iTime * 1.52 * uv.y + sin(iTime)) * sin(iTime* 1.2), 0.0, 1.0);\n" +
                    "    vec4 rand = texture2D(lutTexture, vec2(iTime * 0.01, iTime * 0.02));\n" +
                    "    \n" +
                    "    color.r = texture2D(sTexture, crt(colorshift(sd_uv, 0.025, rand.r), 2.0)).r;\n" +
                    "    color.g = texture2D(sTexture, crt(colorshift(sd_uv, 0.01, rand.g), 2.0)).g;\n" +
                    "    color.b = texture2D(sTexture, crt(colorshift(sd_uv, 0.024, rand.b), 2.0)).b;\n" +
                    "    \n" +
                    "    vec4 scanline_color = vec4(scanline(crt_uv));\n" +
                    "    vec4 slowscan_color = vec4(slowscan(crt_uv));\n" +
                    "    \n" +
                    "    gl_FragColor = vec4((mix(color, mix(scanline_color, slowscan_color, 0.5), 0.05 * scanLine) *\n" +
                    "                         vignette(uv) * mix(1.0, noise(uv), dirty)).rgb, 1.0);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    private float scanLine = 0.5f;
    private float dirty = 0.5f;
    private float uVignette = 1.0f;
    private float scanGap = 0.5f;
    private float scanSpeed = 1.0f;
    private float scanParam = 1.0f;
    private int hTex;
    private Bitmap lutTexture;

    public GPUImageDirtyOldCart(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, DIRTYOLDCART_FRAGMENT_SHADER);
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
        GLES20.glUniform1f(getHandle("scanLine"), scanLine);
        GLES20.glUniform1f(getHandle("dirty"), dirty);
        GLES20.glUniform1f(getHandle("uVignette"), uVignette);
        GLES20.glUniform1f(getHandle("scanGap"), scanGap);
        GLES20.glUniform1f(getHandle("scanSpeed"), scanSpeed);
        GLES20.glUniform1f(getHandle("scanParam"), scanParam);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
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
