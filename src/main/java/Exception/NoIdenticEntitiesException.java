package Exception;

public class NoIdenticEntitiesException extends Exception{

    public NoIdenticEntitiesException(String entityId) {
        super("There is another entity with same id in the list: " + entityId);
    }
}
