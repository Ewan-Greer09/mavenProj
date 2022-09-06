package com.mycompany.app;

import static org.junit.Assert.*;


import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    private static App myApp = new App();

    // test for valid word
    @Test
    public void testValidWord() throws IOException, InterruptedException {
        String word = "hello";
        boolean result = App.validateInput(word);
        assertTrue(result);
    }

    @Test
    public void testInValidWord() throws IOException, InterruptedException {
        String word = "<>>";
        boolean result = App.validateInput(word);
        assertFalse(result);
    }
    

}
