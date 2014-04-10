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

package io.github.aritzhack.aritzh;

import junit.framework.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aritz Lopez
 */
public class ReflectionUtilTest {

    @Test
    public void testClassHasAnnotation() throws Exception {
        Assert.assertTrue("Class does not have the annotation!", ReflectionUtil.classHasAnnotation(WithAnnotation.class, Annotation.class));

        Assert.assertTrue("Class does not have the annotation!", ReflectionUtil.classHasAnnotation(ToTest.class, Annotation.class));
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private static @interface Annotation {}

    @Annotation
    private static class WithAnnotation {}

    private static class ToTest extends WithAnnotation {}
}
