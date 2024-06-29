package sn.ept.git.seminaire.tp;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Jury {



    // passe
    //  ne passe pas
    public boolean passe(double moyenne){
        return moyenne>=12;
    }


    //list null
    //list vide
    // note negative non acceptees
    //notes plus grandes que 20 non acceptees
    //cas normal : liste de notes => alors la bonne moyenne doit etre retournee
    public double obtenirMoyenne(List<Double> notes) {

      if (null == notes || notes.isEmpty()) {
            throw new IllegalArgumentException("Liste de notes invalide");
        }

        double somme = 0;
        for (Double note : notes) {
            if (note < 0) {
                throw new IllegalArgumentException("Les notes negatives ne sont pas permises");
            }
            if (note > 20) {
                throw new IllegalArgumentException("Les notes plus grandes que 20 ne sont pas permises");
            }
            somme += note;
        }
        return somme / notes.size();
    }
}
