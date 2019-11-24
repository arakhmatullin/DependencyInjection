package ru.itpark.implementation.service;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.domain.Auto;
import ru.itpark.implementation.repository.AutoRepository;

import java.sql.SQLException;
import java.util.List;

@Component
public class AutoService {
    private final AutoRepository repository;

    public AutoService(AutoRepository repository) {
        this.repository = repository;
    }

    public List<Auto> getAll() throws SQLException {
        return repository.getAll();
    }

    public List<Auto> search(String searchText) throws SQLException {
        return repository.search(searchText);
    }

    public void create(String name, String description, String imageUrl) throws SQLException {
        repository.create(name, description, imageUrl);
    }
}
