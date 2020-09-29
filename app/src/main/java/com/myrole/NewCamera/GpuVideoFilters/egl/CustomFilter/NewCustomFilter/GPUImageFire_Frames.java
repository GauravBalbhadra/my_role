package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageFire_Frames extends GlFilter {
    private static final String FIEFRAMES_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled; // ## 1\n" +
                    "\n" +
                    "uniform float fire1; // ## -5, 5.0, 1.5\n" +
                    "uniform float fire2; // ## 0, 3.0, 0.8\n" +
                    "uniform float fire3; // ## 1.0, 4.0, 2.0\n" +
                    "uniform float fire4; // ## -2, 2.0, 0.25\n" +
                    "\n" +
                    "\n" +
                    "uniform float noiseScale; // ## 0, 20, 10\n" +
                    "uniform float noiseTimeScale; // ## 0, 3, 0.3\n" +
                    "uniform float noiseUpSpeed; // ## 0, 10, 5.0\n" +
                    "\n" +
                    "//const float noiseScale = 10.;\n" +
                    "//const float noiseTimeScale = 0.3;\n" +
                    "//const float noiseUpSpeed = 5.0;\n" +
                    "\n" +
                    "uniform int noiseCurlSteps; // ## 0, 10, 2\n" +
                    "uniform float noiseCurlValue; // ## 0, 3, 0.5\n" +
                    "\n" +
                    "//const int noiseCurlSteps = 2;\n" +
                    "//const float noiseCurlValue = 0.5;\n" +
                    "//const float noiseCurlStepValue = noiseCurlValue / float(noiseCurlSteps);\n" +
                    "\n" +
                    "const int colorsCount = 16;\n" +
                    "const vec3 c0 = vec3(1.00, 1.00, 1.00);\n" +
                    "const vec3 c1 = vec3(1.00, 0.97, 0.70);\n" +
                    "const vec3 c2 = vec3(0.99, 0.94, 0.50);\n" +
                    "const vec3 c3 = vec3(0.98, 0.90, 0.30);\n" +
                    "const vec3 c4 = vec3(0.98, 0.85, 0.25);\n" +
                    "const vec3 c5 = vec3(0.98, 0.80, 0.20);\n" +
                    "const vec3 c6 = vec3(0.98, 0.75, 0.15);\n" +
                    "const vec3 c7 = vec3(0.98, 0.70, 0.10);\n" +
                    "const vec3 c8 = vec3(0.98, 0.60, 0.00);\n" +
                    "const vec3 c9 = vec3(0.95, 0.50, 0.00);\n" +
                    "const vec3 c10 = vec3(0.90, 0.40, 0.00);\n" +
                    "const vec3 c11 = vec3(0.75, 0.30, 0.00);\n" +
                    "const vec3 c12 = vec3(0.60, 0.20, 0.00);\n" +
                    "const vec3 c13 = vec3(0.50, 0.10, 0.00);\n" +
                    "const vec3 c14 = vec3(0.40, 0.10, 0.00);\n" +
                    "const vec3 c15 = vec3(0.00, 0.00, 0.00);\n" +
                    "\n" +
                    "vec3 getColor(int i) {\n" +
                    "return i<8 ?\n" +
                    "i<4 ? i<2 ? i==0 ? c0 : c1 : i==2 ? c2 : c3 : i<6 ? i==4 ? c4 : c5 : i==6 ? c6 : c7 :\n" +
                    "i<12 ? i<10 ? i==8 ? c8 : c9 : i==10 ? c10 : c11 : i<14 ? i==12 ? c12 : c13 : i==14 ? c14 : c15;\n" +
                    "\n" +
                    "}\n" +
                    "vec3 getColor(float v) {\n" +
                    "v = 1.0 - v;\n" +
                    "int i = int(v * float(colorsCount-1));\n" +
                    "vec3 color1 = getColor(i);\n" +
                    "vec3 color2 = getColor(i+1);\n" +
                    "return mix(color1, color2, fract(v * float(colorsCount-1)));\n" +
                    "}\n" +
                    "\n" +
                    "float simplex(vec3 v);\n" +
                    "float getNoise(vec3 v, float curl);\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "uv.y = 1.0 - uv.y;\n" +
                    "vec3 noisePos = vec3(uv * noiseScale - vec2(0, iTime * noiseUpSpeed), iTime * noiseTimeScale);\n" +
                    "float noise = getNoise(noisePos, 0.5+sqrt(uv.y)/2.);  //more curly in the upper part\n" +
                    "float fire = noise*noise*fire1 + noise*fire2 - uv.y*fire3 + fire4;  //more contrast noise in the upper part\n" +
                    "fire = clamp(fire, 0.0, 1.0);\n" +
                    "\n" +
                    "vec4 fireFrame = vec4(getColor(fire), 1.0);\n" +
                    "vec4 video = texture2D(sTexture, vTextureCoord);\n" +
                    "vec4 whiteColor = vec4(1.0);\n" +
                    "gl_FragColor = whiteColor - ((whiteColor - video) * (whiteColor - fireFrame));\n" +
                    "\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//    noise\n" +
                    "\n" +
                    "float fbm3(vec3 v) {\n" +
                    "float result = simplex(v);\n" +
                    "result += simplex(v * 2.) / 2.;\n" +
                    "result += simplex(v * 4.) / 4.;\n" +
                    "result /= (1. + 1./2. + 1./4.);\n" +
                    "return result;\n" +
                    "}\n" +
                    "\n" +
                    "float fbm5(vec3 v) {\n" +
                    "float result = simplex(v);\n" +
                    "result += simplex(v * 2.) / 2.;\n" +
                    "result += simplex(v * 4.) / 4.;\n" +
                    "result += simplex(v * 8.) / 8.;\n" +
                    "result += simplex(v * 16.) / 16.;\n" +
                    "result /= (1. + 1./2. + 1./4. + 1./8. + 1./16.);\n" +
                    "return result;\n" +
                    "}\n" +
                    "\n" +
                    "float getNoise(vec3 v, float curl) {\n" +
                    "float noiseCurlStepValue = noiseCurlValue / float(noiseCurlSteps);\n" +
                    "\n" +
                    "//  make it curl\n" +
                    "for (int i=0; i<noiseCurlSteps; i++) {\n" +
                    "v.xy += vec2(fbm3(v), fbm3(vec3(v.xy, v.z + 1000.))) * noiseCurlStepValue * curl;\n" +
                    "}\n" +
                    "//  normalize\n" +
                    "return fbm5(v) / 2. + 0.5;\n" +
                    "}\n" +
                    "\n" +
                    "//\n" +
                    "// Description : Array and textureless GLSL 2D/3D/4D simplex\n" +
                    "//               noise functions.\n" +
                    "//      Author : Ian McEwan, Ashima Arts.\n" +
                    "//  Maintainer : ijm\n" +
                    "//     Lastmod : 20110822 (ijm)\n" +
                    "//     License : Copyright (C) 2011 Ashima Arts. All rights reserved.\n" +
                    "//               Distributed under the MIT License. See LICENSE file.\n" +
                    "//               https://github.com/ashima/webgl-noise\n" +
                    "//\n" +
                    "\n" +
                    "vec3 mod289(vec3 x) {\n" +
                    "return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 mod289(vec4 x) {\n" +
                    "return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "\n" +
                    "vec4 permute(vec4 x) {\n" +
                    "return mod289(((x*34.0)+1.0)*x);\n" +
                    "}\n" +
                    "\n" +
                    "vec4 taylorInvSqrt(vec4 r)\n" +
                    "{\n" +
                    "return 1.79284291400159 - 0.85373472095314 * r;\n" +
                    "}\n" +
                    "\n" +
                    "float simplex(vec3 v)\n" +
                    "{\n" +
                    "const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;\n" +
                    "const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);\n" +
                    "\n" +
                    "// First corner\n" +
                    "vec3 i  = floor(v + dot(v, C.yyy) );\n" +
                    "vec3 x0 =   v - i + dot(i, C.xxx) ;\n" +
                    "\n" +
                    "// Other corners\n" +
                    "vec3 g = step(x0.yzx, x0.xyz);\n" +
                    "vec3 l = 1.0 - g;\n" +
                    "vec3 i1 = min( g.xyz, l.zxy );\n" +
                    "vec3 i2 = max( g.xyz, l.zxy );\n" +
                    "\n" +
                    "//   x0 = x0 - 0.0 + 0.0 * C.xxx;\n" +
                    "//   x1 = x0 - i1  + 1.0 * C.xxx;\n" +
                    "//   x2 = x0 - i2  + 2.0 * C.xxx;\n" +
                    "//   x3 = x0 - 1.0 + 3.0 * C.xxx;\n" +
                    "vec3 x1 = x0 - i1 + C.xxx;\n" +
                    "vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y\n" +
                    "vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y\n" +
                    "\n" +
                    "// Permutations\n" +
                    "i = mod289(i);\n" +
                    "vec4 p = permute( permute( permute(\n" +
                    "i.z + vec4(0.0, i1.z, i2.z, 1.0 ))\n" +
                    "+ i.y + vec4(0.0, i1.y, i2.y, 1.0 ))\n" +
                    "+ i.x + vec4(0.0, i1.x, i2.x, 1.0 ));\n" +
                    "\n" +
                    "// Gradients: 7x7 points over a square, mapped onto an octahedron.\n" +
                    "// The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)\n" +
                    "float n_ = 0.142857142857; // 1.0/7.0\n" +
                    "vec3  ns = n_ * D.wyz - D.xzx;\n" +
                    "\n" +
                    "vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)\n" +
                    "\n" +
                    "vec4 x_ = floor(j * ns.z);\n" +
                    "vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)\n" +
                    "\n" +
                    "vec4 x = x_ *ns.x + ns.yyyy;\n" +
                    "vec4 y = y_ *ns.x + ns.yyyy;\n" +
                    "vec4 h = 1.0 - abs(x) - abs(y);\n" +
                    "\n" +
                    "vec4 b0 = vec4( x.xy, y.xy );\n" +
                    "vec4 b1 = vec4( x.zw, y.zw );\n" +
                    "\n" +
                    "//vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;\n" +
                    "//vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;\n" +
                    "vec4 s0 = floor(b0)*2.0 + 1.0;\n" +
                    "vec4 s1 = floor(b1)*2.0 + 1.0;\n" +
                    "vec4 sh = -step(h, vec4(0.0));\n" +
                    "\n" +
                    "vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;\n" +
                    "vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;\n" +
                    "\n" +
                    "vec3 p0 = vec3(a0.xy,h.x);\n" +
                    "vec3 p1 = vec3(a0.zw,h.y);\n" +
                    "vec3 p2 = vec3(a1.xy,h.z);\n" +
                    "vec3 p3 = vec3(a1.zw,h.w);\n" +
                    "\n" +
                    "//Normalise gradients\n" +
                    "vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));\n" +
                    "p0 *= norm.x;\n" +
                    "p1 *= norm.y;\n" +
                    "p2 *= norm.z;\n" +
                    "p3 *= norm.w;\n" +
                    "\n" +
                    "// Mix final noise value\n" +
                    "vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);\n" +
                    "m = m * m;\n" +
                    "return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1),\n" +
                    "dot(p2,x2), dot(p3,x3) ) );\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageFire_Frames() {
        super(DEFAULT_VERTEX_SHADER, FIEFRAMES_FRAGMENT_SHADER);

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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);

        GLES20.glUniform1f(getHandle("fire1"), 2);
        GLES20.glUniform1f(getHandle("fire2"), 0.8f);
        GLES20.glUniform1f(getHandle("fire3"), 2.0f);
        GLES20.glUniform1f(getHandle("fire4"), -0.10f);

        GLES20.glUniform1f(getHandle("noiseScale"), 10.0f);
        GLES20.glUniform1f(getHandle("noiseTimeScale"), 0.3f);

        GLES20.glUniform1f(getHandle("noiseUpSpeed"), 5.0f);
        GLES20.glUniform1f(getHandle("noiseCurlSteps"), 2);
        GLES20.glUniform1f(getHandle("noiseCurlValue"), 0.5f);


    }


}
