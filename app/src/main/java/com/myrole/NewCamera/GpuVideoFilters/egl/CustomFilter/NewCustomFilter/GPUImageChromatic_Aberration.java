package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageChromatic_Aberration extends GlFilter {
    private static final String CHROMATICABREVATION_FRAGMENT_SHADER =
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
                    "uniform float strength;\n" +
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float speed; // ## 1.0, 2.5, 1.0\n" +
                    "\n" +
                    "\n" +
                    "uniform float zoom; // ## 1.0, 3.0, 1.0\n" +
                    "\n" +
                    "float MAX_SCALE = 3.0;\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "if (!enabled) {\n" +
                    "vec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "float targetScale = 1.0 - ((1.0 / MAX_SCALE) * (zoom - 1.0));\n" +
                    "uv = ((uv-0.5) * targetScale) + 0.5;\n" +
                    "//vec2 scaledUV = (uv-0.5) * targetScale;\n" +
                    "//vec3 scaledImage = texture2D(sTexture, scaledUV+0.5).xyz;\n" +
                    "\n" +
                    "vec4 color;\n" +
                    "\n" +
                    "float go = sin(iTime*speed)*0.01;\n" +
                    "float go2 = sin(iTime*speed)*0.01;\n" +
                    "\n" +
                    "vec2 strenght = vec2(5.0*strength,2.0*strength);\n" +
                    "\n" +
                    "color.r = texture2D(sTexture,uv-vec2(go,0.0)*strenght).r;\n" +
                    "\n" +
                    "color.g = texture2D(sTexture,uv-vec2(0.005,go2)*strenght).g;\n" +
                    "\n" +
                    "color.b=mix(texture2D(sTexture,uv).b,texture2D(sTexture,uv).g, strength);\n" +
                    "//        color.b=texture2D(sTexture,uv).g;\n" +
                    "color.a=1.0;\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "gl_FragColor = color;\n" +
                    "\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageChromatic_Aberration() {
        super(DEFAULT_VERTEX_SHADER, CHROMATICABREVATION_FRAGMENT_SHADER);

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
        GLES20.glUniform1f(getHandle("strength"), 0.3f);
        GLES20.glUniform1f(getHandle("speed"), 2.5f);
        GLES20.glUniform1f(getHandle("zoom"), 1.0f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
