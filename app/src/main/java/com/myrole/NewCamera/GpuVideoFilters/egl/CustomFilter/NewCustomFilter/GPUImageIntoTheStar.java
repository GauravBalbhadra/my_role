package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.opengl.GLES20;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageIntoTheStar extends GlFilter {

    private static final String INTOSTAR_FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "precision highp float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform sampler2D sTexture;\n" +
                    "\n" +
                    "uniform float iTime;\n" +
                    "// uniform vec2 mouse;\n" +
                    "#define mouse vec2(iTime* 0.013, iTime * 0.006)\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "\n" +
                    "#define iterations 12\n" +
                    "#define formuparam2 0.79\n" +
                    "\n" +
                    "#define volsteps 7\n" +
                    "#define stepsize 0.290\n" +
                    "\n" +
                    "#define zoom 1.0\n" +
                    "#define tile   0.850\n" +
                    "#define speed2  0.2\n" +
                    "\n" +
                    "#define brightness 0.0015\n" +
                    "#define darkmatter 0.100\n" +
                    "#define distfading 0.560\n" +
                    "#define saturation 0.90\n" +
                    "\n" +
                    "\n" +
                    "#define transverseSpeed zoom\n" +
                    "#define cloud 0.17\n" +
                    "\n" +
                    "\n" +
                    "float triangle(float x, float a) {\n" +
                    "float output2 = 2.0*abs(  3.0*  ( (x/a) - floor( (x/a) + 0.5) ) ) - 1.0;\n" +
                    "return output2;\n" +
                    "}\n" +
                    "\n" +
                    "float field(in vec3 p) {\n" +
                    "float strength = 7. + .03 * log(1.e-6 + fract(sin(iTime) * 373.11));\n" +
                    "float accum = 0.;\n" +
                    "float prev = 0.;\n" +
                    "float tw = 0.;\n" +
                    "\n" +
                    "for (int i = 0; i < 6; ++i) {\n" +
                    "float mag = dot(p, p);\n" +
                    "p = abs(p) / mag + vec3(-.5, -.8 + 0.1*sin(-iTime*0.1 + 2.0), -1.1+0.3*cos(iTime*0.3));\n" +
                    "float w = exp(-float(i) / 7.);\n" +
                    "accum += w * exp(-strength * pow(abs(mag - prev), 2.3));\n" +
                    "tw += w;\n" +
                    "prev = mag;\n" +
                    "}\n" +
                    "return max(0., 5. * accum / tw - .7);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "void main() {\n" +
                    "vec2 uv2 = 2. * gl_FragCoord.xy / vec2(512) - 1.;\n" +
                    "vec2 uvs = uv2 * vec2(512)  / 512.;\n" +
                    "\n" +
                    "float time2 = iTime;\n" +
                    "float speed = -speed2;\n" +
                    "speed = .005 * cos(time2*0.02 + 3.1415926/4.0);\n" +
                    "//speed = 0.0;\n" +
                    "float formuparam = formuparam2;\n" +
                    "\n" +
                    "//get coords and direction\n" +
                    "vec2 uv = uvs;\n" +
                    "//mouse rotation\n" +
                    "float a_xz = 0.9;\n" +
                    "float a_yz = -.6;\n" +
                    "float a_xy = 0.9 + iTime*0.08;\n" +
                    "\n" +
                    "mat2 rot_xz = mat2(cos(a_xz),sin(a_xz),-sin(a_xz),cos(a_xz));\n" +
                    "mat2 rot_yz = mat2(cos(a_yz),sin(a_yz),-sin(a_yz),cos(a_yz));\n" +
                    "mat2 rot_xy = mat2(cos(a_xy),sin(a_xy),-sin(a_xy),cos(a_xy));\n" +
                    "\n" +
                    "\n" +
                    "float v2 =1.0;\n" +
                    "vec3 dir=vec3(uv*zoom,1.);\n" +
                    "vec3 from=vec3(0.0, 0.0,0.0);\n" +
                    "from.x -= 2.0*(mouse.x-0.5);\n" +
                    "from.y -= 2.0*(mouse.y-0.5);\n" +
                    "\n" +
                    "\n" +
                    "vec3 forward = vec3(0.,0.,1.);\n" +
                    "from.x += transverseSpeed*(1.0)*cos(0.01*iTime) + 0.001*iTime;\n" +
                    "from.y += transverseSpeed*(1.0)*sin(0.01*iTime) +0.001*iTime;\n" +
                    "from.z += 0.003*iTime;\n" +
                    "\n" +
                    "dir.xy*=rot_xy;\n" +
                    "forward.xy *= rot_xy;\n" +
                    "dir.xz*=rot_xz;\n" +
                    "forward.xz *= rot_xz;\n" +
                    "dir.yz*= rot_yz;\n" +
                    "forward.yz *= rot_yz;\n" +
                    "\n" +
                    "from.xy*=-rot_xy;\n" +
                    "from.xz*=rot_xz;\n" +
                    "from.yz*= rot_yz;\n" +
                    "\n" +
                    "\n" +
                    "//zoom\n" +
                    "float zooom = (time2-3311.)*speed;\n" +
                    "from += forward* zooom;\n" +
                    "float sampleShift = mod( zooom, stepsize );\n" +
                    "\n" +
                    "float zoffset = -sampleShift;\n" +
                    "sampleShift /= stepsize; // make from 0 to 1\n" +
                    "\n" +
                    "//volumetric rendering\n" +
                    "float s=0.24;\n" +
                    "float s3 = s + stepsize/2.0;\n" +
                    "vec3 v=vec3(0.);\n" +
                    "float t3 = 0.0;\n" +
                    "\n" +
                    "vec3 backCol2 = vec3(0.);\n" +
                    "for (int r=0; r<volsteps; r++) {\n" +
                    "vec3 p2=from+(s+zoffset)*dir;// + vec3(0.,0.,zoffset);\n" +
                    "vec3 p3=from+(s3+zoffset)*dir;// + vec3(0.,0.,zoffset);\n" +
                    "\n" +
                    "p2 = abs(vec3(tile)-mod(p2,vec3(tile*2.))); // tiling fold\n" +
                    "p3 = abs(vec3(tile)-mod(p3,vec3(tile*2.))); // tiling fold\n" +
                    "#ifdef cloud\n" +
                    "t3 = field(p3);\n" +
                    "#endif\n" +
                    "\n" +
                    "float pa,a=pa=0.;\n" +
                    "for (int i=0; i<iterations; i++) {\n" +
                    "p2=abs(p2)/dot(p2,p2)-formuparam; // the magic formula\n" +
                    "//p=abs(p)/max(dot(p,p),0.005)-formuparam; // another interesting way to reduce noise\n" +
                    "float D = abs(length(p2)-pa); // absolute sum of average change\n" +
                    "a += i > 7 ? min( 12., D) : D;\n" +
                    "pa=length(p2);\n" +
                    "}\n" +
                    "\n" +
                    "\n" +
                    "//float dm=max(0.,darkmatter-a*a*.001); //dark matter\n" +
                    "a*=a*a; // add contrast\n" +
                    "//if (r>3) fade*=1.-dm; // dark matter, don't render near\n" +
                    "// brightens stuff up a bit\n" +
                    "float s1 = s+zoffset;\n" +
                    "// need closed form expression for this, now that we shift samples\n" +
                    "float fade = pow(distfading,max(0.,float(r)-sampleShift));\n" +
                    "//t3 += fade;\n" +
                    "v+=fade;\n" +
                    "//backCol2 -= fade;\n" +
                    "\n" +
                    "// fade out samples as they approach the camera\n" +
                    "if( r == 0 )\n" +
                    "fade *= (1. - (sampleShift));\n" +
                    "// fade in samples as they approach from the distance\n" +
                    "if( r == volsteps-1 )\n" +
                    "fade *= sampleShift;\n" +
                    "v+=vec3(s1,s1*s1,s1*s1*s1*s1)*a*brightness*fade; // coloring based on distance\n" +
                    "\n" +
                    "backCol2 += mix(.4, 1., v2) * vec3(1.8 * t3 * t3 * t3, 1.4 * t3 * t3, t3) * fade;\n" +
                    "\n" +
                    "\n" +
                    "s+=stepsize;\n" +
                    "s3 += stepsize;\n" +
                    "}//фор\n" +
                    "\n" +
                    "v=mix(vec3(length(v)),v,saturation); //color adjust\n" +
                    "\n" +
                    "vec4 forCol2 = vec4(v*.01,1.);\n" +
                    "#ifdef cloud\n" +
                    "backCol2 *= cloud;\n" +
                    "#endif\n" +
                    "backCol2.b *= 1.8;\n" +
                    "backCol2.r *= 0.05;\n" +
                    "\n" +
                    "backCol2.b = 0.5*mix(backCol2.g, backCol2.b, 0.8);\n" +
                    "backCol2.g = 0.0;\n" +
                    "backCol2.bg = mix(backCol2.gb, backCol2.bg, 0.5*(cos(iTime*0.01) + 1.0));\n" +
                    "//vec4 fragCol = forCol2 + vec4(backCol2, 1.0);\n" +
                    "gl_FragColor = forCol2 + texture2D(sTexture, vTextureCoord);\n" +
                    "}";
    final long START_TIME = System.currentTimeMillis();
    int heights;
    int widths;

    public GPUImageIntoTheStar() {
        super(DEFAULT_VERTEX_SHADER, INTOSTAR_FRAGMENT_SHADER);

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
