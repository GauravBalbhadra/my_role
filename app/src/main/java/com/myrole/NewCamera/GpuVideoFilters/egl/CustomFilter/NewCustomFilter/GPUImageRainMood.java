package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageRainMood extends GlFilter {
    private static final String HELLOWORLD_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform  float iTime;\n" +
                    "uniform  float sliderValue ;\n" +
                    "\n" +
                    "const   int MAX_RADIUS = 2;   //2\n" +
                    "\n" +
                    "// Set to 1 to hash twice. Slower, but less patterns.\n" +
                    "const  int DOUBLE_HASH = 1;\n" +
                    "\n" +
                    "//const  float HASHSCALE1 = .1031;\n" +
                    "const  float HASHSCALE1 = .1031;\n" +
                    "//const  vec3  HASHSCALE3 = vec3(.1031, .1030, .0973);\n" +
                    "const  vec3  HASHSCALE3 = vec3(.0631, .0530, .0473);\n" +
                    "\n" +
                    "\n" +
                    "float hash12( vec2 p)\n" +
                    "{\n" +
                    "vec3 p3  = fract(vec3(p.xyx) * HASHSCALE1 );\n" +
                    "p3 += dot(p3, p3.yzx + 19.19);\n" +
                    "return fract((p3.x + p3.y) * p3.z);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 hash22( vec2 p)\n" +
                    "{\n" +
                    "vec3 p3 = fract(vec3(p.xyx) * HASHSCALE3);\n" +
                    "p3 += dot(p3, p3.yzx+19.19);\n" +
                    "return fract((p3.xx+p3.yz)*p3.zy);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "vec2 U = vTextureCoord;\n" +
                    "vec2 iResolution = vec2(1.0,1.0);\n" +
                    "\n" +
                    "float tempTime = mod(iTime,5.0) ;\n" +
                    "\n" +
                    "float resolution = 10. * exp2(-3.*(sliderValue+0.01));\n" +
                    "\n" +
                    "vec2 uv = vTextureCoord.xy / iResolution.y * resolution;\n" +
                    "vec2 p0 = floor(uv);\n" +
                    "\n" +
                    "\n" +
                    "vec2 circles = vec2(0.);\n" +
                    "\n" +
                    "for (int j = -MAX_RADIUS; j <= MAX_RADIUS; ++j)\n" +
                    "{\n" +
                    "for (int i = -MAX_RADIUS; i <= MAX_RADIUS; ++i)\n" +
                    "{\n" +
                    "vec2 pi = p0 + vec2(i, j);\n" +
                    "vec2 hsh;\n" +
                    "\n" +
                    "if (DOUBLE_HASH == 1)\n" +
                    "{\n" +
                    "hsh = hash22(pi);\n" +
                    "}\n" +
                    "if (DOUBLE_HASH == 0)\n" +
                    "{\n" +
                    "hsh = pi;\n" +
                    "}\n" +
                    "\n" +
                    "vec2 p = pi + hash22(hsh);\n" +
                    "\n" +
                    "float t = fract(0.3*tempTime + hash12(hsh));\n" +
                    "vec2  v = p - uv;\n" +
                    "float d = length(v) - (float(MAX_RADIUS) + 1.)*t;\n" +
                    "\n" +
                    "float h = 1e-3;\n" +
                    "float d1 = d - h;\n" +
                    "float d2 = d + h;\n" +
                    "float p1 = sin(31.*d1) * smoothstep(-0.6, -0.3, d1) * smoothstep(0., -0.3, d1);\n" +
                    "float p2 = sin(31.*d2) * smoothstep(-0.6, -0.3, d2) * smoothstep(0., -0.3, d2);\n" +
                    "circles += 0.5 * normalize(v) * ((p2 - p1) / (2. * h) * (1. - t) * (1. - t));\n" +
                    "}\n" +
                    "}\n" +
                    "circles /= float((MAX_RADIUS*2+1)*(MAX_RADIUS*2+1));\n" +
                    "\n" +
                    "float intensity = mix(0.01, 0.15, smoothstep(0.1, 0.6, abs(fract(0.05*tempTime + 0.5)*2.-1.)));\n" +
                    "vec3 n = vec3(circles, sqrt(1. - dot(circles, circles)));\n" +
                    "\n" +
                    "vec3 color = texture2D(sTexture, uv/resolution - intensity*n.xy).rgb + 5.*pow(clamp(dot(n, normalize(vec3(1., 0.7, 0.5))), 0., 1.), 6.);\n" +
                    "\n" +
                    "gl_FragColor = vec4(color, 1.0);\n" +
                    "}";


    final long START_TIME = System.currentTimeMillis();

    public GPUImageRainMood() {
        super(DEFAULT_VERTEX_SHADER, HELLOWORLD_FRAGMENT_SHADER);

    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("sliderValue"), 1.0f);

    }


}
