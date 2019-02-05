package app.recruiter.intellingent.intellicruiter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecruiterRequestCard extends AppCompatActivity implements View.OnClickListener {


    //a list to store all the client from firebase database
    List<Client> clients;
    int count_clients;

    //a list to store all the client requirements from firebase database
    List<Requirement> requirements;
    int count_requirements;

    //a list to store all the requirements to be shown to recruiter
    ArrayList<Map<String, String>> requirementList;
    ArrayList<List<String>> requirementBackup;
    ArrayList<List<String>> clientBackup;

    //the recyclerview
    RecyclerView recyclerView;

    //our database reference object
    DatabaseReference databaseClients;
    DatabaseReference databaseRequirements;

    //Elements
    Button backToClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("Harshit Flow: "," A)          onCreate");
        setContentView(R.layout.view_recruiter_requirement);

        backToClient = findViewById(R.id.backToClient);

        requirementBackup = new ArrayList<>();
        clientBackup = new ArrayList<>();


        //initializing the requirementlist
        clients = new ArrayList<>();
        requirements = new ArrayList<>();
        requirementList = new ArrayList<>();

        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseClients = database.getReference("clients");
        databaseRequirements = FirebaseDatabase.getInstance().getReference("requirements/");

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backToClient.setOnClickListener(this);


    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.i("Harshit Flow: "," B)          onStart");

        databaseRequirements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("Harshit Flow: ","databaseRequirements :: onDataChange");
                requirements.clear();
                requirementBackup.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String clientID = postSnapshot.getKey();
                    for (DataSnapshot Snapshot : postSnapshot.getChildren()) {
                        Requirement requirement = Snapshot.getValue(Requirement.class);
                        requirements.add(requirement);

                        List<String> req = new ArrayList<String>();
                        req.add(clientID);
                        req.add(String.valueOf(requirement.getRequirementId()));
                        req.add(String.valueOf(requirement.getRequirementTitle()));
                        req.add(String.valueOf(requirement.getRequirementDescription()));
                        req.add(String.valueOf(requirement.getNumberOfPositions()));
                        requirementBackup.add(req);
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
                Log.i("Harshit Flow: ","databaseClients      :: onDataChange");
                //clearing the previous client list and requirementList
                clients.clear();
                clientBackup.clear();


                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting client
                    Client client = postSnapshot.getValue(Client.class);
                    //adding client to the list
                    clients.add(client);


                    List<String> clnt = new ArrayList<String>();
                    clnt.add(client.getClientId());
                    clnt.add(client.getClientName());
                    clnt.add(client.getClientEmail());
                    clnt.add(client.getClientIndustry());
                    clientBackup.add(clnt);

                }

                Log.i("Harshit Action: ","   *************        Calling createupdateRequirementList from databaseClients  ::  onStart() :: onDataChange       *************");
                createupdateRequirementList(clientBackup, requirementBackup);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == backToClient) {

            startActivity(new Intent(this, ClientActivity.class));
            finish();
        }

    }


    private void createupdateRequirementList(ArrayList<List<String>> _clients, ArrayList<List<String>> _requirements) {
        Log.i("Harshit Flow: ","createupdateRequirementList");
        requirementList.clear();

        for (int i = 0; i < _requirements.size(); i++) {
            for (int j = 0; j < _clients.size(); j++) {

                String client_ID = _clients.get(j).get(0);
                String requirement_client_ID = _requirements.get(i).get(0);
                Map<String, String> clientrequirementmap = new HashMap<>();

                if (client_ID.equals(requirement_client_ID)) {

                    clientrequirementmap.clear();
                    clientrequirementmap.put("clientId", _clients.get(j).get(0));
                    clientrequirementmap.put("clientName", _clients.get(j).get(1));
                    clientrequirementmap.put("clientEmail", _clients.get(j).get(2));
                    clientrequirementmap.put("clientIndustry", _clients.get(j).get(3));
                    clientrequirementmap.put("requirementId", _requirements.get(i).get(1));
                    clientrequirementmap.put("requirementTitle", _requirements.get(i).get(2));
                    clientrequirementmap.put("requirementDescription", _requirements.get(i).get(3));
                    clientrequirementmap.put("numberOfPositions", _requirements.get(i).get(4));

                    requirementList.add(clientrequirementmap);
                }
                if (!clientrequirementmap.isEmpty()) {

                }
            }
        }


        Log.i("Harshit Data: ","requirementList Size = "+requirementList.size());

        //creating recyclerview adapter
        RequirementRecycleAdapter adapter = new RequirementRecycleAdapter(this, requirementList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }


}
