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

    }

}