package app.recruiter.intellingent.intellicruiter;

public class RecommendedRequirements {
    double score;
    Client client;
    Requirement requirement;
    public RecommendedRequirements(double score, Client c, Requirement r){
        this.client = c;
        this.requirement =r;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Client getClient() {
        return client;
    }

    public Requirement getRequirement() {
        return requirement;
    }
}
