package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageElectronicGlow extends GlFilter {

    private static final String ELECTRONICGLOW_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform float iTime;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "#define SHADER_TIME (iTime*0.15)\n" +
                    "#define tau 6.2831853\n" +
                    "mat2 makem2(in float theta){float c = cos(theta);float s = sin(theta);return mat2(c,-s,s,c);}\n" +
                    "float noise( in vec2 x ){return texture2D(sTexture, x*.1125).x;}\n" +
                    "\n" +
                    "float fbm(in vec2 p)\n" +
                    "{\n" +
                    "    float z=1.;\n" +
                    "    float rz = 0.;\n" +
                    "    vec2 bp = p;\n" +
                    "    for (float i= 1.;i < 6.;i++)\n" +
                    "    {\n" +
                    "        rz+= abs((noise(p)-0.5)*2.)/z;\n" +
                    "        z = z*2.;\n" +
                    "        p = p*2.;\n" +
                    "    }\n" +
                    "    return rz;\n" +
                    "}\n" +
                    "\n" +
                    "float dualfbm(in vec2 p)\n" +
                    "{\n" +
                    "    //get two rotated fbm calls and displace the domain\n" +
                    "    vec2 p2 = p*.7;\n" +
                    "    vec2 basis = vec2(fbm(p2-iTime*1.6),fbm(p2+iTime*1.7));\n" +
                    "    basis = (basis-.5)*.2;\n" +
                    "    p += basis;\n" +
                    "    \n" +
                    "    //coloring\n" +
                    "    return fbm(p*makem2(iTime*0.2));\n" +
                    "}\n" +
                    "\n" +
                    "float circ(vec2 p)\n" +
                    "{\n" +
                    "    float r = length(p);\n" +
                    "    r = log(sqrt(r));\n" +
                    "    return abs(mod(r*4.,tau)-3.14)*3.+.2;\n" +
                    "    \n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        \n" +
                    "        \n" +
                    "        //setup system\n" +
                    "        vec2 p = gl_FragCoord.xy / resolution.xy-0.5;\n" +
                    "        p.x *= resolution.x/resolution.y;\n" +
                    "        p*=4.;\n" +
                    "        \n" +
                    "        float rz = dualfbm(p);\n" +
                    "        \n" +
                    "        //rings\n" +
                    "        //p /= exp(mod(iTime*10.,3.14159));\n" +
                    "        //rz *= pow(abs((0.1-circ(p))),.9);\n" +
                    "        \n" +
                    "        //final color\n" +
                    "        vec3 col = vec3(1.,1.,1.)/rz;\n" +
                    "        col=pow(abs(col),vec3(.99));\n" +
                    "        vec4 ocol = texture2D(sTexture, gl_FragCoord.xy / resolution.xy);\n" +
                    "        gl_FragColor = vec4(col,1.) * ocol;\n" +
                    "        \n" +
                    "        \n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "    \n" +
                    "    \n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageElectronicGlow() {
        super(DEFAULT_VERTEX_SHADER, ELECTRONICGLOW_FRAGMENT_SHADER);

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


    }
}
