package UI;

import Domain.Appointment;
import Domain.Patient;
import Persistancy.Repository;
import Utils.DateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class UI {
    public static Scanner scanner = new Scanner(System.in);

    public static int readCommand() {
        System.out.println(displayMenu());
        int command = scanner.nextInt();
        scanner.nextLine();
        return command;
    }

    public static String displayMenu() {
        return """
                1.Display all the appointments
                2.Add appointment
                3.Delete appointment
                4.Update appointment
                5.Get all the appointments in one day
                0.exit
                """;
    }

    public static Date readDate() {
        while(true)
            try {
                System.out.println("Please enter the data in following formmat (MM DD HH): ");
                String date = scanner.nextLine();
                DateFormatter date1 = new DateFormatter(date);
                return date1.getDate();
            } catch (ParseException err) {
                System.out.println(err);
            }
    }
    public static Appointment readAppointment() {
        System.out.println("""
                           Read appointment
                           """);
        int id = readID();
        Date date = readDate();
        return new Appointment(id, date);
    }

    public static Patient readPatient() {
        System.out.println("""
                           Read Patient
                           """);
        int id = readID();
        System.out.println("Enter firstname: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter surname: ");
        String surname = scanner.nextLine();
        return new Patient(id, firstName, surname);
    }

    public static int readID() {
        System.out.println("Read ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public static void printRepo(Repository<Patient, Appointment, Integer> repo) {
        for(Map.Entry<Patient, ArrayList<Appointment>> elem : repo.getAll().entrySet()) {
            if(elem.getValue().size() != 0) {
                System.out.println(elem.getKey());
                for (Appointment value : elem.getValue())
                    System.out.println("    " + value.toString());
            }
        }
    }

    public static void printListOfAppointments(ArrayList<Appointment> appointments) {
        for(Appointment appointment : appointments)
            System.out.println(appointment);
    }
    public static void printErr(Exception err) {
        System.out.println(err);
    }
}
