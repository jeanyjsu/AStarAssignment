/*******************************************
 * AStar.java
 * Ya Ching Su -T00645580
 * This is an attempt at implementing an A* search algorithm for a goal-based agent.
 * The software looks for the route starting from node A to reach the goal state, and then in the end.
 * Compares the the three different paths to find the cheapest path in the end.
 */
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class AStar {
     public static void main(String[] args) {
          int[][] cost_matrix =
                  {{0, 0, 0, 6, 1, 0, 0, 0, 0, 0},
                          {5, 0, 2, 0, 0, 0, 0, 0, 0, 0},
                          {9, 3, 0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 1, 0, 2, 0, 0, 0, 0, 0},
                          {6, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                          {0, 0, 0, 7, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 2, 0, 0, 0, 0, 0},
                          {0, 9, 0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 5, 0, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 8, 7, 0, 0, 0}};

          int[] heuristic_vector = {5, 7, 3, 4, 6, 8, 5, 0, 0, 0};

          //creating the nodes and have their heuristic value tie to them as well
          Node n1 = new Node("A", 5);
          n1.adjacency = new Edge[]{};
          Node n2 = new Node("B", 7);
          n2.adjacency = new Edge[]{};
          Node n3 = new Node("C", 3);
          n3.adjacency = new Edge[]{};
          Node n4 = new Node("D", 4);
          n4.adjacency = new Edge[]{};
          Node n5 = new Node("E", 6);
          n5.adjacency = new Edge[]{};
          Node n6 = new Node("H", 6);
          n6.adjacency = new Edge[]{};
          Node n7 = new Node("J", 5);
          n7.adjacency = new Edge[]{};
          Node n8 = new Node("G1", 0);
          n8.adjacency = new Edge[]{};
          Node n9 = new Node("G2", 0);
          n9.adjacency = new Edge[]{};
          Node n10 = new Node("G3", 0);
          n10.adjacency = new Edge[]{};

          //creating the edges with their travel cost(g), but also follows the direction that the graph shows
          n1.adjacency = new Edge[]{new Edge(n2, 5), new Edge(n5, 6), new Edge(n3, 3)};
          n2.adjacency = new Edge[]{new Edge(n8, 9), new Edge(n3, 3)};
          n3.adjacency = new Edge[]{new Edge(n2, 2), new Edge(n4, 1)};
          n4.adjacency = new Edge[]{new Edge(n9, 5), new Edge(n6, 7), new Edge(n1, 6)};
          n5.adjacency = new Edge[]{new Edge(n4, 2), new Edge(n7, 2), new Edge(n1, 1)};
          n6.adjacency = new Edge[]{new Edge(n5, 2), new Edge(n10, 8)};
          n7.adjacency = new Edge[]{new Edge(n10, 7)};


          AStarSearch(n1,n8, cost_matrix, heuristic_vector);
          AStarSearch(n1, n9, cost_matrix, heuristic_vector);
          AStarSearch(n1, n10, cost_matrix, heuristic_vector);

          List<Node> path = printPath(n8);
          List<Node> path2 = printPath(n9);
          List<Node> path3 = printPath(n10);

          //Now with the three different optimal paths towards the three goals, they are being stored into a list.
          //The list is being looped over can the costs are being compared. The cheapest route is then decided.
          List<List<Node>> paths = new ArrayList<>();
          paths.add(path);
          paths.add(path2);
          paths.add(path3);
          List<Node> cheapest = null;
          int cheapestCost = Integer.MAX_VALUE;

          for(List<Node> theOnePath: paths)
          {
               if (theOnePath != null) {
                    int cost = 0;
                    for (int i = 0; i < theOnePath.size() - 1; i++) {
                         Node current = theOnePath.get(i);
                         Node next = theOnePath.get(i + 1);

                         for (Edge e : current.adjacency) {
                              if (e.target == next) {
                                   cost += e.cost;
                                   break;
                              }
                         }
                    }
                    if (cost < cheapestCost) {
                         cheapestCost = cost;
                         cheapest = theOnePath;
                    }
               }
          }
          System.out.println("Gimme the cheapest: " + cheapest);
     }

     //loops from the goal to the nodes before it, and all the way back to the starting node 'A'.
     //it then adds the nodes into the arraylist we created.
     //need to reverse it as the loop took us from the goal states to 'A' and that affects the total cost.
     public static List<Node> printPath(Node goal) {
          List<Node> path = new ArrayList<Node>();
          for (Node node = goal; node != null; node = node.parent) {
               path.add(node);
          }
          Collections.reverse(path);
          return path;
     }


     public static List<Node> AStarSearch(Node startNode, Node goalNode, int[][] costMatrix, int[] heuristicVector) {
          Set<Node> explored = new HashSet<Node>();
          //First create a priority queue to compare all the possible node options, it'll put the one with the lower f values
          // as the priority.
          PriorityQueue<Node> queue = new PriorityQueue<Node>(10, new Comparator<Node>()
          {
               public int compare(Node x, Node y) {
                    if (x.fValue > y.fValue) {
                         return 1;
                    } else if (x.fValue < y.fValue) {
                         return -1;
                    } else {
                         return 0;
                    }
               }
          }
          );

          //The starting node is 'A' and the g value is 0 right now as it's not moving anywhere yet
          startNode.gValue = 0;
          queue.add(startNode);

          int counter = 0;
          boolean found = false;

          //while queue is not empty and the goal not found yet, the current is the first element in the priority
          // queue, which is the node that has the lowest f value.
          while ((!queue.isEmpty()) && (!found)) {
               Node current = queue.poll();
               explored.add(current);

               if (current.value.equals(goalNode.value)) {
                    found = true;
               }

               //Loops through all the adjacency links from the current node determines the next target based on the cost
               for (Edge links : current.adjacency) {
                    Node child = links.target;
                    int cost = links.cost;
                    int tempGValue = current.gValue + cost;
                    int tempFValue = tempGValue + costMatrix[current.index][child.index] + heuristicVector[child.index];

                    //if the next node is already in the explored list, and the newer f value is higher the loop goes to another
                    //option.
                    //else if next node is not in queue or newer f value is lower then the values are changed to the next
                    //node.
                    if ((explored.contains(child)) && (tempFValue >= child.fValue))
                    {
                         continue;
                    }
                    else if ((!queue.contains(child)) || (tempFValue < child.fValue))
                    {
                         child.parent = current;
                         child.gValue = tempGValue;
                         child.fValue = tempFValue;

                         if (queue.contains(child))
                         {
                              queue.remove(child);
                         }
                         queue.add(child);
                    }
               }
               counter++;
          }
          System.out.println("Number of cycles: " + counter);
          return null;
     }
}
