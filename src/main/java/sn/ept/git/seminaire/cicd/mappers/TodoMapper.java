package sn.ept.git.seminaire.cicd.mappers;

import org.mapstruct.Mapper;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.models.TodoDTO;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    Todo toEntity(TodoDTO dto);

    TodoDTO toDTO(Todo todo);

    default List<Todo> toEntitiesList(List<TodoDTO> todoDTOList) {
        return Optional
                .ofNullable(todoDTOList)
                .orElse(List.of())
                .stream().map(this::toEntity).toList();
    }

    default List<TodoDTO> toDTOlist(List<Todo> todoList) {
        return Optional
                .ofNullable(todoList)
                .orElse(List.of())
                .stream().map(this::toDTO).toList();
    }

}