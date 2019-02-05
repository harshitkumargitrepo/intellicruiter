package app.recruiter.intellingent.intellicruiter;

public class Skill {

    private String skillID;
    private String skillName;
    private String skillExperienceYears;

    public Skill(){

    }

    public Skill(String skillID, String skillName, String skillExperienceYears) {
        this.skillID = skillID;
        this.skillName = skillName;
        this.skillExperienceYears = skillExperienceYears;
    }

    public String getSkillID() {
        return skillID;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillExperienceYears() {
        return skillExperienceYears;
    }
}
