package roast.app.com.dealbreaker.models;


import java.util.HashMap;

public class RelationshipAttribute {
    private String profilePic;
    //Class Variables for the base registration
    private String firstName, lastName, sex, birthDate;
    //Class Variables for attribute assignment
    private Long age, height;
    private int mark;
    private String goodQualities, badQualities, sexualOrientation, location;
    HashMap<String, Object> timeStampLastChanged;

    public RelationshipAttribute(){}

    public RelationshipAttribute(int mark){
        this.mark = mark;
    }

    public RelationshipAttribute(String firstName, String lastName, String sex, Long age, String sexualOrientation, String location, String profilePic, int mark){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.sexualOrientation = sexualOrientation;
        this.location = location;
        this.profilePic = profilePic;
        this.mark = mark;
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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public HashMap<String, Object> getTimeStampLastChanged() {
        return timeStampLastChanged;
    }
}
