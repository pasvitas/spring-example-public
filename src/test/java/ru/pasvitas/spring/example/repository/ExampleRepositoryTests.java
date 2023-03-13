package ru.pasvitas.spring.example.repository;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.pasvitas.spring.example.model.ExampleEntity;
import ru.pasvitas.spring.example.model.ExampleEntityChild;

//Помечаем аннотациями тест для настройки окружения
@RunWith(SpringRunner.class)
@SpringBootTest
//Говорим спрингу про грязный контекст после каждого теста. Таким образом, он нам после каждого теста пересоздаст БД
//TODO: Найти другой способ, аля @Before или @After, которые сейчас не работают, почему то
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//Тестовый профиль для тестов
@ActiveProfiles("test")
public class ExampleRepositoryTests {

    //Тестируемый реп
    @Autowired
    private ExampleRepository repository;

    private List<ExampleEntityChild> childList;
    private List<ExampleEntityChild> childList2;

    @Before
    public void setUp() {
        childList = new ArrayList<>();
        childList.add(ExampleEntityChild.builder().imgLink("Link").build());
        childList.add(ExampleEntityChild.builder().imgLink("Link1").build());

        childList2 = new ArrayList<>();
        childList2.add(ExampleEntityChild.builder().imgLink("Link2").build());
        childList2.add(ExampleEntityChild.builder().imgLink("Link21").build());

    }

    //Проверка, что сущности сохраняются
    @Test
    public void entitySaves() {
        //Делаем ентити для сейва
        ExampleEntity entityToSave = ExampleEntity.builder().title("Hello world").childs(childList).build();
        //Сейвим и получаем сохраненную ентити
        ExampleEntity result = repository.save(entityToSave);
        //Проверяем что id не null (так как БД должна выставить сама)
        assertNotNull(result.getId());
        //Проверяем, что титул - наш
        assertEquals("Hello world", result.getTitle());
        //Проверяем, что чилды тоже наши
        assertEquals(childList, result.getChilds());
    }

    //Проверка, что несколько сущностей сохраняется
    @Test
    public void severalEntitySaves() {
        ExampleEntity entityToSave1 = ExampleEntity.builder().title("Hello world1").childs(childList).build();
        ExampleEntity entityToSave2 = ExampleEntity.builder().title("Hello world2").childs(childList2).build();
        //Сейвим сразу 2, обе должны сохраниться с разными айдишниками (1 и 2)
        ExampleEntity result1 = repository.save(entityToSave1);
        ExampleEntity result2 = repository.save(entityToSave2);

        //Проверяем что обе сущности ок
        assertNotNull(result1.getId());
        assertNotNull(result2.getId());

        assertEquals("Hello world1", result1.getTitle());
        assertEquals("Hello world2", result2.getTitle());

        //Чекаем айдишники
        assertEquals(1L, (long) result1.getId());
        assertEquals(2L, (long) result2.getId());

        //Проверяем, что чилды тоже наши
        assertEquals(childList, result1.getChilds());
        assertEquals(childList2, result2.getChilds());
    }

    //Проверка, что поиск по тайтлу работает нормально
    @Test
    public void findByTitleIfOneEntity() {

        //Сейвим ентити в базу
        ExampleEntity entityToSave = ExampleEntity.builder().title("Hello world").childs(childList).build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity1 = ExampleEntity.builder().title("Hello world1").build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity2 = ExampleEntity.builder().title("Hello world2").build();
        repository.save(fakeEntity1);
        ExampleEntity savedEntity = repository.save(entityToSave);
        repository.save(fakeEntity2);

        //Ищем ентити
        List<ExampleEntity> entities =  repository.findByTitle("Hello world");
        //Проверяем количество
        assertEquals(1, entities.size());
        ExampleEntity findedEntity = entities.get(0);

        //Проверяем, что сущность не нулловая
        assertNotNull(findedEntity);

        //Проверяем, что тайтл тот, ка и ид
        assertEquals("Hello world", findedEntity.getTitle());
        assertEquals(2L, (long) findedEntity.getId());
        assertEquals(childList.get(0).getImgLink(), findedEntity.getChilds().get(0).getImgLink());
        assertEquals(childList.get(1).getImgLink(), findedEntity.getChilds().get(1).getImgLink());
    }

