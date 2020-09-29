package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageEvii_Britney extends GlFilter {


    private static final String EVI_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "// Default Variables\n" +
                    "uniform vec2 resolution;\n" +
                    "uniform bool enabled;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float attr1; // ## 0.0, 1.0, 1.0\n" +
                    "uniform float color; // ## 0.0, 1.0, 1.0\n" +
                    "\n" +
                    "#define T texture2D(sTexture,.5+(p.xy*=.98))\n" +
                    "void main()\n" +
                    "{\n" +
                    "    if (!enabled) {\n" +
                    "        vec3 iResolution = vec3(resolution, 1.0);\n" +
                    "        vec3 p=gl_FragCoord.xyz/iResolution-.5;\n" +
                    "        vec3 t=T.rgb;\n" +
                    "        vec3 o = vec3(t.r, mix(t.g,t.b,color), t.b);\n" +
                    "        for (float i=0.;i<50.*attr1;i++) p.z+=pow(max(0.,.5-length(T.rg)),2.)*exp(-i*.1);\n" +
                    "        gl_FragColor=vec4(o*o+p.z,1);\n" +
                    "    } else {\n" +
                    "        gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n" +
                    "\n";


    int heights;
    int widths;

    public GPUImageEvii_Britney() {
        super(DEFAULT_VERTEX_SHADER, EVI_FRAGMENT_SHADER);

    }

    @Override
    public void setFrameSize(int width, int height) {
        super.setFrameSize(width, height);
        widths = width;
        heights = height;
    }

    @Override
    public void onDraw() {
        GLES20.glUniform1f(getHandle("attr1"), 1.0f);
        GLES20.glUniform1f(getHandle("color"), 0.5f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);

    }

}
