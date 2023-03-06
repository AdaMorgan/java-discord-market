package core;

/*
*
* ItemName | ID | Stack (true or false)
*
*/
public interface ItemListener {

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
