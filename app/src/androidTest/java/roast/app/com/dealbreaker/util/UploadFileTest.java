package roast.app.com.dealbreaker.util;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class UploadFileTest extends TestCase {

    UploadFile uploadFile = new UploadFile("hello");
    UploadFile uploadFile2 = new UploadFile("");
    @Test
    public void testDoInBackground() throws Exception {
        File file = new File("");
        assertSame(null, uploadFile.doInBackground(null));
        assertSame(null,uploadFile.doInBackground(file));
        assertSame(null,uploadFile2.doInBackground(file));

        //Cannot make File due to constraints of JUnit
    }
}