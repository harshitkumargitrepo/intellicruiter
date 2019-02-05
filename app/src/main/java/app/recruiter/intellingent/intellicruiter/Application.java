package app.recruiter.intellingent.intellicruiter;

public class Application {
    private String applicationId;
    private String requirementId;
    private String candidateId;
    private Candidate candidate;
    private Requirement requirement;
    private String status;

    public Application(){}

    public Application(String applicationId, String requirementId, String candidateId, Candidate candidate, Requirement requirement, String status) {
        this.applicationId = applicationId;
        this.requirementId = requirementId;
        this.candidateId = candidateId;
        this.candidate = candidate;
        this.requirement = requirement;
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public String getStatus() {
        return status;
    }
}
