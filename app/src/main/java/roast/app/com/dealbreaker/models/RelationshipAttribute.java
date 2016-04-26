package roast.app.com.dealbreaker.models;


import java.util.HashMap;

public class RelationshipAttribute {
    private String profilePic;
    //Class Variables for the base registration
    private String firstName, lastName, sex, birthDate;
    //Class Variables for attribute assignment
    private Long age, height;
    private String goodQualities, badQualities, sexualOrientation, location;
    HashMap<String, Object> timeStampLastChanged;

    public RelationshipAttribute(){}

    public RelationshipAttribute(String firstName, Long age, String lastName, String sexualOrientation, String location, String profilePic){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sexualOrientation = sexualOrientation;
        this.location = location;
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getBirthDate() {
        return birthDate;
    }


    public String getSex() {
        return sex;
    }


    public Long getAge() {
        return age;
    }


    public Long getHeight() {
        return height;
    }


    public String getGoodQualities() {
        return goodQualities;
    }


    public String getBadQualities() {
        return badQualities;
    }


    public String getLocation() {
        return location;
    }


    public String getSexualOrientation() {
        return sexualOrientation;
    }


    public HashMap<String, Object> getTimeStampLastChanged() {
        return timeStampLastChanged;
    }
}
