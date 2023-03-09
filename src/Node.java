/*******************************************
 * Node.java
 * Ya Ching Su -T00645580
 * This is the Node class.
 */
class Node{
     public final String value;
     public int gValue; 
     public final int heuristic;
     public int fValue = 0;
     public Edge[] adjacency;
     public Node parent;
     public int index;


     public Node(String val, int hVal){
          value = val;
          heuristic = hVal;
     }
     public String toString(){
          return value;
     }
}
