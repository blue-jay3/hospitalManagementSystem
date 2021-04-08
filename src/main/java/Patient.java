public class Patient {
    String name; // declare variables
    String doctor;
    String comments;
    String illness;
    String address;
    String phoneNum;
    String dob;
    int patientID;
    String gender;
    String roomNumber;

    Patient() // set the variables to default values
    {
        this.name = "Unknown";
        this.doctor = "None";
        this.comments = "None";
        this.illness = "Not specified";
        this.dob = "Not specified";
        this.gender = "x";
        this.phoneNum = "N/A";
        this.address = "None";
        this.roomNumber = "0";
    }

    public String toString() // create a toString method for testing
    {
        String output = "Name: " + this.name +
                "\nBirthday: " + this.dob +
                "\nGender: " + this.gender +
                "\nIllness: " + this.illness +
                "\nDoctor: " + this.doctor;

        return output; // display patient info
    }

}
