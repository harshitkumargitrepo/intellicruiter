package app.recruiter.intellingent.intellicruiter;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CandidateHomeTabFragment extends Fragment {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private String globalemailID;
    private String globalCandidateID;
    private String candidateName;
    private String candidateEmail;
    private Boolean profileExists = Boolean.FALSE;

    DatabaseReference databaseCandidates;
    DatabaseReference databaseUsers;

    //a list to store all the candidate information from firebase database
    List<Candidate> candidates;
    List<User> users;

    // Intilize an array list from array
     List<Skill> skillsList = new ArrayList();
    private int skillcount =0;


    Spinner spinnerAvailability;
    EditText editTextSkills;

    RelativeLayout HomeFragment;
    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private Button mButtonAdd;

    private Button update_create_candidate;
    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView textViewName;
    private TextView textViewEmail;

    //we will use these constants later to pass the artist name and id to another activity
    public static final String CANDIDATE_NAME = "app.recruiter.intellingent.jobinsightkey.candidatename";
    public static final String CANDIDATE_EMAIL = "app.recruiter.intellingent.jobinsightkey.candidateemail";
    public static final String CANDIDATE_AVAILABILITY = "app.recruiter.intellingent.jobinsightkey.candidateavailability";
    public static final String CANDIDATE_ID = "app.recruiter.intellingent.jobinsightkey.candidateid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        /**
         * DATA PROCESSING
         */

        //initializing the requirement and user list
        candidates = new ArrayList<>();
        users = new ArrayList<>();
        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseCandidates = database.getReference("candidates");
        databaseUsers      = database.getReference("users");
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        globalemailID = firebaseAuth.getCurrentUser().getEmail();
        globalCandidateID = null;
        candidateName =null;
        candidateEmail = null;
        return inflater.inflate(R.layout.tab_fragment_home_candidate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




         spinnerAvailability     = (Spinner) view.findViewById(R.id.spinnerAvailability);


        spinnerAvailability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String avaliability=spinnerAvailability.getSelectedItem().toString();
                if(avaliability.compareTo("Available") ==0)
                    spinnerAvailability.setBackgroundColor(Color.parseColor("#00FF00"));
                else
                    spinnerAvailability.setBackgroundColor(Color.parseColor("#ff0000"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



         update_create_candidate = (Button)  view.findViewById(R.id.update_create_candidate);
         textViewName            = (TextView)view.findViewById(R.id.textViewName);
         textViewEmail           = (TextView)view.findViewById(R.id.textViewEmail);

        update_create_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addCandidate()
                //the method is defined below
                //this method is actually performing the write operation
                checkIfCandidateProfileExists(users,candidates);
                if(profileExists) {
                    updateCandidate(globalCandidateID);
                }
                else {
                 addCandidate();
                }
            }
        });




        update_create_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addCandidate()
                //the method is defined below
                //this method is actually performing the write operation
                checkIfCandidateProfileExists(users,candidates);
                if(profileExists) {
                    updateCandidate(globalCandidateID);
                }
                else {
                    addCandidate();
                }
            }
        });


        // Get the application fragement context

        HomeFragment = (RelativeLayout) view;
        mButtonAdd = (Button) HomeFragment.findViewById(R.id.btn_add);
        mRecyclerView = (RecyclerView) HomeFragment.findViewById(R.id.recycler_view);

        // Get the application fragement context
        mContext = getContext();

        // Define a layout for RecyclerView
        mLayoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new SkillAdapter(mContext,skillsList);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        editTextSkills = (EditText) HomeFragment.findViewById(R.id.editTextSkills);

        // Set a click listener for add item button
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Specify the position
                int position = 0;

                // Add an item to skills list
                String skillname = editTextSkills.getText().toString().trim();
                editTextSkills.setText("");
                Skill skill = new Skill(String.valueOf(++skillcount),skillname,"0");
                skillsList.add(position,skill);

                /*
                    public final void notifyItemInserted (int position)
                        Notify any registered observers that the item reflected at position has been
                        newly inserted. The item previously at position is now at position position + 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                    position : Position of the newly inserted item in the data set

                */

                // Notify the adapter that an item inserted
                mAdapter.notifyItemInserted(position);

                /*
                    public void scrollToPosition (int position)
                        Convenience method to scroll to a certain position. RecyclerView does not
                        implement scrolling logic, rather forwards the call to scrollToPosition(int)

                    Parameters
                    position : Scroll to this adapter position

                */
                // Scroll to newly added item position
                mRecyclerView.scrollToPosition(position);

                // Show the added item label
                Toast.makeText(mContext,"Added : " + skillname,Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();



        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String userID = postSnapshot.getKey();
                    User user     = postSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        databaseCandidates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                candidates.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String candidateID = postSnapshot.getKey();
                    Candidate candidate = postSnapshot.getValue(Candidate.class);
                    candidates.add(candidate);
                }

                LoadProfile(users,candidates);
