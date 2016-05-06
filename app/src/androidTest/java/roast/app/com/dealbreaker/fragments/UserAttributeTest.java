package roast.app.com.dealbreaker.fragments;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import roast.app.com.dealbreaker.models.Age;

public class UserAttributeTest extends TestCase {

    private UserAttribute UA;

    @Before
    public void setUp()throws Exception{
        UA = new UserAttribute();
    }

    @Test
    public void testIsThisDateValid(String dataToValidate, String dateFormat) throws Exception {

        //The date is not NULL
        //assertNotNull(dataToValidate);
        //assertEquals(dataToValidate.length(), 10);   //"MM/DD/YYYY" length equals 10 characters

        //test data null string
        dataToValidate = "";
        assertEquals(false, UA.isThisDateValid(dataToValidate, dateFormat));

        //test data on user birthday that is between 18 and 130
        dataToValidate = "9/12/1990";
        dateFormat = "";
        assertEquals(true, UA.isThisDateValid(dataToValidate, dateFormat));

        //test data on user birthday younger than 18
        dataToValidate = "9/12/2000";
        dateFormat = "";
        assertEquals(true, UA.isThisDateValid(dataToValidate, dateFormat));

        //test age beyond 130
        dataToValidate = "9/12/1885";
        dateFormat = "";
        assertEquals(true, UA.isThisDateValid(dataToValidate, dateFormat));
        UA.isThisDateValid(dataToValidate, dateFormat);


    }

    //THe check and Send Data tests will not work unless the actual function is modified
    //so during testing I modified the function so it did not set errors on the Edit Text
    //as they would be null in testing environment
    @Test
    public void testcheckAndSendData() throws Exception{
        Age a = new Age();
        String birthDate = "11/16/1995";
        String futureBirthDate = "11/30/2100";

        Date birth = a.ConvertToDate(birthDate);
        Date birthFutureCase = a.ConvertToDate(futureBirthDate);

        Integer dateTest1Age = Integer.valueOf(a.calculateAge(birth));
        Integer futureAge = Integer.valueOf(a.calculateAge(birthFutureCase));

        //18-130 range not including 130
        //String dateTest2Age = "";
        assertSame(20, dateTest1Age);

        assert(futureAge<0);
        assertNotNull(dateTest1Age);
        assertNotSame("not same Error", birthDate, "01/23/1996");
        String firstname = "";

        boolean result = UA.checkAndSendData("password",firstname,"taco", birthDate, "hello","H","st","m");
        assertSame(false,result);
        //will show errors due to having to modify the functin to allow for testing by creating extra paramters for the
        //function to work in JUNIT
        //checking is the User has a name birthdate Location, etc.
        // as well as testing if different ages checkout and if the
        boolean result2 = UA.checkAndSendData("password",firstname,"taco", birthDate, "hello","H","st","m");
        assertSame(true,UA.checkAndSendData("password","dark","swwap",birthDate,"Fresno, California, United States","74","straight","male"));
        //18 years old
        assertSame(true,UA.checkAndSendData("password","1","swwap","11/16/1993","Fresno, California, United States","74","straight","male"));
        assertSame(true,UA.checkAndSendData("password","PO","swwap",birthDate,"Fresno, California, United States","74","gay","male"));
        assertSame(true,UA.checkAndSendData("password","dark","2",birthDate,"Fresno, California, United States","74","bisexual","female"));
        assertSame(true,UA.checkAndSendData("password","dark","c",birthDate,"Fresno, California, United States","74","straight","male"));
        assertSame(true,UA.checkAndSendData("password","c","swwap",birthDate,"Fresno, California, United States","74","straight","male"));


        assertSame(false,UA.checkAndSendData("","","swwap",birthDate,"Fresno, California, United States","74","straight","male"));
        assertSame(false,UA.checkAndSendData("","dark","",birthDate,"Fresno, California, United States","74","straight","female"));
        assertSame(false,UA.checkAndSendData("","dark","swwap",birthDate,"","74","straight","female"));
        assertSame(false,UA.checkAndSendData("","dark","swwap","","","Fresno, California, United States","stpaight","female"));
        assertSame(false,UA.checkAndSendData("","dark","swwap","11/2/2020","Fresno, California, United States","74","straight","male"));
        assertSame(false,UA.checkAndSendData("","dark","swwap",birthDate,"","Fresno, California, United States","straight","females"));
        assertSame(false,UA.checkAndSendData("","dark","swwap",birthDate,"Fresno, California, United States","74","straight","males"));
        assertSame(false,UA.checkAndSendData("","dark","swwap",birthDate,"","74","straight","female"));
        assertSame(false,UA.checkAndSendData("","dark","swwap","11/4/1212","Fresno, California, United States","74","straight","female"));


    }

}