package ru.pasvitas.spring.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pasvitas.spring.example.model.ExampleEntity;

//Репозиторий для взаимодействия с БД через Spring data jpa
@Repository
public interface ExampleRepository extends JpaRepository<ExampleEntity, Long> {
    //Ищем по тайтлу. Просто объявляем метод, Spring Data Jpa все сделает за нас
    List<ExampleEntity> findByTitle(String title);
    Optional<ExampleEntity> findById(Long id);
}