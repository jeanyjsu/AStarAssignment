/*******************************************
 * Edge.java
 * Ya Ching Su -T00645580
 * This is the Edge class.
 */
class Edge{
     public final int cost;
     public final Node target;

     public Edge(Node targetNode, int costVal){
          target = targetNode;
          cost = costVal;
     }
}