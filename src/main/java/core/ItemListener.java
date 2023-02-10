package core;

import core.annotation.Database;

/*
*
* ItemName | ID | Stack (true or false)
*
*/
@Database
public class ItemListener {

    private long getItemID() {
        return 1L;
    }

    private String getItemName(long id) {
        return "";
    }

    private boolean getItemStack(long id) {
        return false;
    }

    private int getItemCount(long id, int count) {
        return getItemStack(id) ? count : 1;
    }
}
