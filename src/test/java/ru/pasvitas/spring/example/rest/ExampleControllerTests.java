package ru.pasvitas.spring.example.rest;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.pasvitas.spring.example.exceptions.EntityNotFoundException;
import ru.pasvitas.spring.example.rest.model.AddExampleRequest;
import ru.pasvitas.spring.example.rest.model.GetExampleResponse;
import ru.pasvitas.spring.example.rest.model.GetExamplesListResponse;
import ru.pasvitas.spring.example.service.ExampleServiceImpl;

//Нам не нужно развертывать весь спринг для этого, так что сделаем через MocktoJUnitRunner
@RunWith(MockitoJUnitRunner.class)
public class ExampleControllerTests {

    @Mock
    private ExampleServiceImpl service;

    private ExampleController exampleController;

    private final GetExampleResponse expectedResponseData = GetExampleResponse.builder().id(1L).title("Hello world").build();

    @Before
    public void setUp() {
        exampleController = new ExampleController(service);
    }

    @Test
    public void getExample() throws EntityNotFoundException {

        when(service.getExample(1L)).thenReturn(expectedResponseData);

        ResponseEntity<?> answer = exampleController.getExample(1L);

        assertEquals(answer.getStatusCode(), HttpStatus.OK);
        assertEquals(answer.getBody(), expectedResponseData);
    }

    @Test
    public void getExampleNotFound() throws EntityNotFoundException {

        when(service.getExample(1L)).thenThrow(new EntityNotFoundException());

        ResponseEntity<?> answer = exampleController.getExample(1L);

        //При экзепшне мы должны дать 404
        assertEquals(answer.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetExample() {

        List<GetExampleResponse> response = new ArrayList<>();
        response.add(GetExampleResponse.builder().id(1L).title("Hello world").build());
        response.add(GetExampleResponse.builder().id(2L).title("Hello world").build());

        GetExamplesListResponse expectedResponse = GetExamplesListResponse.builder().examples(response).build();

        when(service.getExample("Hello world")).thenReturn(expectedResponse);

        ResponseEntity<?> answer = exampleController.getExample("Hello world");

        assertEquals(answer.getStatusCode(), HttpStatus.OK);
        assertEquals(answer.getBody(), GetExamplesListResponse.builder().examples(response).build());
    }

    @Test
    public void testGetExample1() {

        List<String> links = new ArrayList<>();
        links.add("Link1");
        links.add("link2");

        when(service.addExample("Hello world", links)).thenReturn(expectedResponseData);

        ResponseEntity<?> answer = exampleController.addExample(new AddExampleRequest("Hello world", links));

        assertEquals(answer.getStatusCode(), HttpStatus.CREATED);
        assertEquals(answer.getBody(), expectedResponseData);
    }
}