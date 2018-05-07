/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Castilho
 */
public class AdminConsoleIT {
    
    public AdminConsoleIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
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
        assertEquals(expResult, result);
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
