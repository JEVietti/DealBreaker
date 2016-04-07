package roast.app.com.dealbreaker.models;

public class UserImages {
    private String profilePic;

    public UserImages(){}

    //Constructor
    public UserImages(String profilePic) {
        this.profilePic = profilePic;
    }

    //Getters and Setters

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
