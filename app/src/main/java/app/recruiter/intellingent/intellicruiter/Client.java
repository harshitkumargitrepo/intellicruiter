package app.recruiter.intellingent.intellicruiter;

import android.util.Log;

public class Client {
    private String clientId;
    private String clientName;
    private String clientEmail;
    private String clientIndustry;

    public Client(){
    }

    public Client(String clientId, String clientName, String clientEmail,String clientIndustry) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientIndustry = clientIndustry;
    }

    public String getClientIndustry() {
        return clientIndustry;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }


    @Override
    public String toString() {
        return "Client:"+clientId+" "+clientName+" "+clientEmail+" "+clientIndustry;
    }
}
