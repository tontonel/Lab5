package Exception;

public class NoEntityFound extends Exception{

    public NoEntityFound(String entityId) {
        super("The entity with id: " + entityId + " was not found in the list");
    }
}
