package ru.itpark.framework.router;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface Router {
    void route(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException;
}
