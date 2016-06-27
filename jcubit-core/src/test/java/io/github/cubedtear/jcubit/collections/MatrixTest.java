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

package io.github.cubedtear.jcubit.collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aritz Lopez
 */
public class MatrixTest {

	public static Matrix<Integer> newMultiplicationTable(int x, int y) {
		Matrix<Integer> ret = new Matrix<>(x, y, 0);
		for (int ix = 0; ix < x; ix++) {
			for (int iy = 0; iy < y; iy++) {
				ret.set(ix * iy, ix, iy);
			}
		}
		return ret;
	}

	@Test
	public void getElementTest() {
		Matrix<Integer> mat = MatrixTest.newMultiplicationTable(10, 10);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				Assert.assertEquals("For coords (" + x + ", " + y + ")", x * y, mat.get(x, y).intValue());
			}
		}
	}

	@Test
	public void paramFuncTest() {
		Matrix<Integer> mat = MatrixTest.newMultiplicationTable(10, 10);

		mat.runForEach(new ParametrizedFunction<Matrix<Integer>.MatrixElement<Integer>, Object>() {
			@Override
			public Object apply(Matrix<Integer>.MatrixElement<Integer> element, Object... args) {
				int x = element.getX();
				int y = element.getY();
				Assert.assertEquals("For coords (" + x + ", " + y + ")", x * y, element.getE().intValue());
				return null;
			}
		});
	}
}
