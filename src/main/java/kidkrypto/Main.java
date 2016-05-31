
package kidkrypto;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JOptionPane;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author pestilence
 */
public class Main {

    private Graph graph;            // the main graph
    String[][] edgesList;           // contains the list of edges
    ArrayList<Node> privateNodeList;// contains the private nodes
    String[] nodes;                 // contains the nodes
    int[] nodeWeight;               // contains the node weights
    int[] replacedWeight;           // contains the replaced weights
    int numOfNodes;                 // denotes the total number of nodes
    Random rand = new Random();     // randomizer instance
    

    public Graph createPublicUnweightedgraph(int numNodes) {
        graph = new SingleGraph("Graph");
        numOfNodes = numNodes; // initialize num of nodes
        nodes = new String[numOfNodes]; // instantiate nodes
        edgesList = new String[numOfNodes][numOfNodes]; // instantiate edges

        graph.setAutoCreate(true);

        //assigning Nodes
        int count = 0;
        for (int i = 0; i < numOfNodes; i++) {
            //generating node alphabet
            char randomNode = (char) ('A' + count++); //typecasting ascii to character
            graph.addNode("" + randomNode); // adding node to graph
            nodes[i] = "" + randomNode;     // nodes collection
        }
        
        //assigning Edges
        for (int i = 0; i < numOfNodes; i++) {
            /**
             * PHASE ONE : Adding oneself to edgelist
             * No Edge created, cannot make edge to oneself
             */
            edgesList[i][0] = "" + nodes[i]; 

            /**
             * PHASE TWO : Attempting to have connected graph,
             * Iterating over each node and creating an edge to the next node
             */
            // This threshold is used to avoid to build a perfect graph everytime
            // has probability of 10/1000 = 0.01 * n
            int threshold2 = rand.nextInt(1000) + 1;
            if (threshold2 > 10) {
                // for nodes from 1 to n-1, i.e, A->B, B->C and so on
                if (i < numOfNodes - 1) {
                    if (uniqueEdge(nodes[i], nodes[i + 1])) {
                        edgesList[i][1] = "" + nodes[i + 1];
                        graph.addEdge("" + nodes[i] + nodes[i + 1], nodes[i], nodes[i + 1]);
                    }
                // for node n - node 1 , i.e, N->A.
                } else if (uniqueEdge(nodes[i], nodes[0])) {
                    edgesList[i][1] = "" + nodes[0];
                    graph.addEdge("" + nodes[i] + nodes[0], nodes[i], nodes[0]);
                }
            }
            
            /**
             * PHASE THREE : Creating random edges
             */
            int edgeCount = 0; // To allow 1 chance of finding a random edge
            int nodeCount = 2; // startNode = 2; 1 node has already been inserted manually
            while (edgeCount <= 0) {
                int threshold = rand.nextInt(1000) + 1;
                // 520/1000 = 0.521
                if (threshold > 520) {
                    int chooseNodeIndex = rand.nextInt(numOfNodes) + 0; // choosing random node

                    if (i == chooseNodeIndex) {continue;} // no point linking to the same node

                    // check if the proposed edge is already created or not
                    if (uniqueEdge(nodes[i], nodes[chooseNodeIndex])) {
                        edgesList[i][nodeCount++] = "" + nodes[chooseNodeIndex];
                        String edge = "" + nodes[i] + nodes[chooseNodeIndex];
                        String from = "" + nodes[i];
                        String to = "" + nodes[chooseNodeIndex];
                        graph.addEdge(edge, from, to); // add new edge to graph
                    }
                }
                edgeCount++;
            }
        }
        //graph.display();
        return graph;
    }

    /**
     * This method is used to check if the edge is unique
     * @param from one node
     * @param to the other node
     * @return true/false 
     */
    private boolean uniqueEdge(String from, String to) {
        boolean unique = false;
        
        // using ascii to find the index of the node, A=65,B=66, and so on.
        int ascii = (int) from.charAt(0);
        int fromIndex = ascii - 65;
        // check if the "from" node has "to" edge 
        for (String test : edgesList[fromIndex]) {
            if (test == null) {continue;}
            if (test.equals(to)) {
                unique = false;
                break;
            }
            unique = true;
        }
        
        // if unique is already false from first part, then exit
        // else test if [ to => from ] exists. It's a undirected graph, 
        // so, to => from and from => to are the same
        if (unique == true) {
            ascii = (int) to.charAt(0);
            int toIndex = ascii - 65;
            // check if the "to" node has "from" edge 
            for (String test : edgesList[toIndex]) {
                if (test == null) {continue;}
                if (test.equals(from)) {
                    unique = false;
                    break;
                }
                unique = true;
            }
        }
        return unique;
    }
    
