package roast.app.com.dealbreaker;

//Data Struct for User Objects

public class User {

    //Class Variables for the base registration
    private String userName,email, password, firstName, lastName, sex;
    //Class Variables for attribute assignment
    private Long age, height;
    private String goodQualities, badQualities, sexualOrientation;

    public User() {
    }

    //For the basic Registration
    public User(String email, String password,String userName, String firstName,String lastName, Long age, String sex) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
    }
    //For the attribute Registrations
    public User(String email, String password, String userName, String firstName, String lastName, String sex, Long age,String sexualOrientation ,Long height, String goodQualities, String badQualities) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
        this.sexualOrientation = sexualOrientation;
        this.height = height;
        this.goodQualities = goodQualities;
        this.badQualities = badQualities;
    }

    //Getter Statements for Class Variables

    public String getEmail() { return email; }

    public String getUserName() { return userName; }

    public String getPassword() { return password; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getSex() { return sex; }

    public Long getAge() { return age; }

    public String getSexualOrientation() { return sexualOrientation; }

    public Long getHeight() { return height; }

    public String getGoodQualities() { return goodQualities; }

    public String getBadQualities() { return badQualities; }
}
