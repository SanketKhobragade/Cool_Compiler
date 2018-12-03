package cool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import cool.AST.class_;


public class Semantic{
	private boolean errorFlag = false;
	public void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}

/*
	Don't change code above this line
*/
	ScopeTable<AST.attr> scopeTable = new ScopeTable<AST.attr>();
	inherit inheritanceclass = new inherit();
	listOfClasses classTable = new listOfClasses();
	String filename;
	
	public Semantic(AST.program program){
		//Write Semantic analyzer code here
		
		
		AnalyseGraph(program.classes);
		List<Error> errors = classTable.getErrors();	
		for(Error e : errors) {
			reportError(e.filename, e.lineNo, e.err);
		}
		
		for(AST.class_ e : program.classes) {
			filename = e.filename;				
			scopeTable.enterScope();			
			scopeTable.insert("self", new AST.attr("self", e.name, new AST.no_expr(e.lineNo), e.lineNo));		
			ArrayList<AST.attr> atrli = classTable.getAttrs(e.name);	
			AST.attr atr;
			for(int i = 0; i < atrli.size(); i++){
				atr = atrli.get(i);
				scopeTable.insert(atr.name, new AST.attr(atr.name, atr.typeid, new AST.no_expr(atr.lineNo), atr.lineNo));
			}

			Analyse(e);
			scopeTable.exitScope();				
		}

		classInfo main_class = classTable.getclassInfo("Main");
		if(main_class == null)
			reportError(filename, 1, "No class 'Main'");
		else if(hasdispatch(main_class.method, "main") == false)
			reportError(filename, 1, "'Main' class does not have 'main' method");

	}

	
	private void AnalyseGraph(List <AST.class_> classes) {

		inheritanceclass.initialise(classes);
		
		inheritanceclass.makegraph();

		inheritanceclass.checkforcycles();

		inheritanceclass.classinfo();
		
		classTable = inheritanceclass.classTable;
	}
	
	private void Analyse(AST.class_ cl) {
		for(int i=0;i<cl.features.size();i++) {
			AST.feature f = cl.features.get(i);
			if(f.getClass() == AST.method.class) 
				AnalyseFunc((AST.method)f);
			else
				AnalyseAttr((AST.attr)f);
		}
	}
	
	private void AnalyseFunc(AST.method method) {
		AST.attr sel = scopeTable.lookUpLocal("self");
		scopeTable.enterScope();
		for(int i=0;i<method.formals.size();i++) {
			AST.formal e = method.formals.get(i);
			AST.attr f = scopeTable.lookUpLocal(e.name);
			if(f != null) 
				reportError(filename, f.lineNo, "multiple definition of attributes");
			scopeTable.insert(e.name, new AST.attr(e.name, e.typeid, new AST.no_expr(e.lineNo), e.lineNo));
		}
		Analyse(method.body);
		if(classTable.conforms(method.body.type, method.typeid) == false) {
			reportError(filename, method.body.lineNo, "declared returned type is different from inferred return type ");
		}
		scopeTable.exitScope();
	}
	
	private void AnalyseAttr(AST.attr attr) {
		AST.attr sel = scopeTable.lookUpLocal("self");
			
		if(attr.value.getClass() != AST.no_expr.class) {
			Analyse(attr.value);
			if(classTable.conforms(attr.value.type, attr.typeid) == false) {
				reportError(filename, attr.value.lineNo, "declared returned type is different from inferred return type ");
			}
		}
	}
	
	private void Analyse(AST.expression expr) {
		if(expr.getClass() == AST.assign.class)
			Analyse((AST.assign)expr);
		else if(expr.getClass() == AST.int_const.class)
			Analyse((AST.int_const)expr);
		else if(expr.getClass() == AST.string_const.class)
			Analyse((AST.string_const)expr);
		else if(expr.getClass() == AST.bool_const.class)
			Analyse((AST.bool_const)expr);
		else if(expr.getClass() == AST.cond.class)
			Analyse((AST.cond)expr);
		else if(expr.getClass() == AST.loop.class)
			Analyse((AST.loop)expr);
		else if(expr.getClass() == AST.block.class)
			Analyse((AST.block)expr);
		else if(expr.getClass() == AST.let.class)
			Analyse((AST.let)expr);
		else if(expr.getClass() == AST.typcase.class)
			Analyse((AST.typcase)expr);
		else if(expr.getClass() == AST.new_.class)
			Analyse((AST.new_)expr);
		else if(expr.getClass() == AST.isvoid.class)
			Analyse((AST.isvoid)expr);
		else if(expr.getClass() == AST.object.class)
			Analyse((AST.object)expr);
		else if(expr.getClass() == AST.static_dispatch.class)
			Analyse((AST.static_dispatch)expr);
		else if(expr.getClass() == AST.dispatch.class)
			Analyse((AST.dispatch)expr);
		else if(expr.getClass() == AST.plus.class)
			Analyse((AST.plus)expr);
		else if(expr.getClass() == AST.sub.class)
			Analyse((AST.sub)expr);
		else if(expr.getClass() == AST.mul.class)
			Analyse((AST.mul)expr);
		else if(expr.getClass() == AST.divide.class)
			Analyse((AST.divide)expr);
		else if(expr.getClass() == AST.comp.class)
			Analyse((AST.comp)expr);
		else if(expr.getClass() == AST.lt.class)
			Analyse((AST.lt)expr);
		else if(expr.getClass() == AST.leq.class)
			Analyse((AST.leq)expr);
		else if(expr.getClass() == AST.eq.class)
			Analyse((AST.eq)expr);
		else if(expr.getClass() == AST.neg.class)
			Analyse((AST.neg)expr);
				
	}
	
	private void Analyse(AST.assign assign) {
		Analyse(assign.e1);
		AST.attr a = scopeTable.lookUpGlobal(assign.name);
		if(a == null)
			reportError(filename, assign.lineNo, "variable not declared ");
		else if(classTable.conforms(assign.e1.type, a.typeid) == false)
			reportError(filename, assign.lineNo, "declared returned type is different from inferred return type" );
		assign.type = assign.e1.type;
	}
	
	AST.method dispatch(ArrayList <AST.method> li, String name){
		AST.method temp;
		for(int i = 0; i < li.size(); i++){
			temp = li.get(i);
			if(temp.name.equals(name))
				return temp;
		}
		return null;
	}

	Boolean hasdispatch(ArrayList <AST.method> li, String name){
		AST.method temp;
		for(int i = 0; i < li.size(); i++){
			temp = li.get(i);
			if(temp.name.equals(name))
				return true;
		}
		return false;
	}
	
	private void Analyse(AST.static_dispatch x) {
		Analyse(x.caller);				
		
		for(int i=0;i< x.actuals.size();i++){
			AST.expression e = x.actuals.get(i);
			Analyse(e);
		}
		classInfo c = classTable.getclassInfo(x.typeid);
		AST.method m = null;
		boolean found = false;
		if(c == null)
			reportError(filename, x.lineNo, "static dispatch to undefined class " + x.typeid);
		else if(classTable.conforms(x.caller.type, c.name) == false)
			reportError(filename, x.lineNo, "expresion type not confrom");
		else if(hasdispatch(c.method, x.name)){
			found = true;
			m = dispatch(c.method, x.name);
			if(x.actuals.size() != m.formals.size())
				reportError(filename, x.lineNo, "wrong number of arguments");
			else {
				for(int i = 0; i < x.actuals.size(); ++i) {
					String actual_type = x.actuals.get(i).type;
					String formal_type = m.formals.get(i).typeid;
					if(classTable.conforms(actual_type, formal_type) == false)
						reportError(filename, x.lineNo, "arguments type not matching");			
				}
			}
		}
		else
			reportError(filename, x.lineNo, "static dispatch to undefined method " + x.name);
		
		x.type = (found)? (m.typeid) : ("Object");
	}
	
	
	private void Analyse(AST.dispatch x) {
		Analyse(x.caller);
		
		
		for(int i=0;i<x.actuals.size();i++){
			AST.expression e = x.actuals.get(i);
			Analyse(e);
		}
		
		classInfo c = classTable.getclassInfo(x.caller.type);
		AST.method m = null;
		boolean found = false;
		
		if(c == null) {
			reportError(filename, x.lineNo, "Class not defined");
		}
		else if(hasdispatch(c.method, x.name)) {
			found = true;
			m = dispatch(c.method, x.name);
			if(x.actuals.size() != m.formals.size())
				reportError(filename, x.lineNo, "wrong number of arguments");
			else {
				for(int j = 0; j < x.actuals.size(); ++j) {
					String actual_type = x.actuals.get(j).type;
					String formal_type = m.formals.get(j).typeid;
					if(classTable.conforms(actual_type, formal_type) == false)
						reportError(filename, x.lineNo, "declared returned type is different from inferred return type");			
				}
			}	
		}
		else
			reportError(filename, x.lineNo, "Dispatch to undefined method " + x.name);
		
		x.type = (found) ? (m.typeid):("Object");	
	}


	private void Analyse(AST.cond x){
		Analyse(x.predicate);
		if(x.predicate.type.equals("Bool") == false) 
			reportError(filename, x.predicate.lineNo, "condition of 'if' does not return Bool");
		Analyse(x.ifbody);
		Analyse(x.elsebody);
		x.type = classTable.least_common(x.ifbody.type, x.elsebody.type);
	}
	private void Analyse(AST.loop x) {
		Analyse(x.predicate);
		if(x.predicate.type.equals("Bool") == false) 
			reportError(filename, x.predicate.lineNo, "condition of Loop does not return Bool");
		Analyse(x.body);
		x.type = "Object";
	}
	private void Analyse(AST.block x) {
		int i=0;
		for(i=0;i< x.l1.size();i++){
			AST.expression expr = x.l1.get(i);
			Analyse(expr);
		}
		x.type = x.l1.get(x.l1.size() - 1).type;
	}
	private void Analyse(AST.let x){
		scopeTable.enterScope();
		if(x.value.getClass() != AST.no_expr.class) {
			Analyse(x.value);
			if(classTable.conforms(x.value.type, x.typeid) == false)
				reportError(filename, x.lineNo, "declared returned type is different from inferred return type" );
		}
		AST.attr temp = new AST.attr(x.name, x.typeid, x.value, x.lineNo);
		scopeTable.insert(x.name, temp);
		Analyse(x.body);
		x.type = x.body.type;
		scopeTable.exitScope();
	}
	private void Analyse(AST.typcase x) {
		Analyse(x.predicate);
		for(int i=0;i< x.branches.size();i++) {
			AST.branch e = x.branches.get(i);
			scopeTable.enterScope();
			classInfo c = classTable.getclassInfo(e.type);
			if(c == null) {
				reportError(filename, e.lineNo, "no definition of Class type");
				scopeTable.insert(e.name, new AST.attr(e.name, "Object", e.value, e.lineNo));	
			}
			else 
				scopeTable.insert(e.name, new AST.attr(e.name, e.type, e.value, e.lineNo));
			Analyse(e.value);
			scopeTable.exitScope();
		}
		HashMap <String, Boolean> br_types = new HashMap<String, Boolean> ();
		AST.branch b = x.branches.get(0);
		String typ = b.value.type;
		
		for(int i=0;i<x.branches.size();i++) {
			AST.branch br = x.branches.get(i);
			if(br_types.containsKey(br.type) == false)
				br_types.put(br.type, true);
			else
				reportError(filename, br.lineNo, "duplicate branching ");
			typ = classTable.least_common(typ, br.value.type);
		}
		x.type = typ;
	}

	private void Analyse(AST.new_ x) {
		classInfo c = classTable.getclassInfo(x.typeid);
		if(c == null)
			reportError(filename, x.lineNo, "not defined class for 'new' ");
		else
			x.type = x.typeid;
	}

	private void Analyse(AST.isvoid x) {
		x.type = "Bool";
	}

	private void arithmetic(AST.expression ex1, AST.expression ex2, int lineNo){
		Analyse(ex1);
		Analyse(ex2);
		if(ex1.type .equals("Int") == false || ex2.type.equals("Int") == false) {
			reportError(filename, lineNo, "arguments are not of type Int");
		}

	}
	private void Analyse(AST.plus x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Int";
	}
	
	private void Analyse(AST.sub x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Int";
	}
	
	private void Analyse(AST.mul x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Int";
	}
	
	private void Analyse(AST.divide x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Int";
	}
	private void Analyse(AST.lt x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Bool";
	}
	private void Analyse(AST.leq x) {
		arithmetic(x.e1, x.e2, x.lineNo);
		x.type = "Bool";
	}

	private void Analyse(AST.comp x) {	
		Analyse(x.e1);
		if(x.e1.type.equals("Bool") == false)
			reportError(filename, x.lineNo, "argument is not of type Bool");
		x.type = "Bool";
	}
	
	private boolean check_basicTypes(String s){
		if(s.equals("String") || s.equals("Int") || s.equals("Bool"))
			return true;
		return false;
	}
	
	private void Analyse(AST.eq x) {
		Analyse(x.e1);
		Analyse(x.e2);
		if((check_basicTypes(x.e1.type) || check_basicTypes(x.e2.type)) && (!(x.e1.type.equals(x.e2.type)))) {
			reportError(filename, x.lineNo, "comparing element have different types");
		}
		x.type = "Bool";
	}
	private void Analyse(AST.neg x) {		
		Analyse(x.e1);
		if(x.e1.type.equals("Int") == false)
			reportError(filename, x.lineNo, "different argument type");
		x.type = "Int";
	}
	private void Analyse(AST.object x) {

		AST.attr attribute = scopeTable.lookUpGlobal(x.name);
		if(attribute == null) {
			reportError(filename, x.lineNo, "not declared " + x.name);
			x.type = "Object";
		}
		else
			x.type = attribute.typeid;
	}
	
	private void Analyse(AST.string_const x) {
		x.type = "String";
	}
	
	private void Analyse(AST.int_const x) {
		x.type = "Int";
	}
	private void Analyse(AST.bool_const x) {
		x.type = "Bool";
	}
}
