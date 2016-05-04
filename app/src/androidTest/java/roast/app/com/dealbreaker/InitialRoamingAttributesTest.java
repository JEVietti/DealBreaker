package roast.app.com.dealbreaker;

import android.widget.Button;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DovaJoe on 5/3/2016.
 */
public class InitialRoamingAttributesTest extends TestCase {

    @Test
    public void testAge(Long ageToTest) throws Exception
    {
        assertTrue(ageToTest >= 18 && ageToTest < 130);
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

