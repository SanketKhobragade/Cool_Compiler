package cool;
import java.util.*;
import java.util.Map.Entry;




public class listOfClasses {
	private ArrayList <classInfo> classes=new ArrayList<classInfo>();		
	public List<Error> errors = new ArrayList<Error>();
	
	public listOfClasses() {
		classInfo temp1 = new classInfo("Default", null);
		classInfo temp2 = new classInfo("Default", null);
		classInfo temp3 = new classInfo("Default", null);
		classInfo temp4 = new classInfo("Default", null);
		classInfo temp5 = new classInfo("Default", null);
		ArrayList <AST.method> methods = new ArrayList <AST.method>();
		ArrayList <AST.attr> attributes = new ArrayList <AST.attr>();
		ArrayList <AST.formal> formals = new ArrayList <AST.formal>();

		methods.add(new AST.method("abort", formals, "Object", new AST.no_expr(0), 0));
		methods.add(new AST.method("type_name", formals, "String", new AST.no_expr(0), 0));
		
		temp1.name = "Object";
		temp1.parent = null;
		temp1.attr = attributes;
		temp1.method = methods;
		temp1.ht = 0;
		classes.add(temp1);
		methods = new ArrayList<AST.method>();
		attributes = new ArrayList<AST.attr>();
		formals = new ArrayList<AST.formal>();

		formals.add(new AST.formal("str", "String", 0));
		methods.add(new AST.method("out_string", formals, "IO", new AST.no_expr(0), 0));
		formals = new ArrayList<AST.formal>();

		formals.add(new AST.formal("int", "Int", 0));
		methods.add(new AST.method("out_int", formals, "IO", new AST.no_expr(0), 0));
		formals = new ArrayList<AST.formal>();
		
		methods.add(new AST.method("in_string", formals, "String", new AST.no_expr(0), 0));
		methods.add(new AST.method("in_int", formals, "Int", new AST.no_expr(0), 0));

		temp2.name = "IO";
		temp2.parent = "Object";
		temp2.attr = attributes;
		methods.addAll(classes.get(0).method);
		temp2.method = methods;
		temp2.ht = 1;
		classes.add(temp2);
		methods = new ArrayList<AST.method>();
		attributes = new ArrayList<AST.attr>();	
		
		
		temp3.name = "Int";
		temp3.parent = "Object";
		temp3.attr = attributes;
		methods.addAll(classes.get(0).method);
		temp3.method = methods;
		temp3.ht = 1;
		classes.add(temp3);
		methods = new ArrayList<AST.method>();
		attributes = new ArrayList<AST.attr>();	
		
		
		
		temp4.name = "Bool";
		temp4.parent = "Object";
		temp4.attr = attributes;
		methods.addAll(classes.get(0).method);
		temp4.method = methods;
		temp4.ht = 1;
		classes.add(temp4);
		methods = new ArrayList<AST.method>();
		attributes = new ArrayList<AST.attr>();
		formals = new ArrayList<AST.formal>();
		

		methods.add(new AST.method("length", formals, "Int", new AST.no_expr(0), 0));
		formals = new ArrayList<AST.formal>();

		formals.add(new AST.formal("s", "String", 0));
		methods.add(new AST.method("concat", formals, "String", new AST.no_expr(0), 0));
		formals = new ArrayList<AST.formal>();

		formals.add(new AST.formal("i", "Int", 0));
		formals.add(new AST.formal("j", "Int", 0));
		methods.add(new AST.method("substr", formals, "String", new AST.no_expr(0), 0));
		formals = new ArrayList<AST.formal>();
		
		temp5.name = "String";
		temp5.parent = "Object";
		temp5.attr = attributes;
		methods.addAll(classes.get(0).method);
		temp5.method = methods;
		temp5.ht = 1;
		classes.add(temp5);
		methods = new ArrayList<AST.method>();
		attributes = new ArrayList<AST.attr>();
		formals = new ArrayList<AST.formal>();

		
	}

