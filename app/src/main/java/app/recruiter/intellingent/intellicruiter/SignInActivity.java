package app.recruiter.intellingent.intellicruiter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.UnknownServiceException;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String USER_NAME = "app.recruiter.intellingent.jobinsightkey.clientname";
    public static final String USER_EMAIL = "app.recruiter.intellingent.jobinsightkey.clientemail";

    //defining views
    private Button buttonSignIn;
    private Button welcome;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    //our database reference object
    DatabaseReference databaseUser;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    //Background Animation
    RelativeLayout mySignInLayout;
        AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HARSHIT :","SignInActivity :: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // animation layout
        mySignInLayout = (RelativeLayout) findViewById(R.id.mySignInLayout);
        AnimateBackground(mySignInLayout);

        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://intellicruiter.firebaseio.com/");
        databaseUser = database.getReference("users");

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {

            // Check if the user profile is already generated
            CheckifUserProfileAlreadyCreated();
            //close this activity
            finish();

        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        welcome = (Button) findViewById(R.id.welcome);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);

        editTextEmail.setBackgroundColor(Color.TRANSPARENT);
        editTextPassword.setBackgroundColor(Color.TRANSPARENT);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        welcome.setOnClickListener(this);
    }

    //method for user login
    private void userLogin(){

        Log.i("HARSHIT :","SignInActivity :: userLogin");
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Signing in Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity

                            // Check if the user profile is already generated
                            CheckifUserProfileAlreadyCreated();

                        }
                        else{
                            Toast.makeText(SignInActivity.this,"Login Error: Check Email/Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        Log.i("HARSHIT :","SignInActivity :: onClick");
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
        if(view == welcome){
            finish();
            startActivity(new Intent(this, Introduction.class));
        }
    }

    public void AnimateBackground (View view){
        animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();
    }

    public void CheckifUserProfileAlreadyCreated(){
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean found = Boolean.FALSE;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.i("HARSHIT :", "SignInActivity ::  CheckifUserProfileAlreadyCreated  :: onDataChange  ::  " + postSnapshot.child("email").getValue().toString());
                    User user = postSnapshot.getValue(User.class);

                    if (firebaseAuth.getCurrentUser().getEmail().compareTo(postSnapshot.child("email").getValue().toString()) == 0) {
                        finish();
                        found = Boolean.TRUE;
                        if (user.getType().compareTo("Client") == 0 || user.getType().compareTo("Recruiter") == 0) {
                            // open app in client/recruiter mode
                            Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                            //putting client name and id to intent
                            intent.putExtra(USER_NAME, postSnapshot.child("name").getValue().toString());
                            intent.putExtra(USER_EMAIL, postSnapshot.child("email").getValue().toString());
                            startActivity(intent);

                        } else if (user.getType().compareTo("Candidate") == 0) {
                            // open app in candidate mode
                            Intent intent = new Intent(getApplicationContext(), CandidateActivity.class);
                            startActivity(intent);

                        } else {
                            // Fall back approach
                            Intent intent = new Intent(getApplicationContext(), Introduction.class);
                            startActivity(intent);
                        }

                    }
                }
                    if(!found){
                        //if not, creating an intent
                        Intent intent = new Intent(getApplicationContext(), CommonUserProfileActivity.class);
                        //starting the activity with intent
                        startActivity(intent);
                        finish();
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}