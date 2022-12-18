package Tests;

import Domain.Appointment;
import Domain.Patient;
import Persistancy.PatientsRepository;

import Exception.*;

import Utils.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepositoryTest {
    PatientsRepository repo;

    @BeforeEach
    public void setUp() throws ParseException {
        HashMap<Patient, ArrayList<Appointment>> patientsMap = new HashMap<>();
        patientsMap.put(new Patient(1,"Ionut", "Pop"),
                new ArrayList<Appointment>(List.of(
                        new Appointment(1, new DateFormatter("12 12 2022 12").getDate())
                ))
        );
        patientsMap.put(new Patient(2, "Mihai", "Luca"),
                new ArrayList<Appointment>(List.of(
                        new Appointment(2, new DateFormatter("12 10 13").getDate())
                ))
        );
        patientsMap.put(new Patient(3, "Serban", "Andrei"),
                new ArrayList<Appointment>(List.of(
                        new Appointment(3, new DateFormatter("12 11 13").getDate())
                ))
        );
        repo = new PatientsRepository(patientsMap);
    }

    @Test
    public void add() throws ParseException, IOException, NoIdenticEntitiesException {
        repo.addEntity(new Patient(1,"Ionut", "Pop"), new Appointment(5, new DateFormatter("12 12 2022 14").getDate()));
        assert repo.getAll().values().size() == 3;
    }

    @Test
    public void delete() throws ParseException, IOException {
        repo.deleteEntity(new Patient(1,"Ionut", "Pop"), new Appointment(5, new DateFormatter("12 12 2022 14").getDate()));
        assert repo.getAll().values().size() == 3;

    }

    @Test
    public void update() throws ParseException, NoEntityFound, IOException, NoIdenticEntitiesException {
        repo.updateEntity(3, new Appointment(5, new DateFormatter("12 12 2022 14").getDate()));
        assert repo.findById(5).getKey().equals(new Patient(3, "Serban", "Andrei"));
    }

    @Test
    public void find() throws NoEntityFound {
        assert repo.findById(3).getKey().equals(new Patient(3, "Serban", "Andrei"));
    }

    @Test
    public void getAll() {
        assert repo.getAll().size() == 3;
    }
}