    /**
     * Finding the best set of private nodes for the graph
     */
    public void selectPrivateNodes() {
        int maxPrivateCount = 0; // holds the maximum value of PrivateNode List
        
        // iterate over each node and find max num of private nodes
        for (Node pNode : graph) {
            ArrayList<Node> privateNodes = new ArrayList<>(); // holder for private nodes
            int probNCount = 0; //once a node is explored, the node and its neighbours are pushed to prohibited list
            String[] prohibitedNList = new String[numOfNodes]; // prohibibited list can have at most n ndoes

            // add node to private and check for maximum selection
            privateNodes.add(pNode); // add the curretn  node to privateList
            prohibitedNList[probNCount++] = pNode.getId(); // add the curretn  node to prohibitedList
            //finding the neighbours 
            for (String nbour : getNeighbours(pNode)) {
                prohibitedNList[probNCount++] = nbour; // adding neighnours
            }

            // for each node - check if others can be private or not
            for (Node n : graph) {
                boolean push = true;
                ArrayList<String> temp = getNeighbours(n); // list of neighbours 
                for (String nbour : temp) {
                    // check if the node is in prohibited list
                    int index = Arrays.asList(prohibitedNList).indexOf(nbour);
                    // -1 means its not in prohibited list and can be choosen as privateNode
                    if (index > -1) {push = false;}
                }
                // if the node can be choosen as private
                if (push == true) {
                    privateNodes.add(n); // adding node to private
                    prohibitedNList[probNCount++] = n.getId(); // adding the node to prohibited now
                    for (String nbour : temp) { 
                        prohibitedNList[probNCount++] = nbour; // adding the neighbours
                    }
                }
            }
            
            /**
             * Checks for maxima among the ProvateNodeList
             * If new maxima is found, it is regarded as the PrivateNodeList
             */
            if(privateNodes.size() > maxPrivateCount ){
                maxPrivateCount = privateNodes.size();
                privateNodeList = privateNodes;
            }
        }
    }
    
    /**
     * Coloring the best set of private nodes for the graph
     */
    public void colorPrivateNodes() {
        // coloring the private Nodes once the list is finalized
        for (Node n : privateNodeList) {
            n.addAttribute("ui.style", "fill-color: rgb(0,100,255);text-size:20;"); // css styling
            
        }
    }
    
    /**
     * This method finds the neighbors of a given node
     * @param n The node whose neighbors need to be found
     * @return The list of neighbors of a node n
     */
    private ArrayList<String> getNeighbours(Node n1) {
        ArrayList<String> neighbour = new ArrayList<>();
        
        // iterating over the neighbours
        Iterator<? extends Node> nodes = n1.getNeighborNodeIterator();
        while (nodes.hasNext()) {
            Node node = nodes.next();
            neighbour.add(node.getId());
        }
        return neighbour;
    }

    /**
     * This method Assigns weight to the nodes
     * @param message The message that needs to be encrypted
     */
    public void assignWeight(int message) {
        nodeWeight = new int[numOfNodes]; // holds the weight for each node
        int total = 0; // The sum of all nodes - to assure it sums to the message
        char lastNode = (char) (numOfNodes + 65 - 1); // last node character from ASCII value
        int adjust = 0; // holds the FALSE value that the last nodes has
        
        int i = 0;
        // assigning weights to nodes
        for (Node n1 : graph) {
            /**
             * get random value: nextInt(SIZE) + STARTVALUE
             * if message = 0; then random value between -10 and 10 is generated
             */
            int nodeVal = rand.nextInt(20) + (message - 10); 
            total += nodeVal;
            adjust = nodeVal; // replacing it everytime so we can get the last value, subtract from total and replace it
            nodeWeight[i++] = nodeVal;
            n1.addAttribute("ui.label", n1.getId() + "(" + nodeVal + ")");
            n1.addAttribute("ui.style","text-size:20;");// adding weights to UI : CSS styling
        }

        Node n1 = graph.getNode("" + lastNode); // last node
        int newVal = message - (total - adjust); // the value the last node should have
        n1.addAttribute("ui.label", n1.getId() + "(" + (newVal) + ")"); // update last node
        nodeWeight[numOfNodes-1] = newVal;
    }
    
