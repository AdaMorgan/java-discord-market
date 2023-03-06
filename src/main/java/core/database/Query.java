package core.database;

import java.sql.SQLException;

public interface Query {
//    static void createTable(long id) throws SQLException {
//        Connect.getConnect().createStatement().executeQuery().close();
//    }

    static void addValue() throws SQLException {
        Connect.getConnect().createStatement().executeQuery("").close();
    }
}
