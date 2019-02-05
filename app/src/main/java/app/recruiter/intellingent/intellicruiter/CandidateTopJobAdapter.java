package app.recruiter.intellingent.intellicruiter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CandidateTopJobAdapter extends RecyclerView.Adapter<CandidateTopJobAdapter.MyViewHolder> {

    private static List<RecommendedRequirements> top10requirements;

    //a list to store all the requirements to be shown to recruiter
    private static String globalCandidateID;
    public static List<Requirement> requirements;
    public static List<Client> clients;
    public static List<Application> applications;
    public static List<Candidate> candidates;

    public CandidateTopJobAdapter(  List<Requirement> requirement,     List<Client> client,     List<Application> application,     List<Candidate> candidate, String globalCandidateid, List<RecommendedRequirements> top10requirement) {
        this.top10requirements = top10requirement;
        this.requirements = requirement;
        this.clients = client;
        this.applications = application;
        this.candidates = candidate;
        globalCandidateID = globalCandidateid;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        // create a new view
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tab_candidate_top_jobs_list, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {
        myViewHolder.mTitle.setText(top10requirements.get(i).getRequirement().getRequirementTitle());
        myViewHolder.mLocation.setText(top10requirements.get(i).getClient().getClientIndustry());
        myViewHolder.mClientName.setText(top10requirements.get(i).getClient().getClientName());
        switch (top10requirements.get(i).getClient().getClientName()){
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
        return top10requirements.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle;
        public TextView mLocation;
        public TextView mClientName;
        public ImageView mImage;
        public Button Apply;

        public MyViewHolder( View itemView) {
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
            //Toast.makeText(v.getContext(), "Item clicked: "+getLayoutPosition(), Toast.LENGTH_SHORT).show();
            applyJob(v, getLayoutPosition());
        }
        void applyJob(View v, final int position){
            Context context = v.getContext();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater =(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View dialogView = inflater.inflate(R.layout.update_dialog_candidate_apply_job, null);
            dialogBuilder.setView(dialogView);

            TextView  Title;
            TextView  Location;
            TextView  ClientName;
            ImageView Image;
            Button buttonApplyJob;
            Button buttonWithdrawJob;

            Title = (TextView) dialogView.findViewById(R.id.mTitle);
            Location = (TextView) dialogView.findViewById(R.id.mLocation);
            ClientName = (TextView) dialogView.findViewById(R.id.mClientName);
            Image = (ImageView) dialogView.findViewById(R.id.mImage);
            buttonApplyJob = (Button) dialogView.findViewById(R.id.buttonApplyJob);
            buttonWithdrawJob = (Button) dialogView.findViewById(R.id.buttonWithdrawJob);

            Title.setText(mTitle.getText());
            Location.setText(mLocation.getText());
            ClientName.setText(mClientName.getText());
            Image.setImageDrawable(mImage.getDrawable());

            dialogBuilder.setTitle("Quick Apply");
            final AlertDialog b = dialogBuilder.create();

            b.show();


            /**
             * Process Application
             */

            buttonApplyJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processApplication(view, position, Boolean.FALSE);
                    b.dismiss();
                }
            });

            buttonWithdrawJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processApplication(view, position, Boolean.TRUE);
                    b.dismiss();
                }
            });
        }

        private void processApplication(View view, int position, Boolean Withdraw) {

            ProcessApplication handleApplication = new ProcessApplication(clients,requirements,candidates,applications);
            String returncode = handleApplication.processTopApplicationHandler(globalCandidateID,top10requirements.get(position),Withdraw);

            switch (returncode){
                case "SUCCESS":{
                    Toast.makeText(view.getContext(), "Application Submitted Successfully", Toast.LENGTH_SHORT).show();
                    break;
                }
                case "DUPLICATE":{
                    Toast.makeText(view.getContext(), "You have already applied", Toast.LENGTH_SHORT).show();
                    break;
                }
                case "CANDIDATE_NOT_EXISTS":{
                    Toast.makeText(view.getContext(), "Kindly first create your profile on Home Page ", Toast.LENGTH_SHORT).show();
                    break;
                }
                case "WITHDRAW":{
                    Toast.makeText(view.getContext(), "Application Withdrawn ", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:{
                    Toast.makeText(view.getContext(), "Currently not able to process Application.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }






}