    public void assignLabel(int message) {
        nodeWeight = new int[numOfNodes]; // holds the weight for each node
        int total = 0; // The sum of all nodes - to assure it sums to the message
        char lastNode = (char) (numOfNodes + 65 - 1); // last node character from ASCII value
        int adjust = 0; // holds the FALSE value that the last nodes has
        
        int i = 0;
        // assigning weights to nodes
        for (Node n1 : graph) {
            /**
             * get random value: nextInt(SIZE) + STARTVALUE
             * if message = 0; then random value between -10 and 10 is generated
             */
            int nodeVal = rand.nextInt(20) + (message - 10); 
            total += nodeVal;
            adjust = nodeVal; // replacing it everytime so we can get the last value, subtract from total and replace it
            nodeWeight[i++] = nodeVal;
            n1.addAttribute("ui.label", n1.getId());
            n1.addAttribute("ui.style","text-size:20;");// adding weights to UI : CSS styling
        }

      
    }

    /**
     * This method Assigns NEW weight to the nodes that can be shared publicly
     */
    public void calculateNewWeight() {
        replacedWeight = new int[numOfNodes]; // holds the replaced weights
        int count = 0;
        // iterating over each node
        for (Node n1 : graph) {
            int newVal = 0; // holds the new weight for the node
            int index = Arrays.asList(nodes).indexOf(n1.getId()); // finds the index of the node from ID
            newVal += nodeWeight[index]; // adds its weight
            
            // now adding weights of its neighbors
            ArrayList<String> neighbours = getNeighbours(n1);
            for(String nb : neighbours ){
                Node nbour = graph.getNode(nb);
                index = Arrays.asList(nodes).indexOf(nbour.getId());
                newVal += nodeWeight[index];
            }
            replacedWeight[count++] = newVal;
            n1.addAttribute("ui.label", n1.getId() + "(" + newVal + ")"); // update the new weight
        }
    }

    /**
     * This method decrypts the message
     * @return int Original Message
     */
    public int decryptSecret(){
        int message = 0;
        
        // iterate over the private nodes
        for(Node n1 : privateNodeList){
            System.out.println(n1.getId());
            
            int ascii = (int) n1.getId().charAt(0);
            int index = ascii - 65; // ascii of A is 65
        
            message += replacedWeight[index];
        }
        return message;
    }

    /**
     * Main method
     */
    public static void main(String args[]) {
        Main mk = new Main();
        int minPrivate = 1; // just initializing : holds the minimum number of private nodes that the graph is supposed to have
        int foundPrivate = 0; // holds the maximum number of private nodes actually found from the graph
        int testCount = 1;
        
        int numOfNodes = mk.rand.nextInt(6)+6; // randomizing the no. of nodes
        do{
            //System.out.println("##################################");
            
            //Creating the initial graph witht he given number of nodes
            mk.createPublicUnweightedgraph(numOfNodes);
            
            // select the optimal private node collection
            mk.selectPrivateNodes();
            
            //mk.graph.display();
            
            //Calculation of expected PrivateNode Count
            if(mk.numOfNodes % 3 == 0)
                minPrivate = (int) ( (mk.numOfNodes / 3) );
            else
                minPrivate = (int) ( (mk.numOfNodes / 3) + 1);
            
            // Calculation of actual PrivateNode Count
            foundPrivate = mk.privateNodeList.size(); 
            
            System.out.println("Iteration : " + testCount);
            System.out.println("NodeCount = " + numOfNodes);
            
            System.out.println("minimum" + minPrivate);
            System.out.println("Private" + foundPrivate);
            
            // in 100 times, if proper graph is not found then shuffle the no. of nodes
            if(testCount % 99 == 0){ numOfNodes = mk.rand.nextInt(6)+6;}
            // attempt to create a valid graph
            if(testCount++ > 10000){break;} 
        }while(minPrivate > foundPrivate);
        
        //perfect graph could not be found
        if(minPrivate > foundPrivate){
            System.out.println("xx GRAPH GENERATION xx");
          //  System.exit(0);
        }
        
        // Ask for the Message
        int someNumber = Integer.parseInt(JOptionPane.showInputDialog("SECRET: Number"));
        mk.assignLabel(someNumber);
        
        //Clone and show inital graph
        mk.colorPrivateNodes();
        Graph initialGraph = Graphs.clone(mk.graph);
        initialGraph.display();
        mk.assignWeight(someNumber); // pass the message u want to send
        
        // Clone and show graph with private and initial weights
        Graph intermediateGraph = Graphs.clone(mk.graph);
        intermediateGraph.display();
        
        
        
        //Calculate new weight and dsiplay the updates
        mk.calculateNewWeight();
        mk.graph.display();
        
        System.out.println("THE SECRET MESSAGE IS");
        //Find the Message
        System.out.println(mk.decryptSecret()); 
    }
}
