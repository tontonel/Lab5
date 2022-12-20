package Persistancy;

import Domain.Appointment;
import Domain.Patient;
import Utils.DateFormatter;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.HashMap;

import Exception.*;
import org.junit.jupiter.api.IndicativeSentencesGeneration;

public class PatientsDBRepository extends AbstractRepository<Patient, Appointment, Integer> {
    private static final String JDBC_URL =
            "jdbc:sqlite:data/patients.db";

    private Connection conn = null;

    public PatientsDBRepository() throws SQLException {
        super(new HashMap<>());
        openConnection();
        createSchema();
        initTables();
        getFromDB();
        closeConnection();
    }

    private void openConnection()
    {
        try
        {
            if (conn == null || conn.isClosed())
                conn = DriverManager.getConnection(JDBC_URL);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    void createSchema()
    {
        try
        {
            final Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients(pid int, name varchar(50), surname varchar(50));");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS appointments(aid int, appointment_date Date, pid int);");
            stmt.close();
        }
        catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    private void getFromDB() {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * from appointments");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("aid");
                java.util.Date date = new java.util.Date(rs.getDate("appointment_date").getTime());
                int pid = rs.getInt("pid");
                Appointment appointment = new Appointment(id, date);
                PreparedStatement statement2 = conn.prepareStatement(
                        "SELECT * from patients where pid = ?");
                statement2.setInt(1, pid);
                ResultSet rs2 = statement2.executeQuery();
                if (rs2.next()) {
                    String name = rs2.getString("name");
                    String surname = rs2.getString("surname");
                    Patient patient = new Patient(pid, name, surname);
                    super.addEntity(patient, appointment);
                }
                statement2.close();
            }
            statement.close();
        } catch (SQLException e) {
            closeConnection();
            throw new RuntimeException(e);
        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException(e);
        } catch (NoIdenticEntitiesException e) {
            closeConnection();
            throw new RuntimeException(e);
        }
    }

    void initTables() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String qry = "SELECT * FROM patients";
        String qry1 = "SELECT * FROM appointments";

        try {
            stmt = (PreparedStatement) conn.prepareStatement(qry);
            rs =  stmt.executeQuery();

            boolean empty1 = true;
            while( rs.next() ) {
                // ResultSet processing here
                empty1 = false;
            }
            boolean empty2 = true;

            stmt = (PreparedStatement) conn.prepareStatement(qry1);
            rs =  stmt.executeQuery();
            while( rs.next() ) {
                // ResultSet processing here
                empty2 = false;
            }
            if(empty1 && empty2) {
                final Statement insertPatients = conn.createStatement();
                insertPatients.executeUpdate("INSERT INTO patients(pid, name, surname) VALUES(1, 'John', 'Smith');");
                insertPatients.executeUpdate("INSERT INTO patients(pid, name, surname) VALUES(2, 'Jane', 'Doe');");
                insertPatients.executeUpdate("INSERT INTO patients(pid, name, surname) VALUES(3, 'John', 'Doe');");
                PreparedStatement insertAppointments = conn.prepareStatement(
                        "INSERT INTO appointments(aid, appointment_date, pid) VALUES(?, ?, ?);");
                insertAppointments.setInt(1, 1);
                insertAppointments.setDate(2, new Date(new DateFormatter("05 07 15 2022").getDate().getTime()));
                insertAppointments.setInt(3, 1);
                insertAppointments.executeUpdate();
                insertAppointments.setInt(1, 2);
                insertAppointments.setDate(2, new Date(new DateFormatter("05 07 14 2022").getDate().getTime()));
                insertAppointments.setInt(3, 2);
                insertAppointments.executeUpdate();
                insertAppointments.setInt(1, 3);
                insertAppointments.setDate(2, new Date(new DateFormatter("05 07 13 2022").getDate().getTime()));
                insertAppointments.setInt(3, 3);
                insertAppointments.executeUpdate();
                stmt.close();
                insertPatients.close();
                insertAppointments.close();
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEntity(Patient key, Appointment value) throws NoIdenticEntitiesException, IOException {
        super.addEntity(key, value);
        openConnection();
        PreparedStatement stmt = null;
        String qry = "INSERT INTO patients(pid, name, surname) VALUES(?, ?, ?);";
        String qry1 = "INSERT INTO appointments(aid, appointment_date, pid) VALUES(?, ?, ?);";
        try {
            stmt = (PreparedStatement) conn.prepareStatement(qry);
            stmt.setInt(1, key.getId());
            stmt.setString(2, key.getFirstname());
            stmt.setString(3, key.getSurname());
            stmt.executeUpdate();

            stmt = conn.prepareStatement(qry1);
            stmt.setInt(1, value.getId());
            stmt.setDate(2, new Date(value.getAppointmentDate().getTime()));
            stmt.setInt(3, key.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            closeConnection();
            System.err.println("[ERROR] addEntity : " + e.getMessage());
        }
        closeConnection();
    }
    public boolean deleteEntity(Patient key, Appointment value) throws IOException {
        boolean deleted = super.deleteEntity(key, value);
        openConnection();
        PreparedStatement stmt = null;
        String qry1 = "DELETE FROM appointments WHERE aid = ? and pid = ?;";
        try {
            stmt = (PreparedStatement) conn.prepareStatement(qry1);
            stmt.setInt(1, value.getId());
            stmt.setInt(2, key.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            closeConnection();
            System.err.println("[ERROR] removeEntity : " + e.getMessage());
        }
        closeConnection();
        return deleted;
    }

    public void updateEntity(Integer id, Appointment value) throws IOException, NoEntityFound, NoIdenticEntitiesException {
        super.updateEntity(id, value);
        openConnection();
        PreparedStatement stmt = null;
        String qry1 = "UPDATE appointments SET appointment_date = ?, aid = ?  WHERE aid = ?;";
        try {
            stmt = (PreparedStatement) conn.prepareStatement(qry1);
            stmt.setDate(1, new Date(value.getAppointmentDate().getTime()));
            stmt.setInt(2, value.getId());
            stmt.setInt(3, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            closeConnection();
            System.err.println("[ERROR] updateEntity : " + e.getMessage());
        }
        closeConnection();
    }

}
