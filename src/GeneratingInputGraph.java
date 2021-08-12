import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class GeneratingInputGraph extends SCCTarjan{
	
	public GeneratingInputGraph(DirectedSparseGraph<String, String> graph) {
		super(graph);
	}

	public static void main(String[] args) throws IOException { 
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Unesite broj grana za ucitavanje (zbog lepseg ispisa preporucujem ne vise od 200):");
		int number = Integer.parseInt(br.readLine());
		DirectedSparseGraph<String, String> graph = readFile(number);
		for(int i=0; i<20; i++) { 
			RandomlyRemoveEdge(graph);
		}
		printGraph(graph);
		SCCTarjan scc = new SCCTarjan(graph);
		System.out.println("Algoritam ce sada vratiti k jako povezanih komponenti ; k > 0 " + "\n");
		printComponents(scc.getComponents());
		
		System.out.println();
		System.out.println("Neophodne grane za dodavanje da bi graf bio jako povezan: " + "\n");
		DirectedSparseGraph<String, String> graphNew = addEdges(graph, scc.getComponents());
		SCCTarjan scc2 = new SCCTarjan(graphNew);
		
		System.out.println();
		System.out.println("Nakon ponovnog pozivanja algoritma, vidimo da postoji samo jedna jako povezana komponenta.");
		printComponents(scc2.getComponents());
		printGraph(graphNew);
		System.out.println();
		
		String[] data = RandomlyChooseVertex(graph);
		String source = data[0];
		String destination = data[1];
		System.out.println("Sad je ostalo samo jos pronalazenje puta izmedju dva cvora."
				+ "Put izmedju cvora " + source + " i cvora " + destination + ":");
		ComponentClustererDFS ccb = new ComponentClustererDFS(graphNew, source, destination);

	}
	
	public static DirectedSparseGraph<String, String> readFile(int number) throws IOException {  
		
		BufferedReader br = new BufferedReader(new FileReader("routes.dat"));
		DirectedSparseGraph<String, String> graph = new DirectedSparseGraph<String, String>();
		try {
			int counter = 0;
		    while (counter != number) {
		    	String[] lineString = br.readLine().split(",");
		    	String source = lineString[2].trim();
		    	String destination = lineString[4].trim();

		    	if(!graph.containsVertex(source)) { 
		    		graph.addVertex(source);
		    	}
		    	if(!graph.containsVertex(destination)) {
		    		graph.addVertex(destination);
		    	}
		    	if(graph.findEdge(source, destination) == null)  {
		    		graph.addEdge(source + " " + destination, source, destination, EdgeType.DIRECTED);
		    	}
		    	counter++;
		    } 
		}finally  {
			br.close();
		}
		return graph;
	}
	
	public static String[] RandomlyChooseVertex(DirectedSparseGraph<String, String> graph) {
		ArrayList<String> lista = new ArrayList<String>(graph.getVertices());
		Random r = new Random();
		int n = r.nextInt(lista.size());
		String first = lista.remove(n);
		int k = r.nextInt(lista.size());
		String second = lista.remove(k);
		String[] array = {first, second};
		return array;
	}
	
	public static void RandomlyRemoveEdge(DirectedSparseGraph<String, String> graph) {
		ArrayList<String> lista = new ArrayList<String>(graph.getEdges());
		Random r = new Random();
		int n = r.nextInt(lista.size());
		graph.removeEdge(lista.get(n));
	}
	
	public static DirectedSparseGraph<String, String> addEdges(DirectedSparseGraph<String, String> graph, ArrayList<ArrayList<String>> components) { 
		DirectedSparseGraph<String, String> graphNew = copyGraph(graph);
		for(int i=0; i<components.size()-1; i++)  {
			ArrayList<String> comp1 = components.get(i);
			ArrayList<String> comp2 = components.get(i+1);
			String first = comp1.get(0);
			for(String s : comp1)  {
				if(graph.outDegree(s)+graph.inDegree(s) > graph.outDegree(first)+graph.inDegree(first)) { 
					first = s;
				}
			}
			String second = comp2.get(0);
			for(String s : comp2)  {
				if(graph.outDegree(s)+graph.inDegree(s) > graph.outDegree(second)+graph.inDegree(second)) { 
					second = s;
				}
			}
			graphNew.addEdge(first + " " + second, first, second, EdgeType.DIRECTED);
			System.out.println(first + " -> " + second);			
		}
		graphNew.addEdge(components.get(components.size()-1).get(0) + " " + components.get(0).get(0), 
				components.get(components.size()-1).get(0), components.get(0).get(0), EdgeType.DIRECTED);
		System.out.println(components.get(components.size()-1).get(0) + " -> " + components.get(0).get(0));
		return graphNew;
	}
	
	public static DirectedSparseGraph<String, String> copyGraph(DirectedSparseGraph<String, String> graph) { 
		DirectedSparseGraph<String, String> newGraph = new DirectedSparseGraph<String, String>();
		for(String vertex : graph.getVertices()) { 
			newGraph.addVertex(vertex);
		}
		for(String edge : graph.getEdges()) { 
			newGraph.addEdge(edge, graph.getIncidentVertices(edge));
		}
		return newGraph;
	}
	
	public static void printGraph(DirectedSparseGraph<String, String> graph) { 
	    Layout<Integer, String> layout = new ISOMLayout(graph);
	    layout.setSize(new Dimension(800,800));
	     BasicVisualizationServer<Integer,String> vv =  new BasicVisualizationServer<Integer,String>(layout);
	     vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	     vv.setPreferredSize(new Dimension(1000,1000)); 
	     JFrame frame = new JFrame("Simple Graph View");
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.getContentPane().add(vv); 
	     frame.pack();
	     frame.setVisible(true); 
	}
}
