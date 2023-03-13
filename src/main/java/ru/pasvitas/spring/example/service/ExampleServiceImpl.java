package ru.pasvitas.spring.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pasvitas.spring.example.exceptions.EntityNotFoundException;
import ru.pasvitas.spring.example.interfaces.ExampleService;
import ru.pasvitas.spring.example.model.ExampleEntity;
import ru.pasvitas.spring.example.model.ExampleEntityChild;
import ru.pasvitas.spring.example.repository.ExampleRepository;
import ru.pasvitas.spring.example.rest.model.GetExampleResponse;
import ru.pasvitas.spring.example.rest.model.GetExamplesListResponse;

@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    @Override
    public GetExampleResponse getExample(Long id) throws EntityNotFoundException {
        Optional<ExampleEntity> result = exampleRepository.findById(id);
        //Если сущность нашли
        if (result.isPresent()) {
            ExampleEntity entity = result.get();
            List<String> links = new ArrayList<>();
            entity.getChilds().forEach(link -> links.add(link.getImgLink()));
            return GetExampleResponse.builder().id(entity.getId()).title(entity.getTitle()).imgLinks(links).build();
        }
        //Нет - кидаем экзепшн, который ловим в контроллере
        else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public GetExamplesListResponse getExample(String title) {
        List<ExampleEntity> result = exampleRepository.findByTitle(title);
        List<GetExampleResponse> responses = new ArrayList<>();
        result.forEach(entity -> {
            List<String> links = new ArrayList<>();
            entity.getChilds().forEach(link -> links.add(link.getImgLink()));
            responses.add(GetExampleResponse.builder().id(entity.getId()).title(entity.getTitle()).imgLinks(links).build());
        });
        return GetExamplesListResponse.builder().examples(responses).build();
    }

    @Override
    public GetExampleResponse addExample(String title, List<String> links) {

        List<ExampleEntityChild> childs = new ArrayList<>();
        links.forEach(link -> {
            childs.add(ExampleEntityChild.builder().imgLink(link).build());
        });
        ExampleEntity entity = ExampleEntity.builder().title(title).childs(childs).build();
        //Сейвим в бд
        ExampleEntity result = exampleRepository.save(entity);
        return GetExampleResponse.builder().id(result.getId()).title(result.getTitle()).imgLinks(links).build();
    }
}
