package roast.app.com.dealbreaker.models;

public class UserQualities extends User{
    //Class Variables
    private String biography, goodQualities, badQualities, profilePic;

    //Empty Constructor
    public UserQualities(){}


    //Constructor for Bio, and Qualities (good/bad)
    public UserQualities(String biography, String goodQualities, String badQualities) {
        this.biography = biography;
        this.goodQualities = goodQualities;
        this.badQualities = badQualities;
    }
    //Constructor for getting image from Firebase
    public UserQualities(String profilePic){
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    //Setters and Getters
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String getGoodQualities() {
        return goodQualities;
    }

    public void setGoodQualities(String goodQualities) {
        this.goodQualities = goodQualities;
    }

    @Override
    public String getBadQualities() {
        return badQualities;
    }

    public void setBadQualities(String badQualities) {
        this.badQualities = badQualities;
    }
}
