package cool;
import java.util.HashMap;
import java.util.*;
import java.util.Map.Entry;

public class classInfo {
	public String name;
	public String parent = null;
	public int ht;
	public ArrayList<AST.attr> attr;
	public ArrayList<AST.method> method;
	
	classInfo(String nm, String pr) {
		name = new String(nm);
		if(pr != null) parent = new String(pr);
	}
}