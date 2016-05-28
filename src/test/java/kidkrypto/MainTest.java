/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kidkrypto;

import org.graphstream.graph.Graph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author suman
 */
public class MainTest {
    
    public MainTest() {
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
     * Test of createPublicUnweightedgraph method, of class Main.
     */
    @Test
    public void testCreatePublicUnweightedgraph() {
        System.out.println("createPublicUnweightedgraph");
        int numNodes = 0;
        Main instance = new Main();
        Graph expResult = null;
        Graph result = instance.createPublicUnweightedgraph(numNodes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectPrivateNodes method, of class Main.
     */
    @Test
    public void testSelectPrivateNodes() {
        System.out.println("selectPrivateNodes");
        Main instance = new Main();
        instance.selectPrivateNodes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of colorPrivateNodes method, of class Main.
     */
    @Test
    public void testColorPrivateNodes() {
        System.out.println("colorPrivateNodes");
        Main instance = new Main();
        instance.colorPrivateNodes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignWeight method, of class Main.
     */
    @Test
    public void testAssignWeight() {
        System.out.println("assignWeight");
        int message = 0;
        Main instance = new Main();
        instance.assignWeight(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateNewWeight method, of class Main.
     */
    @Test
    public void testCalculateNewWeight() {
        System.out.println("calculateNewWeight");
        Main instance = new Main();
        instance.calculateNewWeight();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decryptSecret method, of class Main.
     */
    @Test
    public void testDecryptSecret() {
        System.out.println("decryptSecret");
        Main instance = new Main();
        int expResult = 0;
        int result = instance.decryptSecret();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Main.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

