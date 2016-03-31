package test.apis;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2016-03-31.
 */
public class TestString {
    @Test
    public void sameInstance() throws Exception {
        assertTrue("data" != new String("data"));
    }
}
