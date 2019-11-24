package ru.itpark.util;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class JdbcTemplate {

    public static <T> List<T> execQuery(DataSource ds, String sql, RowMapper<T> mapper) {
        return execQuery(ds,sql,mapper,null);
    }

    public static <T> List<T> execQuery(DataSource ds, String sql, RowMapper<T> mapper, StatementSetter setter) {
        List<T> result = new LinkedList<>();
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            if (setter != null) {
                setter.set(statement);
            };
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void execUpdate(DataSource ds, String sql, StatementSetter setter) {
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            setter.set(statement);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