	AST.attr findAttribute(ArrayList <AST.attr> attr, String s){
		for(int i = 0; i < attr.size(); i++){
			AST.attr at = attr.get(i);
			if(at.name.equals(s))
				return at;
		}
		return null;
	}

	AST.method findMethod(ArrayList <AST.method> meth, String s){
		for(int i = 0; i < meth.size(); i++){
			AST.method me = meth.get(i);
			if(me.name.equals(s))
				return me;
		}
		return null;
	}
	void insert(AST.class_ c) {
		classInfo tc = new classInfo(c.name, c.parent);	
		classInfo parentclass = getclassInfo(c.parent);

		ArrayList <AST.attr> attrParent = parentclass.attr;
		ArrayList <AST.method> methodParent = parentclass.method; 
		ArrayList <AST.attr> attrList = new ArrayList <AST.attr>();
		ArrayList <AST.method> methodList = new ArrayList <AST.method>();

		for(int i = 0; i < c.features.size(); i++){
			AST.feature e = c.features.get(i);
			if(e.getClass() == AST.attr.class) {
				AST.attr at = findAttribute(attrList,((AST.attr) e).name);
				if(at!=null)
					errors.add(new Error(c.filename, at.lineNo, "multiple definition of attribute"));
				else
					attrList.add((AST.attr) e);
			}
			else if(e.getClass() == AST.method.class) {
				AST.method me = findMethod(methodList,((AST.method) e).name);
				if(me!=null)	
					errors.add(new Error(c.filename, me.lineNo, "multiple definition of method"));
				else
					methodList.add((AST.method) e);
			}
		}
		for(int i = 0; i < c.features.size(); i++){
			AST.feature e = c.features.get(i);
			if(e.getClass() == AST.attr.class) {
				AST.attr at = (AST.attr) e;
				if(findAttribute(attrParent,at.name) != null)
					errors.add(new Error(c.filename, at.lineNo, " attribute is of inherited class."));
				else
					attrList.add(at);
			}
		}

		for(int i = 0; i < c.features.size(); i++){
			AST.feature e = c.features.get(i);
			Boolean flag = false;
			if(e.getClass() == AST.method.class) {
				AST.method me1 = (AST.method) e;
				AST.method me2 = findMethod(methodParent,me1.name);
				if(me2 != null){
					flag = check(c,me1,me2);
				}
				else if(flag)
					methodList.add(me2);
				flag = false;

			}
		}

		tc.ht = parentclass.ht + 1;
		methodList.addAll(methodParent);
		tc.attr = attrList;
		tc.method = methodList;
		classes.add(tc);
	}

	

	Boolean check(AST.class_ c, AST.method me1, AST.method me2){
		if(me1.formals.size() != me2.formals.size()) {
			errors.add(new Error(c.filename, me1.lineNo, "Wrong number of arguments in redefinition" + me1.name));
			return false;
		}
		else if(me1.typeid.equals(me2.typeid) == false) {
			errors.add(new Error(c.filename, me1.lineNo, "different return type in redefinition of method" ));
			return false;
		}
		for(int i = 0; i < me1.formals.size(); i++) {
			if(me1.formals.get(i).typeid.equals(me2.formals.get(i).typeid) == false) {
				errors.add(new Error(c.filename, me1.lineNo, "different  type in redefinition of method "));
				return false;
			}
		}
		return true;
	}

	List<Error> getErrors() {
		return errors;
	}
	
	ArrayList<AST.attr> getAttrs(String className) {
		return getclassInfo(className).attr;
	}
	
	classInfo getclassInfo(String className) {
		for(int i = 0; i < classes.size(); i++){
			classInfo t = classes.get(i);
			if(t.name.equals(className))
				return t;
		}
		return null;
	}

	boolean conforms(String a, String b) {
		if(a.equals(b))
			return true;
		else {
			a = getclassInfo(a).parent;
			if(a == null) return false;
			else return conforms(a, b);
		}
	}
	
	String least_common(String a, String b) {
		if(a.equals(b)) return a;
		else if(getclassInfo(a).ht < getclassInfo(b).ht)		
			return least_common(b, a);
		else
			return least_common(getclassInfo(a).parent, b);
	}
}
