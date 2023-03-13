package ru.pasvitas.spring.example.rest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Класс для возврата экземпляра респонса
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExampleResponse {

    private Long id;

    private String title;

    private List<String> imgLinks;
}
