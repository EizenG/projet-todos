package sn.ept.git.seminaire.poc.demo;

import java.security.SecureRandom;

public  class CurrencyService {

    SecureRandom r = new SecureRandom();

    public double convert(Currency from, Currency to, double value){
        //appel du WS, valeur aléatoire volontaire
        return from.equals(to) ? value : r.nextDouble()*value;
    }

}
