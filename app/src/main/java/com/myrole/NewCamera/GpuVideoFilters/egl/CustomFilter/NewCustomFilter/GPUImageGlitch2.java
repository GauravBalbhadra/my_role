package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageGlitch2 extends GlFilter {
    private static final String GLITCH2_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "\n" +
                    "uniform lowp vec2 iMouse ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "\n" +
                    "\n" +
                    "//const lowp vec2 iMouse = vec2(0.5,0.5);   // Need Control\n" +
                    "//const lowp float speed = 1.0 ;   // Need Control\n" +
                    "\n" +
                    "\n" +
                    "lowp float sat( lowp float t ) {\n" +
                    "return clamp( t, 0.0, 1.0 );\n" +
                    "}\n" +
                    "\n" +
                    "lowp vec2 sat(lowp vec2 t ) {\n" +
                    "return clamp( t, 0.0, 1.0 );\n" +
                    "}\n" +
                    "\n" +
                    "//remaps inteval [a;b] to [0;1]\n" +
                    "lowp float remap  ( lowp float t,lowp float a, lowp float b ) {\n" +
                    "return sat( (t - a) / (b - a) );\n" +
                    "}\n" +
                    "\n" +
                    "//note: // t=[0;0.5;1], y=[0;1;0]\n" +
                    "lowp float linterp( lowp float t ) {\n" +
                    "return sat( 1.0 - abs( 2.0*t - 1.0 ) );\n" +
                    "}\n" +
                    "\n" +
                    "lowp vec3 spectrum_offset(lowp float t ) {\n" +
                    "lowp vec3 ret;\n" +
                    "lowp float lo = step(t,0.5);\n" +
                    "lowp float hi = 1.0-lo;\n" +
                    "lowp float w = linterp( remap( t, 1.0/6.0, 5.0/6.0 ) );\n" +
                    "lowp float neg_w = 1.0-w;\n" +
                    "ret = vec3(lo,1.0,hi) * vec3(neg_w, w, neg_w);\n" +
                    "return pow( ret, vec3(1.0/2.2) );\n" +
                    "}\n" +
                    "\n" +
                    "//note: [0;1]\n" +
                    "lowp float rand(lowp vec2 n ) {\n" +
                    "return fract(sin(dot(n.xy, vec2(12.9898, 78.233)))* 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "//note: [-1;1]\n" +
                    "lowp float srand(lowp vec2 n ) {\n" +
                    "return rand(n) * 2.0 - 1.0;\n" +
                    "}\n" +
                    "\n" +
                    "lowp float mytrunc(lowp float x,lowp float num_levels )\n" +
                    "{\n" +
                    "return floor(x*num_levels) / num_levels;\n" +
                    "}\n" +
                    "\n" +
                    "lowp vec2 mytrunc(lowp vec2 x, lowp float num_levels )\n" +
                    "{\n" +
                    "return floor(x*num_levels) / num_levels;\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "\n" +
                    "uv.y = uv.y;\n" +
                    "\n" +
                    "lowp float tempTime = mod(iTime,10.0);\n" +
                    "\n" +
                    "//    lowp float time = mod(iTime*100.0, 32.0)/110.0; // + modelmat[0].x + modelmat[0].z;\n" +
                    "lowp float time = mod(tempTime*100.0, 32.0)/110.0; // + modelmat[0].x + modelmat[0].z;\n" +
                    "\n" +
                    "\n" +
                    "lowp float  tempMouse = iMouse.x;\n" +
                    "\n" +
                    "//    tempMouse = 0.0;\n" +
                    "\n" +
                    "//    lowp float GLITCH = 0.1 + iMouse.x / iResolution.x;\n" +
                    "lowp float GLITCH = 0.1 + tempMouse/ frameWidth;\n" +
                    "GLITCH = sliderValue-0.06;\n" +
                    "\n" +
                    "\n" +
                    "lowp float gnm = sat( GLITCH );\n" +
                    "lowp float rnd0 = rand( mytrunc( vec2(time, time), 6.0 ) );\n" +
                    "lowp float r0 = sat((1.0-gnm)*0.7 + rnd0);\n" +
                    "lowp float rnd1 = rand( vec2(mytrunc( uv.x, 10.0*r0 ), time) ); //horz\n" +
                    "//float r1 = 1.0f - sat( (1.0f-gnm)*0.5f + rnd1 );\n" +
                    "lowp  float r1 = 0.5 - 0.5 * gnm + rnd1;\n" +
                    "r1 = 1.0 - max( 0.0, ((r1<1.0) ? r1 : 0.9999999) ); //note: weird ass bug on old drivers\n" +
                    "lowp float rnd2 = rand( vec2(mytrunc( uv.y, 40.0*r1 ), time) ); //vert\n" +
                    "lowp float r2 = sat( rnd2 );\n" +
                    "\n" +
                    "lowp float rnd3 = rand( vec2(mytrunc( uv.y, 10.0*r0 ), time) );\n" +
                    "lowp float r3 = (1.0-sat(rnd3+0.8)) - 0.1;\n" +
                    "\n" +
                    "lowp float pxrnd = rand( uv + time );\n" +
                    "\n" +
                    "lowp float ofs = 0.05 * r2 * GLITCH * ( rnd0 > 0.5 ? 1.0 : -1.0 );\n" +
                    "ofs += 0.5 * pxrnd * ofs;\n" +
                    "\n" +
                    "uv.y += 0.1 * r3 * GLITCH;\n" +
                    "\n" +
                    "const lowp int NUM_SAMPLES = 20; //20.0\n" +
                    "\n" +
                    "const lowp float RCP_NUM_SAMPLES_F = 1.0 / float(NUM_SAMPLES);\n" +
                    "\n" +
                    "lowp vec4 sum = vec4(0.0);\n" +
                    "lowp vec3 wsum = vec3(0.0);\n" +
                    "for( int i=0; i<NUM_SAMPLES; ++i )\n" +
                    "{\n" +
                    "lowp float t = float(i) * RCP_NUM_SAMPLES_F;\n" +
                    "uv.x = sat( uv.x + ofs * t );\n" +
                    "lowp vec4 samplecol = texture2D( sTexture, uv, -10.0 );\n" +
                    "lowp vec3 s = spectrum_offset( t );\n" +
                    "samplecol.rgb = samplecol.rgb * s;\n" +
                    "sum += samplecol;\n" +
                    "wsum += s;\n" +
                    "}\n" +
                    "sum.rgb /= wsum;\n" +
                    "sum.a *= RCP_NUM_SAMPLES_F;\n" +
                    "\n" +
                    "gl_FragColor.a = sum.a;\n" +
                    "gl_FragColor.rgb = sum.rgb; // * outcol0.a;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "//   gl_FragColor =  texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageGlitch2() {
        super(DEFAULT_VERTEX_SHADER, GLITCH2_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), 1.0f);
        GLES20.glUniform1f(getHandle("frameWidth"), widths);
    }


}
