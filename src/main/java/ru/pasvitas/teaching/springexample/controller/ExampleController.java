package ru.pasvitas.teaching.springexample.controller;

import jakarta.websocket.server.PathParam;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pasvitas.teaching.springexample.controller.model.ExamplePageModel;
import ru.pasvitas.teaching.springexample.model.ExampleEntity;
import ru.pasvitas.teaching.springexample.service.ExampleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/examples")
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("/")
    public ResponseEntity<ExampleEntity> createExample(@RequestBody ExampleEntity entity) {
        entity.setId(null);
        return ResponseEntity.ok(exampleService.saveExample(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExampleEntity> editExample(
            @PathParam("id") Long id,
            @RequestBody ExampleEntity entity) {
        entity.setId(id);
        return ResponseEntity.ok(exampleService.saveExample(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExample(
            @PathVariable("id") Long id
    ) {
        Optional<ExampleEntity> exampleEntityOptional = exampleService.getExampleById(id);
        if (exampleEntityOptional.isPresent()) {
            exampleService.deleteExample(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExampleEntity> getById(
            @PathVariable("id") Long id
    ) {
        return exampleService.getExampleById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public ResponseEntity<ExamplePageModel> find(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Page<ExampleEntity> pageData;
        if (title != null) {
            pageData = exampleService.findExampleByTitle(title, page, size);
        }
        else {
            pageData = exampleService.getExamples(page, size);
        }
        return ResponseEntity.ok(new ExamplePageModel(page, pageData.getTotalPages(), pageData.getTotalElements(), pageData.getContent()));
    }

}
