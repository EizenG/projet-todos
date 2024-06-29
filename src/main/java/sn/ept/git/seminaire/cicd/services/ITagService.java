package sn.ept.git.seminaire.cicd.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ept.git.seminaire.cicd.models.TagDTO;

import java.util.List;
import java.util.Optional;

public interface ITagService {

    TagDTO save(TagDTO dto);

    void delete(String id);

    Optional<TagDTO> findById(String id);

    List<TagDTO> findAll();

    Page<TagDTO> findAll(Pageable pageable);

    TagDTO update(String id, TagDTO dto);

    void deleteAll() ;

}