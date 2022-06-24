package db;

public class DBException extends RuntimeException {

    public DBException(String msg) {
        super(msg);
    }

    public DBException(Throwable throwable) {
        super(throwable);
    }

}