package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;


public class GPUImageOldTv extends GlFilter {
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
                    "\n" +
                    "// Custom Variables (min, max, default)\n" +
                    "uniform float blotch; // ## 0.0, 1.0, 1.0\n" +
                    "uniform bool blackAndWhite; // ## 1\n" +
                    "\n" +
                    "\n" +
                    "#define BLACK_AND_WHITE\n" +
                    "#define LINES_AND_FLICKER\n" +
                    "#define BLOTCHES\n" +
                    "#define GRAIN\n" +
                    "\n" +
                    "#define FREQUENCY 15.0\n" +
                    "\n" +
                    "vec2 uv;\n" +
                    "\n" +
                    "float rand(vec2 co){\n" +
                    "return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);\n" +
                    "}\n" +
                    "\n" +
                    "float rand(float c){\n" +
                    "return rand(vec2(c,1.0));\n" +
                    "}\n" +
                    "\n" +
                    "float randomLine(float seed)\n" +
                    "{\n" +
                    "float b = 0.01 * rand(seed);\n" +
                    "float a = rand(seed+1.0);\n" +
                    "float c = rand(seed+2.0) - 0.5;\n" +
                    "float mu = rand(seed+3.0);\n" +
                    "\n" +
                    "float l = 1.0;\n" +
                    "\n" +
                    "if ( mu > 0.2)\n" +
                    "l = pow(  abs(a * uv.x + b * uv.y + c ), 1.0/8.0 );\n" +
                    "else\n" +
                    "l = 2.0 - pow( abs(a * uv.x + b * uv.y + c), 1.0/8.0 );\n" +
                    "\n" +
                    "return mix(0.5, 1.0, l);\n" +
                    "}\n" +
                    "\n" +
                    "// Generate some blotches.\n" +
                    "float randomBlotch(float seed)\n" +
                    "{\n" +
                    "float x = rand(seed);\n" +
                    "float y = rand(seed+1.0);\n" +
                    "float s = 0.01 * rand(seed+2.0);\n" +
                    "\n" +
                    "vec2 p = vec2(x,y) - uv;\n" +
                    "p.x *= resolution.x / resolution.y;\n" +
                    "float a = atan(p.y,p.x);\n" +
                    "float v = 1.0;\n" +
                    "float ss = s*s * (sin(6.2831*a*x)*0.1 + 1.0);\n" +
                    "\n" +
                    "if ( dot(p,p) < ss ) v = 0.2;\n" +
                    "else\n" +
                    "v = pow(dot(p,p) - ss, 1.0/16.0);\n" +
                    "\n" +
                    "return mix(0.3 + 0.2 * (1.0 - (s / 0.02)), 1.0, v);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "uv = gl_FragCoord.xy / resolution.xy;\n" +
                    "\n" +
                    "if (!enabled) {\n" +
                    "// Set frequency of global effect to 20 variations per second\n" +
                    "float t = float(int(iTime * FREQUENCY));\n" +
                    "\n" +
                    "// Get some image movement\n" +
                    "vec2 suv = uv + 0.002 * vec2( rand(t), rand(t + 23.0));\n" +
                    "\n" +
                    "// Get the image\n" +
                    "vec3 image = texture2D( sTexture, vec2(suv.x, suv.y) ).xyz;\n" +
                    "\n" +
                    "// Pass it to B/W\n" +
                    "vec3 oldImage = image;\n" +
                    "if (blackAndWhite) {\n" +
                    "float luma = dot( vec3(0.2126, 0.7152, 0.0722), image );\n" +
                    "oldImage = luma * vec3(0.7, 0.7, 0.7);\n" +
                    "}\n" +
                    "\n" +
                    "// Create a iTime-varyting vignetting effect\n" +
                    "float vI = 16.0 * (uv.x * (1.0-uv.x) * uv.y * (1.0-uv.y));\n" +
                    "vI *= mix( 0.7, 1.0, rand(t + 0.5));\n" +
                    "\n" +
                    "// Add additive flicker\n" +
                    "vI += 1.0 + 0.4 * rand(t+8.);\n" +
                    "\n" +
                    "// Add a fixed vignetting (independent of the flicker)\n" +
                    "vI *= pow(16.0 * uv.x * (1.0-uv.x) * uv.y * (1.0-uv.y), 0.4);\n" +
                    "\n" +
                    "// Add some random lines (and some multiplicative flicker. Oh well.)\n" +
                    "#ifdef LINES_AND_FLICKER\n" +
                    "//        int l = int(8.0 * rand(t+7.0));\n" +
                    "int l = int((1.0 + blotch*7.0) * rand(t+7.0));\n" +
                    "\n" +
                    "if ( 0 < l ) vI *= randomLine( t+6.0+17.* float(0));\n" +
                    "if ( 1 < l ) vI *= randomLine( t+6.0+17.* float(1));\n" +
                    "if ( 2 < l ) vI *= randomLine( t+6.0+17.* float(2));\n" +
                    "if ( 3 < l ) vI *= randomLine( t+6.0+17.* float(3));\n" +
                    "if ( 4 < l ) vI *= randomLine( t+6.0+17.* float(4));\n" +
                    "if ( 5 < l ) vI *= randomLine( t+6.0+17.* float(5));\n" +
                    "if ( 6 < l ) vI *= randomLine( t+6.0+17.* float(6));\n" +
                    "if ( 7 < l ) vI *= randomLine( t+6.0+17.* float(7));\n" +
                    "\n" +
                    "#endif\n" +
                    "\n" +
                    "// Add some random blotches.\n" +
                    "#ifdef BLOTCHES\n" +
                    "//        int s = int( max(8.0 * rand(t+18.0) -2.0, 0.0 ));\n" +
                    "int s = int( max((1.0 + blotch*7.0) * rand(t+18.0) -2.0, 0.0 ));\n" +
                    "\n" +
                    "if ( 0 < s ) vI *= randomBlotch( t+6.0+19.* float(0));\n" +
                    "if ( 1 < s ) vI *= randomBlotch( t+6.0+19.* float(1));\n" +
                    "if ( 2 < s ) vI *= randomBlotch( t+6.0+19.* float(2));\n" +
                    "if ( 3 < s ) vI *= randomBlotch( t+6.0+19.* float(3));\n" +
                    "if ( 4 < s ) vI *= randomBlotch( t+6.0+19.* float(4));\n" +
                    "if ( 5 < s ) vI *= randomBlotch( t+6.0+19.* float(5));\n" +
                    "\n" +
                    "#endif\n" +
                    "\n" +
                    "// Show the image modulated by the defects\n" +
                    "gl_FragColor.xyz = oldImage * vI;\n" +
                    "\n" +
                    "// Add some grain (thanks, Jose!)\n" +
                    "#ifdef GRAIN\n" +
                    "gl_FragColor.xyz *= (1.0+(rand(uv+t*.01)-.2)*.15*blotch);\n" +
                    "gl_FragColor.a = 1.0;\n" +
                    "#endif\n" +
                    "} else {\n" +
                    "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n" +
                    "\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;
    public GPUImageOldTv() {
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
        GLES20.glUniform1f(getHandle("blotch"), 1.0f);
        GLES20.glUniform1f(getHandle("blackAndWhite"), 1f);
        GLES20.glUniform2f(getHandle("resolution"), widths, heights);
    }


}
