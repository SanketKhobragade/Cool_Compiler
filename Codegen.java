package cool;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;




public class Codegen{
	
	IRClassTable IRclassTable;
	ArrayList < ArrayList <Integer> > classGraph;
	HashMap <Integer, String> idxName;
	HashMap <String, AST.class_> idxCont;
	ArrayList <String> attributes;
	int var_counter = 0;
	int label_ct = 0;
	int ret_val = -1;
	int strCount = 0;
	PrintWriter out;
	//out.println(";sgdfgsdf");
	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
		out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
	       
		// go through all classes. For each class make object structures, then include virtual table for a class.
		CodegenInit(out);
		this.out = out;
		
		IRclassTable = new IRClassTable();
		attributes = new ArrayList <String>();
		ProcessGraph(program.classes, out);

		AST.class_ theClass = idxCont.get("Main");

		List<AST.feature> theFeatures = new ArrayList<AST.feature>();
		theFeatures = theClass.features;
		
		for(int i = 0; i < theFeatures.size(); ++i)
		{
			AST.feature ftr = new AST.feature();
			ftr = theFeatures.get(i);
			if(ftr.getClass() == AST.attr.class)
			{
				AST.expression expr = new AST.expression();
				AST.attr temp = (AST.attr)ftr;
				expr = temp.value;

				ProcessStr(expr);
				attributes.add(temp.name);

			}
			else if(ftr.getClass() == AST.method.class)
			{
				AST.expression expr = new AST.expression();
				AST.method temp = (AST.method)ftr;
				expr = temp.body;
				ret_val = -1;
				var_counter = 0;
				label_ct = 0;
				out.println("\nentry:");
				ProcessStr(expr);
				ret_val = var_counter - 1;
				if(!temp.typeid.equals("Object"))
					out.println("ret %" + ret_val);
				else
					out.println("ret void");
				out.println("\n");



			}

		}

