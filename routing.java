import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

//this class is to compute the prefix routing table for each node
public class routing {                               

	private static Map<Integer, String> toIP =new HashMap<Integer, String>();	
	public static class pair        //<IP, next hop> structure
	{
		String to;
		int nextHop;
		
		public pair(String content, int next)                  
		{                                                 
			this.to=content;
			this.nextHop=next;
		}
	}	
	
    //read the input file for IP addresses, and save them is a hash map
    public static void readIP(String filePath)           
    {                    
    	 try {                 
    	  String encoding="GBK";    	 
          File file=new File(filePath);
          if(file.isFile() && file.exists()){ 
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            int i=0;
            while((lineTxt = bufferedReader.readLine()) != null){
            	 if (lineTxt.equals("")) {               //if there is a blank row
                     lineTxt = bufferedReader.readLine();
                     if (lineTxt.equals(""))                           
                     break;}
            	String bn = getAddress(lineTxt);
            	toIP.put(i, bn);
            	i++;
            }
            read.close();}   
        else{
            System.out.println("cannot open file");
        }
        } catch (Exception e) {
            System.out.println("error while reading file");
            e.printStackTrace();}
    }
    
    //convert IP address to binary form 
    public static String toBinaryNumber(String ipAddress) {
		String[] octetArray = ipAddress.split("\\.");
		String binaryNumber = "";
		for(String str: octetArray) {
			int octet = Integer.parseInt(str, 10);
			String binaryOctet = Integer.toBinaryString(octet);
			int bolength = binaryOctet.length();
			if(bolength < 8) {
				for (int i = 0; i < 8 - bolength; i++) {
					binaryOctet = '0' + binaryOctet;			
				}
			}
			binaryNumber += (binaryOctet);
		}
		return binaryNumber;
	}
    
    public static String getAddress(String cidrAddress) {
		String binaryNumber = toBinaryNumber(cidrAddress);
		return binaryNumber;
	}
    
    public static void main(String[] args){
    ssp r=new ssp();
  	int[][] table;
  	table=r.build(args);                         //table[i][j]=x; where i is the source, j is the destination and x is the next hop
    int routerNum=table[0].length;               //total number of nodes
    readIP(args[1]);                             //read IP address from file
    pair pairTable[][]=new pair[routerNum][routerNum];     //pairTable[i][j]=pair, where i is the source, j is the destination and pair.to is the destination IP and nextHop is the node number of its next hop     
    binaryTrie[] bt=new binaryTrie[routerNum];      
    for(int i=0;i<routerNum;i++)                    //for each node as the source node
    {
    	bt[i]=new binaryTrie();
    	for(int j=0;j<routerNum;j++)              //for each node as the destination node
    	{
    		if (i==j)                              //if source is also destination
    			continue;
    		pairTable[i][j]=new pair(toIP.get(j),table[i][j]);    		   
    		bt[i].insert(pairTable[i][j]);
    	}
    	bt[i].traverse();                     //postorder traversal each trie
    }
    
    int from=Integer.parseInt(args[2]);
    int to=Integer.parseInt(args[3]);
    String IP=toIP.get(to);
    int minWeight=r.minWeight(args);
    System.out.println(Integer.toString(minWeight));          //print the min distance from source to destination
    while (from!=to){
    bt[from].print(IP);                                 //print the prefix
    from=table[from][to];
    }
    System.out.println();
    }
}