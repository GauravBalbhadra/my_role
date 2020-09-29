/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;


import com.myrole.NewCamera.GpuVideoFilters.egl.EglUtil;
import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

public class GPUImageLookupFilter extends GlFilter {

    public static final String LOOKUP_FRAGMENT_SHADER =
            "precision mediump float;" +
                    "varying highp vec2 vTextureCoord;\n" +
                    " varying highp vec2 textureCoordinate2; // TODO: This is not used\n" +
                    " \n" +
                    " uniform sampler2D sTexture;\n" +
                    " uniform sampler2D lutTexture; // lookup texture\n" +
                    " \n" +
                    " uniform lowp float intensity;\n" +
                    " \n" +
                    " void main()\n" +
                    " {\n" +
                    "     highp vec4 textureColor = texture2D(sTexture, vTextureCoord);\n" +
                    "     \n" +
                    "     highp float blueColor = textureColor.b * 63.0;\n" +
                    "     \n" +
                    "     highp vec2 quad1;\n" +
                    "     quad1.y = floor(floor(blueColor) / 8.0);\n" +
                    "     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n" +
                    "     \n" +
                    "     highp vec2 quad2;\n" +
                    "     quad2.y = floor(ceil(blueColor) / 8.0);\n" +
                    "     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n" +
                    "     \n" +
                    "     highp vec2 texPos1;\n" +
                    "     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                    "     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                    "     \n" +
                    "     highp vec2 texPos2;\n" +
                    "     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
                    "     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
                    "     \n" +
                    "     lowp vec4 newColor1 = texture2D(lutTexture, texPos1);\n" +
                    "     lowp vec4 newColor2 = texture2D(lutTexture, texPos2);\n" +
                    "     \n" +
                    "     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
                    "     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n" +
                    " }";


    float intensity = 1.0f;
    private int hTex;
    private Bitmap lutTexture;

    public GPUImageLookupFilter(Bitmap bitmap) {
        super(DEFAULT_VERTEX_SHADER, LOOKUP_FRAGMENT_SHADER);
        this.lutTexture = bitmap;
        hTex = EglUtil.NO_TEXTURE;
    }
    public GPUImageLookupFilter(Resources resources, int fxID) {
        super(DEFAULT_VERTEX_SHADER, LOOKUP_FRAGMENT_SHADER);
        this.lutTexture = BitmapFactory.decodeResource(resources, fxID);
        hTex = EglUtil.NO_TEXTURE;
    }

    @Override
    public void onDraw() {
        int offsetDepthMapTextureUniform = getHandle("lutTexture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, hTex);
        GLES20.glUniform1i(offsetDepthMapTextureUniform, 3);
        GLES20.glUniform1f(getHandle("intensity"), intensity);
    }

    @Override
    public void setup() {
        super.setup();
        loadTexture();
    }

    private void loadTexture() {
        if (hTex == EglUtil.NO_TEXTURE) {
            hTex = EglUtil.loadTexture(lutTexture, EglUtil.NO_TEXTURE, false);
        }
    }

    public void releaseLutBitmap() {
        if (lutTexture != null && !lutTexture.isRecycled()) {
            lutTexture.recycle();
            lutTexture = null;
        }
    }

    public void reset() {
        hTex = EglUtil.NO_TEXTURE;
        hTex = EglUtil.loadTexture(lutTexture, EglUtil.NO_TEXTURE, false);
    }

}
