/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
 */

package io.github.aritzhack.aritzh.bds;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.Arrays;

/**
 * @author Aritz Lopez
 */
public class BDSString extends BDS {

    private final String data;

    public BDSString(String data, String name) {
        this.data = data;
        this.name = name;
    }

    public BDSString(byte[] data) {
        this(ByteStreams.newDataInput(Arrays.copyOfRange(data, 1, data.length)));
    }

    public BDSString(ByteArrayDataInput input) {
        if (input == null) {
            this.data = "";
            this.name = "";
            return;
        }
        try {
            this.name = input.readUTF();
            this.data = input.readUTF();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse BDSString", e);
        }
    }

    @Override
    public byte[] getBytes() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeByte(this.getType().toByte());
        output.writeUTF(this.name);
        output.writeUTF(this.data);
        return output.toByteArray();
    }

    @Override
    public BDSType getType() {
        return BDSType.BDS_STRING;
    }

    @Override
    public String getData() {
        return data;
    }
}
