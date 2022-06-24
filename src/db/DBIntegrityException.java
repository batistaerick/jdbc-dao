package db;

public class DBIntegrityException extends RuntimeException {

    public DBIntegrityException(Throwable throwable) {
        super(throwable);
    }

    public DBIntegrityException(String msg) {
        super(msg);
    }

}
