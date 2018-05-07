/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Castilho
 */
public class AdminConsoleNGTest {
    
    public AdminConsoleNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of main method, of class AdminConsole.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        AdminConsole.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connectRmi method, of class AdminConsole.
     */
    @Test
    public void testConnectRmi() {
        System.out.println("connectRmi");
        boolean expResult = false;
        boolean result = AdminConsole.connectRmi();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of InitialMenu method, of class AdminConsole.
     */
    @Test
    public void testInitialMenu() throws Exception {
        System.out.println("InitialMenu");
        AdminConsole.InitialMenu();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
