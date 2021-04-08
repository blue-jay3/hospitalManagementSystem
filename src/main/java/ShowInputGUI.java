import java.awt.*; // declare variables
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

public class ShowInputGUI extends JFrame {
    private JPanel contentPane; // declare variables
    private JTextField name;
    private JTextField roomNum;
    private JTextField address;
    private JTextField phoneNum;
    private JTextField illness;
    private JButton btnRegister;
    JLabel registerLabel;
    ButtonGroup group = new ButtonGroup();

    JRadioButton maleBtn;
    JRadioButton femaleBtn;
    JRadioButton otherBtn;

    String[] doctorList = { "None", "Dr. James", "Dr. Raines", "Dr. Duffley", "Dr. LeBlanc", "Dr. Ramoray" };
    JComboBox doctorDropdown = new JComboBox(doctorList);

    String[] monthList = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    JComboBox monthDropdown = new JComboBox(monthList);
    Integer[] dayList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
    JComboBox dayDropdown = new JComboBox(dayList);
    int year = Calendar.getInstance().get(Calendar.YEAR);
    Integer[] yearList = {};
    JComboBox yearDropdown = new JComboBox(yearList);

    Patient newPatient = new Patient();
    Font font = new Font("Arial", Font.PLAIN, 16);
    Font boldFont = new Font("Arial", Font.BOLD, 17);
    AbstractBorder border = new RoundedRectBorder(Color.darkGray,1,13);

    public void setInputGUIVisible(){ // this method show's the gui frame
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ShowInputGUI frame = new ShowInputGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ShowInputGUI() {

        for (int i = year; i > 1900; i--) { // fill the year dropdown with the years from 1900 to 2021
            yearDropdown.addItem(i);
        }

        setTitle("Butterfly Hospital Management"); // set the preferences of the frame
        setBounds(450, 190, 380, 420);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        ImageIcon logo = new ImageIcon("src/com/company/butterflyLogo.png");
        setIconImage(logo.getImage());

        addDropdownToPane(dayDropdown, 150, 110, 40, 25); // set the gui components preferences
        addDropdownToPane(monthDropdown, 198, 110, 85, 25); // and add them to the frame
        addDropdownToPane(yearDropdown, 290, 110, 57, 25);
        addDropdownToPane(doctorDropdown, 150, 250, 150, 25);

        address = new JTextField();
        addTextFieldToPane(address, 150, 215, 150, 25);
        phoneNum = new JTextField();
        addTextFieldToPane(phoneNum, 150, 180, 150, 25);
        illness = new JTextField();
        addTextFieldToPane(illness, 150, 285, 150, 25);
        name = new JTextField();
        addTextFieldToPane(name, 150, 40, 150, 25); // y30
        roomNum = new JTextField();
        addTextFieldToPane(roomNum, 150, 75, 150, 25);

        JLabel doctor = new JLabel("Doctor:");
        addLabelToPane(doctor, 15, 250, 100, 25);
        JLabel addressLabel = new JLabel("Address:");
        addLabelToPane(addressLabel, 15, 215, 100, 25);
        JLabel phoneNumLabel = new JLabel("Phone Number:");
        addLabelToPane(phoneNumLabel, 15, 180, 150, 25);
        JLabel illnessLabel = new JLabel("Illness:");
        addLabelToPane(illnessLabel, 15, 285, 100, 25);
        JLabel genderLabel = new JLabel("Gender:");
        addLabelToPane(genderLabel, 15, 145, 100, 25);
        registerLabel = new JLabel("Register a Patient:");
        addLabelToPane(registerLabel, 124, 5, 325, 25);
        JLabel nameLabel = new JLabel("Name: ");
        addLabelToPane(nameLabel, 15, 40, 100, 25);
        JLabel roomNumLabel = new JLabel("Room Number: ");
        addLabelToPane(roomNumLabel, 15, 75, 150, 25);
        JLabel dobLabel = new JLabel("Date of Birth:");
        addLabelToPane(dobLabel, 15, 110, 100, 25);

        maleBtn = new JRadioButton("Male");
        addRadioBtnToPane(maleBtn, 150, 145, 65, 25);
        femaleBtn = new JRadioButton("Female");
        addRadioBtnToPane(femaleBtn, 215, 145, 80, 25);
        otherBtn = new JRadioButton("Other");
        addRadioBtnToPane(otherBtn, 295, 145, 100, 25);

        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 15));
        btnRegister.setBackground(Color.lightGray);
        btnRegister.setOpaque(true);
        btnRegister.setBorder(border);
        btnRegister.setBounds(140, 335, 100, 25);
        btnRegister.setFocusable(false);
        btnRegister.addActionListener(listener); // add a listener to determine the functionality of the button
        contentPane.add(btnRegister);
    }

    // create methods to automatically add the components to the frame when given the dimensions
    public void addDropdownToPane(JComboBox dropdown, int x, int y, int width, int height){
        dropdown.setBounds(x, y, width, height);
        dropdown.setBackground(Color.lightGray);
        contentPane.add(dropdown);
    }

    public void addLabelToPane(JLabel label, int x, int y, int width, int height){
        if (label == registerLabel)
            label.setFont(boldFont);
        else
            label.setFont(font);

        label.setBounds(x, y, width, height);
        contentPane.add(label);
    }

    public void addTextFieldToPane(JTextField field, int x, int y, int width, int height){
        field.setFont(font);
        field.setBounds(x, y, width, height);
        contentPane.add(field);
        field.setColumns(10);
    }

    public void addRadioBtnToPane(JRadioButton button, int x, int y, int width, int height){
        button.setFont(font);
        button.setFocusable(false);
        button.setBounds(x, y, width, height);
        button.addActionListener(listener);
        contentPane.add(button);
        group.add(button); // add the radio buttons to a group so only one can be selected at a time
    }

    ActionListener listener = new ActionListener() { // determine the result of pressing the button
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnRegister){

                // if the fields are empty, set the value to default
                // if not, save the given value
                newPatient.name = !name.getText().isEmpty() ? name.getText() : newPatient.name;
                newPatient.illness = !illness.getText().isEmpty() ? illness.getText() : newPatient.illness;
                newPatient.address = !address.getText().isEmpty() ? address.getText() : newPatient.address;
                newPatient.phoneNum = !phoneNum.getText().isEmpty() ? phoneNum.getText() : newPatient.phoneNum;
                newPatient.gender = maleBtn.isSelected() ? "M" : femaleBtn.isSelected() ? "F" : otherBtn.isSelected() ? "X" : "x";

                if (!roomNum.getText().isEmpty()) {
                    try {
                        newPatient.roomNumber = roomNum.getText();
                    } catch (NumberFormatException exception) {
                        System.out.println(exception);
                    }
                }

                newPatient.dob = monthDropdown.getSelectedItem() + " " + dayDropdown.getSelectedItem() + ", " + yearDropdown.getSelectedItem();
                newPatient.doctor = (String) doctorDropdown.getSelectedItem();

                try { // add the patient to the list and the database
                    ShowMenuGUI.addPatient(newPatient, true);
                } catch (SQLException throwables) { // catch any sql exceptions
                    throwables.printStackTrace();
                }

                setVisible(false); // close the window
            }
        }
    };

}