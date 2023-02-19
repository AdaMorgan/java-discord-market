import core.database.Connect;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRuntime {

    @Test
    public void isConnect() throws SQLException {
        assertTrue(Connect.getConnect().isValid(5000), "connection is established ");
    }
}
