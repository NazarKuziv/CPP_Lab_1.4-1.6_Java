import java.time.LocalDate;

public class Prisoner implements java.io.Serializable {
    public int ID;
    public String Surname;
    public String Name;
    public String Middle_Name;
    public LocalDate Birthday;
    public String[] Dates_of_Convictions;
    public LocalDate Date_of_Last_Imprisonment;
    public LocalDate Date_of_Last_Dismissal = LocalDate.MIN;

    public void setID(int id) {ID = id;}
    public int getID() {return ID;}
    public String getSurname() {return Surname;}
    public void setSurname(String surname) {Surname = surname;}
    public String getName() {return Name;}
    public void setName(String name) {Name = name;}
    public String getMiddle_Name() {return Middle_Name;}
    public void setMiddle_Name(String middle_Name) {Middle_Name = middle_Name;}
    public LocalDate getBirthday() {return Birthday;}
    public void setBirthday(LocalDate birthday) {Birthday = birthday;}
    public String[] getDates_of_Convictions() {return Dates_of_Convictions;}
    public void setDates_of_Convictions(String[] dates_of_Convictions) {Dates_of_Convictions = dates_of_Convictions;}
    public LocalDate getDate_of_Last_Imprisonment() { return Date_of_Last_Imprisonment;}
    public void setDate_of_Last_Imprisonment(LocalDate date_of_Last_Imprisonment) { Date_of_Last_Imprisonment = date_of_Last_Imprisonment;}
    public LocalDate getDate_of_Last_Dismissal() {return Date_of_Last_Dismissal;}
    public void setDate_of_Last_Dismissal(LocalDate date_of_Last_Dismissal) {Date_of_Last_Dismissal = date_of_Last_Dismissal;}

}
