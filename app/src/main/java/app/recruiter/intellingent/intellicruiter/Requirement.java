package app.recruiter.intellingent.intellicruiter;

import java.util.List;

public class Requirement {
    private String requirementId;
    private String clientId;
    private String requirementTitle;
    private String requirementDescription;
    private int numberOfPositions;
    private List<Skill> skill;

    public  Requirement(){ }

    public Requirement(String clientid, String requirementId, String requirementTitle, String requirementDescription, int numberOfPositions, List<Skill> skills) {
        this.requirementId = requirementId;
        this.requirementTitle = requirementTitle;
        this.requirementDescription = requirementDescription;
        this.numberOfPositions = numberOfPositions;
        this.skill = skills;
        this.clientId = clientid;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public String getRequirementTitle() {
        return requirementTitle;
    }

    public String getRequirementDescription() {
        return requirementDescription;
    }

    public int getNumberOfPositions() {
        return numberOfPositions;
    }

    public List<Skill> getSkill() {
        return skill;
    }
}
