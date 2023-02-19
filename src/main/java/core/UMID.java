package core;

import java.io.Serializable;

// Unique Message Identifier
// 16-04742719
// Check question serializable for save value ID
public final class UMID {
    public static void main(String[] args) {
        System.out.println(getLongID());
    }

    public static String getStringID() {
        return String.format("16%s", getGenerateID());
    }

    public static long getLongID() {
        return Long.parseLong(getStringID());
    }

    private static long getGenerateID() {
        return 1L;
    }
}
