package sn.ept.git.seminaire.cicd.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import sn.ept.git.seminaire.cicd.ReplaceCamelCase;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;
import sn.ept.git.seminaire.cicd.services.impl.TodoServiceImpl;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;
    @InjectMocks
    TodoServiceImpl service;

    private static TodoMapper mapper;

    private TodoDTO dto;
    private Todo entity;

    private String randomId;


    @BeforeAll
    static void beforeAll() {
        mapper = Mappers.getMapper(TodoMapper.class);
    }

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(service, "mapper", mapper);
        dto = TodoDTOTestData.defaultDTO();
        entity = mapper.toEntity(dto);
        randomId = UUID.randomUUID().toString();
    }

    private void mockSaveAndFlush() {
        Mockito.when(todoRepository.saveAndFlush(Mockito.any(Todo.class))).then(invocation -> {
            Instant now = Instant.now();
            Todo todo = invocation.getArgument(0, Todo.class);
            todo.setId(Optional.ofNullable(todo.getId()).orElse(randomId));
            todo.setCreatedDate(Optional.ofNullable(todo.getCreatedDate()).orElse(now));
            todo.setLastModifiedDate(now);
            return todo;
        });
    }


    private void mockFindByTitleWithIdNotEquals() {
        Mockito.when(todoRepository.findByTitleWithIdNotEquals(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(entity));
    }

    private void mockFindByTitleWithIdNotEqualsNotFound() {
        Mockito.when(todoRepository.findByTitleWithIdNotEquals(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
    }


    private void mockFindAll() {
        Mockito.when(todoRepository.findAll())
                .thenReturn(List.of(entity));
    }

    private void mockFindAllPageable() {
        Mockito.when(todoRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));

    }

    private void mockFindById() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(entity));
    }

    private void mockFindByIdNotFound() {
        Mockito.when(todoRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());
    }


    private void mockDeleteById() {
        Mockito.doNothing().when(todoRepository)
                .deleteById(Mockito.anyString());
    }

    private void mockDeleteAll() {
        Mockito.doNothing().when(todoRepository).deleteAll();
    }

    @Test
    void saveShouldSaveTodo() {

        Mockito.when(todoRepository.findByTitle(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(todoRepository.saveAndFlush(Mockito.any(Todo.class)))
                .then(args -> {
                    Todo todo = args.getArgument(0, Todo.class);
                    Instant now = Instant.now();
                    todo.setId(UUID.randomUUID().toString());
                    todo.setCreatedDate(now);
                    todo.setLastModifiedDate(now);
                    return todo;
                });
        //verifier que le titre n'est pas duplique
        //ajouter la todo
        TodoDTO saved  = service.save(dto);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedDate());
        assertNotNull(saved.getLastModifiedDate());

        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void saveWithExistingTitleShouldThrowException() {

        Mockito.when(todoRepository.findByTitle(Mockito.anyString()))
                .thenReturn(Optional.of(entity));

        assertThrows(
                ItemExistsException.class,
                () -> service.save(dto)
        );

    }


    @Test
    void updateShouldSucceed() {
        mockFindById();
        mockFindByTitleWithIdNotEqualsNotFound();
        mockSaveAndFlush();
        TodoDTO result = service.update(dto.getId(), dto);

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .comparingOnlyFields("title", "description")
                .isEqualTo(dto);
    }


    @Test
    void updateWithBadIdShouldThrowException() {
        mockFindByIdNotFound();
        assertThrows(
                ItemNotFoundException.class,
                () -> service.update(randomId, dto)
        );
    }

    @Test
    void updateWithExistingTitleShouldThrowException() {
        mockFindById();
        mockFindByTitleWithIdNotEquals();
        String id = dto.getId();
        assertThrows(
                ItemExistsException.class,
                () -> service.update(id, dto)
        );
    }


    @Test
    void findAllShouldReturnListOfTodos() {
        mockFindAll();
        final List<TodoDTO> all = service.findAll();
        assertThat(all)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(dto);
    }

    @Test
    void findAllPageableShouldReturnPageOfTotos() {
        mockFindAllPageable();
        final Page<TodoDTO> all = service.findAll(PageRequest.of(0, 10));
        assertThat(all)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(dto);
    }

    @Test
    void findByIdShouldReturnTodo() {
        mockFindById();
        final Optional<TodoDTO> optional = service.findById(dto.getId());
        assertThat(optional)
                .isNotNull()
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(dto);
    }

    @Test
    void findByIdWithBadIdShouldReturnNoResult() {
        mockFindByIdNotFound();
        assertThatThrownBy(() -> service.findById(randomId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining(ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, randomId));
    }

    @Test
    void deleteShouldDeleteTodo() {
        mockFindById();
        mockDeleteById();
        assertThatCode(() -> service.delete(dto.getId())).doesNotThrowAnyException();
    }


    @Test
    void deleteWithBadIdShouldThrowException() {
        mockFindByIdNotFound();
        assertThatThrownBy(() -> service.delete(randomId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining(ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, randomId));

    }


    @Test
    void deleteAllShouldDeleteTodo() {
        mockDeleteAll();
        assertThatCode(() -> service.deleteAll()).doesNotThrowAnyException();
    }

    @Test
    void completeShouldBeCompleted() {
        mockFindById();
        mockSaveAndFlush();
        TodoDTO completed = service.complete(dto.getId());
        assertThat(completed.isCompleted()).isTrue();
    }

    @Test
    void completeWithBadIdShouldThrowException() {
        mockFindByIdNotFound();
        assertThatThrownBy(() -> service.complete(randomId))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining(ItemNotFoundException.format(ItemNotFoundException.TODO_BY_ID, randomId));
    }


}