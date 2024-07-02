package sn.ept.git.seminaire.cicd.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;
import sn.ept.git.seminaire.cicd.services.ITagService;
import sn.ept.git.seminaire.cicd.utils.ExceptionUtils;
import sn.ept.git.seminaire.cicd.utils.LogUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TagServiceImpl implements ITagService {

    private final TagRepository repository;
    private final TagMapper mapper;
    public static final String CLASS_NAME="TagServiceImpl";


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public TagDTO save(TagDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "save");
        final Optional<Tag> optional = repository.findByName(dto.getName());
        ExceptionUtils.absentOrThrow(optional, ItemExistsException.NAME_EXISTS, dto.getName());
        dto.setId(UUID.randomUUID().toString());
        return
                mapper.toDTO(
                        repository
                                .saveAndFlush(mapper.toEntity(dto)
                                )
                );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(String uuid) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "delete");
        final Optional<Tag> optional = repository.findById(uuid);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException(
                    ItemNotFoundException.format(ItemNotFoundException.TAG_BY_ID, uuid)
            );
        }
        repository.deleteById(uuid);

    }

    @Override
    public Optional<TagDTO> findById(String uuid) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findById");
        return repository
                .findById(uuid)
                .map(mapper::toDTO);
    }

    @Override
    public List<TagDTO> findAll() {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll");
        return repository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "findAll[Pageable]");
        return repository
                .findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public TagDTO update(String uuid, TagDTO dto) {
        log.info(LogUtils.LOG_START,CLASS_NAME, "update");
        Optional<Tag>  optional = repository.findById(uuid);
        ExceptionUtils.presentOrThrow(optional, ItemNotFoundException.TODO_BY_ID, dto.getId());
        Optional<Tag>  optionalTitle = repository.findByNameWithIdNotEquals(dto.getName(),uuid);
        ExceptionUtils.absentOrThrow(optionalTitle, ItemExistsException.TITLE_EXISTS, dto.getName());
        final Tag item = optional.get();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        return mapper.toDTO(repository.saveAndFlush(item));
    }

    @Override
    public void deleteAll() {
        log.info(LogUtils.LOG_START, CLASS_NAME, "deleteAll");
        repository.deleteAll();
    }
    

}
