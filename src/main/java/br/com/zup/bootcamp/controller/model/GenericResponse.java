package br.com.zup.bootcamp.controller.model;

public class GenericResponse {
    private String message;
    private int status;

    public GenericResponse(String message, int status){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}