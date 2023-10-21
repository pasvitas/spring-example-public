package ru.pasvitas.teaching.springexample.controller.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pasvitas.teaching.springexample.model.ExampleEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExamplePageModel {

    private int page;

    private int totalPages;

    private long totalSize;

    private List<ExampleEntity> data;

}
