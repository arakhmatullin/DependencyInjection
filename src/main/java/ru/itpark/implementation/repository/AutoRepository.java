package ru.itpark.implementation.repository;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.domain.Auto;
import ru.itpark.util.JdbcTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Component
public class AutoRepository{
    private final DataSource ds;

    public AutoRepository() throws NamingException, SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS autos (id TEXT PRIMARY KEY , name TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT);";
        Context context = new InitialContext();
        ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        try (Connection conn = ds.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
//
//                stmt.execute("INSERT INTO autos (id, name, description, imageUrl) VALUES (\"1\",\"qwer\",\"iopa\",\"0790f54a-75d0-40a0-962d-6436cac1b945\")");
//                stmt.execute("INSERT INTO autos (id, name, description, imageUrl) VALUES (\"2\",\"wert\",\"uiop\",\"12161402-5f41-4cf2-8d07-de4a54cada16\")");
//                stmt.execute("INSERT INTO autos (id, name, description, imageUrl) VALUES (\"3\",\"erty\",\"yuio\",\"1b4c5848-40dd-4f10-8bdd-bb93057511e5\")");
//                stmt.execute("INSERT INTO autos (id, name, description, imageUrl) VALUES (\"4\",\"rtyu\",\"tyui\",\"63888495-a931-4257-9972-761579cc331f\")");
            }
        }
    }

    public List<Auto> getAll() throws SQLException {
        return search("");
    }

    public List<Auto> search(String searchString){
        final String sql = "SELECT *,( name || \" \" || description) AS q FROM autos WHERE q LIKE ?";
        return JdbcTemplate.execQuery(ds,sql, rs -> new Auto(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("imageUrl")
                ),
                stmt -> stmt.setString(1, "%"+searchString+"%")
        );


    }

    public void create(String name, String description, String imageUrl) throws SQLException {
        final String sql = "INSERT INTO autos (id, name, description, imageUrl) VALUES (?, ?, ?, ?)";
        JdbcTemplate.execUpdate(ds,sql, stmt-> {
                    stmt.setString(1, UUID.randomUUID().toString());
                    stmt.setString(2, name);
                    stmt.setString(3, description);
                    stmt.setString(4, imageUrl);
                }
        );

    }


}
