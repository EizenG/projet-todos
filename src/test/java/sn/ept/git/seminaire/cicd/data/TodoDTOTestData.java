package sn.ept.git.seminaire.cicd.data;

import sn.ept.git.seminaire.cicd.models.TodoDTO;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TodoDTOTestData extends TestData {

    public static TodoDTO defaultDTO() {
        return TodoDTO
                .builder()
                .id(Default.id)
                .createdDate(Default.createdDate)
                .lastModifiedDate(Default.lastModifiedDate)
                .version(Default.version)
                .title(Default.title)
                .description(Default.description)
                .completed(Default.notCompleted)
                .tags(Stream.of(TagDTOTestData.defaultDTO()).collect(Collectors.toSet()))
                .build();
    }

    public static TodoDTO updatedDTO() {
        return TodoDTO
                .builder()
                .id(Default.id)
                .createdDate(Update.createdDate)
                .lastModifiedDate(Update.lastModifiedDate)
                .version(Update.version)
                .title(Update.title)
                .description(Update.description)
                .tags(Stream.of(TagDTOTestData.updatedDTO()).collect(Collectors.toSet()))
                .build();
    }
}
