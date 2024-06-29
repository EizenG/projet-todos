package sn.ept.git.seminaire.cicd.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import sn.ept.git.seminaire.cicd.ReplaceCamelCase;
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayNameGeneration(ReplaceCamelCase.class)
class TagDTOMapperTest {

    TagDTO dto;
    Tag entity;

    private final TagMapper mapper = Mappers.getMapper(TagMapper.class);


    @BeforeEach
    void setUp() {
        dto = TagDTOTestData.defaultDTO();
        entity = mapper.toEntity(dto);
    }

    @Test
    void toEntityShouldMapToCorrectEntity() {
        entity = mapper.toEntity(dto);
        assertThat(entity)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .isEqualTo(dto);
    }

    @Test
    void toDTOShouldMapToCorrectDTO() {
        dto = mapper.toDTO(entity);
        assertThat(dto)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .ignoringFieldsMatchingRegexes("^_")//just to discover
                .isEqualTo(entity);

    }


    @Test
    void toEntitiesShouldMapToCorrectEntites() {
        List<Tag> entitiesList = mapper.toEntitiesList(List.of(dto));
        assertThat(entitiesList)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .isEqualTo(List.of(dto));
    }

    @Test
    void toDTOsShouldMapToCorrectDTOs() {
        List<TagDTO> tagDTOList = mapper.toDTOlist(List.of(entity));
        assertThat(tagDTOList)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("todos")
                .isEqualTo(List.of(entity));

    }

}