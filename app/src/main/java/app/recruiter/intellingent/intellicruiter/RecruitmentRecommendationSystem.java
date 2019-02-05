package app.recruiter.intellingent.intellicruiter;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class RecruitmentRecommendationSystem extends AppCompatActivity {


    //a list to store all the client requirements from firebase database
    List<Requirement> requirement;
    List<Client> client;
    List<Application> application;
    List<Candidate> candidate;

    //our database reference object
    DatabaseReference databaseClients;
    DatabaseReference databaseRequirements;
    DatabaseReference databaseCandidates;
    DatabaseReference databaseApplications;

    public RecruitmentRecommendationSystem(List<Client> clients, List<Requirement> requirements, List<Candidate> candidates, List<Application> applications){
        //initializing the lists
        this.client = clients;
        this.requirement = requirements;
        this.application= applications;
        this.candidate= candidates;

        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseCandidates    = database.getReference("candidates");
        databaseClients       = database.getReference("clients");
        databaseRequirements  = database.getReference("requirements");
        databaseApplications  = database.getReference("applications");
    }

    public List<RecommendedRequirements> getRecommendations(String email){

        List<RecommendedRequirements> top10requirements = new ArrayList<>();

        String candidateID = null;
        Candidate currentcandidate = new Candidate();

        for (int pos = 0; pos < candidate.size(); pos++) {
            if (candidate.get(pos).getCandidateEmail().compareTo(email) == 0) {
                candidateID = candidate.get(pos).getCandidateId();
                currentcandidate = candidate.get(pos);
            }
        }

        if(currentcandidate.getCandidateSkills() == null)
        {
            return top10requirements;
        }

        double totalRequiredSkills = 0.0;
        double candidateSkills = 0.0;
        double requirementScore = 0.0;
        double candidateScore = 0.0;

        for (int pos = 0; pos < candidate.size(); pos++) {
            if (candidate.get(pos).getCandidateEmail().compareTo(email) != 0) {
                List<Skill> otherCandidateSkill = candidate.get(pos).getCandidateSkills();
                List<Skill> currentCandidateSkill = currentcandidate.getCandidateSkills();

                for(int otherskillpos =0; otherskillpos<otherCandidateSkill.size();otherskillpos++ ){
                    for(int currentskillpos =0; currentskillpos<currentCandidateSkill.size();currentskillpos++){
                        if(currentCandidateSkill.get(currentskillpos).getSkillName().toLowerCase().compareTo(otherCandidateSkill.get(otherskillpos).getSkillName().toLowerCase()) ==0){
                            candidateSkills++;
                        }
                    }
                    totalRequiredSkills++;
                }
                candidateScore = candidateScore + (candidateSkills/totalRequiredSkills);
            }
        }

         totalRequiredSkills = 0;
         candidateSkills = 0;

         Map<Requirement,Double> weightedRequirements = new HashMap<>();
        for (int pos = 0; pos < requirement.size(); pos++) {
            List<Skill>  currentRequirementSkill = requirement.get(pos).getSkill();
            List<Skill> currentCandidateSkill = currentcandidate.getCandidateSkills();
            if(currentCandidateSkill != null && currentRequirementSkill != null) {
                for (int otherskillpos = 0; otherskillpos < currentRequirementSkill.size(); otherskillpos++) {
                    for (int currentskillpos = 0; currentskillpos < currentCandidateSkill.size(); currentskillpos++) {
                        if (currentCandidateSkill.get(currentskillpos).getSkillName().toLowerCase().compareTo(currentRequirementSkill.get(otherskillpos).getSkillName().toLowerCase()) == 0) {
                            candidateSkills++;
                        }
                    }
                    totalRequiredSkills++;
                }
            }
            requirementScore = (candidateSkills/totalRequiredSkills);
            weightedRequirements.put(requirement.get(pos),requirementScore);
        }


        Set<Map.Entry<Requirement, Double>> set = weightedRequirements.entrySet();
        List<Map.Entry<Requirement, Double>> list = new ArrayList<Map.Entry<Requirement, Double>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<Requirement, Double>>() {
            public int compare(Map.Entry<Requirement, Double> o1,
                               Map.Entry<Requirement, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int count = 0;
        List<Requirement> top10 = new ArrayList<>();
        for (final Map.Entry<Requirement, Double> entry : list) {
            if(count<10) {
                Log.i("Recommendations", "Requirement ID : " + entry.getKey().getRequirementId() + "Requirement Title :" + entry.getKey().getRequirementTitle() + " Score obtained :" + entry.getValue().toString());
                top10.add(entry.getKey());
                List<Client> result = client.stream()
                        .filter(item -> item.getClientId().equals(entry.getKey().getClientId()))
                        .collect(Collectors.toList());
                if(entry.getValue()>=0.5) {
                    RecommendedRequirements requirement = new RecommendedRequirements(entry.getValue(), result.get(0), entry.getKey());
                    top10requirements.add(requirement);
                }
            }
        }

        return top10requirements;
    }



}