		for(AST.class_ e : program.classes) 
		{
			if(!(e.name.equals("Main")))
			{
				//List<AST.feature> theFeatures = new ArrayList<AST.feature>();
				theFeatures = theClass.features;

				for(int i = 0; i < theFeatures.size(); ++i)
				{
					AST.feature ftr = new AST.feature();
					ftr = theFeatures.get(i);
					if(ftr.getClass() == AST.attr.class)
					{
						AST.expression expr = new AST.expression();
						AST.attr temp = (AST.attr)ftr;
						expr = temp.value;
						ProcessStr(expr);

					}
					else if(ftr.getClass() == AST.method.class)
					{
						AST.expression expr = new AST.expression();
						AST.method temp = (AST.method)ftr;
						expr = temp.body;
						ProcessStr(expr);

					}
	        	}
			}
		}

	}


	private void printStringConst(AST.expression expr_str, PrintWriter out)
	{
		AST.string_const str_obj = (AST.string_const)expr_str;	
  		if(strCount==0){
  			out.println("@.str = private unnamed_addr constant ["+ (str_obj.value.length()+1) + " x i8] c\""+str_obj.value+"\\00\", align 1");
  		}
  		else{
  			out.println("@.str."+ strCount+ " = private unnamed_addr constant ["+ (str_obj.value.length()+1) + " x i8] c\""+str_obj.value+"\\00\", align 1");
  		}
  		strCount++;
	}


	private void ProcessStr(AST.expression expr) {
		
		if(expr.getClass() == AST.assign.class)
			ProcessStr((AST.assign)expr);
		else if(expr.getClass() == AST.int_const.class)
			ProcessStr((AST.int_const)expr);
		else if(expr.getClass() == AST.string_const.class)
			ProcessStr((AST.string_const)expr);
		else if(expr.getClass() == AST.bool_const.class)
			ProcessStr((AST.bool_const)expr);
		
		else if(expr.getClass() == AST.object.class)
			ProcessStr((AST.object)expr);
		else if(expr.getClass() == AST.block.class)
			ProcessStr((AST.block)expr);
		else if(expr.getClass() == AST.cond.class)
			ProcessStr((AST.cond)expr);
		else if(expr.getClass() == AST.loop.class)
			ProcessStr((AST.loop)expr);
		
		else if(expr.getClass() == AST.plus.class)
			ProcessStr((AST.plus)expr);
		else if(expr.getClass() == AST.sub.class)
			ProcessStr((AST.sub)expr);
		else if(expr.getClass() == AST.mul.class)
			ProcessStr((AST.mul)expr);
		else if(expr.getClass() == AST.divide.class)
			ProcessStr((AST.divide)expr);
		else if(expr.getClass() == AST.comp.class)
			ProcessStr((AST.comp)expr);
		else if(expr.getClass() == AST.lt.class)
			ProcessStr((AST.lt)expr);
		else if(expr.getClass() == AST.leq.class)
			ProcessStr((AST.leq)expr);
		else if(expr.getClass() == AST.eq.class)
			ProcessStr((AST.eq)expr);
		else if(expr.getClass() == AST.neg.class)
			ProcessStr((AST.neg)expr);
		else
			out.println(expr.getClass());
			
				
	}

	private void ProcessStr(AST.plus x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = add i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.sub x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = sub i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.mul x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = mul nsw i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.divide x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = sdiv i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.object x) {
		int index = attributes.indexOf(x.name);
		if(index!=-1){
			out.println("%"+var_counter+" = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 "+index);
			var_counter++;
		}
		if(x.type.equals("Int"))
			out.println("%" + var_counter + " = load i32, i32* %" + (var_counter-1) + ", align 4");
		else if(x.type.equals("Bool"))
			out.println("%" + var_counter + " = load i8, i8* %" + (var_counter-1) + ", align 4");
		var_counter++;
	}
	
	private void ProcessStr(AST.string_const x) {
		out.println(var_counter + " = i32 " + x.value );
		var_counter++;
	}
	
	private void ProcessStr(AST.int_const x) {
		out.println("%" + var_counter + " = mul nsw i32 1, " + x.value );
		var_counter++;
	}

	private void ProcessStr(AST.bool_const x) {
		int temp = x.value? 1 : 0;
		out.println("%" + var_counter + " = icmp eq i8 1, " + temp);
		var_counter++;
		out.println("%" + var_counter + " = zext i1 %" + (var_counter-1) + " to i8");
		var_counter++;
	}
	
	private void ProcessStr(AST.assign x) {
		ProcessStr(x.e1);
		int index = attributes.indexOf(x.name);
		int cur = 0;
		int i = var_counter - 2;
		if(x.type.equals("Int")){
			if(index!=-1){
				cur = var_counter;
				out.println("%"+var_counter+" = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 "+index);
				var_counter++;
			}
			out.println("store i32 %" + (var_counter-2) + ", i32* %" + (var_counter-1) + ", align 4");
		}
		else if(x.type.equals("Bool")){
			if(x.e1.getClass() != AST.object.class && x.e1.getClass() != AST.bool_const.class ){
				out.println("%" + var_counter + " = zext i1 %" + (var_counter-1) + " to i8");
				var_counter++;
			}
			if(index!=-1){
				cur = var_counter;
				out.println("%"+var_counter+" = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 "+index);
				var_counter++;
			}	
			out.println("store i8 %" + (var_counter-2) + ", i8* %" + cur + ", align 4");
		}
	}
	
	private void ProcessStr(AST.lt x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = icmp slt i32 %" + i + ", %" + j);
		var_counter++;
	}
	private void ProcessStr(AST.leq x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = icmp sle i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.eq x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("%" + var_counter + " = icmp eq i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.comp x) {	
		ProcessStr(x.e1);
		int i = var_counter - 1;
		out.println("%" + var_counter + " = trunc i8 %" + i + " to i1");
		var_counter++;
		out.println("%" + var_counter + " = xor i1 %" + (var_counter-1) + ", 1");
		var_counter++;
	}

	private void ProcessStr(AST.neg x) {		
		ProcessStr(x.e1);
		int i = var_counter - 1;
		out.println("%" + var_counter + " = sub i32 0, %" + i);
		var_counter++;		
	}

	private void ProcessStr(AST.block x){
		for(AST.expression e : x.l1)
			ProcessStr(e);
	}

	private void ProcessStr(AST.cond x){
		int l1, l2, l3;
		l1 = ++label_ct;
		l2 = ++label_ct;
		l3 = ++label_ct;
		//out.println("COND BEGIN LABEL: " + label_ct);
		ProcessStr(x.predicate);
		out.println("br i1 %" + (var_counter-1) + ", label %Label" +  l1 + ", label %Label" +  l2);
		out.println("\nLabel" + l1 + ":");
		ProcessStr(x.ifbody);
		out.println("br label %Label" + l3);
		out.println("\nLabel" + l2 + ":");
		ProcessStr(x.elsebody);
		out.println("br label %Label" + l3);
		out.println("\nLabel" + l3 + ":");
	}

	private void ProcessStr(AST.loop x){
		int l1, l2, l3;
		l1 = ++label_ct;
		l2 = ++label_ct;
		l3 = ++label_ct;
		//out.println("COND BEGIN LABEL: " + label_ct);
		out.println("br label %Label" + l1);
		out.println("\nLabel" + l1 + ":");
		ProcessStr(x.predicate);
		out.println("br i1 %" + (var_counter-1) + ", label %Label" +  l2 + ", label %Label" +  l3);
		out.println("\nLabel" + l2 + ":");
		ProcessStr(x.body);
		out.println("br label %Label" + l1);
		out.println("\nLabel" + l3 + ":");
	}


	
	private void CodegenInit(PrintWriter out) {
		out.println(Constants.DATA_LAYOUT);
		out.println(Constants.TARGET_TRIPLE);
		out.println(Constants.ERRORS);
		out.println(Constants.CMETHODS);
		out.println(Constants.CMETHOD_HELPERS);
	}
	
	
	// given a method, get its type signature
	private String getIRType(String typeid) {
		if(Constants.FC_TYPES.containsKey(typeid)) {
			return Constants.FC_TYPES.get(typeid);
		}
		else return "%class." + typeid + "*";
	}
	
	private void EmitClassDecl(IRClassPlus irclass, PrintWriter out) {
		out.print("%class." + irclass.name + ".Base = type {");
		for(int i = 0; i < irclass.attrList.size(); ++i) {
			AST.attr at = irclass.attrList.get(i);
			if(i != 0)
				out.print(", ");
			out.print(getIRType(at.typeid));
		}
		out.print(" }\n");
		out.println("%class." + irclass.name + " = type { i32, i8*, %class." + irclass.name + ".Base }\n");
	}
	
	private void EmitClassVT(IRClassPlus irclass, PrintWriter out) {
		out.print("@_ZTV" + irclass.name.length() + irclass.name + " = constant " +
				"[" + irclass.methodList.size() + " x i8*] [");
		for(int i = 0; i < irclass.methodList.size(); ++i) {
			if(i != 0)
				out.print(", ");
			AST.method me = irclass.methodList.get(i);
			out.print("i8* bitcast (");
			out.print(getIRType(me.typeid));
			out.print(" ( ");
			for(int j = 0; j < me.formals.size(); ++j) {
				AST.formal fr = me.formals.get(j);
				if(j != 0)
					out.print(", ");
				out.print(getIRType(fr.typeid));
			}
			out.print(" )* ");
			
			// now name of method
			out.print(irclass.IRname.get(me.name));
			out.print(" to i8*)");
		}
		out.print("] \n");
	}
	

	/* A constructor for all base types is required */
	
	private void EmitMethods(IRClassPlus irclass, PrintWriter out) {
		if(irclass.name.equals("Int") || irclass.name.equals("Bool")) return;
		else if(irclass.name.equals("String")) {
			out.println(Constants.STRING_CONCAT);
			out.println(Constants.STRING_COPY);
			out.println(Constants.STRING_LENGTH);
			out.println(Constants.STRING_SUBSTR);
		} else if(irclass.name.equals("Object")) {
			out.println(Constants.OBJECT_ABORT);
			// constructor for base type
			out.println(Constants.OBJECT_BASE);
			out.println(Constants.OBJECT_TYPENAME);
		} else if(irclass.name.equals("IO")) {
			EmitBaseConstructor(irclass, out);
		} else {
			EmitBaseConstructor(irclass, out);
		}
	}
	
	private String baseClassName(String className) {
		return "class." + className + ".Base";
	}
	
	private void EmitBaseConstructor(IRClassPlus irclass, PrintWriter out) {
		out.print("define void @_Z" + irclass.name.length() + irclass.name + "Base" + "C");
		out.print(" ( %" + irclass.name + ".Base*" + "%this" + " ) { \n");
		out.println("entry: ");
		
		
		int ptr = 0;
		for(int i = 0; i < irclass.attrList.size(); ++i) {
			AST.attr at = irclass.attrList.get(i);
			out.println("%" + ptr + " = getelementptr inbouds %" + baseClassName(at.typeid) + ", " + baseClassName(at.typeid) + "* %this, i32 0, i32 " + i);
			out.println("call void " + "@_Z" + at.typeid.length() + at.typeid + "Base"+ "C" + "( %" + baseClassName(at.typeid) + "*" + "%" + i);
			out.println("return void");
		}
	}
	
	private void ProcessGraph(List <AST.class_> classes, PrintWriter out) {
			
	
		Integer sz = 0;		// stores the number of classes
		idxCont = new HashMap <String, AST.class_> ();
		HashMap <String, Integer> classIdx = new HashMap <String, Integer> ();
		idxName = new HashMap <Integer, String>();
		classGraph = new ArrayList < ArrayList <Integer> >();

		/* Laying the groundwork */
		classIdx.put("Object", 0);
		idxName.put(0, "Object");
		classIdx.put("IO", 1);
		idxName.put(0, "IO");
		
		classGraph.add(new ArrayList <Integer> (Arrays.asList(1)));
		classGraph.add(new ArrayList <Integer>());	// for IO
		idxName.put(0, "Object");
		idxName.put(1, "IO");
		
		sz = sz + 2;	// IO and Object (2 classes) already present
		
		/* Checking for :
		 * - bad redefinitions
		 * - bad inheritance
		 * Also : assigning an integer corresponding to each class.
		 */
		for(AST.class_ e : classes) {
			idxName.put(sz, e.name);			// Reverse lookup. Integer -> className
			classIdx.put(e.name, sz++);			// className -> Integer
			idxCont.put(e.name, e);				// getting the class from name. Used later.
			classGraph.add(new ArrayList <Integer> ());
		}


		
		/* We are creating an undirected graph in this method.
		 * Also: Checking for - undefined parent class error
		 */
		for(AST.class_ e : classes) {
			int u = classIdx.get(e.parent);
			int v = classIdx.get(e.name);
			classGraph.get(u).add(v);			// adding an edge from parent -> child in the graph
		}
		
		
		/* Class Declarations here */
		Queue<Integer> q = new LinkedList<Integer>();				
		q.clear(); q.offer(0);
		while (!q.isEmpty()) {
			int u = q.poll();
			if(u != 1 && u != 0) {
				IRclassTable.insert(idxCont.get(idxName.get(u)));		// insert classes in BFS-order so that methods and attributes can be inherited.
			}
			EmitClassDecl(IRclassTable.getIRClassPlus(idxName.get(u)), out);
			for(Integer v : classGraph.get(u)) {
				q.offer(v);
			}
		
		}
		/* prints virtual table */
		q.clear(); q.offer(0);
		while (!q.isEmpty()) {
			int u = q.poll();
			EmitClassVT(IRclassTable.getIRClassPlus(idxName.get(u)), out);
			for(Integer v : classGraph.get(u)) {
				q.offer(v);
			}
		}
		
		/* prints definitions */
		q.clear(); q.offer(0);
		while (!q.isEmpty()) {
			int u = q.poll();
			EmitMethods(IRclassTable.getIRClassPlus(idxName.get(u)), out);
			for(Integer v : classGraph.get(u)) {
				q.offer(v);
			}
		
		}
		
	}

}