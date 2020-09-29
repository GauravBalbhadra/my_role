package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageWavingGrid extends GlFilter {

    private static final String WAVINGGRID_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "// glslsandbox uniforms\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "// shadertoy globals\n" +
                    "const vec4  iMouse = vec4(0.0);\n" +
                    "\n" +
                    "// --------[ Original ShaderToy begins here ]---------- //\n" +
                    "#define EPS 0.0001\n" +
                    "#define PI 3.14159265359\n" +
                    "#define FLT_MAX 3.402823466e+38\n" +
                    "#define FLT_MIN 1.175494351e-38\n" +
                    "#define DBL_MAX 1.7976931348623158e+308\n" +
                    "#define DBL_MIN 2.2250738585072014e-308\n" +
                    "\n" +
                    "const int maxIterations = 16;\n" +
                    "const float stepScale = .9;\n" +
                    "const float stopThreshold = .005;\n" +
                    "\n" +
                    "float fov = .65;\n" +
                    "float nearClip = 0.;\n" +
                    "float farClip = 80.;\n" +
                    "\n" +
                    "struct Surface {\n" +
                    "float dist;\n" +
                    "vec3 position;\n" +
                    "vec3 baseColor;\n" +
                    "vec3 normal;\n" +
                    "vec3 emissiveColor;\n" +
                    "};\n" +
                    "\n" +
                    "struct Hit {\n" +
                    "Surface surface;\n" +
                    "Surface near;\n" +
                    "vec3 color;\n" +
                    "};\n" +
                    "\n" +
                    "float saturate(float s) {\n" +
                    "return clamp(s, 0., 1.);\n" +
                    "}\n" +
                    "\n" +
                    "float smin(float a, float b, float k) {\n" +
                    "float res = exp(-k * a) + exp(-k * b);\n" +
                    "return -log(res) / k;\n" +
                    "}\n" +
                    "\n" +
                    "mat2 rot2(float t) {\n" +
                    "return mat2(cos(t), -sin(t), sin(t), cos(t));\n" +
                    "}\n" +
                    "\n" +
                    "float scene(vec3 p) {\n" +
                    "vec3 p1 = p;\n" +
                    "p1.xy += vec2(iTime * .8 + 10., iTime * .4 + 20.);\n" +
                    "p1.xy *= rot2(PI * .05);\n" +
                    "\n" +
                    "vec3 p2 = p;\n" +
                    "p2.yz += vec2(iTime * .4 + 30., iTime * .8 + 40.);\n" +
                    "p2.yz *= rot2(PI * .04);\n" +
                    "\n" +
                    "vec3 p3 = p;\n" +
                    "p3.xz += vec2(iTime * .8 + 50., iTime * .6 + 60.);\n" +
                    "p3.xz *= rot2(PI / 2. + iTime * .0);\n" +
                    "\n" +
                    "float m = 6.;\n" +
                    "\n" +
                    "p1.y += sin(sin(p1.z * 1.2 + iTime * 4.) * .3) * .3;\n" +
                    "p1.x += sin(sin(p1.z * 1. + iTime * 2.) * .4) * .2;\n" +
                    "p1.y = mod(p1.y, m) - m * .5;\n" +
                    "p1.x = mod(p1.x, m) - m * .5;\n" +
                    "\n" +
                    "\n" +
                    "p2.y += sin(sin(p2.z * 1.2 + iTime * 4.) * .4) * .4;\n" +
                    "p2.x += sin(sin(p2.z * .5 + iTime * 3.) * .5) * .3;\n" +
                    "p2.y = mod(p2.y, m) - m * .5;\n" +
                    "p2.x = mod(p2.x, m) - m * .5;\n" +
                    "\n" +
                    "p3.y += sin(sin(p3.z * .8 + iTime * 2.) * .4) * .2;\n" +
                    "p3.x += sin(sin(p3.z * 1.1 + iTime * 3.) * .5) * .4;\n" +
                    "p3.y = mod(p3.y, m) - m * .5;\n" +
                    "p3.x = mod(p3.x, m) - m * .5;\n" +
                    "\n" +
                    "float c = smin(length(p1.xy), length(p2.xy), 4.);\n" +
                    "c = smin(c, length(p3.xy), 4.);\n" +
                    "\n" +
                    "return c;\n" +
                    "}\n" +
                    "\n" +
                    "Hit rayMarching(vec3 origin, vec3 dir, float start, float end) {\n" +
                    "Surface cs;\n" +
                    "cs.dist = -1.;\n" +
                    "\n" +
                    "Hit hit;\n" +
                    "hit.color = vec3(0.);\n" +
                    "\n" +
                    "float sceneDist = 0.;\n" +
                    "float rayDepth = start;\n" +
                    "\n" +
                    "for(int i = 0; i < maxIterations; i++) {\n" +
                    "sceneDist = scene(origin + dir * rayDepth);\n" +
                    "\n" +
                    "if((sceneDist < stopThreshold) || (rayDepth >= end)) {\n" +
                    "break;\n" +
                    "}\n" +
                    "rayDepth += sceneDist * stepScale;\n" +
                    "vec3 p = origin + dir * rayDepth;\n" +
                    "vec3 c = sin((iTime + PI / 2.) * 4. * vec3(.123, .456, .789)) * .4 + .6;\n" +
                    "hit.color += max(vec3(0.), .09 / sceneDist * c);\n" +
                    "}\n" +
                    "\n" +
                    "/*\n" +
                    "if (sceneDist >= stopThreshold) {\n" +
                    "rayDepth = end;\n" +
                    "} else {\n" +
                    "rayDepth += sceneDist;\n" +
                    "}\n" +
                    "*/\n" +
                    "\n" +
                    "cs.dist = rayDepth;\n" +
                    "hit.surface = cs;\n" +
                    "\n" +
                    "return hit;\n" +
                    "}\n" +
                    "\n" +
                    "vec3 fog(vec3 color, float distance, vec3 fogColor, float b) {\n" +
                    "float fogAmount = 1. - exp(-distance * b);\n" +
                    "return mix(color, fogColor, fogAmount);\n" +
                    "}\n" +
                    "\n" +
                    "void mainImage(out vec4 fragColor, in vec2 fragCoord) {\n" +
                    "vec2 mouse = iMouse.xy;\n" +
                    "\n" +
                    "vec2 aspect = vec2(resolution.x / resolution.y, 1.);\n" +
                    "vec2 screenCoord = (2. * fragCoord.xy / resolution.xy - 1.) * aspect;\n" +
                    "\n" +
                    "// displacement\n" +
                    "vec2 uv = screenCoord;\n" +
                    "uv.xy *= rot2(iTime * .07);\n" +
                    "uv.y += sin(screenCoord.x * 2.4 + iTime * .05) * .16;\n" +
                    "uv.x += sin(uv.y * 2.4 + iTime * .1) * .12;\n" +
                    "\n" +
                    "// mouse = mouse.xy / resolution.xy - .5;\n" +
                    "\n" +
                    "// camera settings\n" +
                    "//vec3 lookAt = vec3(cos(iTime * .4) * .5, sin(iTime * .3) * .5, 0.);\n" +
                    "float z = iTime * -5.;\n" +
                    "vec3 lookAt = vec3(0., 0., z - 1.);\n" +
                    "vec3 cameraPos = vec3(0., 0., z);\n" +
                    "\n" +
                    "// camera vectors\n" +
                    "vec3 forward = normalize(lookAt - cameraPos);\n" +
                    "vec3 right = normalize(cross(forward, vec3(0., 1., 0.)));\n" +
                    "vec3 up = normalize(cross(right, forward));\n" +
                    "\n" +
                    "// raymarch\n" +
                    "vec3 rayOrigin = cameraPos;\n" +
                    "vec3 rayDirection = normalize(forward + fov * uv.x * right + fov * uv.y * up);\n" +
                    "Hit hit = rayMarching(rayOrigin, rayDirection, nearClip, farClip);\n" +
                    "Surface surface = hit.surface;\n" +
                    "\n" +
                    "surface.position = rayOrigin + rayDirection * surface.dist;\n" +
                    "\n" +
                    "// color\n" +
                    "vec3 sceneColor = vec3(0.);\n" +
                    "\n" +
                    "sceneColor = hit.color;\n" +
                    "\n" +
                    "sceneColor = fog(sceneColor, surface.dist, vec3(0.), .065);\n" +
                    "\n" +
                    "// vignet by channel\n" +
                    "float vignetR = 1. - smoothstep(0., 2.5 + sin(iTime * 1.) * 1.5, length(screenCoord)) * .8;\n" +
                    "float vignetG = 1. - smoothstep(0., 2.5 + cos(iTime * 1.2) * 1.5, length(screenCoord)) * .8;\n" +
                    "float vignetB = 1. - smoothstep(0., 2.5 + sin(iTime * 1.4) * 1.5, length(screenCoord)) * .8;\n" +
                    "\n" +
                    "sceneColor.x *= vignetR;\n" +
                    "sceneColor.y *= vignetG;\n" +
                    "sceneColor.z *= vignetB;\n" +
                    "\n" +
                    "// debug distance color\n" +
                    "//sceneColor.rgb = vec3(surface.dist / farClip);\n" +
                    "\n" +
                    "fragColor = vec4(sceneColor, 1.);\n" +
                    "}\n" +
                    "\n" +
                    "// --------[ Original ShaderToy ends here ]---------- //\n" +
                    "\n" +
                    "void main(void)\n" +
                    "{\n" +
                    "mainImage(gl_FragColor, gl_FragCoord.xy);\n" +
                    "\n" +
                    "gl_FragColor += texture2D(sTexture, vTextureCoord);\n" +
                    "}\n";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageWavingGrid() {
        super(DEFAULT_VERTEX_SHADER, WAVINGGRID_FRAGMENT_SHADER);

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
