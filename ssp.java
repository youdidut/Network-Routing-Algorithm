import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;      

public class ssp
{  
  //compute the path from destination to source with DIJKSTRA's algorithm
  public static void computePaths(FibHeap.FibNode source)              
    {
        source.minDistance = 0;
        FibHeap myHeap = new FibHeap();
        myHeap.insert(source);                //insert the source node with minDistance 0 to heap first
        source.inHeap=true;                   //denotes that this node is inserted in to heap
        
         while (myHeap.nodeNum!=0) {           //while there is still node in heap
          FibHeap.FibNode u = myHeap.min;      //get  min node
          myHeap.removeMin();                  //remove min node from heap
          u.inHeap=false;                      //denote this node has been removed
                    
            for (Edge e : u.adjacencies)        //compute minDistance of the neighbors of this node via this node
            {
              FibHeap.FibNode v = e.target;
                int weight = e.weight;
                int distanceThroughU = u.minDistance + weight;
                
        if (distanceThroughU < v.minDistance) {        //if a shorter path is found, replace the longer one
            v.minDistance = distanceThroughU ;
            v.previous = u;            
            if (v.inHeap==true)
              myHeap.decrease(v, distanceThroughU);
            else {myHeap.insert(v);          
                  v.inHeap=true;}
            }        
        }          
     }         
}

    //show each node on the shortest path 
    public static List<FibHeap.FibNode> getShortestPathTo(FibHeap.FibNode target)                   
    {
        List<FibHeap.FibNode> path = new ArrayList<FibHeap.FibNode>();
        for (FibHeap.FibNode FibNode = target; FibNode != null; FibNode = FibNode.previous)           //from destination to source
            path.add(FibNode);
        Collections.reverse(path);                                //reverse the order of nodes in path
        return path;
    }
    
    //read from file and get total router number involved
    public static int getFibNodeNum(String filePath){
        int num_ver = 0;
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){                             //if file exists
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    lineTxt = bufferedReader.readLine();
                    String[] sourceStrArray = lineTxt.split(" ");
                    num_ver = Integer.parseInt(sourceStrArray[0]); 
                    read.close();
        }else{
            System.out.println("Cannot find the file.");
        }
        } catch (Exception e) {
            System.out.println("Error encounted when reading file");
            e.printStackTrace();
        }
        return num_ver;
        }                                           
                    
    //read from file and build up the fibnacci node
    public static void readTxtFile(String filePath, FibHeap.FibNode[] v){
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){                           //if file exits
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    lineTxt = bufferedReader.readLine();
                    String[] sourceStrArray;                
                    
                    while((lineTxt = bufferedReader.readLine()) != null){
                        if (lineTxt.equals("")) {
                            lineTxt = bufferedReader.readLine();
                            if (lineTxt.equals("")) 
                            break;}
                        sourceStrArray = lineTxt.split(" ");
                        int i=Integer.parseInt(sourceStrArray[0]);
                        int j=Integer.parseInt(sourceStrArray[1]);
                        int k=Integer.parseInt(sourceStrArray[2]);
                        v[i].addEdge(new Edge(v[j], k)) ;                 //save the edge information into each node structure
                        v[j].addEdge(new Edge(v[i], k)) ;                   
                    }
                    read.close();
        }else{
            System.out.println("Cannot find the file");
        }
        } catch (Exception e) {
            System.out.println("Error encounted when reading from file");
            e.printStackTrace();
        }}    
         
    //build up routing table for all router, with the format table[source][destination]=nextHop
    public int[][] build(String[] args)                                
    {
      String filePath = args[0];
        int num_ver = getFibNodeNum(filePath);
        FibHeap.FibNode v[] = new FibHeap.FibNode[num_ver];           
                 
        int table[][]=new int[num_ver][num_ver];
        for(int i=0;i<num_ver;i++)                               //for each source
        {
          for(int m=0;m<num_ver;m++) v[m]=new FibHeap.FibNode(Integer.toString(m));
          readTxtFile(filePath, v);  
          int from = i;   
          computePaths(v[from]);
          for (int j=0;j<num_ver;j++)                          //for each destination
          {
            int to = j;                
                FibHeap.FibNode ve=v[to];
                List<FibHeap.FibNode> path = getShortestPathTo(ve);
                if (path.size()>1)                             //if destination is not also source
                table[i][j]=Integer.parseInt(path.get(1).name);
                else table[i][j]=0;                         
          }
        }
        return table;
    }
    
    //compute the minWeight from source to destination
    public int minWeight(String[] args)
    {
    String filePath = args[0];
    int num_ver = getFibNodeNum(filePath);
    FibHeap.FibNode v[] = new FibHeap.FibNode[num_ver];    
    for(int i=0;i<num_ver;i++) v[i]=new FibHeap.FibNode(Integer.toString(i));    
    readTxtFile(filePath, v);   
    int from = Integer.parseInt(args[2]);
    int to = Integer.parseInt(args[3]);
    computePaths(v[from]);
    FibHeap.FibNode ve=v[to];    
    return ((int)ve.minDistance);     
    }           
   
    
    public static void main(String[] args)
    {
      //long time = System.currentTimeMillis();                 //this is to test the efficiency of this program
    String filePath = args[0];
    int num_ver = getFibNodeNum(filePath);
    FibHeap.FibNode v[] = new FibHeap.FibNode[num_ver];    
    for(int i=0;i<num_ver;i++) v[i]=new FibHeap.FibNode(Integer.toString(i));    
    readTxtFile(filePath, v);   
    int from = Integer.parseInt(args[1]);                        //get the source router number from input
    int to = Integer.parseInt(args[2]);                          //get the destination router number from input
    computePaths(v[from]);
    FibHeap.FibNode ve=v[to];                                    //destination node
    System.out.println((int)ve.minDistance);
    List<FibHeap.FibNode> path = getShortestPathTo(ve);
    for (int i = 0, len = path.size(); i < len; i++) {  
       System.out.print(path.get(i).name+" ");                    //print the shortest path
        }           
    System.out.println();
    //System.out.println(System.currentTimeMillis() - time);      //this is to test the efficiency of this program
    }
}
