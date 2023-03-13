package ru.pasvitas.spring.example.interfaces;

import java.util.List;
import org.springframework.http.ResponseEntity;
import ru.pasvitas.spring.example.exceptions.EntityNotFoundException;
import ru.pasvitas.spring.example.model.ExampleEntity;
import ru.pasvitas.spring.example.rest.model.GetExampleResponse;
import ru.pasvitas.spring.example.rest.model.GetExamplesListResponse;

//Интерфейс сервиса. Зачем? Как и обычные интерфейсы
public interface ExampleService {

    //Получить экзампл по идшнику
    GetExampleResponse getExample(Long id) throws EntityNotFoundException;

    //Получить экзампл по тайтлу
    GetExamplesListResponse getExample(String title);

    //Добавить экзампл
    GetExampleResponse addExample(String title, List<String> links);
}
