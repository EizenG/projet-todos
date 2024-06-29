package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.ReplaceCamelCase;
import sn.ept.git.seminaire.cicd.data.TodoDTOTestData;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.mappers.TodoMapper;
import sn.ept.git.seminaire.cicd.entities.Todo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
class TodoDTOMapperTest {

    TodoDTO dto;
    Todo entity;

    private final TodoMapper mapper = Mappers.getMapper(TodoMapper.class);

    @BeforeEach
    void setUp() {
        dto = TodoDTOTestData.defaultDTO();
        entity = mapper.toEntity(dto);
    }

    @Test
    void toEntityShouldReturnCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(entity)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(dto);
    }

    @Test
    void toDTOShouldReturnCorrectDTO() {
        dto = mapper.toDTO(entity);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .ignoringFieldsMatchingRegexes("^_")//just to discover
                .withEqualsForFields((idOne, idTwo) -> {
                    return idOne instanceof String && idTwo.toString().equalsIgnoreCase(idOne.toString());
                }, "id") //just to discover
                .isEqualTo(entity);

    }


    @Test
    void toEntitiesShouldMapToCorrectEntites() {
        List<Todo> entitiesList = mapper.toEntitiesList(List.of(dto));
        assertThat(entitiesList)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(List.of(dto));
    }

    @Test
    void toDTOsShouldMapToCorrectDTOs() {
        List<TodoDTO> todoDTOList = mapper.toDTOlist(List.of(entity));
        assertThat(todoDTOList)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("tags")
                .isEqualTo(List.of(entity));

    }
}
