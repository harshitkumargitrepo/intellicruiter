package app.recruiter.intellingent.intellicruiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String CLIENT_NAME = "app.recruiter.intellingent.jobinsightkey.clientname";
    public static final String CLIENT_EMAIL = "app.recruiter.intellingent.jobinsightkey.clientemail";
    public static final String CLIENT_INDUSTRY = "app.recruiter.intellingent.jobinsightkey.clientindustry";
    public static final String CLIENT_ID = "app.recruiter.intellingent.jobinsightkey.clientid";

    //view objects
    EditText editTextName;
    EditText editTextEmail;
    Spinner spinnerIndustries;
    Button buttonAddClient;
    ListView listViewClients;
    Button buttonViewAll;

    //a list to store all the client from firebase database
    List<Client> clients;

    //our database reference object
    DatabaseReference databaseClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseClients = database.getReference("clients");


        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        spinnerIndustries = (Spinner) findViewById(R.id.spinnerIndustries);
        listViewClients = (ListView) findViewById(R.id.listViewClients);

        buttonAddClient = (Button) findViewById(R.id.buttonAddClient);
        buttonViewAll = (Button) findViewById(R.id.buttonViewAll);
        //list to store clients
        clients = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addClient()
                //the method is defined below
                //this method is actually performing the write operation
                addClient();
            }
        });


        //adding an onclicklistener to button
        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecruiterRequestCard.class);
                startActivity(intent);
            }
        });


        //attaching listener to listview
        listViewClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Client client = clients.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), RequirementActivity.class);

                //putting client name and id to intent
                intent.putExtra(CLIENT_ID, client.getClientId());
                intent.putExtra(CLIENT_NAME, client.getClientName());
                intent.putExtra(CLIENT_EMAIL, client.getClientEmail());
                intent.putExtra(CLIENT_INDUSTRY, client.getClientIndustry());
                //starting the activity with intent
                startActivity(intent);
            }
        });


        listViewClients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Client client = clients.get(i);
                showUpdateDeleteDialog(client.getClientId(), client.getClientName(), client.getClientEmail(), client.getClientIndustry());
                return true;
            }
        });
    }

    /*
     * This method is saving a new client to the
     * Firebase Realtime Database
     * */
    private void addClient() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String industry = spinnerIndustries.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Client
            String id = databaseClients.push().getKey();

            //creating an Artist Object
            Client client = new Client(id, name, email, industry);
            String client_created = client.toString();
            //Saving the Artist
            databaseClients.child(id).setValue(client);

            //setting edittext to blank again
            editTextName.setText("");
            editTextEmail.setText("");

            //displaying a success toast
            Toast.makeText(this, "Client added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name and email", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseClients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                clients.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Client client = postSnapshot.getValue(Client.class);
                    //adding artist to the list
                    clients.add(client);
                }

                //creating adapter
                ClientList clientAdapter = new ClientList(ClientActivity.this, clients);
                //attaching adapter to the listview
                listViewClients.setAdapter(clientAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String clientId, final String clientName, String clientEmail, String clientIndustry) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_client, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.editTextEmail);
        final Spinner spinnerIndustries = (Spinner) dialogView.findViewById(R.id.spinnerIndustries);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateClient);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteClient);

        dialogBuilder.setTitle(clientName);
        editTextEmail.setText(clientEmail);
        spinnerIndustries.setSelection(((ArrayAdapter) spinnerIndustries.getAdapter()).getPosition(clientIndustry));
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString().trim();
                String industry = spinnerIndustries.getSelectedItem().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(industry)) {
                    updateClient(clientId, clientName, email, industry);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteClient(clientId);
                b.dismiss();

            }
        });
    }

    private boolean updateClient(String id, String name, String email, String industry) {
        //getting the specified client reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clients").child(id);

        //updating artist
        Client client = new Client(id, name, email, industry);
        dR.setValue(client);
        Toast.makeText(getApplicationContext(), "Client Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteClient(String id) {
        //getting the specified client reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clients").child(id);

        //removing client
        dR.removeValue();

        //getting the requirements reference for the specified client
        DatabaseReference drRequirements = FirebaseDatabase.getInstance().getReference("requirements").child(id);

        //removing all requirements
        drRequirements.removeValue();
        Toast.makeText(getApplicationContext(), "Client Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

}
