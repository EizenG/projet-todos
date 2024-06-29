package sn.ept.git.seminaire.cicd.repositories;

import sn.ept.git.seminaire.cicd.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
    //JPA et Spring Data

    //repository, DAL=Data Access Layer
    @Query("select s from Todo  s where s.title=:title")
    Optional<Todo> findByTitle(@Param("title") String title);

    @Query("select s from Todo  s where s.title=:title and s.id<>:id")
    Optional<Todo> findByTitleWithIdNotEquals(@Param("title") String title, @Param("id")  String id);

}
