package Exception;

public class AppointmentException extends Exception {
    public AppointmentException() {
        super("There is another appointment at the same time");
    }
}
