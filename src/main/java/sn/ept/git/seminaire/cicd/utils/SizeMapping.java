package sn.ept.git.seminaire.cicd.utils;

import lombok.experimental.UtilityClass;

/**
 * @author ISENE
 */
@UtilityClass
public final class SizeMapping {


    @UtilityClass
    public static final class Name {
        public static final int MIN = 2;
        public static final int MAX = 50;
    }

    @UtilityClass
    public static final class Description {
        public static final int MIN = 0;
        public static final int MAX = 255;
    }

    @UtilityClass
    public static final class Title {
        public static final int MIN = 2;
        public static final int MAX = 80;
    }
}