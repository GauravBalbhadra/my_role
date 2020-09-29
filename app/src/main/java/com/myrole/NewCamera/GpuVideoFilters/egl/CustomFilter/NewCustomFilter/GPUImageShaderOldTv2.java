package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageShaderOldTv2 extends GlFilter {
    private static final String SHADEROLDTV_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "uniform sampler2D lutTexture;   // ## Noise_RGBA2.png\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "uniform bool flip;  // ## 0\n" +
                    "\n" +
                    "\n" +
                    "#define M_PI (3.1415926535897932384626433832795)\n" +
                    "\n" +
                    "float qScanLine (vec2 uv, float n) {\n" +
                    "    return abs (sin (uv.y*M_PI*n)) ;\n" +
                    "}\n" +
                    "\n" +
                    "float qVignete (vec2 uv,float q, float o) {\n" +
                    "    float x = clamp (1.0 - distance (uv, vec2 (0.5,0.5))*q, 0.0, 1.0);\n" +
                    "    return (log((o - 1.0/exp (o))*x + 1.0/exp (o)) + o)/(log(o) + o);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "vec2 vCrtCurvature (vec2 uv, float q) {\n" +
                    "    float x = 1.0- distance (uv, vec2 (0.5, 0.5));\n" +
                    "    vec2 g = vec2 (0.5, 0.5) - uv;\n" +
                    "    return uv + g*x*q;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 v2DNoiseSample (vec2 gPos) {\n" +
                    "    vec2 nPos = vec2(\n" +
                    "                     mod (gPos.x+iTime*9.66,1.0),\n" +
                    "                     mod (gPos.y+iTime*7.77,1.0)\n" +
                    "                     );\n" +
                    "    return texture2D(lutTexture, nPos);\n" +
                    "}\n" +
                    "\n" +
                    "vec4 v1DNoiseSample (float idx, float s) {\n" +
                    "    return texture2D(lutTexture, vec2 (\n" +
                    "                                     mod (idx, 1.0),\n" +
                    "                                     mod (iTime*s, 1.0))\n" +
                    "                    );\n" +
                    "}\n" +
                    "\n" +
                    "float q2DNoiseSample (vec2 gPos) {\n" +
                    "    vec4 nPnt = v2DNoiseSample (gPos);\n" +
                    "    return nPnt.x;\n" +
                    "}\n" +
                    "\n" +
                    "float q1DNoiseSample (float idx, float s){\n" +
                    "    vec4 nPnt = v1DNoiseSample (idx, s);\n" +
                    "    return nPnt.x;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 cSignalNoise (vec4 c,float q, vec2 gPos) {\n" +
                    "    return c*(1.0 - q) + q*q2DNoiseSample(gPos);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 vScanShift (vec2 uv, float q, float dy, float dt) {\n" +
                    "    return vec2 (uv.x + q1DNoiseSample (uv.y*dy, dt)*q, uv.y);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 vFrameShift (vec2 uv, float q, float dt) {\n" +
                    "    float s = (q1DNoiseSample (0.5, dt) - 0.5)/500.0;\n" +
                    "    return vec2 (uv.x, mod (uv.y + iTime*(q+s), 1.0));\n" +
                    "}\n" +
                    "\n" +
                    "vec2 vDirShift (vec2 uv, float angle, float q) {\n" +
                    "    float a =(angle/180.0)*M_PI;\n" +
                    "    vec2 dir = vec2 (sin (a), cos (a));\n" +
                    "    return uv + dir*q;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 vRGBWithShift (vec2 uv, float angle, float q) {\n" +
                    "    vec2 rPos = vDirShift (uv, angle, q);\n" +
                    "    vec2 gPos = uv;\n" +
                    "    vec2 bPos = vDirShift (uv, -angle, q);\n" +
                    "    vec4 rPix = texture2D(sTexture, rPos);\n" +
                    "    vec4 gPix = texture2D(sTexture, gPos);\n" +
                    "    vec4 bPix = texture2D(sTexture, bPos);\n" +
                    "    return vec4 (rPix.x, gPix.y, bPix.z, 1.0);\n" +
                    "}\n" +
                    "\n" +
                    "vec4 vPowerNoise (vec4 col, vec2 uv, float b, float dt, float w) {\n" +
                    "    float s = q1DNoiseSample (0.0, 0.001)/500.0;\n" +
                    "    float y = mod (iTime * (dt + s) , 1.0);\n" +
                    "    float d = 1.0 - clamp (abs (uv.y - y), 0.0, w)/w;\n" +
                    "    return pow (col,vec4(1.0/(1.0 + b*d)) ) ;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 qGamma (vec4 i, vec4 g){\n" +
                    "    return pow(i, 1.0/g);\n" +
                    "}\n" +
                    "\n" +
                    "vec4 vRGBTint (vec4 col, vec3 g, float q) {\n" +
                    "    return qGamma (col, vec4 (g, 1.0))*q + (1.0 - q)*col;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 rgb2hsv(vec3 c)\n" +
                    "{\n" +
                    "    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n" +
                    "    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n" +
                    "    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n" +
                    "    \n" +
                    "    float d = q.x - min(q.w, q.y);\n" +
                    "    float e = 1.0e-10;\n" +
                    "    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n" +
                    "}\n" +
                    "\n" +
                    "vec3 hsv2rgb(vec3 c)\n" +
                    "{\n" +
                    "    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" +
                    "    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\n" +
                    "    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n" +
                    "}\n" +
                    "\n" +
                    "vec4 vColorDrift (vec4 col, float q) {\n" +
                    "    vec3 hsv = rgb2hsv (col.xyz);\n" +
                    "    hsv.y = mod (hsv.y * q, 1.0);\n" +
                    "    return vec4 (hsv2rgb (hsv), col.w);\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec2 cRes = resolution.xy;\n" +
                    "        vec2 gRes = resolution.xy;\n" +
                    "        vec2 gPos = vTextureCoord;//gl_FragCoord.xy / gRes;\n" +
                    "        \n" +
                    "        // flip y\n" +
                    "        if (flip) {\n" +
                    "            gPos.y = 1.0 - gPos.y;\n" +
                    "        }\n" +
                    "        \n" +
                    "        vec2 cPos = gPos ;\n" +
                    "        vec4 cCol = vec4(1.0);\n" +
                    "        vec2 bPos = vec2(1.0);\n" +
                    "        float qNoise = q1DNoiseSample(0.01,0.01);\n" +
                    "        \n" +
                    "        cPos = vScanShift (cPos, 0.02, 0.1, 0.1);\t\t\t// snaline shift\n" +
                    "        cPos = vCrtCurvature (cPos, 0.3);\t\t\t\t\t// crt curving of coords\n" +
                    "        bPos = vCrtCurvature (gPos, 0.3);\t\t\t\t\t// curvature for the noize bar\n" +
                    "        cPos = vFrameShift (cPos, 0.01, 0.001);\t\t\t\t// frame shift\n" +
                    "        cCol = vColorDrift (cCol, 1.0 - qNoise);\n" +
                    "        cCol = vRGBWithShift (cPos, 100.0, 0.01); \t\t\t// sample signal color\n" +
                    "        cCol = cSignalNoise (cCol, qNoise * 0.8, gPos);\t\t\t\t// add signal noise\n" +
                    "        cCol = vPowerNoise (cCol, bPos, 4.0, -0.2, 0.1); \t// power line noize\n" +
                    "        cCol = vRGBTint (cCol, vec3 (0.9, 0.7, 1.2), 1.0);\t// gamma tint\n" +
                    "        cCol = cCol * qScanLine (gPos, 120.0); \t\t\t\t// add scanlines\n" +
                    "        cCol = cCol * qVignete (gPos, 1.5, 3.0); \t\t\t// add edge darkening\n" +
                    "        gl_FragColor = cCol;\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    private int hTex;
    private Bitmap lutTexture;

    public GPUImageShaderOldTv2(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, SHADEROLDTV_FRAGMENT_SHADER);
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
