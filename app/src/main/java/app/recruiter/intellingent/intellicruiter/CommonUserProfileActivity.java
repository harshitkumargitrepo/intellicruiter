package app.recruiter.intellingent.intellicruiter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommonUserProfileActivity extends AppCompatActivity implements View.OnClickListener{



    //we will use these constants later to pass the user profile details to another activity

    // User Login information
    public static final String USER_ID = "app.recruiter.intellingent.intellicruiter.userid";
    public static final String USER_NAME = "app.recruiter.intellingent.intellicruiter.username";
    public static final String USER_EMAIL = "app.recruiter.intellingent.intellicruiter.useremail";

    //User personal information
    public static final String USER_MOBILE = "app.recruiter.intellingent.intellicruiter.usermobile";
    public static final String USER_DOB = "app.recruiter.intellingent.intellicruiter.userrole";
    public static final String USER_GENDER = "app.recruiter.intellingent.intellicruiter.usergender";

    //User Address
    public static final String USER_ADDRESS = "app.recruiter.intellingent.intellicruiter.useraddress";
    public static final String USER_CITY = "app.recruiter.intellingent.intellicruiter.usercity";
    public static final String USER_STATE = "app.recruiter.intellingent.intellicruiter.userstate";
    public static final String USER_COUNTRY = "app.recruiter.intellingent.intellicruiter.usercountry";


    // Screen Elements
    private EditText editTextName;
    private TextView textViewEmail;
    private EditText editTextMobile;
    private EditText editTextDob;
    private Spinner  spinnertGender;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextState;
    private EditText editTextCountry;
    private Spinner  spinnerType;
    private Button   buttonAddUser;


    //a list to store all the users from firebase database
    User user;

    //Logged in Username is fetched from database connection reference
    String email;

    //our database reference object
    DatabaseReference databaseUser;

    private ProgressDialog progressDialog;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //firebase auth object
    private FirebaseAuth firebaseAuth;


    //Background Animation
    RelativeLayout myProfileLayout;
    AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HARSHIT :","CommonUserProfileActivity :: onCreate");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_user_profile);


        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://intellicruiter.firebaseio.com/");
        databaseUser = database.getReference("users");

        progressDialog = new ProgressDialog(this);

        //getting views
        textViewEmail   = (TextView) findViewById(R.id.textViewEmail);
        buttonAddUser   = (Button)   findViewById(R.id.buttonAddUser);



        //attaching click listener
        buttonAddUser.setOnClickListener(this);

        //Setting the Email of the logged in User
        email = firebaseAuth.getCurrentUser().getEmail();
        textViewEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        // animation layout
        myProfileLayout = (RelativeLayout) findViewById(R.id.myProfileLayout);
        AnimateBackground(myProfileLayout);



        //initializing awesomevalidation object
        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */

        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextMobile, RegexTemplate.TELEPHONE, R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        //awesomeValidation.addValidation(this, R.id.editTextAddress, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextCity, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextState, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextCountry, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextDob, new SimpleCustomValidation() {
            @Override
            public boolean compare(String input) {
                // check if the age is >= 18
                try {
                    Calendar calendarBirthday = Calendar.getInstance();
                    Calendar calendarToday = Calendar.getInstance();
                    calendarBirthday.setTime(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(input));
                    int yearOfToday = calendarToday.get(Calendar.YEAR);
                    int yearOfBirthday = calendarBirthday.get(Calendar.YEAR);
                    if (yearOfToday - yearOfBirthday > 18) {
                        return true;
                    } else if (yearOfToday - yearOfBirthday == 18) {
                        int monthOfToday = calendarToday.get(Calendar.MONTH);
                        int monthOfBirthday = calendarBirthday.get(Calendar.MONTH);
                        if (monthOfToday > monthOfBirthday) {
                            return true;
                        } else if (monthOfToday == monthOfBirthday) {
                            if (calendarToday.get(Calendar.DAY_OF_MONTH) >= calendarBirthday.get(Calendar.DAY_OF_MONTH)) {
                                return true;
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }, R.string.err_birth);



    }
    @Override
    public void onClick(View view) {
        Log.i("HARSHIT :","CommonUserProfileActivity :: onClick");

        if(view == buttonAddUser){
            if (awesomeValidation.validate()) {
                Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

                //process the data further
                addUser();
            }
        }

    }

    public void addUser(){

        Log.i("HARSHIT :","CommonUserProfileActivity :: addUser");

        //getting views
        editTextName    = (EditText) findViewById(R.id.editTextName);
        spinnertGender  = (Spinner)  findViewById(R.id.spinnertGender);
        editTextMobile  = (EditText) findViewById(R.id.editTextMobile);
        editTextDob     = (EditText) findViewById(R.id.editTextDob);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextCity    = (EditText) findViewById(R.id.editTextCity);
        editTextState   = (EditText) findViewById(R.id.editTextState);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);
        spinnerType     = (Spinner)  findViewById(R.id.spinnerType);


        String Name     = editTextName.getText().toString().trim();
        String Gender   = spinnertGender.getSelectedItem().toString();
        String Mobile   = editTextMobile.getText().toString().trim();
        String Dob      = editTextDob.getText().toString().trim();
        String Address  = editTextAddress.getText().toString().trim();
        String City     = editTextCity.getText().toString().trim();
        String State    = editTextState.getText().toString().trim();
        String Country  = editTextCountry.getText().toString().trim();
        String Type     = spinnerType.getSelectedItem().toString();


        //checking if email and passwords are empty

        if(TextUtils.isEmpty(Type)){
            Toast.makeText(this,"Please enter how you want System to identify you",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Name)){
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Gender)){
            Toast.makeText(this,"Please enter Gender",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Mobile)){
            Toast.makeText(this,"Please enter Mobile",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Dob)){
            Toast.makeText(this,"Please enter Date of birth",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Address)){
            Toast.makeText(this,"Please enter Address",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(City)){
            Toast.makeText(this,"Please enter City",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(State)){
            Toast.makeText(this,"Please enter State",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Country)){
            Toast.makeText(this,"Please enter Country",Toast.LENGTH_LONG).show();
            return;
        }

        //if the all the fields are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Profile Saving Please Wait...");
        progressDialog.show();

        //checking if the value is provided
        if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Gender) && !TextUtils.isEmpty(Mobile) && !TextUtils.isEmpty(Dob) && !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(City) && !TextUtils.isEmpty(State) && !TextUtils.isEmpty(Country)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Client
            String id = databaseUser.push().getKey();

            //creating an Artist Object
            User user = new User(id, Name, email, Mobile, Dob, Gender, Address, City, State, Country,Type);
            //Saving the Artist
            databaseUser.child(id).setValue(user);

            //displaying a success toast
            Toast.makeText(this, "User Profile added", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            finish();

            if(user.getType().compareTo("Client") == 0 || user.getType().compareTo("Recruiter")==0) {
                // open app in client/recruiter mode
                Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                //putting client name and id to intent
                intent.putExtra(USER_NAME, user.getName());
                intent.putExtra(USER_EMAIL, user.getEmail());
                startActivity(intent);
            }
            else if(user.getType().compareTo("Candidate") ==0){
                // open app in candidate mode
                Intent intent = new Intent(getApplicationContext(), CandidateActivity.class);
                startActivity(intent);
            }
            else {
                // Fall back approach
                Intent intent = new Intent(getApplicationContext(), Introduction.class);
                startActivity(intent);
            }

        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Failed to save User Profile. Try Again!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }

    public void AnimateBackground (View view){
        animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();
    }


}
