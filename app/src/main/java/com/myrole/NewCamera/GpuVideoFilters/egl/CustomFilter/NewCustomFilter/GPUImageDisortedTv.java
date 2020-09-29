package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageDisortedTv extends GlFilter {
    private static final String DISORTEDTV_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float vertMovementOpt; // ## 0.0, 1.0, 1.0\n" +
                    "uniform float bottomStaticOpt; // ## 0.0, 1.0, 1.0\n" +
                    "uniform float rgbOffsetOpt; // ## 0.0, 1.0, 1.0\n" +
                    "uniform float horzFuzzOpt; // ## 0.0, 1.0, 1.0\n" +
                    "\n" +
                    "// change these values to 0.0 to turn off individual effects\n" +
                    "float vertJerkOpt = vertMovementOpt;\n" +
                    "float scalinesOpt = 1.0;\n" +
                    "\n" +
                    "// Noise generation functions borrowed from:\n" +
                    "// https://github.com/ashima/webgl-noise/blob/master/src/noise2D.glsl\n" +
                    "\n" +
                    "vec3 mod289(vec3 x) {\n" +
                    "    return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "\n" +
                    "vec2 mod289(vec2 x) {\n" +
                    "    return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 permute(vec3 x) {\n" +
                    "    return mod289(((x*34.0)+1.0)*x);\n" +
                    "}\n" +
                    "\n" +
                    "float snoise(vec2 v)\n" +
                    "{\n" +
                    "    const vec4 C = vec4(0.211324865405187,  // (3.0-sqrt(3.0))/6.0\n" +
                    "                        0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)\n" +
                    "                        -0.577350269189626,  // -1.0 + 2.0 * C.x\n" +
                    "                        0.024390243902439); // 1.0 / 41.0\n" +
                    "    // First corner\n" +
                    "    vec2 i  = floor(v + dot(v, C.yy) );\n" +
                    "    vec2 x0 = v -   i + dot(i, C.xx);\n" +
                    "    \n" +
                    "    // Other corners\n" +
                    "    vec2 i1;\n" +
                    "    //i1.x = step( x0.y, x0.x ); // x0.x > x0.y ? 1.0 : 0.0\n" +
                    "    //i1.y = 1.0 - i1.x;\n" +
                    "    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);\n" +
                    "    // x0 = x0 - 0.0 + 0.0 * C.xx ;\n" +
                    "    // x1 = x0 - i1 + 1.0 * C.xx ;\n" +
                    "    // x2 = x0 - 1.0 + 2.0 * C.xx ;\n" +
                    "    vec4 x12 = x0.xyxy + C.xxzz;\n" +
                    "    x12.xy -= i1;\n" +
                    "    \n" +
                    "    // Permutations\n" +
                    "    i = mod289(i); // Avoid truncation effects in permutation\n" +
                    "    vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))\n" +
                    "                     + i.x + vec3(0.0, i1.x, 1.0 ));\n" +
                    "    \n" +
                    "    vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);\n" +
                    "    m = m*m ;\n" +
                    "    m = m*m ;\n" +
                    "    \n" +
                    "    // Gradients: 41 points uniformly over a line, mapped onto a diamond.\n" +
                    "    // The ring size 17*17 = 289 is close to a multiple of 41 (41*7 = 287)\n" +
                    "    \n" +
                    "    vec3 x = 2.0 * fract(p * C.www) - 1.0;\n" +
                    "    vec3 h = abs(x) - 0.5;\n" +
                    "    vec3 ox = floor(x + 0.5);\n" +
                    "    vec3 a0 = x - ox;\n" +
                    "    \n" +
                    "    // Normalise gradients implicitly by scaling m\n" +
                    "    // Approximation of: m *= inversesqrt( a0*a0 + h*h );\n" +
                    "    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );\n" +
                    "    \n" +
                    "    // Compute final noise value at P\n" +
                    "    vec3 g;\n" +
                    "    g.x  = a0.x  * x0.x  + h.x  * x0.y;\n" +
                    "    g.yz = a0.yz * x12.xz + h.yz * x12.yw;\n" +
                    "    return 130.0 * dot(m, g);\n" +
                    "}\n" +
                    "\n" +
                    "float staticV(vec2 uv) {\n" +
                    "    float staticHeight = snoise(vec2(9.0,iTime*1.2+3.0))*0.3+5.0;\n" +
                    "    float staticAmount = snoise(vec2(1.0,iTime*1.2-6.0))*0.1+0.3;\n" +
                    "    float staticStrength = snoise(vec2(-9.75,iTime*0.6-3.0))*2.0+2.0;\n" +
                    "    return (1.0-step(snoise(vec2(5.0*pow(iTime,2.0)+pow(uv.x*7.0,1.2),pow((mod(iTime,100.0)+100.0)*uv.y*0.3+3.0,staticHeight))),staticAmount))*staticStrength;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec2 uv = gl_FragCoord.xy/resolution.xy;\n" +
                    "        \n" +
                    "        float jerkOffset = (1.0-step(snoise(vec2(iTime*1.3,5.0)),0.8))*0.05;\n" +
                    "        \n" +
                    "        float fuzzOffset = snoise(vec2(iTime*15.0,uv.y*80.0))*0.003;\n" +
                    "        float largeFuzzOffset = snoise(vec2(iTime*1.0,uv.y*25.0))*0.004;\n" +
                    "        \n" +
                    "        float vertMovementOn = (1.0-step(snoise(vec2(iTime*0.2,8.0)),0.4))*vertMovementOpt;\n" +
                    "        float vertJerk = (1.0-step(snoise(vec2(iTime*1.5,5.0)),0.6))*vertJerkOpt;\n" +
                    "        float vertJerk2 = (1.0-step(snoise(vec2(iTime*5.5,5.0)),0.2))*vertJerkOpt;\n" +
                    "        float yOffset = abs(sin(iTime)*4.0)*vertMovementOn+vertJerk*vertJerk2*0.3;\n" +
                    "        float y = mod(uv.y+yOffset,1.0);\n" +
                    "        \n" +
                    "        float xOffset = (fuzzOffset + largeFuzzOffset) * horzFuzzOpt;\n" +
                    "        \n" +
                    "        float staticVal = 0.0;\n" +
                    "        \n" +
                    "        for (float y = -1.0; y <= 1.0; y += 1.0) {\n" +
                    "            float maxDist = 5.0/200.0;\n" +
                    "            float dist = y/200.0;\n" +
                    "            staticVal += staticV(vec2(uv.x,uv.y+dist))*(maxDist-abs(dist))*1.5;\n" +
                    "        }\n" +
                    "        \n" +
                    "        staticVal *= bottomStaticOpt;\n" +
                    "        \n" +
                    "        float red \t=   texture2D(sTexture,vec2(uv.x + xOffset-0.01*rgbOffsetOpt,y)).r+staticVal;\n" +
                    "        float green = \ttexture2D(\tsTexture, \tvec2(uv.x + xOffset,\t  y)).g+staticVal;\n" +
                    "        float blue \t=\ttexture2D(\tsTexture, \tvec2(uv.x + xOffset +0.01*rgbOffsetOpt,y)).b+staticVal;\n" +
                    "        \n" +
                    "        vec3 color = vec3(red,green,blue);\n" +
                    "        float scanline = sin(uv.y*800.0)*0.04*scalinesOpt;\n" +
                    "        color -= scanline;\n" +
                    "        \n" +
                    "        gl_FragColor = vec4(color,1.0);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageDisortedTv() {
        super(DEFAULT_VERTEX_SHADER, DISORTEDTV_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("vertMovementOpt"), 1.0f);
        GLES20.glUniform1f(getHandle("bottomStaticOpt"), 0.25f);
        GLES20.glUniform1f(getHandle("rgbOffsetOpt"), 0.75f);
        GLES20.glUniform1f(getHandle("horzFuzzOpt"), 0.25f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);


    }

}
