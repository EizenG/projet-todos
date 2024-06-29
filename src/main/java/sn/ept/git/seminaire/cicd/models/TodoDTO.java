package sn.ept.git.seminaire.cicd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class TodoDTO extends BaseDTO {

    @NotBlank
    @Size(min = SizeMapping.Title.MIN, max = SizeMapping.Title.MAX)
    private String title;

    @Size(min = SizeMapping.Description.MIN, max = SizeMapping.Description.MAX)
    private String description;

    private boolean completed;

    @JsonIgnore
    private Set<TagDTO> tags ;
}