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
