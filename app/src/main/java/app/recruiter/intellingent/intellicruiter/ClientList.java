package app.recruiter.intellingent.intellicruiter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ClientList extends ArrayAdapter<Client> {
    private Activity context;
    List<Client> clients;


    public ClientList(Activity context, List<Client> clients) {
        super(context, R.layout.layout_client_list, clients);
        this.context = context;
        this.clients = clients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_client_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewIndustry = (TextView) listViewItem.findViewById(R.id.textViewIndustry);
        ImageView mImage = (ImageView) listViewItem.findViewById(R.id.mImage);

        Client client = clients.get(position);
        textViewName.setText(client.getClientName());
        textViewEmail.setText(client.getClientEmail());
        textViewIndustry.setText(client.getClientIndustry());

        switch (client.getClientName()){
            case "SAP":{
                mImage.setImageResource(R.drawable.sap);
                break;
            }
            case "ALLSTATE":{
                mImage.setImageResource(R.drawable.allstates);
                break;
            }
            case "USA AIRFORCE":{
                mImage.setImageResource(R.drawable.usairforce);
                break;
            }
            case "INTEL":{
                mImage.setImageResource(R.drawable.intel);
                break;
            }
            case "TARGET":{
                mImage.setImageResource(R.drawable.target);
                break;
            }
            case "IIT":{
                mImage.setImageResource(R.drawable.iit);
                break;
            }

            default:
                mImage.setImageResource(R.drawable.logo);
        }



        return listViewItem;
    }



}
