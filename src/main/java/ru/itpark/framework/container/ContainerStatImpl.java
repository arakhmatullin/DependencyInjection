package ru.itpark.framework.container;

import ru.itpark.implementation.controller.AutoController;
import ru.itpark.implementation.repository.AutoRepository;
import ru.itpark.implementation.router.RouterImpl;
import ru.itpark.implementation.service.AutoService;
import ru.itpark.implementation.service.FileService;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ContainerStatImpl implements Container {
  @Override
  public Map<Class, Object> init() throws SQLException, NamingException, IOException {
    Map<Class, Object> components = new HashMap<>();
    components.put(AutoRepository.class, new AutoRepository());
    components.put(FileService.class, new FileService());

    components.put(AutoService.class, new AutoService((AutoRepository) components.get(AutoRepository.class)));
    components.put(AutoController.class,
            new AutoController(
                    (AutoService) components.get(AutoService.class),
                    (FileService) components.get(FileService.class)));
    components.put(RouterImpl.class, new RouterImpl((AutoController) components.get(AutoController.class)));
    return components;
  }
}
