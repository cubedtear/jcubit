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

package io.github.aritzhack.aritzh.bds;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.Arrays;

/**
 * @author Aritz Lopez
 */
public class BDSInt extends BDS {

    private int data = 0;

    public BDSInt(int data, String name) {
        this.data = data;
        this.name = name;
    }

    protected BDSInt(byte[] data) {
        this(ByteStreams.newDataInput(Arrays.copyOfRange(data, 1, data.length)));
    }

    protected BDSInt(ByteArrayDataInput input) {
        if (input == null) {
            this.data = 0;
            this.name = "";
            return;
        }
        try {
            this.name = input.readUTF();
            this.data = input.readInt();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse BDSInt", e);
        }
    }

    @Override
    public byte[] getBytes() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeByte(this.getType().toByte());
        output.writeUTF(this.name);
        output.writeInt(this.data);
        return output.toByteArray();
    }

    @Override
    public BDSType getType() {
        return BDSType.BDS_INT;
    }

    @Override
    public Integer getData() {
        return data;
    }
}
