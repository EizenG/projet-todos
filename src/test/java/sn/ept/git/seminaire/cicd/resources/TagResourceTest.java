package sn.ept.git.seminaire.cicd.resources;

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
import sn.ept.git.seminaire.cicd.data.TagDTOTestData;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.services.impl.TagServiceImpl;
import sn.ept.git.seminaire.cicd.utils.TestUtil;
import sn.ept.git.seminaire.cicd.utils.UrlMapping;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TagResource.class)
@DisplayNameGeneration(ReplaceCamelCase.class)
class TagResourceTest {

        @Autowired
        protected MockMvc mockMvc;

        @SuppressWarnings(value = {"unused"})
        @Autowired
        private TagResource tagResource;

        @MockBean
        private TagServiceImpl service;

        private TagDTO dto;
        private TagDTO defaultDto;

        @BeforeEach
        void beforeEach() {
                service.deleteAll();
                defaultDto = TagDTOTestData.defaultDTO();
                dto = TagDTO.builder()
                                .id(defaultDto.getId())
                                .name(defaultDto.getName())
                                .description(defaultDto.getDescription())
                                .createdDate(defaultDto.getCreatedDate())
                                .version(defaultDto.getVersion())
                                .lastModifiedDate(defaultDto.getLastModifiedDate())
                                .build();
        }

        @Test
        void findAll_NoTagsFound_ShouldReturnsEmptyList() throws Exception {
                Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                                .thenReturn(new PageImpl<>(Collections.emptyList()));

                mockMvc.perform(get(UrlMapping.Tag.ALL)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(0)));
        }

        @Test
        void findAll_WhenTagsExist_ShouldReturnListOfTodos() throws Exception {
                List<TagDTO> listOfTag = List.of(dto);
                Mockito.when(service.findAll(Mockito.any(Pageable.class)))
                                .thenReturn(new PageImpl<>(listOfTag));

                mockMvc.perform(get(UrlMapping.Tag.ALL)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(listOfTag.size())))
                                .andExpectAll(
                                        jsonPath("$.content.[0].id").exists(),
                                        jsonPath("$.content.[0].name", is(dto.getName())),
                                        jsonPath("$.content.[0].description", is(dto.getDescription())),
                                        jsonPath("$.content.[0].version", is(dto.getVersion())),
                                        jsonPath("$.content.[0].created_date").value(dto.getCreatedDate().toString()),
                                        jsonPath("$.content.[0].last_modified_date").value(dto.getLastModifiedDate().toString())

                                );
        }

        @Test
        void findById_TagExists_ShouldReturnTag() throws Exception {
                Mockito.when(service.findById(Mockito.anyString()))
                       .thenReturn(Optional.of(dto));

                mockMvc.perform(get(UrlMapping.Tag.FIND_BY_ID, dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpectAll(
                                jsonPath("$.id").exists(),
                                jsonPath("$.name", is(dto.getName())),
                                jsonPath("$.description", is(dto.getDescription())),
                                jsonPath("$.version", is(dto.getVersion())),
                                jsonPath("$.created_date").value(dto.getCreatedDate().toString()),
                                jsonPath("$.last_modified_date").value(dto.getLastModifiedDate().toString())
                        );
        }

        @Test
        void findById_TagDoesNotExist_ShouldReturnNotFound() throws Exception {
                Mockito.when(service.findById(Mockito.anyString()))
                        .thenReturn(Optional.empty());

                mockMvc.perform(get(UrlMapping.Tag.FIND_BY_ID, "bad id")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
        }

        @Test
        void create_WhenValidTagDTOProvided_ShouldReturnCreated() throws Exception {
                Mockito.when(service.save(Mockito.any(TagDTO.class)))
                        .thenReturn(dto);
                
                mockMvc.perform(post(UrlMapping.Tag.ADD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.name").value(dto.getName()))
                        .andExpect(jsonPath("$.description").value(dto.getDescription()));
        }

        @Test
        void create_WhenNameOfProvidedTagDTOAlreadyExist_ShouldReturnConflict() throws Exception {
                Mockito.when(service.save(Mockito.any(TagDTO.class))).thenThrow(new ItemExistsException("Tag already exists"));

                mockMvc.perform(post(UrlMapping.Tag.ADD)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(dto)))
                                .andExpect(status().isConflict());
        }


        @Test
        void delete_WhenTagExists_ShouldReturnNoContent() throws Exception {
                Mockito.doNothing().when(service).delete(Mockito.anyString());

                mockMvc.perform(delete(UrlMapping.Tag.DELETE, dto.getId())
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());
        }

        @Test
        void delete_WhenTagDoesNotExist_ShouldReturnNotFound() throws Exception {
                Mockito.doThrow(new ItemNotFoundException(
                                ItemNotFoundException.format(ItemNotFoundException.TAG_BY_ID, "bad id")))
                                .when(service).delete(Mockito.anyString());
                
                mockMvc.perform(delete(UrlMapping.Tag.DELETE, "bad id")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void update_WhenTagExists_ShouldReturnUpdatedTag() throws Exception {
                Mockito.when(service.update(Mockito.anyString(), Mockito.any(TagDTO.class)))
                       .thenReturn(dto);
                
                mockMvc.perform(put(UrlMapping.Tag.UPDATE, dto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(defaultDto)))
                                .andExpect(status().isAccepted())
                                .andExpect(jsonPath("$.id").value(dto.getId()))
                                .andExpect(jsonPath("$.name").value(dto.getName()))
                                .andExpect(jsonPath("$.description").value(dto.getDescription()));
        }


        @Test
        void update_WhenTagDoesNotExist_ShouldReturnNotFound() throws Exception {
                Mockito.when(service.update(Mockito.anyString(), Mockito.any(TagDTO.class)))
                        .thenThrow(new ItemNotFoundException(
                                                ItemNotFoundException.format(ItemNotFoundException.TAG_BY_ID, "bad id")));
                
                mockMvc.perform(put(UrlMapping.Tag.UPDATE, "bad id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(defaultDto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void update_WhenTagNameExists_ShouldReturnConflict() throws Exception {
                Mockito.when(service.update(Mockito.anyString(), Mockito.any(TagDTO.class)))
                        .thenThrow(new ItemExistsException(
                                                ItemExistsException.TITLE_EXISTS));
                
                mockMvc.perform(put(UrlMapping.Tag.UPDATE, dto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(defaultDto)))
                                .andExpect(status().isConflict());
        }

}