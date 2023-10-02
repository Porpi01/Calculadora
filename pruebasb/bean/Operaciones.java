package net.ausiasmarch.pruebasb.bean;

public class Operaciones {
    public double sumar(double operando1, double operando2) {
        return operando1 + operando2;
    }

    public double restar(double operando1, double operando2) {
        return operando1 - operando2;
    }

   public double multiplicar(double operando1, double operando2) {
        return operando1 * operando2;
    }

    public double dividir (double operando1, double operando2) {
        if (operando2 == 0) {
            throw new ArithmeticException("No se puede dividir por 0");
        }
        return operando1 / operando2;
    }
    
}
