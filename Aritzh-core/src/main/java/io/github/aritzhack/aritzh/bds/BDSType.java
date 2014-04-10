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

/**
 * Enum with all BDS Types
 *
 * @author Aritz Lopez
 */
public enum BDSType {
    BDS_BYTE, BDS_SHORT, BDS_INT, BDS_STRING, BDS_COMPOUND, BDS_COMPEND;

    /**
     * Why cast if you have a method for that?
     *
     * @return {@code (byte) this.ordinal();}
     */
    public byte toByte() {
        return (byte) this.ordinal();
    }
}