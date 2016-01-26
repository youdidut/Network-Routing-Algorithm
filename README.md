# Network-Routing-Algorithm
1. Compiler Information 
The project is programmed in java, and uses javac as the compiler. The compile process can
be achieved by calling "makefile" file with "make" command. 
 
2. Function Prototypes 
To achieve the project requirements of part 1 and part 2, several classes are built up with
their respect methods. Their prototypes and functions will be explained.

Part 1
a. public class FibHeap 
This class is used to build up fibonacci heaps with fibonacci nodes (public static class
FibNode ). Apart from the basic attributes (left sibling, right sibling, parent, child,
childcut, .etc) of a fibonacci node, it also keeps information like the current min distance
from source, the previous node on the min path and so on. The FibHeap class provides a
series of methods that can be operate on the heaps like remove node, insert node, remove
min, pair-wise combine , decrease key, cascading cut. 
One f the duties of the FibHeap class is to acquire node (FibHeap.min) with min distance
value to the source node and then remove (FibHeap.removemin) it. The other duty is to
decrease the min distance value of a node if a shorter distance has been found. 

b. public class Edge
This class is used to keep record the neighbor list of a fibonacci node and the distance
between it and its neighbors. Class Edge information will be added under a list structure
(List<Edge> adjacencies) of each node.

c. public class ssp
This class contains the main function of part 1. The class first read files and build up the
fibonacci nodes. Then it computes the path from a source node to a destination node using
Dijkstra's algorithm by inserting, removing min elements, and decreasing keys in fibonacci
heap. After each node acquires its min path and the previous node on this path, we print the
min distance from a source node and each node on the path. 
This class also provides methods to be called from part 2 of the project (public int[][]
build(String[] args), public int minWeight(String[] args)), which will return the routing table in
the form (source, destination, nextHop), as well as the min distance value of each shortest
path given the source and destination nodes.


 
Part 2
a. public class binaryTrie
This class is used to build up binary tries with binary trie nodes (class trieNode). The trie
node contains a pair structure (public static class pair) which is of the form (IP address in
binary form, next hop router number). This binary trie class also provides methods like
inserting, searching, post-order traversal and combining siblings with the same next hop.
This class provides a way to get the longest prefix matching for each router.

b. public class routing
This class containing the main function of the project of part 2. It first utilizes methods from
class ssp in part 1 to acquire routing table for each router in the form (source, destination,
nextHop), as well as the min distance value of each shortest path given the source and
destination nodes. Then the class transform this table into pairs (public static class pair)
containing in each trie node. With class binaryTrie, we get the routing table in the form
(prefix, nextHop) for each router. At last the result is printed out.
