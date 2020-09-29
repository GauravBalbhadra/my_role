package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageGlitchPixel extends GlFilter {
    private static final String GLITCHPIXEL_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control\n" +
                    "//const lowp float speed = 1.0 ;   // Need Control\n" +
                    "\n" +
                    "lowp float hash(lowp vec2 p)\n" +
                    "{\n" +
                    "lowp float h = dot(p,vec2(127.1,311.7));\n" +
                    "return -1.0 + 2.0*fract(sin(h)*43758.5453123);\n" +
                    "}\n" +
                    "\n" +
                    "lowp float noise(lowp vec2 p)\n" +
                    "{\n" +
                    "lowp vec2 i = floor(p);\n" +
                    "lowp vec2 f = fract(p);\n" +
                    "\n" +
                    "lowp vec2 u = f*f*(3.0-2.0*f);\n" +
                    "\n" +
                    "return mix(mix(hash( i + vec2(0.0,0.0) ),\n" +
                    "hash( i + vec2(1.0,0.0) ), u.x),\n" +
                    "mix( hash( i + vec2(0.0,1.0) ),\n" +
                    "hash( i + vec2(1.0,1.0) ), u.x), u.y);\n" +
                    "}\n" +
                    "\n" +
                    "lowp float noise(lowp vec2 p, lowp int oct)\n" +
                    "{\n" +
                    "lowp mat2 m = mat2( 1.6,  1.2, -1.2,  1.6 );\n" +
                    "lowp float f  = 0.0;\n" +
                    "\n" +
                    "for(lowp int i = 1; i < 3; i++){\n" +
                    "lowp float mul = 1.0/pow(2.0, float(i));\n" +
                    "f += mul*noise(p);\n" +
                    "p = m*p;\n" +
                    "}\n" +
                    "\n" +
                    "return f;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "\n" +
                    "// lowp vec2 uv = fragCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "lowp float glitch = pow(cos(iTime*0.5)*1.2+1.0, 1.2);\n" +
                    "\n" +
                    "\n" +
                    "if(noise(iTime+vec2(0, 0))*glitch > 0.62)\n" +
                    "{\n" +
                    "uv.y = mod(uv.y+noise(vec2(iTime*4.0, 0)), 1.0);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "lowp vec2 hp = vec2(0.0, uv.y);\n" +
                    "\n" +
                    "lowp float nh = noise(hp*7.0+iTime*10.0, 3) * (noise(hp+iTime*0.3)*0.8);\n" +
                    "nh += noise(hp*100.0+iTime*10.0, 3)*0.02;\n" +
                    "\n" +
                    "lowp float rnd = 0.0;\n" +
                    "if(glitch > 0.0){\n" +
                    "rnd = hash(uv);\n" +
                    "if(glitch < 1.0){\n" +
                    "rnd *= glitch;\n" +
                    "}\n" +
                    "}\n" +
                    "nh *= glitch + rnd;\n" +
                    "lowp float r = texture2D(sTexture, uv+vec2(nh, 0.08)*nh).r;\n" +
                    "lowp float g = texture2D(sTexture, uv+vec2(nh-0.07, 0.0)*nh).g;\n" +
                    "lowp float b = texture2D(sTexture, uv+vec2(nh, 0.0)*nh).b;\n" +
                    "\n" +
                    "lowp vec3 col = vec3(r, g, b);\n" +
                    "//    fragColor = vec4(col.rgb, 1.0);\n" +
                    "\n" +
                    "gl_FragColor = vec4(col.rgb, 1.0) + texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "\n" +
                    "//    gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "}\n";
    //    int heights;
//    int widths;
    final long START_TIME = System.currentTimeMillis();

    public GPUImageGlitchPixel() {
        super(DEFAULT_VERTEX_SHADER, GLITCHPIXEL_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
//        widths = width;
//        heights = height;


    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        GLES20.glUniform1f(getHandle("iTime"), time);
//        GLES20.glUniform1f(getHandle("amplitude"), 1.0f);
//        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
