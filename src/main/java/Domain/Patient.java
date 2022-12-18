package Domain;

import java.io.Serializable;
import java.util.Objects;


public class Patient implements Identifiable<Integer>, Serializable {

    public static final long serialVersionUID = 1L;
    private String firstname;
    private String surname;
    private  int PatientId;

    @Override
    public Integer getId() {
        return this.PatientId;
    }

    @Override
    public void setID(Integer id) {
        PatientId = id;
    }

    public Patient(int patientId, String firstname, String surname) {
        this.firstname = firstname;
        this.surname = surname;
        PatientId = patientId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "PatientId=" + PatientId +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return PatientId == patient.PatientId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PatientId);
    }
}
