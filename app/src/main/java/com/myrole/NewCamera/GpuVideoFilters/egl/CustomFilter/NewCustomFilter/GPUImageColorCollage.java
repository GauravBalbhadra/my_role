package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageColorCollage extends GlFilter {


    private static final String COLORCOLLAGE_FRAGMENT_SHADER =
            "varying highp vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "//uniform float fractionalWidthOfPixel;\n" +
                    "//uniform float aspectRatio;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "//uniform lowp float frameWidth ;\n" +
                    "//uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "uniform lowp float sliderValue2 ;\n" +
                    "uniform lowp float sliderValue3 ;\n" +
                    "uniform lowp vec2  iMouse;\n" +
                    "\n" +
                    "#define SPEED 40.0\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    \n" +
                    "    lowp vec2 uv =  vTextureCoord.xy; ///iResolution.xy;\n" +
                    "    lowp float c = cos(iTime * SPEED * sliderValue);\n" +
                    "    lowp float s = sin(iTime * SPEED * sliderValue);\n" +
                    "    \n" +
                    "    lowp vec4 orgColor = texture2D(sTexture, uv);\n" +
                    "    lowp int id;\n" +
                    "\n" +
                    "    /*if ( iMouse.w > 0.5)\n" +
                    "    {\n" +
                    "        vec2 muv = iMouse.xy / iResolution.xy;\n" +
                    "        id = int(floor(muv.x * 3.0)) + int(floor(muv.y * 3.0))*3;\n" +
                    "    }\n" +
                    "    else\n" +
                    "    {*/\n" +
                    "    \n" +
                    "        id = int(floor(uv.x * 3.0)) + int(floor(uv.y * 3.0))*3;\n" +
                    "    \n" +
                    "    //}\n" +
                    "\n" +
                    "    // Test\n" +
                    "    //lowp vec2 texuv = (iMouse.w>0.5) ? uv : fract( uv * 3.0 );\n" +
                    "    lowp vec2 texuv = fract(uv * 3.0 );\n" +
                    "    //lowp vec2 texuv = fract( uv * 4.0 );\n" +
                    "    \n" +
                    "    //texuv.x = fract( uv.x * 1.0 );\n" +
                    "    //texuv.y = fract( uv.y * 4.0 );\n" +
                    "\n" +
                    "    lowp vec4 col = texture2D(sTexture, vec2(texuv.x,texuv.y) );\n" +
                    "\n" +
                    "    //test\n" +
                    "    //col = vec4(uv.x, uv.y, 0.5+0.5*sin(iTime), 1.0 );\n" +
                    "\n" +
                    "    lowp vec3 c_r = vec3(100,0,0);\n" +
                    "    lowp vec3 c_g = vec3(0,100,0);\n" +
                    "    lowp vec3 c_b = vec3(0,0,100);\n" +
                    "\n" +
                    "\n" +
                    "    //gl_FragColor = orgColor;\n" +
                    "    \n" +
                    "     \n" +
                    "    //Deuteranomaly (\"red/green\" 6% of males, 0.4% of females)\n" +
                    "    if ( id == 0 ) {\n" +
                    "        c_r = vec3(80, 20, 0);\n" +
                    "        c_g = vec3(25.833, 74.167, 0);\n" +
                    "        c_b = vec3(0, 14.167, 85.833);\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Protanopia (\"red/green\", 1% of males)\n" +
                    "    else if ( id == 1 ) {\n" +
                    "        c_r = vec3(56.667, 43.333, 0);\n" +
                    "        c_g = vec3(55.833, 44.167, 0);\n" +
                    "        c_b = vec3(0, 24.167, 75.833);\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Protanomaly (\"red/green\", 1% of males, 0.01% of females)\n" +
                    "    else if ( id == 2 ) {\n" +
                    "        c_r = vec3(81.667, 18.333, 0);\n" +
                    "        c_g = vec3(33.333, 66.667, 0);\n" +
                    "        c_b = vec3(0, 12.5, 87.5);\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Deuteranopia (\"red/green\", 1% of males)\n" +
                    "    else if ( id == 3 ) {\n" +
                    "        c_r = vec3(62.5, 37.5, 0);\n" +
                    "        c_g = vec3(70, 30, 0);\n" +
                    "        c_b = vec3(0, 30, 70);\n" +
                    "    }\n" +
                    "    \n" +
                    "    else if ( id == 4 ) {\n" +
                    "        //NO ADJUSTMENT\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Tritanomaly (\"blue/yellow\", 0.01% for males and females)\n" +
                    "    else if ( id == 5 ) {\n" +
                    "        c_r = vec3(96.667, 3.333, 0);\n" +
                    "        c_g = vec3(0, 73.333, 26.667);\n" +
                    "        c_b = vec3(0, 18.333, 81.667);\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Achromatopsia (\"Total color blindness\")\n" +
                    "    else if ( id == 6 ) {\n" +
                    "        c_r = vec3(29.9, 58.7, 11.4);\n" +
                    "        c_g = vec3(29.9, 58.7, 11.4);\n" +
                    "        c_b = vec3(29.9, 58.7, 11.4);\n" +
                    "    }\n" +
                    "    \n" +
                    "    //Achromatomaly (\"Total color blindness\")\n" +
                    "    else if ( id == 7 ) {\n" +
                    "        c_r = vec3(61.8, 32, 6.2);\n" +
                    "        c_g = vec3(16.3, 77.5, 6.2);\n" +
                    "        c_b = vec3(16.3, 32.0, 51.6);\n" +
                    "    }\n" +
                    "    //Tritanopia (\"blue/yellow\", <1% of males and females)\n" +
                    "    else if ( id == 8 )\n" +
                    "    {\n" +
                    "        c_r = vec3(95, 5, 0);\n" +
                    "        c_g = vec3(0, 43.333, 56.667);\n" +
                    "        c_b = vec3(0, 47.5, 52.5);\n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "        c_r = vec3(0,0,0);\n" +
                    "        c_g = vec3(0,0,0);\n" +
                    "        c_b = vec3(0,0,0);\n" +
                    "    }\n" +
                    "    \n" +
                    "    \n" +
                    "    c_r /= 100.0;\n" +
                    "    c_g /= 100.0;\n" +
                    "    c_b /= 100.0;\n" +
                    "    \n" +
                    "    \n" +
                    "    // c_r *\n" +
                    "    \n" +
                    "    c_r = mix(vec3(1,0,0),c_r,sliderValue) ;\n" +
                    "    c_g = mix(vec3(0,1,0),c_g,sliderValue) ;\n" +
                    "    c_b = mix(vec3(0,0,1),c_b,sliderValue) ;\n" +
                    "\n" +
                    "     lowp vec3 rgb = vec3( dot(col.rgb,c_r), dot(col.rgb,c_g), dot(col.rgb,c_b) );\n" +
                    "     lowp vec4 fragColor = vec4( rgb, 1 );\n" +
                    "    \n" +
                    "//    lowp vec4 fragColor = pixel;\n" +
                    "//\n" +
                    "//    //\n" +
                    "//    lowp vec3 texColor = texture2D(sTexture,uv).rgb;\n" +
                    "//    if (vTextureCoord.x < iMouse.x)\n" +
                    "//    {\n" +
                    "//        fragColor = vec4(texColor, 1.0);\n" +
                    "//    }\n" +
                    "\n" +
                    "    gl_FragColor = fragColor;\n" +
                    "    \n" +
                    "//*/\n" +
                    "    \n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    private float sliderValue = 1.0f;


    public GPUImageColorCollage() {
        super(DEFAULT_VERTEX_SHADER, COLORCOLLAGE_FRAGMENT_SHADER);

    }


    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);


    }

    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
        GLES20.glUniform1f(getHandle("sliderValue2"), sliderValue);
        GLES20.glUniform1f(getHandle("sliderValue3"), sliderValue);
    }
}
