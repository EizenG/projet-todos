package sn.ept.git.seminaire.cicd.resources;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.ept.git.seminaire.cicd.ReplaceCamelCase;
import sn.ept.git.seminaire.cicd.data.TestData;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.services.impl.TodoServiceImpl;
import sn.ept.git.seminaire.cicd.utils.TestUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(TodoResource.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
class TodoResourceTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TodoServiceImpl service;

    @Autowired
    private TodoResource todoResource;

    private TodoDTO dto;
    private TodoDTO vm;

    @BeforeEach
    void beforeEach() {
        service.deleteAll();
        vm = TodoDTOTestData.defaultDTO();
        dto = TodoDTO.builder()
                .id(vm.getId())
                .title(vm.getTitle())
                .description(vm.getDescription())
                .completed(vm.isCompleted())
                .createdDate(vm.getCreatedDate())
                .lastModifiedDate(vm.getLastModifiedDate())
                .build();
    }


    @Test
    void findAll_shouldReturnTodos() throws Exception {
        List<TodoDTO> list = List.of(dto);
        Mockito.when(service.findAll(Mockito.any()))
                .thenReturn(new PageImpl<>(list));
        int size = list.size();
        mockMvc.perform(get(UrlMapping.Todo.ALL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(size)))
                .andExpect(jsonPath("$.content.[0].id").exists())
                .andExpect(jsonPath("$.content.[0].version").exists())
                .andExpect(jsonPath("$.content.[0].title", is(dto.getTitle())))
                .andExpect(jsonPath("$.content.[0].description").value(dto.getDescription()));

    }

    @Test
    void findById_shouldReturnTodo() throws Exception {
        Mockito.when(service.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(dto));

        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID, dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void findById_withBadId_shouldReturnNotFound() throws Exception {
        Mockito.when(service.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(UrlMapping.Todo.FIND_BY_ID, UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void add_shouldCreateTodo() throws Exception {
        Mockito.when(service.save(Mockito.any()))
                .thenAnswer(args -> {
                    TodoDTO todoDTO = args.getArgument(0, TodoDTO.class);
                    todoDTO.setCompleted(false);
                    todoDTO.setCreatedDate(Instant.now());
                    todoDTO.setLastModifiedDate(Instant.now());
                    return todoDTO;
                });
        mockMvc.perform(post(UrlMapping.Todo.ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(vm.getTitle()))
                .andExpect(jsonPath("$.description").value(vm.getDescription()))
                .andExpect(jsonPath("$.completed", is(false)));
    }



    @Test
    void update_shouldUpdateTodo() throws Exception {
        Mockito.when(service.update(Mockito.any(), Mockito.any()))
                .thenAnswer(args -> {
                    TodoDTO todoDTO = args.getArgument(1, TodoDTO.class);
                    todoDTO.setId(args.getArgument(0, String.class));
                    todoDTO.setCreatedDate(Instant.now());
                    todoDTO.setLastModifiedDate(Instant.now());
                    return todoDTO;
                });
        mockMvc.perform(put(UrlMapping.Todo.UPDATE, dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vm)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.title").value(vm.getTitle()))
                .andExpect(jsonPath("$.description").value(vm.getDescription()));
    }

    @Test
    void delete_shouldDeleteTodo() throws Exception {
        Mockito.doNothing().when(service).delete(Mockito.any());
        mockMvc.perform(
                delete(UrlMapping.Todo.DELETE, dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    void delete_withBadId_shouldReturnNotFound() throws Exception {
        Mockito.doThrow(new ItemNotFoundException())
                .when(service).delete(Mockito.any());
        mockMvc.perform(delete(UrlMapping.Todo.DELETE, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void complete_shouldCompleteTodo() throws Exception {
        Mockito.when(service.complete(Mockito.any()))
                .thenAnswer(args -> {
                    vm.setCompleted(true);
                    return vm;
                });
        mockMvc.perform(put(UrlMapping.Todo.COMPLETE, vm.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    void complete_withBadId_shouldReturnNotFound() throws Exception {
        Mockito.when(service.complete(Mockito.any(String.class)))
                .thenThrow(new ItemNotFoundException());
        mockMvc.perform(put(UrlMapping.Todo.COMPLETE, UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}