package ru.pasvitas.teaching.springexample.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.pasvitas.teaching.springexample.model.ExampleEntity;

public interface ExampleService {

    ExampleEntity saveExample(ExampleEntity exampleEntity);

    Optional<ExampleEntity> getExampleById(Long id);

    Page<ExampleEntity> findExampleByTitle(String title, int page, int size);

    Page<ExampleEntity> getExamples(int page, int size);

    void deleteExample(Long id);
}
