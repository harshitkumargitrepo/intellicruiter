package app.recruiter.intellingent.intellicruiter;

import java.util.Calendar;
import java.util.Date;

class User {

    // User Profile members
    private String Id;
    private String Name;
    private String Email;
    private String Mobile;
    private String Dob;
    private String Gender;
    private String Address;
    private String City;
    private String State;
    private String Country;
    private String Type;

    // Default Constructor
    public User() {}

    public User(String id, String name, String email, String mobile, String dob, String gender, String address, String city, String state, String country, String type) {
        Id = id;
        Name = name;
        Email = email;
        Mobile = mobile;
        Dob = dob;
        Gender = gender;
        Address = address;
        City = city;
        State = state;
        Country = country;
        Type = type;

    }

    public String getType() {
        return Type;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getDob() {
        return Dob;
    }

    public String getGender() {
        return Gender;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getCountry() {
        return Country;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setType(String type) {
        Type = type;
    }
}
