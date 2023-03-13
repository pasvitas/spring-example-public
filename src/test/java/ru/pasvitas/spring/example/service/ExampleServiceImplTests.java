package ru.pasvitas.spring.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import ru.pasvitas.spring.example.exceptions.EntityNotFoundException;
import ru.pasvitas.spring.example.model.ExampleEntity;
import ru.pasvitas.spring.example.model.ExampleEntityChild;
import ru.pasvitas.spring.example.repository.ExampleRepository;
import ru.pasvitas.spring.example.rest.model.GetExampleResponse;
import ru.pasvitas.spring.example.rest.model.GetExamplesListResponse;

//Нам не нужно развертывать весь спринг для этого, так что сделаем через MocktoJUnitRunner
@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceImplTests {

    private ExampleServiceImpl exampleService;

    //Мок - на самом деле repository не существует. Мы лишь задаем методы которые нам нужны и определяем ответы на них
    @Mock
    private ExampleRepository repository;

    //Эту сущность будет возвращать наш мок
    private ExampleEntity exampleEntity;

    private List<ExampleEntityChild> childList;
    private List<ExampleEntityChild> childList2;

    private List<String> links1;
    private List<String> links2 = new ArrayList<>();


    @Before
    public void setUp() {
        //Инициалиризуем сервис с мокнутым репозиторием
        exampleService = new ExampleServiceImpl(repository);

        links1 = new ArrayList<>();
        links1.add("Link");
        links1.add("Link1");

        links2 = new ArrayList<>();
        links2.add("Link2");
        links2.add("Link21");

        childList = new ArrayList<>();
        childList.add(ExampleEntityChild.builder().imgLink("Link").build());
        childList.add(ExampleEntityChild.builder().imgLink("Link1").build());

        childList2 = new ArrayList<>();
        childList2.add(ExampleEntityChild.builder().imgLink("Link2").build());
        childList2.add(ExampleEntityChild.builder().imgLink("Link21").build());

        exampleEntity = ExampleEntity.builder().id(1L).title("Hello World").childs(childList).build();
    }

    @Test
    public void testGetExampleById() throws EntityNotFoundException {

        //Чудеса мокито - когда мы будем просить у репозитория что-то с айдшником 1, он вернет exampleEntity.
        //А просить мы будем через сервис. Вот.
        when(repository.findById(1L)).thenReturn(Optional.of(exampleEntity));

        GetExampleResponse answer = exampleService.getExample(1L);

        //Проверим, что ответ не нуловый
        assertNotNull(answer);

        GetExampleResponse expectedResponse = GetExampleResponse.builder().id(exampleEntity.getId()).title(exampleEntity.getTitle()).imgLinks(links1).build();
        //Проверяем, что ответ верен
        assertEquals(expectedResponse, answer);
    }

    //Ожидаем выкинутый экзепшн
    @Test(expected = EntityNotFoundException.class)
    public void testGetExampleByIdWithNotFound() throws EntityNotFoundException {

        //Чудеса мокито - когда мы будем просить у репозитория что-то с айдшником 1, он вернет пустоту, соотвественно, сервис кинет экешпн
        when(repository.findById(1L)).thenReturn(Optional.empty());

        //Тут прилетит ожидаемый экщепн
        exampleService.getExample(1L);
    }

    @Test
    public void testGetExampleByTitle() {

        List<ExampleEntity> entitesToReturn = new ArrayList<>();
        entitesToReturn.add(ExampleEntity.builder().id(1L).title("Hello world").childs(childList).build());

        when(repository.findByTitle("Hello world")).thenReturn(entitesToReturn);

        GetExamplesListResponse answer = exampleService.getExample("Hello world");

        //Проверим, что ответ не нуловый
        assertNotNull(answer);

        List<GetExampleResponse> expectedExamples = new ArrayList<>();
        expectedExamples.add(GetExampleResponse.builder().id(1L).title("Hello world").imgLinks(links1).build());
        GetExamplesListResponse expectedResponse = GetExamplesListResponse.builder().examples(expectedExamples).build();

                //Проверяем, что ответ верен
        assertEquals(expectedResponse, answer);
    }

    @Test
    public void testGetExampleByTitleIfMoreThenOne() {

        List<ExampleEntity> entitesToReturn = new ArrayList<>();
        entitesToReturn.add(ExampleEntity.builder().id(1L).title("Hello world").childs(childList).build());
        entitesToReturn.add(ExampleEntity.builder().id(2L).title("Hello world").childs(childList2).build());

        when(repository.findByTitle("Hello world")).thenReturn(entitesToReturn);

        GetExamplesListResponse answer = exampleService.getExample("Hello world");

        //Проверим, что ответ не нуловый
        assertNotNull(answer);

        List<GetExampleResponse> expectedExamples = new ArrayList<>();
        expectedExamples.add(GetExampleResponse.builder().id(1L).title("Hello world").imgLinks(links1).build());
        expectedExamples.add(GetExampleResponse.builder().id(2L).title("Hello world").imgLinks(links2).build());

        GetExamplesListResponse expectedResponse = GetExamplesListResponse.builder().examples(expectedExamples).build();
        //Проверяем, что ответ верен
        assertEquals(expectedResponse, answer);
    }


    @Test
    public void testAddExample() {

        when(repository.save(ExampleEntity.builder().title("Hello world").childs(childList).build())).thenReturn(exampleEntity);

        List<String> links = new ArrayList<>();
        links.add("Link");
        links.add("Link1");

        GetExampleResponse answer = exampleService.addExample("Hello world", links);

        assertNotNull(answer);
        GetExampleResponse expectedResponse = GetExampleResponse.builder().id(exampleEntity.getId()).title(exampleEntity.getTitle()).imgLinks(links).build();
        //Проверяем, что ответ верен
        assertEquals(expectedResponse, answer);
    }
}