package roast.app.com.dealbreaker;

import android.widget.Button;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import roast.app.com.dealbreaker.models.Age;

import static org.junit.Assert.*;

/**
 * Created by DovaJoe on 5/3/2016.
 */
public class InitialRoamingAttributesTest extends TestCase {

    @Test
    public void testAge(Long ageToTest) throws Exception
    {
        assertTrue(ageToTest >= 18 && ageToTest < 130);
        Age testAge = new Age();
        String birthDate = "3/25/1983";
        String futureBirthDate = "12/3/2200";

        Date birth = testAge.ConvertToDate(birthDate);
        Date birthFutureCase = testAge.ConvertToDate(futureBirthDate);

        Integer dateTest1Age;
        dateTest1Age = Integer.valueOf(testAge.calculateAge(birth));
        Integer futureAge;
        futureAge = Integer.valueOf(testAge.calculateAge(birthFutureCase));

        //18-130 range

        assertSame(33, dateTest1Age);

        if ((futureAge >= 0)) throw new AssertionError();
        assertNotNull(dateTest1Age);
        assertNotSame("not same Error", birthDate, "01/23/1996");
    }

    @Test
    public void testHeight(Long heightToTest) throws Exception
    {
        assertTrue(heightToTest >= 25 && heightToTest <= 91);
    }

    @Test
    public void testSexualOrientation(String sexualOrientationToTest)
    {
        assertTrue(sexualOrientationToTest == "straight" || sexualOrientationToTest == "gay"
                || sexualOrientationToTest == "bisexual");
    }

    @Test
    public void testGenderButton(Button maleButtonToTest, Button femaleButtonToTest)
    {

    }
    @Test
    public void testOnCreate() throws Exception
    {

    }

    @Test
    public void testIsDataValid() throws Exception
    {

    }

    @Test
    public void testSetData() throws Exception
    {

    }
}

