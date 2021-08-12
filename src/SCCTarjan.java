import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class SCCTarjan {
	
	DirectedSparseGraph<String, String> graph;
	ArrayList<ArrayList<String>> components = new ArrayList<ArrayList<String>>();
	
	int dfsIndex = 0;
	HashMap<String, Integer> dfsIndexNodes = new HashMap<String, Integer>();
	HashMap<String, Integer> lowIndexNodes = new HashMap<String, Integer>();
	Stack<String> nodes = new Stack<String>();
	HashSet<String> nodesOnStack = new HashSet<String>();
	
		
	public ArrayList<ArrayList<String>> getComponents() {
		return components;
	}
	public SCCTarjan(DirectedSparseGraph<String, String> graph)  {
		
		if(graph == null || graph.getVertexCount() == 0) { 
				throw new IllegalArgumentException();
		}
		this.graph = graph;
		identifyStrongComponents(graph);
		
	}
	public static void printComponents(ArrayList<ArrayList<String>> components) { 
		Iterator<ArrayList<String>> it = components.iterator();
		while(it.hasNext()) {
			ArrayList<String> set = it.next();
			System.out.println(set);
		}
	}
	public boolean visited(String node) {
		if(!dfsIndexNodes.containsKey(node))  
			return false;
		
		return true; 
	}
	public void identifyStrongComponents(DirectedSparseGraph<String, String> graph) { 
		Iterator<String> it = graph.getVertices().iterator();
		while(it.hasNext()) { 
			String nextNode = it.next();
			if(!visited(nextNode)) { 
				dfs(nextNode);
			}
		}
	}
	public void dfs(String currentNode) { 
		
		int currentDfsIndex = ++dfsIndex;
		int currentLowIndex = currentDfsIndex;
		dfsIndexNodes.put(currentNode, dfsIndex );
		nodes.push(currentNode);
		nodesOnStack.add(currentNode);
		
		Iterator<String> subgraphIt = graph.getSuccessors(currentNode).iterator();
		while(subgraphIt.hasNext()) { 
			String succ = subgraphIt.next();
			
			if(!visited(succ)) { 
				dfs(succ);
				currentLowIndex = Math.min(currentLowIndex, lowIndexNodes.get(succ));
			}
			else if(nodesOnStack.contains(succ)){ 
				currentLowIndex = Math.min(currentLowIndex, dfsIndexNodes.get(succ));
			}
		}
		lowIndexNodes.put(currentNode, currentLowIndex);
			
		if(currentDfsIndex == currentLowIndex) { 
			ArrayList<String> comp = new ArrayList<String>();
			String root = null;
			do { 
				root = nodes.pop();
				nodesOnStack.remove(root);
				comp.add(root);
			}while(root != currentNode);
			
			components.add(comp);
		}
	}
}