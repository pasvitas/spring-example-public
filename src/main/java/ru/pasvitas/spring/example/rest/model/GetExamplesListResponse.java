package ru.pasvitas.spring.example.rest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetExamplesListResponse {

    private List<GetExampleResponse> examples;
}
