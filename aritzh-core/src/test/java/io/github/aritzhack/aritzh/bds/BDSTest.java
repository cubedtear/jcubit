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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Aritz Lopez
 */
public class BDSTest {
    @Test
    public void intTest() throws Exception {
        for (int i = Integer.MIN_VALUE / 2; i <= Integer.MAX_VALUE / 2; i += 10000) {
            byte[] data = new BDSInt(i, Integer.toString(i)).getBytes();
            int out = new BDSInt(data).getData();
            assertEquals(i, out);
        }
    }

    @Test
    public void shortTest() throws Exception {
        for (short s = Short.MIN_VALUE; s < Short.MAX_VALUE; s++) {
            byte[] data = new BDSShort(s, Short.toString(s)).getBytes();
            short out = new BDSShort(data).getData();
            assertEquals(s, out);
        }
    }

    @Test
    public void byteTest() throws Exception {
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            byte[] data = new BDSByte(b, Byte.toString(b)).getBytes();
            byte out = new BDSByte(data).getData();
            assertEquals(b, out);
        }
    }

    @Test
    public void stringTest() throws Exception {
        for (int i = Integer.MIN_VALUE / 2; i <= Integer.MAX_VALUE / 2; i += 10000) {
            String nameBefore = Integer.toString(i) + "N";
            String dataBefore = Integer.toString(i);
            BDSString string = new BDSString(new BDSString(dataBefore, nameBefore).getBytes());
            String nameAfter = string.getName();
            String dataAfter = string.getData();

            assertEquals(nameBefore, nameAfter);
            assertEquals(dataBefore, dataAfter);
        }
    }

    @Test
    public void compoundTest() throws Exception {

        String beforeString = "text";
        byte beforeByte = 5;
        short beforeShort = 45;
        int beforeInt = 45687;


        BDSString beforeStringBDS = new BDSString(beforeString, "string");
        BDSByte beforeByteBDS = new BDSByte(beforeByte, "byte");
        BDSInt beforeIntBDS = new BDSInt(beforeInt, "int");
        BDSShort beforeShortBDS = new BDSShort(beforeShort, "short");
        BDSCompound beforeNestedBDS = new BDSCompound("nestedComp").add(beforeStringBDS)
            .add(beforeByteBDS)
            .add(beforeShortBDS)
            .add(beforeIntBDS);

        BDSCompound beforeComp = new BDSCompound("compound");
        beforeComp.add(beforeStringBDS)
            .add(beforeNestedBDS)
            .add(beforeByteBDS)
            .add(beforeShortBDS)
            .add(beforeIntBDS);


        BDSCompound afterComp = new BDSCompound(beforeComp.getBytes());

        BDSString afterStringBDS = afterComp.getString("string");
        BDSByte afterByteBDS = afterComp.getByte("byte");
        BDSShort afterShortBDS = afterComp.getShort("short");
        BDSInt afterIntBDS = afterComp.getInt("int");


        BDSCompound nestedComp = afterComp.getComp("nestedComp");

        BDSString afterNestedStringBDS = nestedComp.getString("string");
        BDSByte afterNestedByteBDS = nestedComp.getByte("byte");
        BDSShort afterNestedShortBDS = nestedComp.getShort("short");
        BDSInt afterNestedIntBDS = nestedComp.getInt("int");

        assertEquals(beforeComp, afterComp);

        assertEquals(beforeString, afterStringBDS.getData());
        assertEquals(beforeByte, afterByteBDS.getData().byteValue());
        assertEquals(beforeShort, afterShortBDS.getData().shortValue());
        assertEquals(beforeInt, afterIntBDS.getData().intValue());

        assertEquals(beforeString, afterNestedStringBDS.getData());
        assertEquals(beforeByte, afterNestedByteBDS.getData().byteValue());
        assertEquals(beforeShort, afterNestedShortBDS.getData().shortValue());
        assertEquals(beforeInt, afterNestedIntBDS.getData().intValue());

    }
}
