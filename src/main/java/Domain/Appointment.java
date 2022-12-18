package Domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Appointment implements Identifiable<Integer>, Comparable<Appointment>, Serializable {

    public static final long serialVersionUID = 1L;
    private int appointmentID;
    private Date appointmentDate;

    public int getAppointmentID() {
        return appointmentID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Appointment(int appointmentID, Date appointmentDate) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
    }

    @Override
    public Integer getId() {
        return appointmentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        boolean dateEqual = appointmentDate.equals(that.appointmentDate);
        return appointmentID == that.appointmentID || dateEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentDate);
    }

    @Override
    public void setID(Integer id) {
        this.appointmentID = id;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID=" + appointmentID +
                ", appointmentDate=" + appointmentDate +
                '}';
    }

    @Override
    public int compareTo(Appointment o) {
        return this.appointmentDate.compareTo(o.getAppointmentDate());
    }
}
