package sn.ept.git.seminaire.cicd.utils;

import lombok.experimental.UtilityClass;

/**
 * @author ISENE
 */
@UtilityClass
public final class UrlMapping {

    public static final String BASE = "/api/";
    public static final String ID = "/{id}";


    @UtilityClass
    public static final class Todo {

        public static final String BASE = UrlMapping.BASE + "todos";
        public static final String ALL = Todo.BASE;
        public static final String FIND_BY_ID = Todo.BASE + ID;
        public static final String ADD = Todo.BASE;
        public static final String UPDATE = Todo.FIND_BY_ID;
        public static final String DELETE = Todo.FIND_BY_ID;
        public static final String COMPLETE = Todo.FIND_BY_ID+"/complete";
    }
    @UtilityClass
    public static final class Tag {

        public static final String BASE = UrlMapping.BASE + "tags";
        public static final String ALL = Tag.BASE;
        public static final String FIND_BY_ID = Tag.BASE + ID;
        public static final String ADD = Tag.BASE;
        public static final String UPDATE = Tag.FIND_BY_ID;
        public static final String DELETE = Tag.FIND_BY_ID;
    }


    public static final class Default {

        private Default() {
            super();
        }

        public static final String HEALTH = "/_health";
        public static final String INDEX = "/";
    }



}