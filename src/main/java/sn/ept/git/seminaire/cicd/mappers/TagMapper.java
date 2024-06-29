package sn.ept.git.seminaire.cicd.mappers;

import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.entities.Tag;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toEntity(TagDTO dto);

    TagDTO toDTO(Tag tag);

    default List<Tag> toEntitiesList(List<TagDTO> tagDTOList) {
        return Optional
                .ofNullable(tagDTOList)
                .orElse(List.of())
                .stream().map(this::toEntity).toList();
    }

    default List<TagDTO> toDTOlist(List<Tag> tagList) {
        return Optional
                .ofNullable(tagList)
                .orElse(List.of())
                .stream().map(this::toDTO).toList();
    }

}