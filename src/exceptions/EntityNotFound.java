package exceptions;

public class EntityNotFound extends RuntimeException {
    private String message;
    public EntityNotFound(String message) {
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
