package Persistancy;

import Domain.Appointment;
import Domain.Patient;
import java.text.DateFormat;
import Exception.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PatientsFileRepository extends AbstractMemoryRepository<Patient, Appointment,  Integer> {

    public PatientsFileRepository(String FileName) throws IOException, NoIdenticEntitiesException, ParseException {
        super(FileName);
    }

    @Override
    public void readFromFile() throws IOException, NoIdenticEntitiesException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        while((line = br.readLine()) != null) {
            String[] elems = line.split("[|]");
            if (elems.length < 5)
                continue;
            Patient patient = new Patient(Integer.parseInt(elems[0].strip()), elems[1].strip(), elems[2].strip());
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yy");
            Date date = format.parse(elems[4].strip());
            Appointment appointment = new Appointment(Integer.parseInt(elems[3].strip()), date);
            if(entityMap.containsKey(patient))
                entityMap.get(patient).add(appointment);
            else
                entityMap.put(patient, new ArrayList<Appointment>(List.of(appointment)));
        }
        br.close();
    }

    @Override
    public void writeToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        try {
            super.getAll().forEach((k, v) -> {
                    v.forEach((value) -> {
                        try {
                            bw.write(k.getId() + " | " + k.getFirstname() + " | " + k.getSurname() + "|");
                            bw.write(value.getId() + " | " + value.getAppointmentDate() + " | ");
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
