package ru.pasvitas.teaching.springexample.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.pasvitas.teaching.springexample.model.ExampleEntity;

public interface ExampleRepository extends PagingAndSortingRepository<ExampleEntity, Long>, JpaRepository<ExampleEntity, Long> {

    Page<ExampleEntity> findExampleEntitiesByTitle(String title, Pageable pageable);
}
