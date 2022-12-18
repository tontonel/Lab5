package Persistancy;

import Domain.Appointment;
import Domain.Identifiable;
import Domain.Patient;

import java.util.HashMap;

public class PatientsRepository extends AbstractRepository<Patient, Appointment, Integer> {
    public PatientsRepository(HashMap entityMap) {
        super(entityMap);
    }
}
