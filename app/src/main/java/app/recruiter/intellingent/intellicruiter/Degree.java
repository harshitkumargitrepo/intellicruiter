package app.recruiter.intellingent.intellicruiter;

public class Degree {

    private String degreeId;
    private String degreeType;
    private String degreeName;
    private String degreeGPA;
    private String degreeCollegeName;
    private String City;
    private String State;
    private String Country;
    private String degreeDescription;

    public Degree(){

    }

    public Degree(String degreeId, String degreeType, String degreeName, String degreeGPA, String degreeCollegeName, String city, String state, String country, String degreeDescription) {
        this.degreeId = degreeId;
        this.degreeType = degreeType;
        this.degreeName = degreeName;
        this.degreeGPA = degreeGPA;
        this.degreeCollegeName = degreeCollegeName;
        City = city;
        State = state;
        Country = country;
        this.degreeDescription = degreeDescription;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public String getDegreeGPA() {
        return degreeGPA;
    }

    public String getDegreeCollegeName() {
        return degreeCollegeName;
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

    public String getDegreeDescription() {
        return degreeDescription;
    }
}
