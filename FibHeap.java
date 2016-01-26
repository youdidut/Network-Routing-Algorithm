import java.util.ArrayList;
import java.util.List;


public class FibHeap {
    
   int nodeNum;         // Total Node number
   FibNode min;        // Min node in heap

   
      public static class FibNode {
      int degree;            
      FibNode left;        // left sibling of node
      FibNode right;        // right sibling of node
      FibNode child;        // child of node
      FibNode parent;        // parent of node
      boolean marked;     // childcut value of node
      public final String name;   //node name e.g. 0,1,2,3,4...
      public List<Edge> adjacencies; //List of adjacent nodes
      public int minDistance;  //node's current min distance to source
      FibNode previous;  //last hop from source on min-distance path
      boolean inHeap;   //if this node is in heap
      

       public FibNode(String name) {
           this.minDistance = (int) Double.POSITIVE_INFINITY;
           this.degree = 0;
           this.marked = false;
           this.left   = this;
           this.right  = this;
           this.parent = null;
           this.child  = null;
           this.name=name;
           this.adjacencies = new ArrayList<Edge>();
           this.previous = null; 
           this.inHeap=false;
      }
       public void addEdge(Edge e) {
           adjacencies.add(e);
       }             
   }

   public FibHeap() {
       this.nodeNum = 0;
       this.min = null;
    }

  /* 
      * Remove node from doubly linked list
      */
    private void removeNode(FibNode node) {
        node.left.right = node.right;
        node.right.left = node.left;
       }
     
     /*
     * add node into doubly linked list before root node 
      *   a .... root
      *   a .... node.... root
     */
    private void addNode(FibNode node, FibNode root) {
        node.left        = root.left;
        root.left.right  = node;
        node.right       = root;
        root.left        = node; 
      }
       
      /*
       * insert node into heap
       */
      public void insert(FibNode node) {
          if (nodeNum == 0){
          min = node;}
         else {
              addNode(node, min);
              node.child=null;
              node.degree=0;
              node.parent=null;
              node.marked=false;
          
              if (node.minDistance < min.minDistance)
                  min = node;
          }
  
          nodeNum++;}
         
      

     /*
      * Remove min node from root's linked list
      */
     private FibNode extractMin() {
         FibNode p = min;
 
         if (p==p.right){
           
             min = null;}
         else {
             removeNode(p);
             min = p.right;
         }
         p.left = p.right = p;
 
         return p;
     }
      
     /*
      * link node to root node
      */
     private void link(FibNode node, FibNode root) {         
         removeNode(node);
         // make node a child of root
         if (root.child == null){
             node.left=node;
             node.right=node;
             root.child = node;}
         else
            addNode(node, root.child);
             
 
         node.parent = root;
         root.degree++;
         node.marked = false;
     }
      
     /* 
      * pairwise combine trees of the same degrees
      */
     private void consolidate() {
       int maxDegree = (int) Math.floor(Math.log(nodeNum) / Math.log(2.0));
                int D = maxDegree + 1;
                FibNode[] cons = new FibNode[D+1];
        
                for (int i = 0; i < D+1; i++)
                    cons[i] = null;              
                
                while (min != null) {
                   FibNode x = extractMin();            
                    int d = x.degree;                        // get the degree value of min tree
                    // cons[d] != null: this means tree x and y have the same degree
                  while (cons[d] != null) {
                        FibNode y = cons[d];                
                        if (x.minDistance > y.minDistance) {    // make sure x is smaller than y
                            FibNode tmp = x;
                            x = y;
                            y = tmp;
                        }
        
                        link(y, x);   
                        cons[d] = null;
                        d++;
                    }
                    cons[d] = x;
                }
                min = null;
             
                // combine nodes of the same degrees until each degree has at most one tree
                for (int i=0; i<D+1; i++) {
        
                    if (cons[i] != null) {
                        if (min == null){
                         
                            min = cons[i];}
                        else {
                            addNode(cons[i], min);
                            if ((cons[i]).minDistance < min.minDistance)
                                min = cons[i];
                        }
                    }
                }
               }
    
  /*
    * remove the min node
    */
   public void removeMin() {
       if (min==null)
           return ;

       FibNode m = min;
     // add each child of min to the linked list of root
        while (m.child != null) {   
         FibNode child = m.child;       
         removeNode(child);
        if (child.right == child)
             m.child = null;
         else
              m.child = child.right;

           addNode(child, min);
           child.parent = null;
       }

        // remove m fron heap
       removeNode(m);       
       // if m is the last node in heap, set min to null
       // else set min to be m.right. This will be further adjusted later in consolidate
        if (m.right == m)
          min = null;
       else {
           min = m.right;
           consolidate();
        }
       nodeNum--;
   }

       
    /* 
     * modify the degree of a tree
  */
   private void renewDegree(FibNode parent) {
     parent.degree--;
   }
      
     /* 
        * cut a node from its parent and insert node into the linked list of root
      */
   private void cut(FibNode node, FibNode parent) {
       removeNode(node);
       renewDegree(parent);
       if (parent.degree==0)
         parent.child=null;
        // node has no sibling
       if (node == node.right) 
           parent.child = null;
      else 
           parent.child = node.right;

       node.parent = null;
       node.left = node.right = node;
       node.marked = false;
       // add tree containing node to the linked list of root
        addNode(node, min);
        if ((node).minDistance < min.minDistance)
            min = node;
   }

    /* 
     *Cuscading cut a node in a recursive way
    */
    private void cascadingCut(FibNode node) {
         FibNode parent = node.parent;

        if (parent != null) {
            if (node.marked == false) 
                 node.marked = true;
          else {
                 cut(node, parent);
                cascadingCut(parent);
           }
        }
    }

   /* 
    * decrease the mindistance value of min 
     */
  void decrease(FibNode node, int minDistance) {
       if (min==null ||node==null) { 
           return ;
       }
       
      if (minDistance > node.minDistance) {
          System.out.printf("decrease failed: the new minDistance(%d) is no smaller than current minDistance(%d)\n", minDistance, node.minDistance);
           return ;
       }

      FibNode parent = node.parent;
     node.minDistance = minDistance;
     if (parent!=null && (node.minDistance < parent.minDistance)) {
          //if node is smaller than its parent, cut it from the tree
            cut(node, parent);
          cascadingCut(parent);
       }       
     if ((node).minDistance < min.minDistance)
         min = node;
   }   
  

  void showFib(FibNode root)
  {
   FibNode node;
   node=root;
   if (root!=null&&root.parent!=null )
   System.out.println(Integer.toString(root.minDistance)+" ");
   else System.out.println(Integer.toString(root.minDistance)+" ");
   while (node.right!=root)
   {
     node=node.right;
     System.out.println(Integer.toString(node.minDistance)+" ");

   }
   if (root.child!=null)
     showFib(root.child);
   node=root;
   while (node.right!=root)
   {
     node=node.right;
     if(node.child!=null)
     showFib(node.child);}
  }
  }