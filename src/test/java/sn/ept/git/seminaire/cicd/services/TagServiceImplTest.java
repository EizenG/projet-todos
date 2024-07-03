package sn.ept.git.seminaire.cicd.services;


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
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.mappers.TagMapper;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;
import sn.ept.git.seminaire.cicd.services.impl.TagServiceImpl;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
public class TagServiceImplTest {
  @Mock
  TagRepository tagRepository;
  @InjectMocks
  TagServiceImpl service;

  private static TagMapper mapper;

  private TagDTO tagDTO;
  @SuppressWarnings("unused")
  private Tag tag;

  private String randomId;

  @BeforeAll
  static void beforeAll() {
        mapper = Mappers.getMapper(TagMapper.class);
    }

  @BeforeEach
  void beforeEach() {
    ReflectionTestUtils.setField(service, "mapper", mapper);
    tagDTO = TagDTOTestData.defaultDTO();
    tag = mapper.toEntity(tagDTO);
    randomId = UUID.randomUUID().toString();
  }

  private void mockSaveAndFlush() {
    Mockito.when(tagRepository.saveAndFlush(Mockito.any(Tag.class))).then(invocation -> {
      Instant now = Instant.now();
      Tag tag = invocation.getArgument(0, Tag.class);
      tag.setId(Optional.ofNullable(tag.getId()).orElse(randomId));
      tag.setCreatedDate(Optional.ofNullable(tag.getCreatedDate()).orElse(now));
      tag.setLastModifiedDate(now);
      return tag;
    });
  }

  private void mockFindByNameNotFound(){
    Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
  }

  private void mockFindByNameFound() {
    Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(tag));
  }

  private void mockFindByIdFound(){
    Mockito.when(tagRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tag));
  }

  private void mockFindByIdNotFound() {
    Mockito.when(tagRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.empty());
  }

  private void mockDeleteById(){
    Mockito.doNothing().when(tagRepository).deleteById(Mockito.anyString());
  }

  private void mockFindAll() {
    Mockito.when(tagRepository.findAll())
        .thenReturn(List.of(tag));
  }

  private void mockFindAllNoTagExist() {
    Mockito.when(tagRepository.findAll())
        .thenReturn(Collections.emptyList());
  }

  private void mockFindAllPageable() {
        Mockito.when(tagRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(tag)));

  }
  
  private void mockFindAllPageableNoTagExist() {
    Mockito.when(tagRepository.findAll(Mockito.any(PageRequest.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));

  }
  
  private void mockFindByNameWithIdNotEqualsNotFound() {
    Mockito.when(tagRepository.findByNameWithIdNotEquals(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Optional.empty());
  }

  private void mockFindByNameWithIdNotEqualsFound() {
    Mockito.when(tagRepository.findByNameWithIdNotEquals(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Optional.of(tag));
  }

  @Test
  public void save_WhenTagDoesNotExist_ShouldSaveTagSuccessfully() {
        mockFindByNameNotFound();
        mockSaveAndFlush();

        TagDTO savedTag = service.save(tagDTO);

        assertThat(savedTag)
          .isNotNull()
          .hasNoNullFieldsOrProperties()
        ;
    }

  @Test
  public void save_WhenTagExists_ShouldThrowItemExistsException(){
    mockFindByNameFound();

    assertThrows(ItemExistsException.class, () -> service.save(tagDTO));
  }

  @Test
  public void delete_WhenTagExists_ShouldDeleteTagSuccessfully() {
    mockFindByIdFound();
    mockDeleteById();

    assertThatCode(() -> service.delete(tagDTO.getId())).doesNotThrowAnyException();
  }

  @Test
  public void delete_WhenTagDoesNotExist_ShouldThrowItemNotFoundException() {
    mockFindByIdNotFound();

    assertThatThrownBy(() -> service.delete(tagDTO
        .getId()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessageContaining(ItemNotFoundException.format(ItemNotFoundException.TAG_BY_ID, tagDTO.getId()));
  }

  @Test
  public void findById_WhenTagExists_ShouldReturnTagDTO() {
    mockFindByIdFound();

    final Optional<TagDTO> optional = service.findById(tagDTO.getId());

    assertThat(optional)
          .isNotNull()
          .isPresent()
          .get()
          .usingRecursiveComparison()
          .isEqualTo(tagDTO);
  }

   @Test
    public void findById_WhenTagDoesNotExist_ShouldReturnEmptyOptional() {
        mockFindByIdNotFound();

        final Optional<TagDTO> optional = service.findById(tagDTO.getId());

        assertThat(optional)
            .isNotNull()
            .isEmpty();
    }

    @Test
    public void findAll_WhenAtLeastOneTagExist_ShouldReturnListOfTag() {
      mockFindAll();

      final List<TagDTO> allTagDTO = service.findAll();

      assertThat(allTagDTO)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1)
            .contains(tagDTO);
    }

    @Test
    public void findAll_WhenNoTagExist_ShouldReturnEmptyList() {
      mockFindAllNoTagExist();

      final List<TagDTO> allTagDTO = service.findAll();

      assertNotNull(allTagDTO);
      assertThat(allTagDTO).isEmpty();
    }

    @Test
    public void findAllPageable_WhenAtLeastOneTagExist_ShouldReturnPageOfTags() {
      mockFindAllPageable();

      final Page<TagDTO> pageTags = service.findAll(PageRequest.of(0, 5));

      assertThat(pageTags)
          .isNotNull()
          .isNotEmpty()
          .hasSize(1)
          .contains(tagDTO);
    }

    @Test
    public void findAllPageable_WhenNoTagExist_ShouldReturnEmptyPage() {
      mockFindAllPageableNoTagExist();

      final Page<TagDTO> pageTags = service.findAll(PageRequest.of(3, 5));

      assertNotNull(pageTags);
      assertThat(pageTags.getContent()).isEmpty();
      assertThat(pageTags.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void update_WhenTagExistsAndNoDuplicateName_ShouldUpdateAndReturnTagDTO() {
      mockFindByIdFound();
      mockFindByNameWithIdNotEqualsNotFound();
      mockSaveAndFlush();

      TagDTO result = service.update(tagDTO.getId(), tagDTO);

      assertThat(result)
          .isNotNull()
          .usingRecursiveComparison()
          .comparingOnlyFields("name", "description")
          .isEqualTo(tagDTO);
    }

    @Test
    public void update_WhenTagDoesNotExist_ShouldThrowItemNotFoundException() {
      mockFindByIdNotFound();

      assertThrows(
          ItemNotFoundException.class,
          () -> service.update(tagDTO.getId(), tagDTO));
    }

    @Test
    public void update_WhenDuplicateTagNameExists_ShouldThrowItemExistsException() {
      mockFindByIdFound();
      mockFindByNameWithIdNotEqualsFound();

      assertThrows(
          ItemExistsException.class,
          () -> service.update(tagDTO.getId(), tagDTO));
    }

    @Test
    void deleteAllShouldDeleteTags() {
      Mockito.doNothing().when(tagRepository).deleteAll();

      assertThatCode(() -> service.deleteAll()).doesNotThrowAnyException();
    }


}
