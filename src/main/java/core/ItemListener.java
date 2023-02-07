package core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.message.MessageEditor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/*
*
* ItemName | ID | Stack (true or false)
*
*/
public class ItemListener {
    public static String getItem(String item) {
        InputStream inputStream = ItemListener.class.getResourceAsStream("");
        JsonObject json = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();

        return "";
    }

    public static boolean getItemStack() {
        return false;
    }
}
