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

import java.util.Arrays;

/**
 * Binary Data Storage
 * Used to store different data types in byte arrays,
 * write them to a file, and then read them back.
 *
 * @author Aritz Lopez
 */
public abstract class BDS {

    protected String name = "";

    /**
     * Returns the data representing this BDS
     *
     * @return An array of bytes that represent this BDS
     */
    public abstract byte[] getBytes();

    /**
     * Returns the name of this BDS
     *
     * @return the name of this BDS
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the type of this BDS, useful to check if it's one type or another
     *
     * @return The type of this BDS
     * @see BDSType
     */
    public abstract BDSType getType();

    /**
     * Returns the data corresponding to this BDS.
     */
    public abstract Object getData();

    /**
     * Returns a hashcode made from the byte array of this BDSCompound, which should be unique,
     * unless the objects are the same or equal.
     */
    @Override
    public int hashCode() {
        return Arrays.toString(this.getBytes()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (this.hashCode() == obj.hashCode() && this.getClass().equals(obj.getClass()));
    }

    @Override
    public String toString() {
        return "[" + this.getType() + ":" + this.getName() + ":" + this.getData() + "]";
    }
}
