package ru.itpark.implementation.router;

import ru.itpark.framework.annotation.Component;
import ru.itpark.framework.router.Router;
import ru.itpark.implementation.controller.AutoController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class RouterImpl implements Router {
    private final AutoController autoController;

    public RouterImpl(AutoController autoController) {
        this.autoController = autoController;
    }


    private void error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(404);
    }

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        String requestUri = request.getRequestURI();
        if (requestUri.equals("/")) {
            String method = request.getMethod().toUpperCase();
            switch (method) {
                case "GET":
                    autoController.getRoot(request, response);
                    request.getRequestDispatcher("/WEB-INF/catalog.jsp").forward(request, response);
                    break;
                case "POST":
                    autoController.postRoot(request, response);
                    response.sendRedirect("/");
                    break;
            }
        } else if (requestUri.equals("/search")) {
            autoController.getSearch(request, response);
            request.getRequestDispatcher("/WEB-INF/catalog.jsp").forward(request, response);
        } else if (requestUri.startsWith("/images")) {
            autoController.getImage(request, response);
        } else {
            error(request, response);
        }
    }
}
