import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRuntime {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before class");
    }

    @Test
    public void test() {
        assertEquals("Hello world!", 5, addition(3, 2));
    }

    public int addition(int x, int y) {
        return x + y;
    }
}
