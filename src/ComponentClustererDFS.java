import java.util.Iterator;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.util.*;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
public class ComponentClustererDFS {
	
	HashSet<String> visited;
	DirectedSparseGraph<String, String> graph;
	ArrayList<String> path;
	public ComponentClustererDFS(DirectedSparseGraph<String, String> g, String source, String destination) { 
		if(g == null || g.getVertexCount() == 0) { 
			throw new IllegalArgumentException();
		}
		this.graph = g;
		visited = new HashSet<String>();
		path = new ArrayList<String>();
		findPath(source, destination);
	}
	public void findPath(String s, String d) {
		path.add(s);         
        dfsTraversal(s, d, visited, path);
    }
    private void dfsTraversal(String u, String d, HashSet<String> visited, ArrayList<String> path) {    
    	
        visited.add(u);  
        if (u.equals(d)) {
        	System.out.println(path);
            return;
        }
        for (String i : graph.getSuccessors(u)) {
            if (!visited.contains(i)) {
                path.add(i);
                dfsTraversal(i, d, visited, path);              
                path.remove(i);
            }
        }
    }
	public ArrayList<String> getPath() {
		return path;
	}
}
