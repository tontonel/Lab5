package Service;
import Domain.Appointment;
import Persistancy.AbstractRepository;
import Domain.Patient;
import Persistancy.Repository;
import Utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import Exception.*;

public class Service {
    private Repository _repo;

    public Service(Repository _repo) {
        this._repo = _repo;
    }

    public ArrayList<Appointment> getAppointmentsForOneDay(Date date) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        for(Object appointmentList : _repo.getAll().values()) {
            for(Appointment appointment : (ArrayList<Appointment>)appointmentList)
                if(DateUtils.isSameDay(appointment.getAppointmentDate(), date))
                    appointments.add(appointment);
        }
        return appointments;
    }

    public void addPatient(Patient patient, Appointment appointment) throws NoIdenticEntitiesException, IOException {
        _repo.addEntity(patient, appointment);
    }

    public void updatePatient(int id, Appointment appointment) throws NoEntityFound, IOException, NoIdenticEntitiesException {
        _repo.updateEntity(id, appointment);
    }

    public boolean deletePatient(Patient patient, Appointment appointment) throws IOException {
        return _repo.deleteEntity(patient, appointment);
    }

    public ArrayList<Appointment> getAppointmentsFromID(int id) throws NoEntityFound {
        return _repo.findListByID(id);
    }
    public HashMap<Patient, ArrayList<Appointment>> getAll() {
        return _repo.getAll();
    }

    public ArrayList<Appointment> sortAfterDate() {
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) _repo.getAll().values();
        Collections.sort(appointments);
        return appointments;
    }

}
