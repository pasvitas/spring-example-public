package ru.pasvitas.spring.example.rest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pasvitas.spring.example.exceptions.EntityNotFoundException;
import ru.pasvitas.spring.example.interfaces.ExampleService;
import ru.pasvitas.spring.example.rest.model.AddExampleRequest;
import ru.pasvitas.spring.example.rest.model.GetExampleResponse;

//Помечаем, что это у нас контроллер реста
@RestController
//Путь ко всем методам контроллера
@RequestMapping("api")
@RequiredArgsConstructor
public class ExampleController {

    //С аннотацией RequiredArgsConstructor и спрингом получаем правильный вариант @Autowired
    private final ExampleService exampleService;

    //Только Get по /api/getExampleById
    @GetMapping("/getExampleById")
    @ApiOperation(value = "Get example", response = GetExampleResponse.class)
    public @ResponseBody ResponseEntity<?> getExample(@Param("id") Long id) {
        //Трайим получение сервиса, если не получается - кидаем ответ с экземшном
        try {
            return ResponseEntity.ok(exampleService.getExample(id));
        } catch (EntityNotFoundException e) {
            //Поймали, что ничего не нашли - кинули 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }


    }

    @GetMapping("/getExampleByTitle")
    @ApiOperation(value = "Get example by title", response = GetExampleResponse.class)
    public @ResponseBody ResponseEntity<?> getExample(@Param("title") String title) {
        try {
            return ResponseEntity.ok(exampleService.getExample(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    @PostMapping("/addExample")
    @ApiOperation(value = "Get example", response = GetExampleResponse.class)
    public @ResponseBody ResponseEntity<?> addExample(@RequestBody AddExampleRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(exampleService.addExample(request.getTitle(), request.getLinks()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
}
