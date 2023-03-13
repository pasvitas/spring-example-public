package ru.pasvitas.spring.example.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.lang.Nullable;

//Аннотации из lombok. Дают кучу вещей, аля Builder, конструкторы без кода, геттеры, сеттеры и тп
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Говорит, что класс относится к бд
@Entity
public class ExampleEntity {

    //Генерирует значение для БД. Аля автоинкремент
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //Столбец в таблице. Возможно необязательно
    @Column(name = "title")
    private String title;

    //Связь один ко многим. Каждой ExampleEntity у нас соответствует несколько ExampleEntityChild
    @OneToMany(cascade = CascadeType.ALL)
    //Шняга ниже - тянет всю таблицу целком, минуя "lazy" подгрузку.
    //Полезно, если нужно подтянуть все данные разом
    //Если данных будет ооооочень много - придет создатель спринга чекать сам понимаешь кого
    @LazyCollection(value = LazyCollectionOption.FALSE)
    private List<ExampleEntityChild> childs;
}
