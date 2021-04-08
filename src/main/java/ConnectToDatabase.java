import javax.swing.*; // import libraries
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectToDatabase {

    ConnectToDatabase() throws SQLException { // create a default constructor and throw the sql exceptions

        Properties login = new Properties(); // create a new properties object to store the sql login information
        try {
            FileReader in = new FileReader("src/main/java/login.properties");
            login.load(in); // read the login information from the file
        } catch (IOException e) { // catch any input/output exceptions
                e.printStackTrace();
        }

        String username = login.getProperty("username"); // store login information in variables
        String password = login.getProperty("password");
        String url = "jdbc:mysql://localhost:3306/hospital?autoReconnect=true&useSSL=false";

        Connection myConn = DriverManager.getConnection(url, username, password); // connect to the database
        Statement myStatement = myConn.createStatement(); // create a statement to execute commands
        System.out.println ("Database connection established");

        //myStatement.executeUpdate("TRUNCATE patients");

        JFrame f = new JFrame("Butterfly Hospital Management"); // create a window for the menu and set the preferences
        f.addWindowListener(new WindowAdapter( ) {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.setSize(600, 350);
        f.setLocation(200, 200);

        ImageIcon logo = new ImageIcon("src/com/company/butterflyLogo.png");
        f.setIconImage(logo.getImage());

        ShowMenuGUI menu = new ShowMenuGUI(myStatement, myConn); // add the menu gui to the frame and pass in the sql information
        f.setContentPane(menu);
        f.setVisible(true); // show the window

        Statement mySecondStatement = myConn.createStatement(); // create a second statement to retrieve the patients in the database on startup
        ResultSet results = mySecondStatement.executeQuery("SELECT * FROM patients");

        while (results.next()) { // go through each row in the data set and store its values in a patient object
            Patient patient = new Patient();

            patient.name = results.getString("name");
            patient.roomNumber = results.getString("room_number");
            patient.dob = results.getString("dob");
            patient.gender = results.getString("gender");
            patient.phoneNum = results.getString("phone_num");
            patient.address = results.getString("address");
            patient.doctor = results.getString("doctor");
            patient.illness = results.getString("illness");

            try {
                ShowMenuGUI.addPatient(patient, false); // add the patient to the gui list but not to the database as the information is already stored
            } catch (SQLException e) {
                e.printStackTrace(); // print any sql exceptions
            }
        }
    }

}
