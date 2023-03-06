package core;

import java.util.Random;

// Unique Message Identifier
// 16-04742719
public class UMID {
    public static String getStringID() {
        return String.format("16%s", isValid());
    }

    public static long getLongID() {
        return Long.parseLong(getStringID());
    }

    public static boolean isValid(long id) {
        return true;
    }

    private static long isValid() {
        return 1L;
        //return !isValid(generate) ? generate : isValid();
    }

    private static long getGenerateID(StringBuilder builder, Random random) {
        for (int i = 0; i < 8; i++)
            builder.append(random.nextInt(9));
        return Long.parseLong(builder.toString());
    }
}
