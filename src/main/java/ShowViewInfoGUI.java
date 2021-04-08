import javax.swing.*; // import libraries
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;


public class ShowViewInfoGUI implements ActionListener {

    JButton editBtn; // declare variables
    private JTextField name;
    private JTextField roomNum;
    private JTextField address;
    private JTextField phoneNum;
    private JTextField illness;
    JLabel nameLabel;
    JLabel roomNumLabel;
    JLabel dobLabel;
    JLabel genderLabel;
    JRadioButton maleBtn;
    JRadioButton femaleBtn;
    JRadioButton otherBtn;
    JLabel phoneNumLabel;
    JLabel doctorLabel;
    JLabel addressLabel;
    JLabel illnessLabel;
    JLabel titleLabel;
    Patient patient;

    String[] doctorList = { "None", "Dr. James", "Dr. Raines", "Dr. Duffley", "Dr. LeBlanc", "Dr. Ramoray" };
    JComboBox doctorDropdown = new JComboBox(doctorList);

    String[] monthList = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    JComboBox monthDropdown = new JComboBox(monthList);
    Integer[] dayList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
    JComboBox dayDropdown = new JComboBox(dayList);
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Integer[] yearList = {};
    JComboBox yearDropdown = new JComboBox(yearList);

    Font font = new Font("Arial", Font.PLAIN, 16);
    Font boldFont = new Font("Arial", Font.BOLD, 17);
    AbstractBorder border = new RoundedRectBorder(Color.darkGray,1,13);

    JFrame f;
    protected ButtonGroup group = new ButtonGroup();
    static Statement sqlStatement;
    static Connection connection;
    int index;

    public ShowViewInfoGUI(Patient newPatient, Statement statement, Connection myCon, int location){
        // receive patient, location, and sql information
        for (int i = year; i > 1900; i--) { // fill the year dropdown with the years from 1900 to 2021
            yearDropdown.addItem(i);
        }

        sqlStatement = statement; // save the sql information to access later
        connection = myCon;
        index = location;

        patient = newPatient;
        f = new JFrame("Butterfly Hospital Management"); // create a new jframe to display the informtion
        f.setBounds(450, 190, 380, 420);
        f.setLayout(null);
        ImageIcon logo = new ImageIcon("src/com/company/butterflyLogo.png");
        f.setIconImage(logo.getImage());

        titleLabel = new JLabel(patient.name + "'s Information:"); // create the gui components and set the preferences
        addLabelToPane(titleLabel, 70, 5, 325, 25);
        roomNumLabel = new JLabel("Room Number: " + patient.roomNumber);
        addLabelToPane(roomNumLabel, 15, 75, 350, 25);
        nameLabel = new JLabel("Name: " + patient.name);
        addLabelToPane(nameLabel, 15, 40, 350, 25);
        dobLabel = new JLabel("Date of birth: " + patient.dob);
        addLabelToPane(dobLabel, 15, 110, 350, 25);
        genderLabel = new JLabel("Gender: " + patient.gender);
        addLabelToPane(genderLabel, 15, 145, 350, 25);
        addressLabel = new JLabel("Address: " + patient.address);
        addLabelToPane(addressLabel, 15, 215, 350, 25);
        phoneNumLabel = new JLabel("Phone Number: " + patient.phoneNum);
        addLabelToPane(phoneNumLabel, 15, 180, 350, 25);
        doctorLabel = new JLabel("Doctor: " + patient.doctor);
        addLabelToPane(doctorLabel, 15, 250, 350, 25);
        illnessLabel = new JLabel("Illness: " + patient.illness);
        addLabelToPane(illnessLabel, 15, 285, 350, 25);

        name = new JTextField();
        addTextFieldToPane(name, 150, 40, 150, 25);
        roomNum = new JTextField();
        addTextFieldToPane(roomNum, 150, 75, 150, 25);
        address = new JTextField();
        addTextFieldToPane(address, 150, 215, 150, 25);
        illness = new JTextField();
        addTextFieldToPane(illness, 150, 285, 150, 25);
        phoneNum = new JTextField();
        addTextFieldToPane(phoneNum, 150, 180, 150, 25);

        maleBtn = new JRadioButton("Male");
        addRadioBtnToPane(maleBtn, 150, 145, 65, 25);
        femaleBtn = new JRadioButton("Female");
        addRadioBtnToPane(femaleBtn, 215, 145, 80, 25);
        otherBtn = new JRadioButton("Other");
        addRadioBtnToPane(otherBtn, 295, 145, 100, 25);

        addDropdownToPane(dayDropdown, 150, 110, 40, 25);
        addDropdownToPane(monthDropdown, 198, 110, 85, 25);
        addDropdownToPane(yearDropdown, 290, 110, 57, 25);
        addDropdownToPane(doctorDropdown, 150, 250, 150, 25);

        editBtn = new JButton("Edit Information");
        editBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        editBtn.setBackground(Color.lightGray);
        editBtn.setBounds(115, 335, 150, 25);
        editBtn.setOpaque(true);
        editBtn.setBorder(border);
        editBtn.setFocusable(false);
        editBtn.addActionListener(this); // add a listener to determine the functionality of the button
        f.add(editBtn);

        f.setVisible(true);
    }

