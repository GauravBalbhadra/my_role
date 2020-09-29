package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageChromaticGlitch extends GlFilter {

//    private static final String CHROMGLITCH_FRAGMENT_SHADER =
//            "precision mediump float;\n" +
//                    "varying vec2 vTextureCoord;\n" +
//                    "uniform lowp sampler2D sTexture;\n" +
//                    "\n" +
//                    "uniform float iTime;\n" +
//                    "uniform int Auto;\n" +
//                    "\n" +
//                    "uniform float sliderValue;    // Work on Distance .\n" +
//                    "uniform float sliderValue2;   //  Work on Round .\n" +
//                    "uniform float sliderValue3;   //  Work on Round .\n" +
//                    "\n" +
//                    "\n" +
//                    "vec2 calDist (float angle ,float radius) {\n" +
//                    "     vec2 center = vec2(0.0,0.0);\n" +
//                    "     return vec2 (radius*cos(angle)+ center.x , radius*sin(angle) + center.y);\n" +
//                    "}\n" +
//                    "\n" +
//                    "void main()\n" +
//                    "{\n" +
//                    "    vec2 iResolution = vec2(1, 1);  // temp need to change\n" +
//                    "    vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
//                    "    \n" +
//                    "    if (Auto == 1)  //  Auto Enable .\n" +
//                    "    {\n" +
//                    "        float Distance =0.5;\n" +
//                    "        \n" +
//                    "        //Distance = sliderValue * 0.7;\n" +
//                    "        \n" +
//                    "        float vertical = sliderValue2 ;\n" +
//                    "        float horizontal = sliderValue3 ;\n" +
//                    "        \n" +
//                    "        float amount = 0.0 ;\n" +
//                    "        \n" +
//                    "        float  power = iTime;\n" +
//                    "        \n" +
//                    "        power *= (sliderValue*2.0);\n" +
//                    "        \n" +
//                    "        amount = (1.0 + sin(power*6.0)) * Distance;\n" +
//                    "        amount *= 1.0 + sin(power*16.0) * Distance;\n" +
//                    "        amount *= 1.0 + sin(power*19.0) * Distance;\n" +
//                    "        amount *= 1.0 + sin(power*27.0) * Distance;\n" +
//                    "        amount = pow(amount, 3.0);\n" +
//                    "        amount *= 0.05;\n" +
//                    "        \n" +
//                    "        vec3 col;\n" +
//                    "        col.r = texture2D( sTexture, vec2(uv.x+(amount*horizontal),uv.y+(amount*vertical))).r;\n" +
//                    "        col.g = texture2D( sTexture, uv ).g;\n" +
//                    "        col.b = texture2D( sTexture, vec2(uv.x-(amount*horizontal),uv.y+(amount*vertical))).b;\n" +
//                    "        col *= (1.0 - amount * 0.5);\n" +
//                    "        \n" +
//                    "        gl_FragColor = vec4(col,1.0);\n" +
//                    "        \n" +
//                    "    } else {\n" +
//                    "        \n" +
//                    "        vec3 col;\n" +
//                    "        \n" +
//                    "        float Distance = 0.5;  //0.5\n" +
//                    "        Distance = (sliderValue2 * 0.5)+0.2 ;\n" +
//                    "        Distance = pow(Distance, 6.0);\n" +
//                    "        \n" +
//                    "        float angleValue ;\n" +
//                    "        angleValue = sliderValue ;\n" +
//                    "        \n" +
//                    "        \n" +
//                    "        float Angle1  = (angleValue * 6.28319) ;\n" +
//                    "        float Angle2  = (angleValue * (6.28319 * 2.06 )) ;\n" +
//                    "        float Angle3  = (angleValue * (6.28319 * (2.06*2.0 ) ));\n" +
//                    "        \n" +
//                    "        col.r = texture2D( sTexture,uv + calDist(Angle1,Distance)).r;\n" +
//                    "        col.g = texture2D( sTexture,uv + calDist(Angle2,Distance)).g;\n" +
//                    "        col.b = texture2D( sTexture,uv + calDist(Angle3,Distance)).b;\n" +
//                    "        \n" +
//                    "        gl_FragColor = vec4(col,1.0);\n" +
//                    "    }\n" +
//                    "}\n";


    private static final String CHROMGLITCH_FRAGMENT_SHADER =
               " varying highp vec2 vTextureCoord;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "//uniform lowp float gamma;\n" +
                    "\n" +
                    "//uniform lowp float fractionalWidthOfPixel;\n" +
                    "//uniform lowp float aspectRatio;\n" +
                    "//uniform highp float dotScaling;\n" +
                    "//uniform int smallDot;\n" +
                    "//uniform int randomClr;\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "uniform lowp float frameWidth ;\n" +
                    "uniform lowp float frameHeight ;\n" +
                    "\n" +
                    "uniform lowp int Auto;   // 0 or 1\n" +
                    "\n" +
                    "uniform lowp float sliderValue ;    // Work on Distance .\n" +
                    "uniform lowp float sliderValue2 ;   //  Work on Round .\n" +
                    "uniform lowp float sliderValue3 ;   //  Work on Round .\n" +
                    "\n" +
                    "\n" +
                    "/*\n" +
                    " -(CGPoint)setPointToAngle:(int)angle center:(CGPoint)centerPoint radius:(double)radius\n" +
                    " {\n" +
                    " return CGPointMake(radius*cos(degreesToRadians(angle)) + centerPoint.x, radius*sin(degreesToRadians(angle)) + centerPoint.y);\n" +
                    " }\n" +
                    " */\n" +
                    "\n" +
                    "\n" +
                    "lowp vec2 calDist (lowp float angle , lowp float radius )\n" +
                    "{\n" +
                    "    lowp vec2 center = vec2(0.0,0.0);\n" +
                    "    \n" +
                    "    return vec2 (radius*cos(angle)+ center.x , radius*sin(angle) + center.y  );\n" +
                    "//    return vec2 (1.0,1.0);\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "\n" +
                    "    lowp vec2 iResolution = vec2(1, 1);  // temp need to change\n" +
                    "    lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "    \n" +
                    "    if (Auto == 1)  //  Auto Enable .\n" +
                    "    {\n" +
                    "        lowp float Distance =0.5;\n" +
                    "        \n" +
                    "        //Distance = sliderValue * 0.7;\n" +
                    "        \n" +
                    "        lowp float vertical = sliderValue2 ;\n" +
                    "        lowp float horizontal = sliderValue3 ;\n" +
                    "\n" +
                    "        lowp float amount = 0.0 ;\n" +
                    "        \n" +
                    "        //lowp float iTime = 0.1 ;\n" +
                    "        \n" +
                    "        \n" +
                    "        lowp float  power = iTime;\n" +
                    "        \n" +
                    "        power *= (sliderValue*2.0);\n" +
                    "        \n" +
                    "        amount = (1.0 + sin(power*6.0)) * Distance;\n" +
                    "        amount *= 1.0 + sin(power*16.0) * Distance;\n" +
                    "        amount *= 1.0 + sin(power*19.0) * Distance;\n" +
                    "        amount *= 1.0 + sin(power*27.0) * Distance;\n" +
                    "        amount = pow(amount, 3.0);\n" +
                    "        amount *= 0.05;\n" +
                    "\n" +
                    "        lowp vec3 col;\n" +
                    "        col.r = texture2D( sTexture, vec2(uv.x+(amount*horizontal),uv.y+(amount*vertical)) ).r;\n" +
                    "        col.g = texture2D( sTexture, uv ).g;\n" +
                    "        col.b = texture2D( sTexture, vec2(uv.x-(amount*horizontal),uv.y+(amount*vertical)) ).b;\n" +
                    "        col *= (1.0 - amount * 0.5);\n" +
                    "        \n" +
                    "        gl_FragColor = vec4(col,1.0);\n" +
                    "    \n" +
                    "    }\n" +
                    "    else\n" +
                    "    {\n" +
                    "   \n" +
                    "        // Full radion      6.28319    -- 2.06\n" +
                    "        \n" +
                    "        lowp vec3 col;\n" +
                    "        \n" +
                    "        lowp float Distance = 0.5;  //0.5\n" +
                    "        Distance = (sliderValue2 * 0.5)+0.2 ;\n" +
                    "//        Distance = pow(Distance, 3.0);\n" +
                    "        Distance = pow(Distance, 6.0);\n" +
                    "        \n" +
                    "//        lowp float angleValue = sliderValue2;\n" +
                    "        \n" +
                    "        lowp float angleValue ;\n" +
                    "//        angleValue = 1.5708 ;\n" +
                    "        angleValue = sliderValue ;\n" +
                    "\n" +
                    "        \n" +
                    "        lowp float Angle1  = (angleValue * 6.28319) ;\n" +
                    "        lowp float Angle2  = (angleValue * (6.28319 * 2.06 )) ;\n" +
                    "        lowp float Angle3  = (angleValue * (6.28319 * (2.06*2.0 ) ));\n" +
                    "        \n" +
                    "        \n" +
                    "//        col.r = texture2D( sTexture, vec2(uv.x+(Distance),uv.y+(Distance)) ).r;\n" +
                    "//        col.g = texture2D( sTexture, vec2(uv.x+(Distance),uv.y+(Distance)) ).g;\n" +
                    "//        col.b = texture2D( sTexture, vec2(uv.x-(Distance),uv.y+(Distance)) ).b;\n" +
                    "    \n" +
                    "        \n" +
                    "        col.r = texture2D( sTexture,uv + calDist(Angle1,Distance)).r;\n" +
                    "        col.g = texture2D( sTexture,uv + calDist(Angle2,Distance)).g;\n" +
                    "        col.b = texture2D( sTexture,uv + calDist(Angle3,Distance)).b;\n" +
                    "        \n" +
                    "//        col.r = texture2D( sTexture, uv).r;\n" +
                    "//        col.g = texture2D( sTexture, uv).g;\n" +
                    "//        col.b = texture2D( sTexture, uv).b;\n" +
                    "\n" +
                    "\n" +
                    "//        col *= (1.0 - Distance * 0.5);\n" +
                    "        \n" +
                    "//\n" +
                    "        gl_FragColor = vec4(col,1.0);\n" +
                    "        \n" +
                    "//            gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "\n" +
                    "    \n" +
                    "    }\n" +
                    "\n" +
                    "    \n" +
                    "}\n" +
                    "\n";

    final long START_TIME = System.currentTimeMillis();
    private int Auto = 1;
    private float sliderValue = 0.5f;
    private float sliderValue2 = 0.5f;
    private float sliderValue3 = 0.5f;


    public GPUImageChromaticGlitch() {
        super(DEFAULT_VERTEX_SHADER, CHROMGLITCH_FRAGMENT_SHADER);


    }


    @Override
    public void onDraw() {
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;

        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("Auto"), Auto);
        GLES20.glUniform1f(getHandle("sliderValue"), sliderValue);
        GLES20.glUniform1f(getHandle("sliderValue2"), sliderValue2);
        GLES20.glUniform1f(getHandle("sliderValue3"), sliderValue3);

    }


}
