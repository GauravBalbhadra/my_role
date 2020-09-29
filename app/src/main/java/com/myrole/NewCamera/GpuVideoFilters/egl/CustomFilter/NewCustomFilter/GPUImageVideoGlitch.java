package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


//Refrence:= // https://www.shadertoy.com/view/XtK3W3(VideoGlitch.fsh)

public class GPUImageVideoGlitch extends GlFilter {
    public static final String VIDEOGLITCH_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "uniform float color;\n" +
                    "uniform float glitchy;\n" +
                    "vec3 mod289(vec3 x) {\n" +
                    "return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "vec2 mod289(vec2 x) {\n" +
                    "return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
                    "}\n" +
                    "vec3 permute(vec3 x) {\n" +
                    "return mod289(((x*34.0)+1.0)*x);\n" +
                    "}\n" +
                    "float snoise(vec2 v)\n" +
                    "{\n" +
                    "   const vec4 C = vec4(0.211324865405187,0.366025403784439,-0.5773502691896,0.024390243902439);\n" +
                    "   vec2 i  = floor(v + dot(v, C.yy) );\n" +
                    "   vec2 x0 = v - i + dot(i, C.xx);\n" +
                    "   vec2 i1;\n" +
                    "   i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);\n" +
                    "  \n" +
                    "   vec4 x12 = x0.xyxy + C.xxzz;\n" +
                    "   x12.xy -= i1;\n" +
                    "   i = mod289(i); \n" +
                    "   vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))\n" +
                    "    + i.x + vec3(0.0, i1.x, 1.0 ));\n" +
                    "  vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);\n" +
                    "    m = m*m ;\n" +
                    "    m = m*m ;\n" +
                    "    vec3 x = 2.0 * fract(p * C.www) - 1.0;\n" +
                    "    vec3 h = abs(x) - 0.5;\n" +
                    "    vec3 ox = floor(x + 0.5);\n" +
                    "    vec3 a0 = x - ox;\n" +
                    "    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );\n" +
                    "    vec3 g;\n" +
                    "    g.x  = a0.x  * x0.x  + h.x  * x0.y;\n" +
                    "    g.yz = a0.yz * x12.xz + h.yz * x12.yw;\n" +
                    "    return 130.0 * dot(m, g);\n" +
                    "}\n" +
                    "float rand(vec2 co)\n" +
                    "{\n" +
                    "  return fract(sin(dot(co.xy,vec2(12.9898,78.233))) * 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "        vec2 uv = vTextureCoord;\n" +
                    "        float time2 = iTime * 2.0;\n" +
                    "        float noise = max(0.0, snoise(vec2(time2, uv.y * 0.3)) - 0.3) * (1.0 / 0.7);\n" +
                    "        noise = noise + (snoise(vec2(time2*10.0, uv.y * 2.4)) - 0.5) * 0.15;\n" +
                    "        noise *= glitchy;\n" +
                    "        float xpos = mix(uv.x, uv.x - noise * noise * 0.25, 1.0);\n" +
                    "        gl_FragColor = texture2D(sTexture, vec2(xpos, uv.y));\n" +
                    "        \n" +
                    "            gl_FragColor.rgb = mix(gl_FragColor.rgb, vec3(rand(vec2(uv.y * time2))), noise * 0.3).rgb;\n" +
                    "        \n" +
                    "             if (floor(mod(gl_FragCoord.y * 0.25, 2.0)) == 0.0)\n" +
                    "        {\n" +
                    "            gl_FragColor.rgb *= 1.0 - (0.15 * noise);\n" +
                    "        }\n" +
                    "        \n" +
                    "        float gcolor = mix(gl_FragColor.r, texture2D(sTexture, vec2(xpos + noise * 0.05, uv.y)).g, 0.25);\n" +
                    "        float bcolor = mix(gl_FragColor.r, texture2D(sTexture, vec2(xpos - noise * 0.05, uv.y)).b, 0.25);\n" +
                    "        \n" +
                    "        gl_FragColor.g = mix(gl_FragColor.g, gcolor, color);\n" +
                    "        gl_FragColor.b = mix(gl_FragColor.b, bcolor, color);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    private float mColor = 0.8f;
    private float mGlitchy = 0.8f;


    public GPUImageVideoGlitch() {
        super(DEFAULT_VERTEX_SHADER, VIDEOGLITCH_FRAGMENT_SHADER);

    }

    @Override
    protected void onDraw() {
        GLES20.glUniform1f(getHandle("color"), mColor);
        GLES20.glUniform1f(getHandle("glitchy"), mGlitchy);
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
    }
}
