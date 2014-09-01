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

package io.github.aritzhack.aritzh.lwjgl.math.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Aritz Lopez
 */
public class BufferUtils {

    public static ByteBuffer toByteBuffer(byte[] array) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        buffer.put(array).flip();
        return buffer;
    }

    public static FloatBuffer toFloatBuffer(float[] array) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(array).flip();
        return buffer;
    }

    public static IntBuffer toIntBuffer(int[] array) {
        IntBuffer buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(array).flip();
        return buffer;
    }
}
