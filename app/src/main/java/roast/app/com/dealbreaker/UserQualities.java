package roast.app.com.dealbreaker;

public class UserQualities extends User{
    //Class Variables
    private String biography, goodQualities, badQualities;

    //Empty Constructor
    public UserQualities(){}

    //Constructor for Bio, and Qualities (good/bad)


    public UserQualities(String biography, String goodQualities, String badQualities) {
        this.biography = biography;
        this.goodQualities = goodQualities;
        this.badQualities = badQualities;
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
