package ru.itpark.framework.container;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface Container {
    Map<Class, Object> init() throws SQLException, NamingException, IOException;
}
