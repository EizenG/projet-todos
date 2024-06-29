package sn.ept.git.seminaire.cicd.data;

import org.apache.commons.lang3.RandomStringUtils;
import sn.ept.git.seminaire.cicd.utils.SizeMapping;

import java.time.Instant;
import java.util.UUID;

public class TestData {

    public TestData(){}

    public static final class Default {
        private Default() {
        }

        public static final String id = UUID.randomUUID().toString();
        public static final Instant createdDate = Instant.now();
        public static final Instant lastModifiedDate = Instant.now();
        public static final int version = 0;
        public static final String title = RandomStringUtils.randomAlphanumeric((SizeMapping.Title.MIN + SizeMapping.Title.MAX) / 2);
        public static final String name = RandomStringUtils.randomAlphanumeric((SizeMapping.Name.MIN + SizeMapping.Name.MAX) / 2);
        public static final String description = RandomStringUtils.randomAlphanumeric((SizeMapping.Description.MIN + SizeMapping.Description.MAX) / 2);
        public static final boolean notCompleted = false;
    }


    public static final class Update {
        private Update(){}
        public static final UUID id = UUID.randomUUID();
        public static final Instant createdDate = Instant.now();
        public static final Instant lastModifiedDate = Instant.now();
        public static final int version = 2;
        public static final  String title = RandomStringUtils.randomAlphanumeric( (SizeMapping.Title.MIN+SizeMapping.Title.MAX)/2);
        public static final  String name = RandomStringUtils.randomAlphanumeric( (SizeMapping.Name.MIN+SizeMapping.Name.MAX)/2);
        public static final  String description=  RandomStringUtils.randomAlphanumeric( (SizeMapping.Description.MIN+SizeMapping.Description.MAX)/2);
    }

}
