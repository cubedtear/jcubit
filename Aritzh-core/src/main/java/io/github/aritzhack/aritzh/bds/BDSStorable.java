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
 * A class implementing this interface must be able to save its data to a BDS compound
 *
 * @author Aritz Lopez
 */
public interface BDSStorable {

    /**
     * Returns a BDSCompound describing this object
     *
     * @return a BDSCompound describing this object
     */
    public BDSCompound toBDS();
}
