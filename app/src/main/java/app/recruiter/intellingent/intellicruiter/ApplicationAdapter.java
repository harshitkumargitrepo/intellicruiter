package app.recruiter.intellingent.intellicruiter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ApplicationAdapter  extends RecyclerView.Adapter<ApplicationAdapter.MyViewHolder>   {


    public static List<Requirement> requirements;
    public static List<Client> clients;
    public static List<Application> applications;
    public static List<Candidate> candidates;
    public static List<User> users;


    public ApplicationAdapter(List<User> user, List<Requirement> requirement,List<Client> client,List<Application> application,List<Candidate> candidate){
        this.requirements = requirement;
        this.clients = client;
        this.applications = application;
        this.candidates = candidate;
        this.users = user;
        Log.i("Application_TAG","ApplicationAdapter Constructor called");
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        Log.i("Application_TAG","onCreateViewHolder called | position value :"+i);
        // create a new view
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.application_list, viewGroup, false);
        ApplicationAdapter.MyViewHolder vh = new ApplicationAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {

        Log.i("Application_TAG","onBindViewHolder called | position value :"+i);


        String CandidateName = null;
        for(int pos = 0; pos<users.size();pos++)
            if(users.get(pos).getEmail().compareTo(applications.get(i).getCandidate().getCandidateEmail()) ==0)
                CandidateName = users.get(pos).getName();

        String ClientName = null;
        for(int pos = 0; pos<clients.size();pos++)
            if(clients.get(pos).getClientId().compareTo(applications.get(i).getRequirement().getClientId()) ==0)
                ClientName = clients.get(pos).getClientName();


        myViewHolder.mCandidateName.setText(CandidateName);
        myViewHolder.mJobTitle.setText(applications.get(i).getRequirement().getRequirementTitle());
        myViewHolder.mClientName.setText(ClientName);
        myViewHolder.mImage.setImageResource(R.drawable.men);


        switch (ClientName){
            case "SAP":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.sap);
                break;
            }
            case "ALLSTATE":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.allstates);
                break;
            }
            case "USA AIRFORCE":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.usairforce);
                break;
            }
            case "INTEL":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.intel);
                break;
            }
            case "TARGET":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.target);
                break;
            }
            case "IIT":{
                myViewHolder.mImageCompany.setImageResource(R.drawable.iit);
                break;
            }

            default:
                myViewHolder.mImageCompany.setImageResource(R.drawable.logo);
        }


    }

    @Override
    public int getItemCount() {
        Log.i("Application_TAG","getItemCount called ");
        return applications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mCandidateName;
        public TextView mJobTitle;
        public TextView mClientName;
        public ImageView mImage;
        public ImageView mImageCompany;
        public Button mProcessApplicationbtn;

        public MyViewHolder( View itemView) {
            super(itemView);
            Log.i("Application_TAG","MyViewHolder constructor called ");
            mCandidateName = (TextView) itemView.findViewById(R.id.mCandidateName);
            mJobTitle = (TextView) itemView.findViewById(R.id.mJobTitle);
            mClientName = (TextView) itemView.findViewById(R.id.mClientName);
            mImage = (ImageView) itemView.findViewById(R.id.mImage);
            mImageCompany =(ImageView)itemView.findViewById(R.id.mImageCompany);
            mProcessApplicationbtn = (Button) itemView.findViewById(R.id.mProcessApplicationbtn);
            mProcessApplicationbtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            processApplicationStatus(v, getLayoutPosition());
        }
        void processApplicationStatus(View v, final int position) {
            Context context = v.getContext();


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View dialogView = inflater.inflate(R.layout.update_dialog_recruiter_process_application, null);
            dialogBuilder.setView(dialogView);


            TextView  CandidateName;
            TextView  JobTitle;
            TextView  ClientName;
            ImageView Image;
            ImageView ImageCompany;
            TextView  candidateSkills;
            TextView  requirementSkills;
            Button    buttonAcceptCandidate;
            Button    buttonRejectCandidate;


            CandidateName        = (TextView)  dialogView.findViewById(R.id.mCandidateName);
            JobTitle             = (TextView)  dialogView.findViewById(R.id.mJobTitle);
            ClientName           = (TextView)  dialogView.findViewById(R.id.mClientName);
            Image                = (ImageView) dialogView.findViewById(R.id.mImage);
            ImageCompany         =(ImageView)  dialogView.findViewById(R.id.mImageCompany);
            candidateSkills       = (TextView)  dialogView.findViewById(R.id.candidateSkills);
            requirementSkills     = (TextView)  dialogView.findViewById(R.id.requirementSkills);
            buttonAcceptCandidate = (Button)    dialogView.findViewById(R.id.buttonAcceptCandidate);
            buttonRejectCandidate = (Button)    dialogView.findViewById(R.id.buttonRejectCandidate);

            CandidateName.setText(mCandidateName.getText());
            JobTitle.setText(mJobTitle.getText());
            ClientName.setText(mClientName.getText());
            Image.setImageDrawable(mImage.getDrawable());
            ImageCompany.setImageDrawable(mImageCompany.getDrawable());

            dialogBuilder.setTitle("Quick Apply");
            final AlertDialog b = dialogBuilder.create();

            b.show();


            String CandidateEmail = null;
            for (int i =0; i<users.size();i++){
                if(users.get(i).getName().compareTo(CandidateName.getText().toString()) ==0){
                    CandidateEmail = users.get(i).getEmail();
                }
            }

            String CandidateSkillSet ="";
            String RequirementSkillSet = "";
            for(int pos = 0 ; pos<applications.size();pos++)
            {
                if(applications.get(pos).getCandidate().getCandidateEmail().compareTo(CandidateEmail) == 0
                        &&
                        applications.get(pos).getRequirement().getRequirementId().compareTo(applications.get(position).getRequirement().getRequirementId()) == 0) {

                    List<Skill> candidateSkillset = applications.get(pos).getCandidate().getCandidateSkills();
                    for (int positions = 0; positions < candidateSkillset.size(); positions++) {
                        CandidateSkillSet =  "    "+ candidateSkillset.get(positions).getSkillName()+ "    "+ CandidateSkillSet;
                    }

                    List<Skill> requirementSkillset = applications.get(pos).getRequirement().getSkill();
                    if(requirementSkillset!=null) {
                        for (int positions = 0; positions < requirementSkillset.size(); positions++) {
                            RequirementSkillSet ="    "+ requirementSkillset.get(positions).getSkillName()+ "    "+ RequirementSkillSet;
                        }
                    }
                }
            }

            candidateSkills.setText(CandidateSkillSet);
            requirementSkills.setText(RequirementSkillSet);
            /**
             * Process Application
             */

            buttonAcceptCandidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processApplication(view, position, Boolean.TRUE);
                    b.dismiss();
                }
            });

            buttonRejectCandidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processApplication(view, position, Boolean.FALSE);
                    b.dismiss();
                }
            });

        }
        private void processApplication(View view, int position, Boolean Decision) {

            DatabaseReference databaseApplications;
            //getting the reference of clients node
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseApplications  = database.getReference("applications/"+applications.get(position).getApplicationId());

            if(Decision){
                databaseApplications.child("status").setValue("ACCEPTED");
                Toast.makeText(view.getContext(), "Candidate Accepted ", Toast.LENGTH_SHORT).show();
            }else{
                databaseApplications.removeValue();
                Toast.makeText(view.getContext(), "Candidate Rejected ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
