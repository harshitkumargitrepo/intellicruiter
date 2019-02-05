package app.recruiter.intellingent.intellicruiter;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessApplication  extends AppCompatActivity {

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

    public ProcessApplication(List<Client> clients, List<Requirement> requirements, List<Candidate> candidates, List<Application> applications){
        Log.i("ProcessApplication","called constructor ProcessApplication()");
        //initializing the requirement list
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


    public String processApplicationHandler(final String globalCandidateEmail, Map<String, String> requirementList, Boolean Withdraw) {


        String candidateID = null;
        String status = "OPEN";
        Candidate currentcandidate = new Candidate();
        Requirement currentrequirement = new Requirement();
        String requirementID = requirementList.get("requirementId").toString();
        Log.i("ProcessApplication", "Candidate email :" + globalCandidateEmail);
        Log.i("ProcessApplication", "Requirement ID :" + requirementID);

        for (int pos = 0; pos < candidate.size(); pos++) {
            if (candidate.get(pos).getCandidateEmail().compareTo(globalCandidateEmail) == 0) {
                candidateID = candidate.get(pos).getCandidateId();
                currentcandidate = candidate.get(pos);
                Log.i("ProcessApplication", "Candidate ID : " + candidate.get(pos).getCandidateId());
            }
        }


        if (Withdraw == Boolean.FALSE) {
            if (candidateID != null) {
                for (int pos = 0; pos < requirement.size(); pos++) {
                    if (requirement.get(pos).getRequirementId().compareTo(requirementID) == 0) {
                        currentrequirement = requirement.get(pos);
                    }
                }

                for (int pos = 0; pos < application.size(); pos++) {
                    if (application.get(pos).getRequirementId().compareTo(requirementID) == 0
                            && application.get(pos).getCandidateId().compareTo(candidateID) == 0) {
                        return "DUPLICATE";
                    }
                }

                //getting a unique id using push().getKey() method
                //it will create a unique id and we will use it as the Primary Key for our Application
                String id = databaseApplications.push().getKey();

                //creating an Application Object
                Application _application = new Application(id, requirementID, candidateID, currentcandidate, currentrequirement, status);
                //Saving the Application
                databaseApplications.child(id).setValue(_application);
                return "SUCCESS";
            } else {
                return "CANDIDATE_NOT_EXISTS";
            }
        }

        if (Withdraw == Boolean.TRUE) {

            for (int pos = 0; pos < application.size(); pos++) {
                if (application.get(pos).getRequirementId().compareTo(requirementID) == 0
                        && application.get(pos).getCandidateId().compareTo(candidateID) == 0) {

                    //removing application
                    databaseApplications.child(application.get(pos).getApplicationId()).removeValue();
                    return "WITHDRAW";
                }
            }

        }


     return "NA";
    }


    public String processTopApplicationHandler(String globalCandidateEmail, RecommendedRequirements recommendedRequirements, Boolean withdraw){

        String candidateID = null;
        String status = "OPEN";
        Candidate currentcandidate = new Candidate();
        Requirement currentrequirement = new Requirement();
        String requirementID = recommendedRequirements.getRequirement().getRequirementId();

        for (int pos = 0; pos < candidate.size(); pos++) {
            if (candidate.get(pos).getCandidateEmail().compareTo(globalCandidateEmail) == 0) {
                candidateID = candidate.get(pos).getCandidateId();
                currentcandidate = candidate.get(pos);
                Log.i("ProcessApplication", "Candidate ID : " + candidate.get(pos).getCandidateId());
            }
        }

        if (withdraw == Boolean.FALSE) {
            if (candidateID != null) {
                for (int pos = 0; pos < requirement.size(); pos++) {
                    if (requirement.get(pos).getRequirementId().compareTo(requirementID) == 0) {
                        currentrequirement = requirement.get(pos);
                    }
                }

                for (int pos = 0; pos < application.size(); pos++) {
                    if (application.get(pos).getRequirementId().compareTo(requirementID) == 0
                            && application.get(pos).getCandidateId().compareTo(candidateID) == 0) {
                        return "DUPLICATE";
                    }
                }

                //getting a unique id using push().getKey() method
                //it will create a unique id and we will use it as the Primary Key for our Application
                String id = databaseApplications.push().getKey();

                //creating an Application Object
                Application _application = new Application(id, requirementID, candidateID, currentcandidate, currentrequirement, status);
                //Saving the Application
                databaseApplications.child(id).setValue(_application);
                return "SUCCESS";
            } else {
                return "CANDIDATE_NOT_EXISTS";
            }
        }

        if (withdraw == Boolean.TRUE) {

            for (int pos = 0; pos < application.size(); pos++) {
                if (application.get(pos).getRequirementId().compareTo(requirementID) == 0
                        && application.get(pos).getCandidateId().compareTo(candidateID) == 0) {

                    //removing application
                    databaseApplications.child(application.get(pos).getApplicationId()).removeValue();
                    return "WITHDRAW";
                }
            }

        }


                return "NA";
    }

}
