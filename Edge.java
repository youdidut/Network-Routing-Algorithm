public class Edge     //to keep record of the neighbors of a node and their distance
{                           
    public final FibHeap.FibNode target;                //adjacent node
    public final int weight;                            //distance
    public Edge(FibHeap.FibNode argTarget, int argWeight)
    { target = argTarget; weight = argWeight; }
}
