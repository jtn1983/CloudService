package ru.tenilin.cloudservice.Exeptions;

public class InvalidCredentials extends RuntimeException{
    public InvalidCredentials(String msg){
        super(msg);
    }
}
