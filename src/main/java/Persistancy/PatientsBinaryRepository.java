package Persistancy;

import Domain.Appointment;
import Domain.Patient;

import Exception.*;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientsBinaryRepository extends AbstractMemoryRepository<Patient, Appointment, Integer> {

    public PatientsBinaryRepository(String FileName) throws IOException, NoIdenticEntitiesException, ParseException {
        super(FileName);
    }

    @Override
    public void readFromFile() throws IOException {
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new FileInputStream(fileName));
            entityMap = (HashMap<Patient, ArrayList<Appointment>>) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void writeToFile() throws IOException {
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(super.getAll());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
