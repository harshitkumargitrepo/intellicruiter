package app.recruiter.intellingent.intellicruiter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequirementRecycleAdapter extends RecyclerView.Adapter<RequirementRecycleAdapter.RequirementViewHolder>  {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private ArrayList<Map<String, String>> requirementList;

    public RequirementRecycleAdapter(){

    }

    //getting the context and product list with constructor
    public RequirementRecycleAdapter(Context mCtx, ArrayList<Map<String, String>> requirementList) {

        Log.i("Harshit Flow: ","          RequirementRecycleAdapter");
        this.mCtx = mCtx;
        this.requirementList = requirementList;
    }


    @Override
    public RequirementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i("Harshit Flow: ","          1)         onCreateViewHolder");
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_requirement_card, null);
        return new RequirementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequirementViewHolder holder, int position) {

        Log.i("Harshit Flow: ","          2)        onBindViewHolder");
        //getting the product of the specified position
        Map<String, String> requirement = requirementList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(String.valueOf(requirement.get("requirementTitle")));
        holder.textViewLocation.setText(String.valueOf(requirement.get("requirementDescription")));
        holder.textViewRating.setText(String.valueOf(requirement.get("numberOfPositions")));
        holder.textViewClientName.setText(String.valueOf(requirement.get("clientName")));
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.app_icon));
    }


    @Override
    public int getItemCount() {
        Log.i("Harshit Flow: ","                              getItemCount");
        return requirementList.size();
    }


    class RequirementViewHolder extends RecyclerView.ViewHolder {


        TextView textViewTitle, textViewLocation, textViewRating, textViewClientName;
        ImageView imageView;

        public RequirementViewHolder(View itemView) {
            super(itemView);

            Log.i("Harshit Flow: ","                              RequirementViewHolder");
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewClientName = itemView.findViewById(R.id.textViewClientName);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
