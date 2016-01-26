public class binaryTrie {
	trieNode root;  //the root node f the trie
	private int count=1;  //test if the trie has been done with postorder traversal
	
	public binaryTrie() {             //initialize the trie
	   this.root = null;
	 }
	
	class trieNode 
	{
	  int isElement;     //if this is an element node
	  routing.pair pair;        //pair of the form <IP, next hop> associated with each node
	  trieNode children[]=new trieNode[2];    //left and right children of node
	  int level;                 //which level the node is at
	  trieNode parent;   //the parent of this node
	  
      public trieNode(routing.pair p)
      {
    	  this.isElement=0;
    	  this.pair=p;
    	  this.children[0]=null;
    	  this.children[1]=null;
    	  this.level=0;
    	  this.parent=null;
      }
	}
	
	public void insert(routing.pair p)    
	{                                                  //insert <IP,next hop> pair to the trie
		if(p.to.length()!=32)
		{
			System.out.println("IP is not 32 bits");   //test if this is an IPV4 address
			return;
		}
		trieNode node=search(p.to);           //get the node where the pair to be inserted is or falls off from
		trieNode newNode;
		if (node==null)                         //if the trie is empty, make the pair the root
		{
			newNode=new trieNode(p);
			root=newNode;
			root.level=1;
			root.isElement=1;
			return;
		}
		
		if (node.isElement==0)          //if the pair falls off from a branch node
		{
			int level=node.level;
			int index = (int) (p.to.charAt(level-1) - '0');
			newNode=new trieNode(p);
			newNode.isElement=1;
			newNode.level=level+1;
			newNode.parent=node;
			node.children[index]=newNode;
		}
		else                                 //if the pair falls off from an element node
		{
			if(node.pair.to==p.to)                    //if the pair has been already inserted
			{
				System.out.println(p.to+" is already inserted");
			}
			else                                         //if the pair is not in the trie, insert it
			{
				String s;                         //s is the future content of the fell-off-from element node
				int i;
				if (node==root) 
					s=null;
				else s=String.valueOf(node.pair.to.charAt(node.level-2));				
				newNode=new trieNode(p);                                         //the new node to be inserted in to the trie
				routing.pair oriPair=new routing.pair(node.pair.to,node.pair.nextHop);
				trieNode oriNode =new trieNode(oriPair);                 //the element node which is fell off from
				oriNode.isElement=1;				
				node.isElement=0;
				node.pair.to=s;
				
				for(i=node.level;;i++)                  //insert each node following the path
				{
					if (p.to.charAt(i-1)!=oriNode.pair.to.charAt(i-1))                //if it is time to insert the element node
					{
						newNode.level=i+1;
						newNode.isElement=1;
						newNode.parent=node;
						oriNode.level=i+1;
						oriNode.parent=node;
						node.children[p.to.charAt(i-1)- '0']=newNode;
						node.children[oriNode.pair.to.charAt(i-1)- '0']=oriNode;
						break;
					}	
					routing.pair branchPair=new routing.pair(String.valueOf(p.to.charAt(i-1)),-1);             //else insert branch nodes
					trieNode branchNode=new trieNode(branchPair);
					branchNode.parent=node;
					int index = (int) (p.to.charAt(i-1) - '0');
					node.children[index]=branchNode;
					node=node.children[index];
					node.level=i+1;								
				}						
			}
		}
	}
	
	private trieNode search(String ch)             
	{                                                   //search for a node following its IP
		trieNode m=root;		
		if(m==null)
			return null;		
		int i;
		for(i=0;i<32;i++)
		{
			int index = (int) (ch.charAt(i) - '0');			
			if(m.children[index] == null) 
				return m;
			m=m.children[index];		
		}
		return m;                                          //return the last node accessed in the search
	}
	
	public void print(String ch)                            
	{                                                       //to print the prefix in the trie given an IP address
		trieNode m=root;		
		if(m==null)
			return;		
		int i;
		for(i=0;i<32;i++)
		{
			int index = (int) (ch.charAt(i) - '0');			
			if(m.children[index] == null) 
				return;
			m=m.children[index];
			if (m.isElement!=1)
			  System.out.printf(m.pair.to);		
			else System.out.printf(String.valueOf(m.pair.to.charAt(i))+" ");	
		}
		return;
	}
	
	private void combine(trieNode m, trieNode n)                     
	{                                                     //combine siblings with the same next hop and make their parents an element node containing prefix
		trieNode parent=m.parent;
		int level=parent.level;
		parent.pair.to=m.pair.to.substring(0,level-1);
		parent.pair.nextHop=m.pair.nextHop;
		parent.isElement=1;
		parent.children[0]=null;
		parent.children[1]=null;
		}
	
	
	public void traverse()                              
	{                                                   //postorder traverse and combine until there is no more change
		while(count!=0)
		{ 
			count=0;
		    postOrder(root);
		}
	}
	
	private void postOrder(trieNode node)
	{                                                       //visit each node in the trie in a postorder manner
		if(node == null) return;		                    
		postOrder(node.children[0]);
		postOrder(node.children[1]); 
				
	   if (node.parent!=null){	
	   if(node.isElement==1 && node.parent.children[0]==node)                     //if this is a left child and an element node
	   {
		   if(node.parent.children[1].isElement==1)
		   {
			   trieNode rightSibling=node.parent.children[1];
			   if (rightSibling.pair.nextHop==node.pair.nextHop)
			   {
				   trieNode parentNode=node.parent;
				   combine(node, rightSibling);                              //combine if the node has the same next hop with its sibling
				   count++;                                                   //count if the trie structure is done with combining
				   while(parentNode.parent.children[0]==null || parentNode.parent.children[1]==null)       //combine until the node has a sibling
				   {
					   combine(parentNode,null);
					   parentNode=parentNode.parent;
				   }					   
			   }
		   }
	   }
	}
}}