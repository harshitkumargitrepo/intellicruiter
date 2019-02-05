package app.recruiter.intellingent.intellicruiter;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequirementActivity extends AppCompatActivity {

    TextView textViewClient;

    EditText editTextRequirementTitle;
    EditText editTextRequirementDesc;
    EditText editTextPositions;

    Button buttonAddRequirement;

    ListView listViewRequirements;

    DatabaseReference databaseRequirements;

    List<Requirement> requirements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);


        Intent intent = getIntent();

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node requirement we are creating a new child with the client id
         * and inside that node we will store all the requirements with unique ids
         * */

        databaseRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(intent.getStringExtra(ClientActivity.CLIENT_ID));

        buttonAddRequirement = (Button) findViewById(R.id.buttonAddRequirement);
        editTextRequirementTitle = (EditText) findViewById(R.id.editTextRequirementTitle);
        editTextRequirementDesc = (EditText) findViewById(R.id.editTextRequirementDesc);
        editTextPositions = (EditText) findViewById(R.id.editTextPositions);

        textViewClient = (TextView) findViewById(R.id.textViewClient);

        listViewRequirements = (ListView) findViewById(R.id.listViewRequirements);

        requirements = new ArrayList<>();

        textViewClient.setText(intent.getStringExtra(ClientActivity.CLIENT_NAME));

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
                showUpdateDeleteDialog(requirement.getRequirementId(), requirement.getRequirementTitle(),requirement.getRequirementDescription(),requirement.getNumberOfPositions());
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
            final List<Skill> skillsList = new ArrayList();
            Intent intent = getIntent();

            Requirement requirement = new Requirement(intent.getStringExtra(ClientActivity.CLIENT_ID),id, RequirementTitle, RequirementDesc,Integer.parseInt(Positions), skillsList);
            databaseRequirements.child(id).setValue(requirement);
            Toast.makeText(this, "Requirement saved", Toast.LENGTH_LONG).show();
            editTextRequirementTitle.setText("");
            editTextRequirementDesc.setText("");
            editTextPositions.setText("");

        } else {
            Toast.makeText(this, "Please enter all requirement inputs", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseRequirements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requirements.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Requirement requirement = postSnapshot.getValue(Requirement.class);
                    requirements.add(requirement);
                }
                RequirementList requirementListAdapter = new RequirementList(RequirementActivity.this, requirements);
                listViewRequirements.setAdapter(requirementListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String requirementId, String RequirementTitle, String RequirementDesc, int  RequirementPositions) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_requirement, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDesc = (EditText) dialogView.findViewById(R.id.editTextDesc);
        final EditText editTextPosition = (EditText) dialogView.findViewById(R.id.editTextPosition);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateRequirement);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteRequirement);

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
                    updateRequirement(requirementId, title, description, position);
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

    private boolean updateRequirement(String id, String title, String description, String position) {

        Intent intent = getIntent();

        //getting the specified requirement reference
        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(intent.getStringExtra(ClientActivity.CLIENT_ID));

        //updating requirement
        final List<Skill> skillsList = new ArrayList();
        Intent intent2 = getIntent();
        Requirement requirement = new Requirement(intent2.getStringExtra(ClientActivity.CLIENT_ID), id, title, description, Integer.parseInt(position), skillsList );
        drRequirements.child(id).setValue(requirement);
        Toast.makeText(getApplicationContext(), "Requirement Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteRequirement(String id) {

        Intent intent = getIntent();

        //getting the requirements reference for the specified client
        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(intent.getStringExtra(ClientActivity.CLIENT_ID));

        //removing all requirements
        drRequirements.child(id).removeValue();
        Toast.makeText(getApplicationContext(), "Requirement Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

}

