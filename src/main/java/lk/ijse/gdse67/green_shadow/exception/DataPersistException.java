package lk.ijse.gdse67.green_shadow.exception;

public class DataPersistException extends RuntimeException{
    public DataPersistException() {
        super();
    }

    public DataPersistException(String message) {
        super(message);
    }

    public DataPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