    // create methods to automatically add the components to the frame when given the dimensions
    public void addDropdownToPane(JComboBox dropdown, int x, int y, int width, int height){
        dropdown.setBounds(x, y, width, height);
        dropdown.setBackground(Color.lightGray);
        dropdown.setVisible(false);
        f.add(dropdown);
    }

    public void addLabelToPane(JLabel label, int x, int y, int width, int height){
        if (label == titleLabel)
            label.setFont(boldFont);
        else
            label.setFont(font);

        label.setBounds(x, y, width, height);
        f.add(label);
    }

    public void addTextFieldToPane(JTextField field, int x, int y, int width, int height){
        field.setFont(font);
        field.setBounds(x, y, width, height);
        field.setVisible(false);
        f.add(field);
        field.setColumns(10);
    }

    public void addRadioBtnToPane(JRadioButton button, int x, int y, int width, int height){
        button.setFont(font);
        button.setFocusable(false);
        button.setVisible(false);
        button.setBounds(x, y, width, height);
        f.add(button);
        group.add(button);
    }

    public void actionPerformed(ActionEvent e){ // determine the result of pressing the button

        if (editBtn.getText().equals("Update")){ // occurs when saving the new information
            String oldName = patient.name; // get the information from the text fields and store it in the list

            patient.name = name.getText();
            updateInfo(nameLabel, name, patient.name, "Name: ");
            titleLabel.setText(patient.name + "'s Information: ");

            patient.roomNumber = roomNum.getText();
            updateInfo(roomNumLabel, roomNum, String.valueOf(patient.roomNumber), "Room Number: ");

            // hide the text fields and show the updated labels
            patient.dob = monthDropdown.getSelectedItem() + " " + dayDropdown.getSelectedItem() + ", " + yearDropdown.getSelectedItem();
            dayDropdown.setVisible(false);
            monthDropdown.setVisible(false);
            yearDropdown.setVisible(false);
            dobLabel.setText("Date of birth: " + patient.dob);

            patient.gender = maleBtn.isSelected() ? "M" : femaleBtn.isSelected() ? "F" : otherBtn.isSelected() ? "X" : "x";
            maleBtn.setVisible(false);
            femaleBtn.setVisible(false);
            otherBtn.setVisible(false);
            genderLabel.setText("Gender: " + patient.gender);

            patient.doctor = (String) doctorDropdown.getSelectedItem();
            doctorDropdown.setVisible(false);
            doctorLabel.setText("Doctor: " + patient.doctor);

            patient.phoneNum = this.phoneNum.getText();
            updateInfo(phoneNumLabel, phoneNum, patient.phoneNum, "Phone Number: ");
            patient.address = this.address.getText();
            updateInfo(addressLabel, address, patient.address, "Address: ");
            patient.illness = this.illness.getText();
            updateInfo(illnessLabel, illness, patient.illness, "Illness: ");

            ShowMenuGUI.changePatientName(patient, oldName);

            try { // update the database with the new information

            updateDatabase("name", patient.name, index);
            updateDatabase("room_number", String.valueOf(patient.roomNumber), index);
            updateDatabase("dob", patient.dob, index);
            updateDatabase("gender", String.valueOf(patient.gender), index);
            updateDatabase("phone_num", patient.phoneNum, index);
            updateDatabase("address", patient.address, index);
            updateDatabase("doctor", patient.doctor, index);
            updateDatabase("illness", patient.illness, index);

            } catch (SQLException throwables) { // catch any sql exceptions
                throwables.printStackTrace();
            }

            editBtn.setText("Edit Information"); // change the button text back to edit
        } else {
            editBtn.setText("Update"); // when pressing the edit button, change the button text to update

            // allow the user to edit the fields by switching the labels to text fields
            // fill the fields by default with the information that has already been stored
            setToEdit(illnessLabel, illness, patient.illness, "Illness:");
            setToEdit(addressLabel, address, patient.address, "Address:");
            setToEdit(phoneNumLabel, phoneNum, patient.phoneNum, "Phone Number:");

            doctorLabel.setText("Doctor:");
            doctorDropdown.setSelectedItem(patient.doctor);
            doctorDropdown.setVisible(true);

            genderLabel.setText("Gender:");
            maleBtn.setVisible(true);
            femaleBtn.setVisible(true);
            otherBtn.setVisible(true);

            switch(patient.gender){
                case "M" -> maleBtn.setSelected(true);
                case "F" -> femaleBtn.setSelected(true);
                default -> otherBtn.setSelected(true);
            }

            dobLabel.setText("Date of birth:");
            //dayDropdown.setSelectedItem();
            dayDropdown.setVisible(true);
            monthDropdown.setVisible(true);
            yearDropdown.setVisible(true);

            setToEdit(roomNumLabel, roomNum, String.valueOf(patient.roomNumber), "Room Number:");
            setToEdit(nameLabel, name, patient.name, "Name:");
        }
    }

