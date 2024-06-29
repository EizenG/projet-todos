package sn.ept.git.seminaire.cicd.entities;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode (callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "acicd_todos")
@DynamicUpdate
public final class Todo extends BaseEntity implements Serializable {

    @NotBlank
    @Size(min = SizeMapping.Title.MIN, max = SizeMapping.Title.MAX)
    @Column(unique = true)
    private String title;

    @Size(min = SizeMapping.Description.MIN, max = SizeMapping.Description.MAX)
    private String description;

    private boolean completed;


    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "tags_todos",
            joinColumns = @JoinColumn(name = "id_todo"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<Tag> tags = new HashSet<>();
    


    @PrePersist
    void prepersit(){
         this.setCreatedDate(Instant.now());
         this.setLastModifiedDate(Instant.now());
    }

    @PreUpdate
    void preUpdate(){
        this.setLastModifiedDate(Instant.now());
    }

}
