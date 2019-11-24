package ru.itpark.framework.container;

import org.reflections.Reflections;
import ru.itpark.framework.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainerDefaultImpl implements Container {
    @Override
    public Map<Class, Object> init() {

        final Reflections reflections = new Reflections();
        final Map<Class, Object> components = new HashMap<>();
        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class, true).stream()
                .filter(o -> !o.isAnnotation())
                .filter(o -> {
                    if (o.getConstructors().length == 1) {
                        return true;
                    } else {
                        throw new RuntimeException("Invalid count of constructors:" + o.getName());
                    }
                })
                .collect(Collectors.toSet());

        Map<Class, Object> currentGeneration;
        while (true) {
            currentGeneration = types.stream()
                    .filter(o -> !components.containsKey(o))
                    .filter(o -> {
                        final Constructor<?>[] constructors = o.getConstructors();
                        final Class<?>[] parameterTypes = constructors[0].getParameterTypes();
                        return components.keySet().containsAll(Arrays.asList(parameterTypes));

                    })
                    .collect(Collectors.toMap(o -> o, o -> {
                        final Constructor<?> constructor = o.getConstructors()[0];
                        final Object[] params = Arrays.stream(constructor.getParameterTypes())
                                .map(components::get).toArray();
                        try {
                            return constructor.newInstance(params);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new RuntimeException();
                        }
                    }));

            if (currentGeneration.isEmpty()) {
                break;
            }
            components.putAll(currentGeneration);
        }
        ;
        System.out.println(components);
        return components;
    }

    public static void main(String[] args) {
        System.out.println(new Reflections().getTypesAnnotatedWith(Component.class));
    }
}
