package net.ausiasmarch.pruebasb.bean;

public class CalculadoraResponse {

      
    private double result;
    private String message;
    private int code;

    public CalculadoraResponse(double result, String message, int code) {
        this.result = result;
        this.message = message;
        this.code = code;
    }

    public double getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    
}
