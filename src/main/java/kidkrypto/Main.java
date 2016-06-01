
package kidkrypto;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JOptionPane;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.SingleGraph;


public class Main {

    private Graph graph;            
    String[][] edgesList;           
    ArrayList<Node> privateNodeList;
    String[] nodes;                 
    int[] nodeWeight;               
    int[] replacedWeight;           
    int numOfNodes;                 
    Random rand = new Random();     
    

    public Graph createPublicUnweightedgraph(int numNodes) {
        graph = new SingleGraph("Graph");
        numOfNodes = numNodes; 
        nodes = new String[numOfNodes]; 
        edgesList = new String[numOfNodes][numOfNodes]; 

        graph.setAutoCreate(true);

        
        int count = 0;
        for (int i = 0; i < numOfNodes; i++) {
            
            char randomNode = (char) ('A' + count++); 
            graph.addNode("" + randomNode); 
            nodes[i] = "" + randomNode;     
        }
        
        
        for (int i = 0; i < numOfNodes; i++) {
            
            edgesList[i][0] = "" + nodes[i]; 

            
            int threshold2 = rand.nextInt(1000) + 1;
            if (threshold2 > 10) {
            
                if (i < numOfNodes - 1) {
                    if (uniqueEdge(nodes[i], nodes[i + 1])) {
                        edgesList[i][1] = "" + nodes[i + 1];
                        graph.addEdge("" + nodes[i] + nodes[i + 1], nodes[i], nodes[i + 1]);
                    }
            
                } else if (uniqueEdge(nodes[i], nodes[0])) {
                    edgesList[i][1] = "" + nodes[0];
                    graph.addEdge("" + nodes[i] + nodes[0], nodes[i], nodes[0]);
                }
            }
            
            
            int edgeCount = 0; 
            int nodeCount = 2; 
            while (edgeCount <= 0) {
                int threshold = rand.nextInt(1000) + 1;
                
                if (threshold > 520) {
                    int chooseNodeIndex = rand.nextInt(numOfNodes) + 0; 

                    if (i == chooseNodeIndex) {continue;} 

                    
                    if (uniqueEdge(nodes[i], nodes[chooseNodeIndex])) {
                        edgesList[i][nodeCount++] = "" + nodes[chooseNodeIndex];
                        String edge = "" + nodes[i] + nodes[chooseNodeIndex];
                        String from = "" + nodes[i];
                        String to = "" + nodes[chooseNodeIndex];
                        graph.addEdge(edge, from, to); 
                    }
                }
                edgeCount++;
            }
        }
        
        return graph;
    }

    
    private boolean uniqueEdge(String from, String to) {
        boolean unique = false;
        
    
        int ascii = (int) from.charAt(0);
        int fromIndex = ascii - 65;
    
        for (String test : edgesList[fromIndex]) {
            if (test == null) {continue;}
            if (test.equals(to)) {
                unique = false;
                break;
            }
            unique = true;
        }
        
    
        if (unique == true) {
            ascii = (int) to.charAt(0);
            int toIndex = ascii - 65;
     
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
    
    
    public void selectPrivateNodes() {
        int maxPrivateCount = 0; 
        
    
        for (Node pNode : graph) {
            ArrayList<Node> privateNodes = new ArrayList<>(); 
            int probNCount = 0; 
            String[] prohibitedNList = new String[numOfNodes]; 

            
            privateNodes.add(pNode); 
            prohibitedNList[probNCount++] = pNode.getId(); 
            
            for (String nbour : getNeighbours(pNode)) {
                prohibitedNList[probNCount++] = nbour; 
            }

            
            for (Node n : graph) {
                boolean push = true;
                ArrayList<String> temp = getNeighbours(n);  
                for (String nbour : temp) {
                    
                    int index = Arrays.asList(prohibitedNList).indexOf(nbour);
                    
                    if (index > -1) {push = false;}
                }
                
                if (push == true) {
                    privateNodes.add(n); 
                    prohibitedNList[probNCount++] = n.getId(); 
                    for (String nbour : temp) { 
                        prohibitedNList[probNCount++] = nbour; 
                    }
                }
            }
            
            
            if(privateNodes.size() > maxPrivateCount ){
                maxPrivateCount = privateNodes.size();
                privateNodeList = privateNodes;
            }
        }
    }
    
    
    public void colorPrivateNodes() {
    
        for (Node n : privateNodeList) {
            n.addAttribute("ui.style", "fill-color: rgb(0,100,255);text-size:20;"); // css styling
            
        }
    }
    
   
    private ArrayList<String> getNeighbours(Node n1) {
        ArrayList<String> neighbour = new ArrayList<>();
        
        
        Iterator<? extends Node> nodes = n1.getNeighborNodeIterator();
        while (nodes.hasNext()) {
            Node node = nodes.next();
            neighbour.add(node.getId());
        }
        return neighbour;
    }

    
    public void assignWeight(int message) {
        nodeWeight = new int[numOfNodes]; 
        int total = 0; 
        char lastNode = (char) (numOfNodes + 65 - 1); 
        int adjust = 0; 
        
        int i = 0;
        
        for (Node n1 : graph) {
           
            int nodeVal = rand.nextInt(20) + (message - 10); 
            total += nodeVal;
            adjust = nodeVal; 
            nodeWeight[i++] = nodeVal;
            n1.addAttribute("ui.label", n1.getId() + "(" + nodeVal + ")");
            n1.addAttribute("ui.style","text-size:20;");
        }

        Node n1 = graph.getNode("" + lastNode); 
        int newVal = message - (total - adjust); 
        n1.addAttribute("ui.label", n1.getId() + "(" + (newVal) + ")"); 
        nodeWeight[numOfNodes-1] = newVal;
    }
    
    public void assignLabel(int message) {
        nodeWeight = new int[numOfNodes]; 
        int total = 0; 
        char lastNode = (char) (numOfNodes + 65 - 1); 
        int adjust = 0;
        
        int i = 0;
        
        for (Node n1 : graph) {
            
            int nodeVal = rand.nextInt(20) + (message - 10); 
            total += nodeVal;
            adjust = nodeVal; 
            nodeWeight[i++] = nodeVal;
            n1.addAttribute("ui.label", n1.getId());
            n1.addAttribute("ui.style","text-size:20;");
        }

      
    }

    
    public void calculateNewWeight() {
        replacedWeight = new int[numOfNodes]; 
        int count = 0;
        
        for (Node n1 : graph) {
            int newVal = 0; 
            int index = Arrays.asList(nodes).indexOf(n1.getId()); 
            newVal += nodeWeight[index]; 
            
           
            ArrayList<String> neighbours = getNeighbours(n1);
            for(String nb : neighbours ){
                Node nbour = graph.getNode(nb);
                index = Arrays.asList(nodes).indexOf(nbour.getId());
                newVal += nodeWeight[index];
            }
            replacedWeight[count++] = newVal;
            n1.addAttribute("ui.label", n1.getId() + "(" + newVal + ")"); 
        }
    }

    
    public int decryptSecret(){
        int message = 0;
        
        
        for(Node n1 : privateNodeList){
            System.out.println(n1.getId());
            
            int ascii = (int) n1.getId().charAt(0);
            int index = ascii - 65; 
        
            message += replacedWeight[index];
        }
        return message;
    }

   
    public static void main(String args[]) {
        Main mk = new Main();
        int minPrivate = 1; 
        int foundPrivate = 0; 
        int testCount = 1;
        
        int numOfNodes = mk.rand.nextInt(6)+6; 
        do{
           
            mk.createPublicUnweightedgraph(numOfNodes);
            
           
            mk.selectPrivateNodes();
            
            
            if(mk.numOfNodes % 3 == 0)
                minPrivate = (int) ( (mk.numOfNodes / 3) );
            else
                minPrivate = (int) ( (mk.numOfNodes / 3) + 1);
            
           
            foundPrivate = mk.privateNodeList.size(); 
            
            System.out.println("Iteration : " + testCount);
            System.out.println("NodeCount = " + numOfNodes);
            
            System.out.println("minimum" + minPrivate);
            System.out.println("Private" + foundPrivate);
            
            
            if(testCount % 99 == 0){ numOfNodes = mk.rand.nextInt(6)+6;}
            
            if(testCount++ > 10000){break;} 
        }while(minPrivate > foundPrivate);
        
       
        if(minPrivate > foundPrivate){
            System.out.println("xx GRAPH GENERATION xx");
          
        }
        
        
        int someNumber = Integer.parseInt(JOptionPane.showInputDialog("SECRET: Number"));
        mk.assignLabel(someNumber);
        
        
        mk.colorPrivateNodes();
        Graph initialGraph = Graphs.clone(mk.graph);
        initialGraph.display();
        mk.assignWeight(someNumber); 
        
        
        Graph intermediateGraph = Graphs.clone(mk.graph);
        intermediateGraph.display();
        
        
        
       
        mk.calculateNewWeight();
        mk.graph.display();
        
        System.out.println("THE SECRET MESSAGE IS");
        System.out.println(mk.decryptSecret()); 
    }
}