//                if(profileExists) {
//                    Toast.makeText(mContext,"Profile Load Successful ",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(mContext,"Fresh Profile. Kindly load your profile. ",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void checkIfCandidateProfileExists(  List<User> user,   List<Candidate> candidate){

        profileExists = Boolean.FALSE;
//        for (int i = 0; i<user.size();i++)
//        {

        for(int i =0 ; i< user.size();i++)
            if(user.get(i).getEmail().compareTo(globalemailID) ==0)
                candidateName  = user.get(i).getName();

        candidateEmail = globalemailID;

        textViewName.setText(candidateName);
        textViewEmail.setText(candidateEmail);


            for(int j =0; j< candidate.size();j++){

                if(candidateEmail.compareTo(candidate.get(j).getCandidateEmail()) == 0){
                    globalCandidateID = candidate.get(j).getCandidateId();
//                    candidateName  = user.get(i).getName();
//                    candidateEmail = user.get(i).getEmail();
//                    skillsList = candidate.get(j).getCandidateSkills();
//                    spinnerAvailability.setSelection(((ArrayAdapter)spinnerAvailability.getAdapter()).getPosition(candidate.get(j).getCandidateAvailability()));
                    profileExists = Boolean.TRUE;
                }
            }
//        }
    }



    public void LoadProfile(  List<User> user,   List<Candidate> candidate){

        profileExists = Boolean.FALSE;

        for(int i =0 ; i< user.size();i++)
            if(user.get(i).getEmail().compareTo(globalemailID) ==0)
                candidateName  = user.get(i).getName();

        textViewName.setText(candidateName);
        textViewEmail.setText(globalemailID);

//        for (int i = 0; i<user.size();i++)
//        {
            for(int j =0; j< candidate.size();j++){

                if(globalemailID.compareTo(candidate.get(j).getCandidateEmail()) == 0){
                    globalCandidateID = candidate.get(j).getCandidateId();
//                    candidateName  = user.get(i).getName();
//                    candidateEmail = user.get(i).getEmail();
                    candidateEmail = candidate.get(j).getCandidateEmail();

                    if(candidate.get(j).getCandidateSkills() != null) {
                        skillsList = candidate.get(j).getCandidateSkills();
                        // Initialize a new instance of RecyclerView Adapter instance
                        mAdapter = new SkillAdapter(mContext, skillsList);

                        // Set the adapter for RecyclerView
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    textViewName.setText(candidateName);
                    textViewEmail.setText(candidateEmail);
                    spinnerAvailability.setSelection(((ArrayAdapter)spinnerAvailability.getAdapter()).getPosition(candidate.get(j).getCandidateAvailability()));
                    if(candidate.get(j).getCandidateAvailability().compareTo("Available") ==0)
                    spinnerAvailability.setBackgroundColor(Color.parseColor("#00FF00"));
                    else
                        spinnerAvailability.setBackgroundColor(Color.parseColor("#ff0000"));

                    //creating fragment object
                    Fragment CandidateProfileFragment = null;
                    Bundle args = new Bundle();
                    CandidateProfileFragment = new CandidateJobTabFragment();
                    args.putString(CandidateHomeTabFragment.CANDIDATE_ID, globalCandidateID);
                    args.putString(CandidateHomeTabFragment.CANDIDATE_NAME, candidateName);
                    args.putString(CandidateHomeTabFragment.CANDIDATE_EMAIL, globalemailID);
                    args.putString(CandidateHomeTabFragment.CANDIDATE_AVAILABILITY, candidate.get(j).getCandidateAvailability());
                    CandidateProfileFragment.setArguments(args);

                    profileExists = Boolean.TRUE;
                }
            }
//        }
    }

    public void updateCandidate(String globalcandidateId){


         List<Skill> candidateSkills   = new ArrayList<>();
         candidateSkills = skillsList;

         String candidateAvailability  = spinnerAvailability.getSelectedItem().toString();

        //getting the specified client reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("candidates").child(globalcandidateId);
        //updating Candidate
        Candidate candidate = new Candidate(globalemailID, globalcandidateId,  candidateSkills, candidateAvailability);
        dR.setValue(candidate);
        Toast.makeText(getContext(), "Candidate Updated", Toast.LENGTH_LONG).show();
        LoadProfile(users,candidates);

        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition(0, 0);
    }


    public void addCandidate(){

        /******STEP 1 *********/
        //getting a unique id using push().getKey() method
        //it will create a unique id and we will use it as the Primary Key for our Candidate
        String id = databaseCandidates.push().getKey();

        /******STEP 2 *********/
        // Get all the required variables
        List<Skill> candidateSkills =new ArrayList<>();
        candidateSkills = skillsList;

        String candidateAvailability  = spinnerAvailability.getSelectedItem().toString();
        /******STEP 3 *********/
         //creating an Candidate Object
        Candidate candidate = new Candidate(globalemailID , id,  candidateSkills, candidateAvailability);

        /******STEP 4 *********/
        //Saving the Artist
        databaseCandidates.child(id).setValue(candidate);

        Toast.makeText(getContext(), "Candidate Added", Toast.LENGTH_LONG).show();
    }
}