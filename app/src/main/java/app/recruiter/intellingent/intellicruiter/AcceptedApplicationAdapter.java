package app.recruiter.intellingent.intellicruiter;

import android.content.Context;
import android.support.annotation.NonNull;
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

import java.util.List;

public class AcceptedApplicationAdapter extends RecyclerView.Adapter<AcceptedApplicationAdapter.MyViewHolder> {

    public static List<Requirement> requirements;
    public static List<Client> clients;
    public static List<Application> applications;
    public static List<Candidate> candidates;
    public static List<User> users;

    public AcceptedApplicationAdapter(List<User> user, List<Requirement> requirement,List<Client> client,List<Application> application,List<Candidate> candidate){
        this.requirements = requirement;
        this.clients = client;
        this.applications = application;
        this.candidates = candidate;
        this.users = user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.accepted_application_list, viewGroup, false);
        AcceptedApplicationAdapter.MyViewHolder vh = new AcceptedApplicationAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        String ClientName = null;
        String ClientIndustry = null;
        for(int pos = 0; pos<clients.size();pos++)
            if(clients.get(pos).getClientId().compareTo(applications.get(i).getRequirement().getClientId()) ==0) {
                ClientName = clients.get(pos).getClientName();
                ClientIndustry = clients.get(pos).getClientIndustry();
            }

        myViewHolder.mTitle.setText(applications.get(i).getRequirement().getRequirementTitle());
        myViewHolder.mLocation.setText(ClientIndustry);
        myViewHolder.mClientName.setText(ClientName);
        switch (ClientName){
            case "SAP":{
                myViewHolder.mImage.setImageResource(R.drawable.sap);
                break;
            }
            case "ALLSTATE":{
                myViewHolder.mImage.setImageResource(R.drawable.allstates);
                break;
            }
            case "USA AIRFORCE":{
                myViewHolder.mImage.setImageResource(R.drawable.usairforce);
                break;
            }
            case "INTEL":{
                myViewHolder.mImage.setImageResource(R.drawable.intel);
                break;
            }
            case "TARGET":{
                myViewHolder.mImage.setImageResource(R.drawable.target);
                break;
            }
            case "IIT":{
                myViewHolder.mImage.setImageResource(R.drawable.iit);
                break;
            }

            default:
                myViewHolder.mImage.setImageResource(R.drawable.logo);
        }

    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView mTitle;
        public  TextView mLocation;
        public TextView mClientName;
        public ImageView mImage;
        public Button Apply;

        Boolean ValidatedYourself = Boolean.FALSE;


        public MyViewHolder(View itemView) {
            super(itemView);


            mTitle = (TextView) itemView.findViewById(R.id.mTitle);
            mLocation = (TextView) itemView.findViewById(R.id.mLocation);
            mClientName = (TextView) itemView.findViewById(R.id.mClientName);
            mImage = (ImageView) itemView.findViewById(R.id.mImage);
            Apply = (Button) itemView.findViewById(R.id.mApplybtn);
            Apply.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            takeInterview(v, getLayoutPosition());
        }

        void takeInterview(View v, final int position) {
            Context context = v.getContext();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View dialogView = inflater.inflate(R.layout.update_dialog_candidate_take_interview, null);
            dialogBuilder.setView(dialogView);


            TextView  Title;
            TextView  Location;
            TextView  ClientName;
            ImageView Image;
            Button buttonVideoInterview;
            Button buttonValidateYuorself;

            Title = (TextView) dialogView.findViewById(R.id.mTitle);
            Location = (TextView) dialogView.findViewById(R.id.mLocation);
            ClientName = (TextView) dialogView.findViewById(R.id.mClientName);
            Image = (ImageView) dialogView.findViewById(R.id.mImage);
            buttonVideoInterview = (Button) dialogView.findViewById(R.id.buttonVideoInterview);
            buttonValidateYuorself = (Button) dialogView.findViewById(R.id.buttonValidateYuorself);

            Title.setText(mTitle.getText());
            Location.setText(mLocation.getText());
            ClientName.setText(mClientName.getText());
            Image.setImageDrawable(mImage.getDrawable());

            dialogBuilder.setTitle("Start Interview");
            final AlertDialog b = dialogBuilder.create();

            b.show();

            /**
             * Process Application
             */

            buttonValidateYuorself.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //takeInterviewProcessApplication(view, position, Boolean.FALSE);
                    ValidatedYourself = Boolean.TRUE;
                    Toast.makeText(view.getContext(), "Validation successful for job " + mTitle.getText(), Toast.LENGTH_SHORT).show();
                    return;
                }
            });

            buttonVideoInterview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ValidatedYourself){
                        Toast.makeText(view.getContext(), "Kindly Validate yourself!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    takeInterviewProcessApplication(view, position, Boolean.TRUE);
                    b.dismiss();
                }
            });


        }
        private void takeInterviewProcessApplication(View view, int position, Boolean Withdraw) {

        }
    }
}
