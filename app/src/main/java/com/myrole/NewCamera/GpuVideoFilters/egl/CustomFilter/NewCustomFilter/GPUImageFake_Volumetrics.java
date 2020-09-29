package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageFake_Volumetrics extends GlFilter {
    private static final String FAKEVOLUME_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "const lowp float THRESHOLD = 0.92 ; //0.92\n" +
                    "//const lowp float INTENSITY = 10.5 ;  //.5\n" +
                    "\n" +
                    "uniform lowp float iTime ;\n" +
                    "\n" +
                    "uniform lowp vec2 iMouse ;\n" +
                    "uniform lowp float sliderValue ;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "lowp vec3 sample(lowp vec2 uv) {\n" +
                    "lowp vec4 col = texture2D(sTexture, uv);\n" +
                    "\n" +
                    "if((col.x+col.y+col.z)/3. < THRESHOLD) {\n" +
                    "col.xyz = vec3(0.);\n" +
                    "}\n" +
                    "//    col = clamp(col-THRESHOLD,0.,1.);\n" +
                    "return col.xyz;\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "lowp vec2 U = vTextureCoord;\n" +
                    "lowp vec2 iResolution = vec2(1.0,1.0);\n" +
                    "// lowp vec2 iMouse = vec2(0.5,0.5);\n" +
                    "\n" +
                    "lowp float INTENSITY = 10.0 *sliderValue ;  //.5\n" +
                    "lowp int SAMPLES = 256 ;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "lowp vec2 uv = vTextureCoord.xy / iResolution.xy;\n" +
                    "//    gl_FragColor = texture2D(sTexture, uv);\n" +
                    "lowp vec4  finalR = texture2D(sTexture, uv);\n" +
                    "\n" +
                    "\n" +
                    "lowp vec2 center = vec2(.25+.25*cos(iTime*1.5),.75+.15*sin(iTime));\n" +
                    "\n" +
                    "/* if(iMouse.z > 0.)\n" +
                    "{*/\n" +
                    "\n" +
                    "center = iMouse.xy/iResolution.xy;\n" +
                    "\n" +
                    "/*}\n" +
                    "*/\n" +
                    "\n" +
                    "lowp  vec3 sum      = vec3(0.);\n" +
                    "lowp float weight = 1. / float(SAMPLES);\n" +
                    "\n" +
                    "\n" +
                    "for(int i = 0; i < SAMPLES; i++) {\n" +
                    "sum += sample(uv);\n" +
                    "\n" +
                    "lowp  vec2 dir = center-uv;\n" +
                    "uv += dir * .01;\n" +
                    "}\n" +
                    "\n" +
                    "finalR.xyz += sum * weight * INTENSITY;\n" +
                    "finalR.w    = 1.;\n" +
                    "\n" +
                    "gl_FragColor = finalR ;\n" +
                    "\n" +
                    "\n" +
                    "//  gl_FragColor = texture2D(sTexture, vTextureCoord );\n" +
                    "\n" +
                    "//        lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n" +
                    "//        gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n" +
                    "\n" +
                    "//-*-* OLD\n" +
                    "/*\n" +
                    "lowp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);\n" +
                    "lowp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;\n" +
                    "gl_FragColor = texture2D(sTexture, samplePos );\n" +
                    "\n" +
                    "*/\n" +
                    "//-*-*\n" +
                    "\n" +
                    "///-*-* new\n" +
                    "\n" +
                    "/*\n" +
                    "highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio );\n" +
                    "highp vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;\n" +
                    "\n" +
                    "\n" +
                    "//    gl_FragColor = texture2D(sTexture, samplePos );\n" +
                    "\n" +
                    "// The mod function returns x minus the product of y and floor(x/y)\n" +
                    "//  ** Floor  ==> 3.01 becomes 3.0\n" +
                    "\n" +
                    "\n" +
                    "highp vec2 textureCoordinateToUse = vec2(vTextureCoord.x, (vTextureCoord.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n" +
                    "\n" +
                    "highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n" +
                    "\n" +
                    "\n" +
                    "highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);  // Calculate Distance between two Points\n" +
                    "\n" +
                    "lowp vec4 inputColor = texture2D(sTexture, samplePos);\n" +
                    "\n" +
                    "\n" +
                    "lowp float big = (fractionalWidthOfPixel * 0.5) * dotScaling;\n" +
                    "lowp float Small = (fractionalWidthOfPixel * 0.5) * (dotScaling-0.1);\n" +
                    "//    lowp float border = (fractionalWidthOfPixel * 0.5) * (dotScaling + 0.2);\n" +
                    "lowp float border = (fractionalWidthOfPixel * 0.9);\n" +
                    "\n" +
                    "\n" +
                    "//stud\n" +
                    "if(distanceFromSamplePoint<big && distanceFromSamplePoint>Small)\n" +
                    "{\n" +
                    "inputColor *= dot(vec2(0.707),normalize(textureCoordinateToUse-adjustedSamplePos))*.5+1.;\n" +
                    "}\n" +
                    "\n" +
                    "//side shadow\n" +
                    "//    lowp vec2 delta = abs(textureCoordinateToUse-adjustedSamplePos)*c*2.0;\n" +
                    "\n" +
                    "lowp vec2 delta = abs(textureCoordinateToUse-adjustedSamplePos)*2.0;\n" +
                    "lowp float sdis = max(delta.x,delta.y);\n" +
                    "if(sdis > border)\n" +
                    "{\n" +
                    "inputColor *= 0.6;\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = inputColor;\n" +
                    "\n" +
                    "*/\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageFake_Volumetrics() {
        super(DEFAULT_VERTEX_SHADER, FAKEVOLUME_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("sliderValue"), 0.5f);


        // GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
