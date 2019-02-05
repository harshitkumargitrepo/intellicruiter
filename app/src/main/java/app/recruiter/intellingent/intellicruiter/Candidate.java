package app.recruiter.intellingent.intellicruiter;

import java.util.List;

public class Candidate {

    private String candidateId;
    private String candidateEmail;
    private List<Skill> candidateSkills;
    private String candidateAvailability;

    public Candidate(){}

    public Candidate(String candidateEmail, String candidateId,  List<Skill> candidateSkills, String candidateAvailability) {
        this.candidateId = candidateId;
        this.candidateSkills = candidateSkills;

        this.candidateAvailability = candidateAvailability;
        this.candidateEmail = candidateEmail;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public List<Skill> getCandidateSkills() {
        return candidateSkills;
    }

    public String getCandidateAvailability() {
        return candidateAvailability;
    }
}

