package app.recruiter.intellingent.intellicruiter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Menu2 extends Fragment {
    //Background Animation
    RelativeLayout HomeFragment;
    AnimationDrawable fragmentAnimationDrawable;
    private Context mContext;

    private int skillcount =0;
    TextView textViewClient;

    EditText editTextRequirementTitle;
    EditText editTextRequirementDesc;
    EditText editTextPositions;
    EditText editTextSkills;

    Button buttonAddRequirement;

    ListView listViewRequirements;

    DatabaseReference databaseRequirements;
    List<Requirement> requirements;

    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private Button mButtonAdd;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    // Intilize an array list from array
    final List<Skill> skillsList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_requirement, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 2");
        HomeFragment = (RelativeLayout) view;
        fragmentAnimationDrawable = (AnimationDrawable) HomeFragment.getBackground();
        fragmentAnimationDrawable.setEnterFadeDuration(2500);
        fragmentAnimationDrawable.setExitFadeDuration(2500);
        fragmentAnimationDrawable.start();

        Bundle bundle = this.getArguments();
//        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node requirement we are creating a new child with the client id
         * and inside that node we will store all the requirements with unique ids
         * */

//        databaseRequirements = FirebaseDatabase.getInstance().getReference("requirements").child( intent.getStringExtra(ClientActivity.CLIENT_ID));
        databaseRequirements = FirebaseDatabase.getInstance().getReference("requirements").child( bundle.getString(Menu1.CLIENT_ID));
        buttonAddRequirement = (Button) HomeFragment.findViewById(R.id.buttonAddRequirement);
        editTextRequirementTitle = (EditText) HomeFragment.findViewById(R.id.editTextRequirementTitle);
        editTextRequirementDesc = (EditText) HomeFragment.findViewById(R.id.editTextRequirementDesc);
        editTextPositions = (EditText) HomeFragment.findViewById(R.id.editTextPositions);

        textViewClient = (TextView) HomeFragment.findViewById(R.id.textViewClient);

        listViewRequirements = (ListView) HomeFragment.findViewById(R.id.listViewRequirements);

        requirements = new ArrayList<>();

        textViewClient.setText(bundle.getString(Menu1.CLIENT_NAME));


        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) HomeFragment.findViewById(R.id.rl);
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


        buttonAddRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRequirement();
            }
        });

        listViewRequirements.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Requirement requirement = requirements.get(i);
                showUpdateDeleteDialog(requirement.getRequirementId(), requirement.getRequirementTitle(),requirement.getRequirementDescription(),requirement.getNumberOfPositions(), requirement.getSkill());
                return true;
            }
        });

        
    }


    private void saveRequirement() {
        String RequirementTitle = editTextRequirementTitle.getText().toString().trim();
        String RequirementDesc = editTextRequirementDesc.getText().toString().trim();
        String Positions = editTextPositions.getText().toString().trim();
        if (!TextUtils.isEmpty(RequirementTitle) && !TextUtils.isEmpty(RequirementDesc) && !TextUtils.isEmpty(Positions)) {
            String id  = databaseRequirements.push().getKey();
            Bundle bundle = this.getArguments();
            Requirement requirement = new Requirement(bundle.getString(Menu1.CLIENT_ID),id, RequirementTitle, RequirementDesc,Integer.parseInt(Positions),skillsList);
            databaseRequirements.child(id).setValue(requirement);
            Toast.makeText(getContext(), "Requirement saved", Toast.LENGTH_LONG).show();
            editTextRequirementTitle.setText("");
            editTextRequirementDesc.setText("");
            editTextPositions.setText("");
            // Initialize a new instance of RecyclerView Adapter instance
            skillsList.clear();
            mAdapter = new SkillAdapter(mContext,skillsList);

            // Set the adapter for RecyclerView
            mRecyclerView.setAdapter(mAdapter);

        } else {
            Toast.makeText(getContext(), "Please enter all requirement inputs", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseRequirements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requirements.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Requirement requirement = postSnapshot.getValue(Requirement.class);
                    requirements.add(requirement);
                }
                RequirementList requirementListAdapter = new RequirementList(getActivity(), requirements);
                listViewRequirements.setAdapter(requirementListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String requirementId, String RequirementTitle, String RequirementDesc, int  RequirementPositions, List<Skill> skill) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_requirement, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDesc = (EditText) dialogView.findViewById(R.id.editTextDesc);
        final EditText editTextPosition = (EditText) dialogView.findViewById(R.id.editTextPosition);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateRequirement);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteRequirement);
        final List<Skill> skills = skill;
        dialogBuilder.setTitle(RequirementTitle);

        editTextTitle.setText(RequirementTitle);
        editTextDesc.setText(RequirementDesc);
        editTextPosition.setText(String.valueOf(RequirementPositions));


        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDesc.getText().toString().trim();
                String position = editTextPosition.getText().toString().trim();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(position)) {
                    updateRequirement(requirementId, title, description, position,skills);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRequirement(requirementId);
                b.dismiss();


            }
        });
    }

    private boolean updateRequirement(String id, String title, String description, String position,List<Skill> skill) {

//        Intent intent = getIntent();
        Bundle bundle = this.getArguments();
        //getting the specified requirement reference
//        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(intent.getStringExtra(ClientActivity.CLIENT_ID));
        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(bundle.getString(Menu1.CLIENT_ID));
        //updating requirement

        Requirement requirement = new Requirement(bundle.getString(Menu1.CLIENT_ID), id, title, description, Integer.parseInt(position),skill );
        drRequirements.child(id).setValue(requirement);
        Toast.makeText(getContext(), "Requirement Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteRequirement(String id) {

//        Intent intent = getIntent();
        Bundle bundle = this.getArguments();
        //getting the requirements reference for the specified client
//        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(intent.getStringExtra(ClientActivity.CLIENT_ID));
        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(bundle.getString(Menu1.CLIENT_ID));
        //removing all requirements
        drRequirements.child(id).removeValue();
        Toast.makeText(getContext(), "Requirement Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
}