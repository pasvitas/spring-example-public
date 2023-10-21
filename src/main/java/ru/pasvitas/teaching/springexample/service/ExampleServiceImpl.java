package ru.pasvitas.teaching.springexample.service;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.pasvitas.teaching.springexample.model.ExampleEntity;
import ru.pasvitas.teaching.springexample.repository.ExampleRepository;

@RequiredArgsConstructor
@Service
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    @Timed("examples.save.time")
    @Counted("examples.save.count")
    @Override
    public ExampleEntity saveExample(ExampleEntity exampleEntity) {
        return exampleRepository.save(exampleEntity);
    }

    @Override
    public Optional<ExampleEntity> getExampleById(Long id) {
        return exampleRepository.findById(id);
    }

    @Timed("examples.find.time")
    @Counted("examples.find.count")
    @Override
    public Page<ExampleEntity> findExampleByTitle(String title, int page, int size) {
        return exampleRepository.findExampleEntitiesByTitle(title, PageRequest.of(page, size));
    }

    @Override
    public Page<ExampleEntity> getExamples(int page, int size) {
        return exampleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public void deleteExample(Long id) {
        exampleRepository.deleteById(id);
    }
}
