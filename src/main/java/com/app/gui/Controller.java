package com.app.gui;

import Domain.Appointment;
import Domain.Patient;
import Utils.DateFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import Service.Service;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

import Exception.*;

public class Controller implements Initializable {


    Service patientService;

    private ObservableList<String> hours = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                                                                                    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");

    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public Controller() {
        this.patientService = null;
    }
    public Controller(Service patientService) {
        this.patientService = patientService;
    }
    @FXML
    private ListView<Appointment> AppointmentList;

    @FXML
    private Button UpdateAppointmentButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private TextField appointmentIDInput;

    @FXML
    private DatePicker dateInput;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField patientIDInput;

    @FXML
    private ListView<Patient> patientsList;

    @FXML
    private TextField surnameInput;


    @FXML
    private ComboBox<String> selectHours;


    @FXML
    void handleAddAppointment(ActionEvent event) {
        String name = nameInput.getText();
        String surname = surnameInput.getText();
        String patientID = patientIDInput.getText();
        String appointmentID = appointmentIDInput.getText();
        String date = dateInput.getValue().toString();
        try {
            Patient patient = new Patient(Integer.parseInt(patientID), name, surname);
            String[] elems = date.split("-");
            Appointment appointment = new Appointment(Integer.parseInt(appointmentID), new DateFormatter(elems[0] + " " + elems[1] + " "
                                            + selectHours.getSelectionModel().getSelectedItem() + " " + elems[2]).getDate());
            patientService.addPatient(patient, appointment);
            appointments.add(appointment);
            clearFields();
            for(Patient p : patients)
                if(p.equals(patient))
                    return;
            patients.add(patient);
        } catch (NoIdenticEntitiesException e) {
            showAlert(e);
        } catch (IOException e) {
            showAlert(e);
        } catch (ParseException e) {
            showAlert(e);
        } catch (Exception e) {
            showAlert(e);
        }
    }

    @FXML
    void handleDeleteAppointment(ActionEvent event)  {
        try {
            if(patients.size() != 0) {
                patientService.deletePatient(patientsList.getSelectionModel().getSelectedItem(), AppointmentList.getSelectionModel().getSelectedItem());
                appointments.remove(AppointmentList.getSelectionModel().getSelectedItem());
                clearFields();
                if(patientService.getAppointmentsFromID(patientsList.getSelectionModel().getSelectedItem().getId()).size() == 0)
                    patients.remove(patientsList.getSelectionModel().getSelectedItem());
            }
        } catch (IOException e) {
            showAlert(e);
        } catch (Exception e) {
            showAlert(e);
        }
    }

    @FXML
    void handleUpdateAppointment(ActionEvent event) {
        try {
            if(patients.size() != 0 && AppointmentList.getSelectionModel().getSelectedItem() != null) {
                String date = dateInput.getValue().toString();
                String[] elems = date.split("-");
                Appointment appointment = new Appointment(Integer.parseInt(appointmentIDInput.getText()), new DateFormatter(elems[0] + " " + elems[1] + " "
                        + selectHours.getSelectionModel().getSelectedItem()).getDate());
                patientService.updatePatient(AppointmentList.getSelectionModel().getSelectedItem().getId(), appointment);
                appointments.remove(AppointmentList.getSelectionModel().getSelectedItem());
                appointments.add(appointment);
                clearFields();
                AppointmentList.refresh();
            }
        } catch (IOException e) {
            showAlert(e);
        } catch (ParseException e) {
            showAlert(e);
        } catch (NoEntityFound e) {
            showAlert(e);
        } catch (Exception e) {
            showAlert(e);
        }
    }

    @FXML
    void handlePatientListClick(MouseEvent event) throws NoEntityFound {
        if(patients.size() != 0) {
            AppointmentList.getItems().clear();
            Patient patient = patientsList.getSelectionModel().getSelectedItem();
            for (Appointment a : patientService.getAppointmentsFromID(patient.getId()))
                appointments.add(a);
            AppointmentList.setItems(appointments);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPatientsList();
        selectHours.setItems(hours);
    }
    public void showPatientsList() {
        for(Patient patient : patientService.getAll().keySet()) {
            patients.add(patient);
        }
        patientsList.setItems(patients);
    }
    public void clearFields() {
        nameInput.setText("");
        surnameInput.setText("");
        patientIDInput.setText("");
        appointmentIDInput.setText("");
        dateInput.setValue(null);
        selectHours.setValue(null);
    }
    private void showAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
