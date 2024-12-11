package exceptions;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    private String message;

    public DatabaseException(String message) {
        super(message);
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
