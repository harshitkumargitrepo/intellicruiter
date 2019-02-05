package app.recruiter.intellingent.intellicruiter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequirementList extends ArrayAdapter<Requirement> {

    private Activity context;
    List<Requirement> requirements;

    public RequirementList(Activity context, List<Requirement> requirements) {
        super(context, R.layout.layout_requirement_list, requirements);
        this.context = context;
        this.requirements = requirements;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_requirement_list, null, true);

        TextView textViewRequirementTitle = (TextView) listViewItem.findViewById(R.id.textViewRequirementTitle);
        TextView textViewRequirementDesc = (TextView) listViewItem.findViewById(R.id.textViewRequirementDesc);
        TextView textViewPositions = (TextView) listViewItem.findViewById(R.id.textViewPositions);

        Requirement requirement = requirements.get(position);
        textViewRequirementTitle.setText(requirement.getRequirementTitle());
        textViewRequirementDesc.setText("Short Description :"+requirement.getRequirementDescription());
        textViewPositions.setText(String.valueOf("Number of positions :"+requirement.getNumberOfPositions()));

        return listViewItem;
    }
}
