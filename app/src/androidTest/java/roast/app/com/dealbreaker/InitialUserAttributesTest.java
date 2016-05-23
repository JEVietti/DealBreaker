package roast.app.com.dealbreaker;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Date;

import roast.app.com.dealbreaker.models.Age;

import static org.junit.Assert.*;

/**
 * Created by DovaJoe on 5/3/2016.
 */
public class InitialUserAttributesTest extends TestCase{

    InitialUserAttributes IUA;

    @Test
    public void testOnCreate() throws Exception {

    }

    /*
    @Test
    public void testcheckAndSendData() throws Exception{
            Age a = new Age();
            String birthDate = "11/16/1995";
            String futureBirthDate = "11/30/2100";`

            Date birth = a.ConvertToDate(birthDate);
            Date birthFutureCase = a.ConvertToDate(futureBirthDate);

            Integer dateTest1Age = Integer.valueOf(a.calculateAge(birth));
            Integer futureAge = Integer.valueOf(a.calculateAge(birthFutureCase));
            //18-130 range
            //String dateTest2Age = "";
            assertSame(20, dateTest1Age);

            assert(futureAge<0);
            assertNotNull(dateTest1Age);
            assertNotSame("not same Error", birthDate, "01/23/1996");
            String firstname = "";

            boolean result = IUA.isDataValid("password",firstname,"taco", birthDate, "hello","H","st","m");
            assertSame(false,result);
            //will show errors due to having to modify the functin to allow for testing by creating extra paramters for the
            //function to work in JUNIT
            //checking is the User has a name birthdate Location, etc.
            // as well as testing if different ages checkout and if the
            boolean result2 = IUA.isDataValid("password", firstname, "taco", birthDate, "hello", "H", "st", "m");
            assertSame(true, IUA.isDataValid("password", "dark", "swwap", birthDate, "Fresno, California, United States", "74", "straight", "male"));
            //18 years old
            assertSame(true,IUA.isDataValid("password","1","swwap","11/16/1993","Fresno, California, United States","74","straight","male"));
            assertSame(true,IUA.isDataValid("password","PO","swwap",birthDate,"Fresno, California, United States","74","gay","male"));
            assertSame(true,IUA.isDataValid("password","dark","2",birthDate,"Fresno, California, United States","74","bisexual","female"));
            assertSame(true,IUA.isDataValid("password","dark","c",birthDate,"Fresno, California, United States","74","straight","male"));
            assertSame(true,IUA.isDataValid("password","c","swwap",birthDate,"Fresno, California, United States","74","straight","male"));


            assertSame(false,IUA.isDataValid("","","swwap",birthDate,"Fresno, California, United States","74","straight","male"));
            assertSame(false,IUA.isDataValid("","dark","",birthDate,"Fresno, California, United States","74","straight","female"));
            assertSame(false,IUA.isDataValid("","dark","swwap",birthDate,"","74","straight","female"));
            assertSame(false,IUA.isDataValid("","dark","swwap","","","Fresno, California, United States","stpaight","female"));
            assertSame(false,IUA.isDataValid("","dark","swwap","11/2/2020","Fresno, California, United States","74","straight","male"));
            assertSame(false,IUA.isDataValid("","dark","swwap",birthDate,"","Fresno, California, United States","straight","females"));
            assertSame(false,IUA.isDataValid("","dark","swwap",birthDate,"Fresno, California, United States","74","straight","males"));
            assertSame(false,IUA.isDataValid("","dark","swwap",birthDate,"","74","straight","female"));
            assertSame(false,IUA.isDataValid("","dark","swwap","11/4/1212","Fresno, California, United States","74","straight","female"));


        }
        */

    @Test
    public void testIsThisDateValid() throws Exception {

    }

    @Test
    public void testSetData() throws Exception {

    }

    @Test
    public void testAddUserToRoamingList() throws Exception {

    }

}