package exceptions;

import javax.swing.table.TableStringConverter;
import java.lang.annotation.Native;

public class BusinessLogicException extends RuntimeException {
  private String message;

  public BusinessLogicException(String message) {
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

