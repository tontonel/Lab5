package com.app.gui;

import Domain.Appointment;
import Domain.Patient;
import Persistancy.*;
import Service.Service;
import Utils.DateFormatter;

import com.app.gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import Exception.*;

import static UI.UI.printErr;

public class Main extends Application {

    public static Repository<Patient, Appointment, Integer> defaultRepository() throws ParseException {
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
        Repository<Patient, Appointment, Integer> repo = new PatientsRepository(patientsMap);
        return repo;
    }

    public static Repository<Patient, Appointment, Integer> memoryRepository () throws ParseException {
        Repository repo = null;

        Properties properties = new Properties();
        try(InputStream is = new FileInputStream("D:\\JavaLabs\\LabGUI\\GUI\\src\\main\\java\\settings.properties")) // try-with-resources
        {
            properties.load(is);
            System.out.println(properties.getProperty("Repository"));
            if(properties.getProperty("Repository").equals("memory")) {
                System.out.println("Using memory repository");
                if(properties.getProperty("MemoryRepository").equals("text"))
                    return new PatientsFileRepository("D:\\JavaLabs\\LabGUI\\GUI\\src\\main\\java\\text.txt");

                else if(properties.getProperty("MemoryRepository").equals("binary")) {
                    System.out.println("here");
                    return new PatientsBinaryRepository("D:\\JavaLabs\\LabGUI\\GUI\\src\\main\\java\\binary.bin");
                }

            }
            else if(properties.getProperty("Repository").equals("database")) {
                return new PatientsDBRepository();
            }
            else {
                return defaultRepository();
            }
        } catch (FileNotFoundException e) {
            printErr(e);
        } catch (IOException e) {
            printErr(e);
        } catch (NoIdenticEntitiesException e) {
            printErr(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return defaultRepository();
    }

    @Override
    public void start(Stage stage) throws IOException, ParseException {
        Repository<Patient, Appointment, Integer> repo = memoryRepository();
        Service patientService = new Service(repo);
        Controller controller = new Controller(patientService);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        fxmlLoader.setController(controller);
        Parent root = (Parent)fxmlLoader.load();

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.setHeight(750);
        stage.setWidth(750);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}