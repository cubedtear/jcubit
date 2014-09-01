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

/**
 * Enum with all BDS Types
 *
 * @author Aritz Lopez
 */
public enum BDSType {
    BDS_BYTE(0), BDS_SHORT(1), BDS_INT(2), BDS_STRING(3), BDS_COMPOUND(4), BDS_COMPEND(5);

    private final byte ordinal;

    BDSType(int ordinal) {
        this.ordinal = (byte) ordinal;
    }

    /**
     * Why cast if you have a method for that?
     *
     * @return {@code (byte) this.ordinal();}
     */
    public byte toByte() {
        return this.ordinal;
    }
}