    public void updateDatabase(String column, String value, int elementPos) throws SQLException {
        // sent an update command to the database to include the new information
        PreparedStatement updateStatement = switch (column) {
            case "name" -> connection.prepareStatement("UPDATE patients SET name = ? WHERE id = ?;");
            case "room_number" -> connection.prepareStatement("UPDATE patients SET room_number = ? WHERE id = ?;");
            case "dob" -> connection.prepareStatement("UPDATE patients SET dob = ? WHERE id = ?;");
            case "gender" -> connection.prepareStatement("UPDATE patients SET gender = ? WHERE id = ?;");
            case "phone_num" -> connection.prepareStatement("UPDATE patients SET phone_num = ? WHERE id = ?;");
            case "address" -> connection.prepareStatement("UPDATE patients SET address = ? WHERE id = ?;");
            case "doctor" -> connection.prepareStatement("UPDATE patients SET doctor = ? WHERE id = ?;");
            default -> connection.prepareStatement("UPDATE patients SET illness = ? WHERE id = ?;");
        };

        updateStatement.setString(1, value);
        updateStatement.setString(2, String.valueOf(elementPos));
        updateStatement.executeUpdate();
    }

    public void setToEdit(JLabel label, JTextField textField, String info, String labelText){
        // replace the label with a text field
        label.setText(labelText);
        textField.setText(info);
        textField.setVisible(true);
    }

    public void updateInfo(JLabel label, JTextField textField, String info, String labelText){
        // replace the text fields with the updated label
        textField.setVisible(false);
        label.setText(labelText + info);

    }
}