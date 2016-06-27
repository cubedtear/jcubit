package io.github.cubedtear.jcubit.bds;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aritz Lopez
 */
@Category(BDS.class)
public class BDSUtilTest {

    @Test
    public void testBDSUtil() throws Exception {
        final ArrayList<Object> list = new ArrayList<>();

        TestClass before = new TestClass(list);

        list.add(before);
        list.add("TestString");

        BDS serialized = BDSUtil.serialize(before);
        Object after = BDSUtil.deserialize(serialized);
        Assert.assertTrue(after instanceof TestClass);
        TestClass afterTyped = (TestClass) after;
        Assert.assertTrue(afterTyped.lista.contains(afterTyped));
        Assert.assertTrue(afterTyped.lista.contains(afterTyped));
    }

    private static class TestClass {
        List<Object> lista;


        public TestClass(List<Object> lista) {this.lista = lista;}

        public TestClass() {this.lista = null;}
    }
}
