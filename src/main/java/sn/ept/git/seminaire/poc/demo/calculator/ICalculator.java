package sn.ept.git.seminaire.poc.demo.calculator;

import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.util.List;

public interface ICalculator {

    double add (double a,double b);
    double subtract(double a, double b);
    double multiply (double a,double b);

    double divide (double a,double b) throws DivisionByZeroException;

    /**
     *Calcule la somme 1+2+...+n
     *
     * @param n le dernier terme de la somme
     * @return le resultat du calcul
     */
    long somme (long n);

    long fact (long n);

    double moyenne (List<Double> valeurs);

     int getRandom(int min, int max);

}
