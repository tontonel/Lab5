package Tests;

import Domain.Appointment;
import Utils.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.Date;

public class AppointmentTest {

    private Appointment appointment;

    @BeforeEach
    public void setUp() throws ParseException {
        Date date = new DateFormatter("12 12 12").getDate();
        appointment = new Appointment(1, date);
    }

    @Test
    public void get() throws ParseException {
        assert appointment.getAppointmentID() == 1;
        assert appointment.getAppointmentDate().equals(new DateFormatter("12 12 12").getDate());
    }
    @Test
    public void set() throws ParseException {
        appointment.setID(4);
        assert appointment.getAppointmentID() == 4;
        appointment.setAppointmentDate(new DateFormatter("12 10 10").getDate());
        assert appointment.getAppointmentDate().equals(new DateFormatter("12 10 10").getDate());
    }

}
