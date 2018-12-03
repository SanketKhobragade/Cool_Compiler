package cool;
import java.util.*;
import java.util.Map.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;


class inherit{
	private List <AST.class_> classes;
	private ArrayList <String> classmap;
	private ArrayList < ArrayList <Integer> > graph;
	listOfClasses classTable = new listOfClasses();

	public void initialise(List <AST.class_> classli){
		classes = classli;
		classmap = new ArrayList < String >();
		classmap.add("Object"); classmap.add("IO");
		int class_ct = invalidclass();
		graph = new ArrayList < ArrayList <Integer> >();
		for(int i = 0; i < class_ct; i++)
			graph.add(new ArrayList <Integer>());
		graph.get(0).add(1);
	}

	public int invalidclass(){
		int class_ct = 2;
		for(int i = 0; i < classes.size(); i++){
			AST.class_ x = classes.get(i);
			if(x.name.equals("Object") || x.name.equals("String") || x.name.equals("Int") || x.name.equals("Bool") || x.name.equals("IO"))  {
				System.err.println(x.filename+ ": " + x.lineNo+ ": " + "Redefinition of class : " + x.name);
				System.exit(1);
			}
			if(x.parent.equals("Int") || x.parent.equals("Bool") || x.parent.equals("String")) {
				System.err.println(x.filename+ ": " + x.lineNo+ ": " + "Cannot inherit class : " + x.parent);
				System.exit(1);
			}
			if(classmap.indexOf(x.name) == -1) {
				classmap.add(x.name);
				class_ct++;
			}
		}
		return class_ct;
	}

	public void makegraph(){
		for(int i = 0; i < classes.size(); i++){
			AST.class_ x = classes.get(i);
			int u = classmap.indexOf(x.parent);
			if(u == -1) {
				System.err.println(x.filename+ ": " + x.lineNo+ ": " + "Parent class does not exist : " + x.parent);
				System.exit(1);
			}
			int v = classmap.indexOf(x.name);
			graph.get(u).add(v);			// adding an edge from parent -> child in the graph
		}
	}

	private int searchnonvisited(Boolean visited[]){
		for(int i = 0; i < classmap.size(); i++){
			if(visited[i] == false)
				return i;
		}
		return -1;
	}
	
	private Boolean all_visited(Boolean visited[]){
		for(int i = 0; i < classmap.size(); i++){
			if(visited[i] == false)
				return false;
		}
		return true;
	}

	public void checkforcycles(){
		Boolean visited[] = new Boolean[classmap.size()];
		for (int i = 0; i < classmap.size() ; i++) {
			visited[i] = false;			
		}
		Queue<Integer> queue = new LinkedList<Integer>(); 
		queue.add(0);// queue.add(1);
		while (all_visited(visited) == false) {
			if(queue.isEmpty())
				queue.add(searchnonvisited(visited));
			int u = queue.remove();
			visited[u] = true;
			for(int i = 0; i < graph.get(u).size(); i++){
				int v = graph.get(u).get(i);
				if(visited[v]){
					int l = 0;
					for(int j = 0; j < classes.size(); j++)
						if(classmap.get(v).equals(classes.get(j).name)){
							l = classes.get(j).lineNo;
							break;
						}
					System.err.println(classes.get(0).filename+ ": " +"Class is involved in inheritance cycle: " +  classmap.get(u));
					System.exit(1);
				}
				queue.add(v);
			}
		}
	}

	public void classinfo(){
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(0);//queue.add(1);
		while (!queue.isEmpty()) {
			int u = queue.remove();
			if(u != 1 && u != 0) 
				classTable.insert(classes.get(u-2));		// insert classes in BFS-order so that methods and attributes can be inherited.
			for(int i = 0; i < graph.get(u).size(); i++){
				int v = graph.get(u).get(i);
				queue.add(v);
			}
		}
	}
}