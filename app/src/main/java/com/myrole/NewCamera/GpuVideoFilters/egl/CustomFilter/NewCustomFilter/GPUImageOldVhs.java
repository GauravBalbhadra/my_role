package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageOldVhs extends GlFilter {
    public static final String OLDVHS_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "varying vec2 textureCoordinate2;\n" +
                    "//filter:linear, wrap:repeat\n" +
                    "uniform sampler2D lutTexture; // ## noise_256.png, GL_LINEAR, GL_LINEAR, GL_REPEAT\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float attr1; // ## 0.0, 1.0, 1.0\n" +
                    "\n" +
                    "#define V vec2(0.,1.)\n" +
                    "#define PI 3.14159265\n" +
                    "#define HUGE 1E9\n" +
                    "//#define VHSRES vec2(320.0,240.0)\n" +
                    "#define saturate(i) clamp(i,0.,1.)\n" +
                    "#define lofi(i,d) floor(i/d)*d\n" +
                    "#define validuv(v) (abs(v.x-0.5)<0.5&&abs(v.y-0.5)<0.5)\n" +
                    "\n" +
                    "float v2random( vec2 uv ) {\n" +
                    "    return texture2D( lutTexture, mod( uv, vec2( 1.0 ) ) ).x;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 rgb2yiq( vec3 rgb ) {\n" +
                    "    return mat3( 0.299, 0.596, 0.211, 0.587, -0.274, -0.523, 0.114, -0.322, 0.312 ) * rgb;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 yiq2rgb( vec3 yiq ) {\n" +
                    "    return mat3( 1.000, 1.000, 1.000, 0.956, -0.272, -1.106, 0.621, -0.647, 1.703 ) * yiq;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 vhsTex2D( vec2 uv ) {\n" +
                    "    if ( validuv( uv ) ) {\n" +
                    "        vec3 y = V.yxx * rgb2yiq( texture2D( sTexture, uv ).xyz );\n" +
                    "        vec3 c = V.xyy * rgb2yiq( texture2D( sTexture, uv - 3.0 * V.yx / resolution.x ).xyz );\n" +
                    "        return yiq2rgb( y + c );\n" +
                    "    }\n" +
                    "    return vec3( 0.1, 0.1, 0.1 );\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    \n" +
                    "    vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "    //     float iTime = iGlobaliTime;\n" +
                    "    //    uv.x = 1.0 - uv.x;\n" +
                    "    \n" +
                    "    vec2 uvn = uv;\n" +
                    "    vec3 col = vec3( 0.0, 0.0, 0.0 );\n" +
                    "    \n" +
                    "    // tape wave\n" +
                    "    uvn.x += ( v2random( vec2( uvn.y / 10.0, iTime / 10.0 ) / 1.0 ) - 0.5 ) / resolution.x * 2.0;\n" +
                    "    uvn.x += ( v2random( vec2( uvn.y, iTime * 10.0 ) ) - 0.5 ) / resolution.x * 2.0;\n" +
                    "    \n" +
                    "    bool portrait = resolution.x <= resolution.y;\n" +
                    "    \n" +
                    "    // tape crease\n" +
                    "    float tcPhase = smoothstep( 0.9, 0.96, sin( uvn.y * 8.0 - ( iTime + 0.14 * v2random( iTime * vec2( 0.67, 0.59 ) ) ) * PI * 1.2 ) );\n" +
                    "    float rx = uvn.y * 4.77;\n" +
                    "    if (portrait) {\n" +
                    "        rx *= 0.2;\n" +
                    "    }\n" +
                    "    float tcNoise = smoothstep( 0.3, 1.0, v2random( vec2( rx, iTime ) ) );\n" +
                    "    float tc = tcPhase * tcNoise;\n" +
                    "    if (portrait) {\n" +
                    "        uvn.x = uvn.x - tc / resolution.x * 8.0 * 5.0;\n" +
                    "    } else {\n" +
                    "        uvn.x = uvn.x - tc / resolution.x * 8.0;\n" +
                    "    }\n" +
                    "    \n" +
                    "    // switching noise\n" +
                    "    float snPhase = smoothstep( 6.0 / resolution.y, 0.0, uvn.y );\n" +
                    "    uvn.y += snPhase * 0.3;\n" +
                    "    uvn.x += snPhase * ( ( v2random( vec2( uv.y * 100.0, iTime * 10.0 ) ) - 0.5 ) / resolution.x * 24.0 );\n" +
                    "    \n" +
                    "    // fetch\n" +
                    "    col = vhsTex2D( uvn );\n" +
                    "    \n" +
                    "    // crease noise\n" +
                    "    float cn = tcNoise * ( 0.3 + 0.7 * tcPhase );\n" +
                    "    float th = 0.29;\n" +
                    "    if (portrait) {\n" +
                    "        th -= 0.10;\n" +
                    "    }\n" +
                    "    if ( th < cn ) {\n" +
                    "        vec2 uvt = ( uvn + V.yx * v2random( vec2( uvn.y, iTime ) ) ) * vec2( 0.1, 1.0 );\n" +
                    "        float n0 = v2random( uvt );\n" +
                    "        float n1 = v2random( uvt + V.yx / resolution.x );\n" +
                    "        if ( n1 < n0 ) {\n" +
                    "            if (portrait) {\n" +
                    "                col = mix( col, 2.0 * V.yyy, pow( n0, 2.0 ) );\n" +
                    "            } else {\n" +
                    "                col = mix( col, 2.0 * V.yyy, pow( n0, 5.0 ) );\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "    \n" +
                    "    // switching color modification\n" +
                    "    col = mix(\n" +
                    "              col,\n" +
                    "              col.yzx,\n" +
                    "              snPhase * 0.4\n" +
                    "              );\n" +
                    "    \n" +
                    "    // ac beat\n" +
                    "    col *= 1.0 + 0.1 * smoothstep( 0.4, 0.6, v2random( vec2( 0.0, 0.1 * ( uv.y + iTime * 0.2 ) ) / 10.0 ) );\n" +
                    "    \n" +
                    "    // color noise\n" +
                    "    col *= 0.9 + 0.1 * texture2D( lutTexture, mod( uvn * vec2( 1.0, 1.0 ) + iTime * vec2( 5.97, 4.45 ), vec2( 1.0 ) ) ).xyz;\n" +
                    "    col = saturate( col );\n" +
                    "    \n" +
                    "    // yiq\n" +
                    "    col = rgb2yiq( col );\n" +
                    "    col = vec3( 0.1, -0.1, 0.0 ) + vec3( 0.7, 2.0, 3.4 ) * col;\n" +
                    "    col = yiq2rgb( col );\n" +
                    "    \n" +
                    "    gl_FragColor = vec4(mix(texture2D(sTexture, vTextureCoord).rgb, col, attr1), 1);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    float attr1 = 1.0f;
    private int hTex;
    private Bitmap lutTexture;

    public GPUImageOldVhs(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, OLDVHS_FRAGMENT_SHADER);
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
        GLES20.glUniform1f(getHandle("attr1"), attr1);
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
