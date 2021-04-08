import java.awt.*; // import libraries
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.lang.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionListener;

public class ShowMenuGUI extends JPanel implements ListSelectionListener, ActionListener {
    GridBagConstraints constraints = new GridBagConstraints(); // declare variables
    static JList list;
    static DefaultListModel listModel;
    JButton viewBtn;
    JButton dischargeBtn;
    static JButton addBtn;
    static JLabel progressLabel;
    static int capacity;
    Font font = new Font("Arial", Font.BOLD, 15);
    AbstractBorder border = new RoundedRectBorder(Color.darkGray,1,13);
    static ArrayList<Patient> patients = new ArrayList<Patient>(); // Create an ArrayList object
    static Statement sqlStatement;
    static Connection connection;
    static ResultSet results;

    public ShowMenuGUI(Statement statement, Connection myCon) throws SQLException {
        // create a constructor that
        // takes in the sql information to be able to execute commands in this class
        sqlStatement = statement; // declare variables
        connection = myCon;
        results = sqlStatement.executeQuery("SELECT * FROM patients");

        capacity = 0;
        progressLabel = new JLabel("Hospital Capacity: " + capacity + "/150");
        // create gui elements and set preferences

        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        progressLabel.setVerticalAlignment(JLabel.CENTER);

        progressLabel.setFont(font);

        addBtn = new JButton("Add Patient");
        addBtn.setBackground(Color.lightGray);
        addBtn.setOpaque(true);
        addBtn.setBorder(border); // give the buttons a border with rounded edges
        dischargeBtn = new JButton("Discharge Patient");
        dischargeBtn.setBackground(Color.lightGray);
        dischargeBtn.setOpaque(true);
        dischargeBtn.setBorder(border);
        viewBtn = new JButton("View Information");
        viewBtn.setBackground(Color.lightGray);
        viewBtn.setOpaque(true);
        viewBtn.setBorder(border);

        listModel = new DefaultListModel();
        list = new JList(listModel); // create the list and put it in a scroll pane
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.clearSelection();

        setLayout(new GridBagLayout()); // set the layout constraints
        constraints.weightx = 5.0;
        constraints.weighty = 5.0;
        constraints.ipadx = 250;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;

        addGB(new JScrollPane(list, // add the components to the frame and set their layout constraints
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), 0, 0);

        constraints.ipadx = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridheight = 1;
        constraints.insets = new Insets(20,10,20,10);
        addGB(addBtn, 1, 0);
        addGB(dischargeBtn, 2, 0);
        constraints.gridwidth = 2;
        addGB(viewBtn, 1, 1);
        constraints.insets = new Insets(0,0,20,0);
        constraints.weighty = 1;
        addGB(progressLabel, 1, 2);
        constraints.gridwidth = 1;

        addBtn.setFont(font); // set the buttons' font and listener
        addBtn.setFocusable(false);
        addBtn.addActionListener(this);
        dischargeBtn.setFont(font);
        dischargeBtn.setFocusable(false);
        dischargeBtn.addActionListener(this);
        dischargeBtn.setEnabled(false);
        viewBtn.setFont(font);
        viewBtn.setFocusable(false);
        viewBtn.addActionListener(this);
        viewBtn.setEnabled(false);

        list.setFocusable(false);
    }

    void addGB(Component component, int x, int y) {
        // create a method to automatically
        // add the components to the frame with the provided constraints
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
    }

    public void actionPerformed(ActionEvent e){
        int index = list.getSelectedIndex(); // get the selected element in the list

        if (e.getSource() == addBtn) { // when the add button is pressed, show the input form

            ShowInputGUI showInput = new ShowInputGUI();
            showInput.setInputGUIVisible();

        } else if (e.getSource() == dischargeBtn) { // remove the selected patient when the discharge button is pressed
            listModel.remove(index);
            patients.remove(index);

            try { // remove the patient from the database
                PreparedStatement removePatient = connection.prepareStatement("DELETE FROM patients WHERE id = ?;");
                removePatient.setString(1, String.valueOf(index));
                removePatient.executeUpdate();

                PreparedStatement updatePatients = connection.prepareStatement("UPDATE hospital.patients SET ID = ID - 1 WHERE ID >= ?;");
                updatePatients.setString(1, String.valueOf(index));
                updatePatients.executeUpdate();
            } catch (SQLException throwables) { // catch any sql exceptions
                throwables.printStackTrace();
            }

            capacity -= 1; // update the hospital capacity
            progressLabel.setText("Hospital Capacity: " + capacity + "/150");

            if (capacity < 150) // allow the add button to be pressed if the max capacity hasn't been reached
                addBtn.setEnabled(true);

        } else if (e.getSource() == viewBtn) { // create a viewinfo object if the view button is pressed
            Patient selectedPatient = patients.get(index); // pass in the sql and patient information
            ShowViewInfoGUI viewInfo = new ShowViewInfoGUI(selectedPatient, sqlStatement, connection, index);
        }

        list.setSelectedIndex(index); // select the next item in the list
        list.ensureIndexIsVisible(index);
    }

    public void valueChanged(ListSelectionEvent e) { // occurs when the users selection is changed
        if (!e.getValueIsAdjusting()) {

            if (list.getSelectedIndex() < 0) { // disable the buttons if nothing is selected
                viewBtn.setEnabled(false);
                dischargeBtn.setEnabled(false);

            } else {
                viewBtn.setEnabled(true);
                dischargeBtn.setEnabled(true);
            }
        }
    }

    public static void changePatientName(Patient patient, String oldName){ // replace the patients name with their new name
        listModel.set(listModel.indexOf(oldName), patient.name);
    }

    public static void addPatient(Patient newPatient, boolean addToDatabase) throws SQLException { // create a method to add a patient to the list and database
        int index = 0; // always add the patient to the beginning of the list

        patients.add(0, newPatient);
        newPatient.patientID = (int)(Math.random() * 999000 + 100000); // randomise the patients id number

        for (int i = 0; i < patients.toArray().length; i++){ // check that the patient id is not the same as any of the previous patients'
            if (patients.get(i).patientID == newPatient.patientID)
                newPatient.patientID = (int)(Math.random() * 999000 + 100000); // if it is, generate a new id
        }

        // in case the name field is left blank, use the patients id as their name
        newPatient.name = newPatient.name.equals("Unknown") ? "Patient #" + newPatient.patientID : newPatient.name;

        if (addToDatabase) { // if addtodatabase is true, add the information to the database
            sqlStatement.executeUpdate("UPDATE hospital.patients SET ID = ID + 1 WHERE ID >= 0;");

            try {
                PreparedStatement smt = connection.prepareStatement("INSERT INTO patients VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                smt.setString(1, String.valueOf(index)); // this line dosent matter because id auto updates
                smt.setString(2, newPatient.name);
                smt.setString(3, String.valueOf(newPatient.roomNumber));
                smt.setString(4, newPatient.dob);
                smt.setString(5, String.valueOf(newPatient.gender));
                smt.setString(6, newPatient.phoneNum);
                smt.setString(7, newPatient.address);
                smt.setString(8, newPatient.doctor);
                smt.setString(9, newPatient.illness);
                smt.executeUpdate();

            } catch (SQLException exception) { // catch any sql exceptions
                exception.printStackTrace();
            }
        }

        capacity += 1; // increase the hospital capacity
        progressLabel.setText("Hospital Capacity: " + capacity + "/150");

        if (capacity == 150) // if the capacity is full, disable the add button
            addBtn.setEnabled(false);

        listModel.insertElementAt(newPatient.name, index); // add the patient's name to the list
    }
}