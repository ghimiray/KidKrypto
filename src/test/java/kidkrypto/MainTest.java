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
public class MainTest extends Main{
    
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
        int result = 0;
        Main instance = new Main();
        Graph expResult = instance.createPublicUnweightedgraph(numNodes);
        assertEquals(expResult.getEdgeCount(), result);
        System.out.println("createPublicUnweightedgraph");
    }

    /**
     * Test of selectPrivateNodes method, of class Main.
     */
 

    /**
     * Test of colorPrivateNodes method, of class Main.
     */
 

    /**
     * Test of assignWeight method, of class Main.
     */
    

    /**
     * Test of calculateNewWeight method, of class Main.
     */


    /**
     * Test of decryptSecret method, of class Main.
     */
    @Test
    public void testDecryptSecret() {
        System.out.println("decryptSecret");
        Main instance = new Main();
        int expResult = 100;
        int result = instance.decryptSecret();
        assertEquals(expResult,result);
        
              
    }

    
    
}

