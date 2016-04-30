package roast.app.com.dealbreaker.models;

//Model for the contact information that Users can make available to their Confirmed Relationships

public class ContactInfo {
    private String contactInfo;
    public ContactInfo(){}

    public ContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
