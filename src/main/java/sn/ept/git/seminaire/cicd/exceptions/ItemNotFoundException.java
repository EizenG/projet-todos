package sn.ept.git.seminaire.cicd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ISENE
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundException  extends RuntimeException {

    public static final String DEFAUL_MESSAGE="Impossible de retrouver l'élément recherché";
    public static final String TAG_BY_ID ="Impossible de retrouver un tag avec l'identifiant <%s>";
    public static final String TODO_BY_ID ="Impossible de retrouver  une todo avec l'identifiant <%s>";

    public ItemNotFoundException() {
        super(DEFAUL_MESSAGE);
    }
    public ItemNotFoundException(String message) {
        super(message);
    }

    public static String format(String template, String ...args) {
        return String.format(template,args);
    }


    }
