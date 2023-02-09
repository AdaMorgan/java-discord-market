package core;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

// Unique Message Identifier
// 16-04742719
public class UMID implements Serializable {

    public static void main(String[] args) {
        System.out.println(randomUMID());
    }

    public static long randomUMID() {
        return new AtomicInteger(0).addAndGet(1);
    }

    public static boolean getUMID() {
        return false;
    }
}
