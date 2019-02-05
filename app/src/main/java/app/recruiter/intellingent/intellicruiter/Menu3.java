package app.recruiter.intellingent.intellicruiter;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Menu3 extends Fragment {
    //Background Animation
    RelativeLayout HomeFragment;
    AnimationDrawable fragmentAnimationDrawable;

    //a list to store all the client requirements from firebase database
    List<Requirement> requirements;
    List<Client> clients;
    List<Application> applications;
    List<Candidate> candidates;
    List<User> users;

    //our database reference object
    DatabaseReference databaseClients;
    DatabaseReference databaseRequirements;
    DatabaseReference databaseCandidates;
    DatabaseReference databaseApplications;
    DatabaseReference databaseUsers;
    FirebaseDatabase database;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private Context mContext;

    RelativeLayout mRelativeLayoutApplication;
    private RecyclerView mRecyclerViewApplication;
    private RecyclerView.Adapter mAdapterApplication;
    private RecyclerView.LayoutManager mLayoutManagerApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_3, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 3");
        HomeFragment = (RelativeLayout) view;
        fragmentAnimationDrawable = (AnimationDrawable) HomeFragment.getBackground();
        fragmentAnimationDrawable.setEnterFadeDuration(2500);
        fragmentAnimationDrawable.setExitFadeDuration(2500);
        fragmentAnimationDrawable.start();


        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseCandidates    = database.getReference("candidates");
        databaseClients       = database.getReference("clients");
        databaseRequirements  = database.getReference("requirements");
        databaseApplications  = database.getReference("applications");
        databaseUsers         = database.getReference("users");


        //initializing the requirement list
        clients = new ArrayList<>();
        requirements = new ArrayList<>();
        users = new ArrayList<>();
        candidates = new ArrayList<>();
        applications = new ArrayList<>();


        /************** Display Applications ****************/
        mRecyclerViewApplication = (RecyclerView) view.findViewById(R.id.recycler_view_applications);
        mRecyclerViewApplication.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManagerApplication = new LinearLayoutManager(getContext());
        mRecyclerViewApplication.setLayoutManager(mLayoutManagerApplication);


    }


    @Override
    public void onStart() {
        super.onStart();


        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        users.add(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                processApplication(users, clients, requirements, candidates, applications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void processApplication(List<User> user, List<Client> client, List<Requirement> requirement, List<Candidate> candidate, List<Application> application){


        List<Application> application_open = new ArrayList<>();
        int intialsize = application.size();
        for(int i =0; i<intialsize;i++)
        {
            if(application.get(i).getStatus().compareTo("ACCEPTED")!=0){
                application_open.add(application.get(i));
            }
        }
        //creating recyclerview adapter
        mAdapterApplication = new ApplicationAdapter(user, requirement,client,application_open,candidate);

        //setting adapter to recyclerview
        mRecyclerViewApplication.setAdapter(mAdapterApplication);
    }
}