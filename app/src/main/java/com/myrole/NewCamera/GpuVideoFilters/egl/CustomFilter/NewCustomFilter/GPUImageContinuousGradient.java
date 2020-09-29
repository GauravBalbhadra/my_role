package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageContinuousGradient extends GlFilter {
    private static final String CONTINOUSGRI_FRAGMENT_SHADER =
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
                    "uniform float progress;\n" +
                    "uniform float strength;\n" +
                    "\n" +
                    "\n" +
                    "#define DX (1.0 - strength)\n" +
                    "#define DY (1.0 - strength)\n" +
                    "//#define DX (iMouse.x/resolution.x)\n" +
                    "//#define DY (iMouse.y/resolution.y)\n" +
                    "#define BORDERRADIUS (6)\n" +
                    "#define GAMMA       (2.2)\n" +
                    "#define PI           (3.14159265359)\n" +
                    "#define LUMWEIGHT    (vec3(0.2126,0.7152,0.0722))\n" +
                    "#define pow3(x,y)      (pow( max(x,0.) , vec3(y) ))\n" +
                    "\n" +
                    "#define BORDERRADIUSf float(BORDERRADIUS)\n" +
                    "#define BORDERRADIUS22f float(BORDERRADIUS*BORDERRADIUS)\n" +
                    "\n" +
                    "// https://www.shadertoy.com/view/MsS3Wc\n" +
                    "// HSV to RGB conversion\n" +
                    "vec3 hsv2rgb_smooth( in vec3 c )\n" +
                    "{\n" +
                    "    vec3 rgb = clamp( abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),6.0)-3.0)-1.0, 0.0, 1.0 );\n" +
                    "    return c.z * mix( vec3(1.0), rgb, c.y);\n" +
                    "}\n" +
                    "\n" +
                    "vec2 viewport(vec2 p)\n" +
                    "{\n" +
                    "    return p/(resolution.xy);\n" +
                    "}\n" +
                    "\n" +
                    "vec3 sampleImage(vec2 coord){\n" +
                    "    return pow3(texture2D(sTexture,viewport(coord)).rgb,GAMMA);\n" +
                    "}\n" +
                    "\n" +
                    "float kernel(int a,int b){\n" +
                    "    return float(a)*exp(-float(a*a + b*b)/BORDERRADIUS22f)/BORDERRADIUSf;\n" +
                    "}\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        gl_FragColor.rgb = sampleImage(gl_FragCoord.xy);\n" +
                    "        \n" +
                    "        vec3 col;\n" +
                    "        vec3 colX = vec3(0.);\n" +
                    "        vec3 colY = vec3(0.);\n" +
                    "        float coeffX,coeffY;\n" +
                    "        \n" +
                    "        for( int i = -BORDERRADIUS ; i <= BORDERRADIUS ; i++ ){\n" +
                    "            for( int j = -BORDERRADIUS ; j <= BORDERRADIUS ; j++ ){\n" +
                    "                coeffX = kernel(i,j);\n" +
                    "                coeffY = kernel(j,i);\n" +
                    "                \n" +
                    "                col = sampleImage(gl_FragCoord.xy+vec2(i,j));\n" +
                    "                colX += coeffX*col;\n" +
                    "                colY += coeffY*col;\n" +
                    "            }\n" +
                    "            \n" +
                    "        }\n" +
                    "        \n" +
                    "        vec3 derivative = sqrt( (colX*colX + colY*colY) )/(BORDERRADIUSf*BORDERRADIUSf);\n" +
                    "        float angle = atan(dot(colY,LUMWEIGHT),dot(colX,LUMWEIGHT))/(2.*PI) + iTime*(1. - DX)/2.;\n" +
                    "        vec3 derivativeWithAngle = hsv2rgb_smooth(vec3(angle,1.,pow(dot(derivative,LUMWEIGHT)*3.,3.)*5.));\n" +
                    "        \n" +
                    "        gl_FragColor.rgb = mix(derivative,gl_FragColor.rgb,DX);\n" +
                    "        gl_FragColor.rgb = mix(derivativeWithAngle,gl_FragColor.rgb,DY);\n" +
                    "        gl_FragColor.rgb = pow3(gl_FragColor.rgb,1./GAMMA);\n" +
                    "        gl_FragColor.a = 1.0;\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "}\n";


    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    // private boolean enabled = true;
    private float progress = 1.0f;
    private float strength = 1.0f;

    public GPUImageContinuousGradient() {
        super(DEFAULT_VERTEX_SHADER, CONTINOUSGRI_FRAGMENT_SHADER);

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
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
        GLES20.glUniform1f(getHandle("iTime"), time);
        GLES20.glUniform1f(getHandle("progress"), progress);
        GLES20.glUniform1f(getHandle("strength"), strength);
    }


}