    //Проверяем что ничего не найдем, если ничего не сохранили
    @Test
    public void findByTitleNotFindEntity() {
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity1 = ExampleEntity.builder().title("Hello world1").build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity2 = ExampleEntity.builder().title("Hello world2").build();

        repository.save(fakeEntity1);
        repository.save(fakeEntity2);

        List<ExampleEntity> entities = repository.findByTitle("Hell world");

        //Проверяем, что количество 0.
        assertEquals(0, entities.size());
    }

    @Test
    public void findByTitleIfEntitiesMoreThenOne() {

        //В этом тесте сначала сохраняем 2, потом1. Зачем? Хер знает
        //Сейвим ентити в базу
        ExampleEntity entityToSave1 = ExampleEntity.builder().title("Hello world").childs(childList).build();
        //Фейковые ентити для наполнения
        ExampleEntity entityToSave2 = ExampleEntity.builder().title("Hello world").childs(childList2).build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity2 = ExampleEntity.builder().title("Hello world2").build();
        repository.save(entityToSave2);
        ExampleEntity savedEntity = repository.save(entityToSave1);
        repository.save(fakeEntity2);

        //Ищем ентити
        List<ExampleEntity> entities =  repository.findByTitle("Hello world");
        //Проверяем количество
        assertEquals(2, entities.size());

        //Смотрим на ентити по очереди
        ExampleEntity findedEntity = entities.get(0);

        //Проверяем, что сущность не нулловая
        assertNotNull(findedEntity);

        //Проверяем, что тайтл тот, ка и ид
        assertEquals("Hello world", findedEntity.getTitle());
        assertEquals(1L, (long) findedEntity.getId());
        assertEquals(childList2.get(0).getImgLink(), findedEntity.getChilds().get(0).getImgLink());
        assertEquals(childList2.get(1).getImgLink(), findedEntity.getChilds().get(1).getImgLink());

        //Следующая
        findedEntity = entities.get(1);

        //Проверяем, что сущность не нулловая
        assertNotNull(findedEntity);

        //Проверяем, что тайтл тот, ка и ид
        assertEquals("Hello world", findedEntity.getTitle());
        assertEquals(2L, (long) findedEntity.getId());
        assertEquals(childList.get(0).getImgLink(), findedEntity.getChilds().get(0).getImgLink());
        assertEquals(childList.get(1).getImgLink(), findedEntity.getChilds().get(1).getImgLink());
    }

    //Тестим поиск гета. TODO: GetOne почему-то не ищет. ВТФ?
    @Test
    public void getById() {
        //Сейвим ентити в базу
        ExampleEntity entityToSave = ExampleEntity.builder().title("Hello world").childs(childList).build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity1 = ExampleEntity.builder().title("Hello world1").build();
        //Фейковые ентити для наполнения
        ExampleEntity fakeEntity2 = ExampleEntity.builder().title("Hello world2").build();
        repository.save(fakeEntity1);
        ExampleEntity savedEntity = repository.save(entityToSave);
        repository.save(fakeEntity2);

        //Ищем ентити
        ExampleEntity findedEntity = repository.findById(2L).get();

        //Проверяем, что сущность не нулловая
        assertNotNull(findedEntity);

        //Проверяем, что тайтл тот, ка и ид
        assertEquals("Hello world", findedEntity.getTitle());
        assertEquals(savedEntity.getId(), findedEntity.getId());
        assertEquals(childList.get(0).getImgLink(), findedEntity.getChilds().get(0).getImgLink());
        assertEquals(childList.get(1).getImgLink(), findedEntity.getChilds().get(1).getImgLink());
    }
}