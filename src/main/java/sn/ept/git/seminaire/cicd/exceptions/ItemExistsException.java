package sn.ept.git.seminaire.cicd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ISENE
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemExistsException extends RuntimeException {

    public static final String NAME_EXISTS="Le nom <%s> existe déjà ";
    public static final String TITLE_EXISTS="Le titre <%s> existe déjà ";

    public ItemExistsException(String message) {
        super(message);
    }

    public static String format(String template, String ...args) {
        return String.format(template,args);
    }
}
