package app.recruiter.intellingent.intellicruiter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateJobTabFragment extends Fragment {

    //a list to store all the client requirements from firebase database
    List<Requirement> requirements;
    List<Client> clients;
    List<Application> applications;
    List<Candidate> candidates;
    //a list to store all the requirements to be shown to recruiter
    ArrayList<Map<String, String>> requirementList;
    List<RecommendedRequirements> recommendedrequirements;

    FirebaseAuth firebaseAuth;
    //our database reference object
    DatabaseReference databaseClients;
    DatabaseReference databaseRequirements;
    DatabaseReference databaseCandidates;
    DatabaseReference databaseApplications;
    Bundle bundle;
    private RecyclerView mRecyclerViewRecommendations;
    private RecyclerView.Adapter mAdapterRecommendations;
    private RecyclerView.LayoutManager mLayoutManagerRecomendations;
    private RecyclerView mRecyclerViewRequirements;
    private RecyclerView.Adapter mAdapterRequirements;
    private RecyclerView.LayoutManager mLayoutManagerRequirements;
    private ArrayList<String> myDataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_job_candidate, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /************** Top N Recommendations ****************/
        mRecyclerViewRecommendations = (RecyclerView) view.findViewById(R.id.recycler_view_recommendations);
        mRecyclerViewRecommendations.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerRecomendations = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewRecommendations.setLayoutManager(mLayoutManagerRecomendations);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        for(int i=1 ; i <=10; i++){
            myDataset.add("New Top Title #"+i);
        }





        /************** All open requirements from recruiter ****************/
        mRecyclerViewRequirements = (RecyclerView) view.findViewById(R.id.recycler_view_requirements);
        mRecyclerViewRequirements.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerRequirements = new LinearLayoutManager(getContext());
        mRecyclerViewRequirements.setLayoutManager(mLayoutManagerRequirements);


        /**
         * DATA PROCESSING
         */

        //initializing the requirement list
        clients = new ArrayList<>();
        requirements = new ArrayList<>();
        requirementList = new ArrayList<>();
        candidates = new ArrayList<>();
        applications = new ArrayList<>();


        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseCandidates    = database.getReference("candidates");
        databaseClients       = database.getReference("clients");
        databaseRequirements  = database.getReference("requirements/");
        databaseApplications  = database.getReference("applications");

//        // specify an adapter (see also next example)
//        myDataset = new ArrayList<>();
//        for(int i=1 ; i <=10; i++){
//            myDataset.add("New All Title #"+i);
//        }
//        mAdapterRequirements = new CandidateAllJobAdapter(myDataset);
//        mRecyclerViewRequirements.setAdapter(mAdapterRequirements);
    }


    @Override
    public void onStart() {
        super.onStart();


        databaseRequirements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                requirements.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String clientID = postSnapshot.getKey();
                    for (DataSnapshot Snapshot : postSnapshot.getChildren()) {
                        Requirement requirement = Snapshot.getValue(Requirement.class);
                        requirements.add(requirement);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attaching value event listener
        databaseClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous client list and requirementList
                clients.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting client
                    Client client = postSnapshot.getValue(Client.class);
                    //adding client to the list
                    clients.add(client);
                }
             createupdateRequirementList(clients, requirements);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attaching value event listener
        databaseCandidates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous client list and requirementList
                candidates.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting client
                    Candidate candidate = postSnapshot.getValue(Candidate.class);
                    //adding client to the list
                    candidates.add(candidate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //attaching value event listener
        databaseApplications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous client list and requirementList
                applications.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting client
                    Application application = postSnapshot.getValue(Application.class);
                    //adding client to the list
                    applications.add(application);
                }
                RecommendationGenerator( clients, requirements, candidates,  applications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void createupdateRequirementList( List<Client> _clients,  List<Requirement> _requirements) {

        requirementList.clear();

        bundle = this.getArguments();

        for (int i = 0; i < _requirements.size(); i++) {
            for (int j = 0; j < _clients.size(); j++) {

                String client_ID = _clients.get(j).getClientId();
                String requirement_client_ID = _requirements.get(i).getClientId();
                Map<String, String> clientrequirementmap = new HashMap<>();

                if (client_ID.equals(requirement_client_ID)) {

                    clientrequirementmap.clear();
                    clientrequirementmap.put("clientId", _clients.get(j).getClientId());
                    clientrequirementmap.put("clientName", _clients.get(j).getClientName());
                    clientrequirementmap.put("clientIndustry", _clients.get(j).getClientIndustry());
                    clientrequirementmap.put("requirementId", _requirements.get(i).getRequirementId());
                    clientrequirementmap.put("requirementTitle", _requirements.get(i).getRequirementTitle());
                    clientrequirementmap.put("requirementDescription", _requirements.get(i).getRequirementDescription());
                    clientrequirementmap.put("numberOfPositions", String.valueOf(_requirements.get(i).getNumberOfPositions()));

                    requirementList.add(clientrequirementmap);
                }
                if (!clientrequirementmap.isEmpty()) {

                }
            }
        }


        //creating recyclerview adapter
        mAdapterRequirements = new CandidateAllJobAdapter(requirementList, requirements, clients, applications, candidates, firebaseAuth.getCurrentUser().getEmail());

        //setting adapter to recyclerview
        mRecyclerViewRequirements.setAdapter(mAdapterRequirements);

    }


    private void RecommendationGenerator( List<Client> clients, List<Requirement> requirements, List<Candidate> candidates, List<Application> applications) {

        RecruitmentRecommendationSystem RecommendationHandler = new RecruitmentRecommendationSystem(clients, requirements, candidates,  applications);
        recommendedrequirements = new ArrayList<>();
        recommendedrequirements = RecommendationHandler.getRecommendations(firebaseAuth.getCurrentUser().getEmail());

        mAdapterRecommendations = new CandidateTopJobAdapter(requirements,clients, applications,candidates, firebaseAuth.getCurrentUser().getEmail(), recommendedrequirements);
        mRecyclerViewRecommendations.setAdapter(mAdapterRecommendations);

    }

}
