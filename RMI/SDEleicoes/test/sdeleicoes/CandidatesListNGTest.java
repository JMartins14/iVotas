/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdeleicoes;

import java.util.concurrent.CopyOnWriteArrayList;
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
public class CandidatesListNGTest {
    
    public CandidatesListNGTest() {
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
     * Test of getVotes method, of class CandidatesList.
     */
    @Test
    public void testGetVotes() {
        System.out.println("getVotes");
        CandidatesList instance = null;
        CopyOnWriteArrayList expResult = null;
        CopyOnWriteArrayList result = instance.getVotes();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addVote method, of class CandidatesList.
     */
    @Test
    public void testAddVote() {
        System.out.println("addVote");
        Vote vote = null;
        CandidatesList instance = null;
        instance.addVote(vote);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class CandidatesList.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        CandidatesList instance = null;
        String expResult = "";
        String result = instance.getType();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setType method, of class CandidatesList.
     */
    @Test
    public void testSetType() {
        System.out.println("setType");
        String type = "";
        CandidatesList instance = null;
        instance.setType(type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class CandidatesList.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        CandidatesList instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class CandidatesList.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        CandidatesList instance = null;
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumCandidates method, of class CandidatesList.
     */
    @Test
    public void testGetNumCandidates() {
        System.out.println("getNumCandidates");
        CandidatesList instance = null;
        int expResult = 0;
        int result = instance.getNumCandidates();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNumCandidates method, of class CandidatesList.
     */
    @Test
    public void testSetNumCandidates() {
        System.out.println("setNumCandidates");
        int numCandidates = 0;
        CandidatesList instance = null;
        instance.setNumCandidates(numCandidates);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCandidates method, of class CandidatesList.
     */
    @Test
    public void testGetCandidates() {
        System.out.println("getCandidates");
        CandidatesList instance = null;
        CopyOnWriteArrayList expResult = null;
        CopyOnWriteArrayList result = instance.getCandidates();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCandidates method, of class CandidatesList.
     */
    @Test
    public void testSetCandidates() {
        System.out.println("setCandidates");
        CopyOnWriteArrayList<String> candidates = null;
        CandidatesList instance = null;
        instance.setCandidates(candidates);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
