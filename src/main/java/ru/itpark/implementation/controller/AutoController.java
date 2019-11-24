package ru.itpark.implementation.controller;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.service.AutoService;
import ru.itpark.implementation.service.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Component
public class AutoController {
    private final AutoService autoService;
    private final FileService fileService;

    public AutoController(AutoService autoService, FileService fileService) {
        this.autoService = autoService;
        this.fileService = fileService;
    }


    public void getRoot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List list = doSearch("");
            request.setAttribute("items", list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void postRoot(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final Part part = request.getPart("image");
        final String image = fileService.writeFile(part);
        autoService.create(name, description, image);
    }

    public void getSearch(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        final String name = request.getParameter("q");
        List<String> result = doSearch(name);
        request.setAttribute("items", result);
    }

    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI() != null) {
            final String[] parts = request.getRequestURI().split("/");
            if (parts.length != 3) {
                throw new RuntimeException("Not found ");
            }
            fileService.readFile(parts[2], response.getOutputStream());
        }
    }

    public List doSearch(String name) throws SQLException {
        if (name.isEmpty()) {
            return autoService.getAll();
        } else {
            return autoService.search(name);
        }
    }
}
