/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.aritzhack.aritzh.lwjgl.util;

import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.NullLogger;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Aritz Lopez
 */
public class ShaderUtils {

    /**
     *
     */
    public static ILogger LOG = new NullLogger();

    private ShaderUtils() {
    }

    public static int load(String vertPath, String fragPath) {
        LOG.t("Loading shaders from:");
        LOG.t("\tVertex: {}", vertPath);
        LOG.t("\tFragment: {}", fragPath);

        String vert = FileUtils.loadAsString(vertPath);
        String frag = FileUtils.loadAsString(fragPath);
        return create(vert, frag);
    }

    public static int create(String vert, String frag) {
        LOG.t("Creating shader program");
        int program = glCreateProgram();
        int vertId = glCreateShader(GL_VERTEX_SHADER);
        int fragId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertId, vert);
        glShaderSource(fragId, frag);

        glCompileShader(vertId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            LOG.e("Failed to compile vertex shader!");
            LOG.e(glGetShaderInfoLog(vertId, 2048));
        }

        glCompileShader(fragId);
        if (glGetShaderi(fragId, GL_COMPILE_STATUS) == GL_FALSE) {
            LOG.e("Failed to compile fragment shader!");
            LOG.e(glGetShaderInfoLog(fragId, 2048));
        }

        glAttachShader(program, vertId);
        glAttachShader(program, fragId);

        glLinkProgram(program);
        glValidateProgram(program);

        return program;
    }
}
