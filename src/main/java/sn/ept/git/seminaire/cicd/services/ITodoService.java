package sn.ept.git.seminaire.cicd.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.models.TodoDTO;

import java.util.List;
import java.util.Optional;


public interface ITodoService  {

    TodoDTO save(TodoDTO dto);

    void delete(String id);

    Optional<TodoDTO> findById(String id);

    List<TodoDTO> findAll();

    Page<TodoDTO> findAll(Pageable pageable);

    TodoDTO update(String id, TodoDTO dto);

    TodoDTO complete(String id);

     void deleteAll() ;
}
