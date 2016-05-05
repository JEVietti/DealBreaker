package roast.app.com.dealbreaker.fragments;

import android.test.UiThreadTest;
import android.widget.Button;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import roast.app.com.dealbreaker.models.Age;
import static org.junit.Assert.*;
import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * Created by Jarrett on 5/4/16.
 */
public class RoamingAttributeTest extends TestCase {

    private RoamingAttribute RA;

    @Before
    public void setUp() throws Exception
    {
        RA = new RoamingAttribute();
    }

    @Test
    public void testGrabEditText() throws Exception
    {


    }
    @UiThreadTest
    /*public void testgrabRoamingButtonValues(RadioButton maleButton, RadioButton femaleButton) throws Exception
    {
        if(maleButton.isChecked()){
            assertTrue(sexRoamingValue == "male");
        }
        else if(femaleButton.isChecked()){
            sexRoamingValue = "female";
        }
    }*/

    @Test
    public void testcheckAndSendData() throws Exception
    {
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
}

