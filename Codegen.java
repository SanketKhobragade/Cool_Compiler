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
	
	ArrayList <String> attributes;
	ArrayList <String> sizelist;
	ArrayList <AST.attr> atrExp;
	ArrayList <AST.method> methodList;
	int var_counter = 0;
	int label_ct = 0;
	int ret_val = -1;
	PrintWriter out;
	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
		out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
	    out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"\ntarget triple = \"x86_64-unknown-linux-gnu\"");
		// go through all classes. For each class make object structures, then include virtual table for a class.
		//CodegenInit(out);
		this.out = out;
		attributes = new ArrayList <String>();							// Storing attribute names 
		sizelist = new ArrayList <String>();							// Storing attribute type
		atrExp = new ArrayList <AST.attr>();							// Storing attribute assignment expression
		methodList = new ArrayList <AST.method>();						// Storing methods in class

		AST.class_ theClass = program.classes.get(0);					// Program runs for only one class Main

		List<AST.feature> theFeatures = new ArrayList<AST.feature>();	// Features of class
		theFeatures = theClass.features;
		int tempct = 0;
		int main_index = -1;
		for(int i = 0; i < theFeatures.size(); ++i)
		{
			AST.feature ftr = new AST.feature();
			ftr = theFeatures.get(i);
			if(ftr.getClass() == AST.attr.class)						// Finding attribute information
			{
				AST.expression expr = new AST.expression();
				AST.attr temp = (AST.attr)ftr;
				atrExp.add(temp);
				attributes.add(temp.name);
				sizelist.add(temp.typeid);

			}
			else if(tempct==0){											// Printing class variables in ll output file
				out.print("%class.Main = type {");
				for(int j = 0; j < sizelist.size(); j++){
					if(sizelist.get(j).equals("Int"))
						out.print("i32");
					else
						out.print("i8");
					if(j!=sizelist.size()-1)
						out.print(", ");
				} 
				out.println("}\n");
				tempct++;
			}
			if(ftr.getClass() == AST.method.class)						// Storing methods in list
			{
				AST.method temp = (AST.method)ftr;
				methodList.add(temp);
				if(temp.name.equals("main"))
					main_index = methodList.size() - 1;
			}
		}

		ProcessMethod(methodList.get(main_index));						// First calling main function and then other function
		for(int i = 0; i < methodList.size(); i++){
			if(i!=main_index)
				ProcessMethod(methodList.get(i));
		}

	}

	private void ProcessMethod(AST.method temp){
		AST.expression expr = new AST.expression();
		expr = temp.body;
		ret_val = -1;
		var_counter = 0;
		label_ct = 0;
		String s = "";
		Boolean flag = false;
		if(temp.typeid.equals("Object"))								// Storing return type of function
			s = "void";
		else if(temp.typeid.equals("Int"))
			s = "i32";
		else if(temp.typeid.equals("Bool"))
			s = "i8";
		out.print("define " + s + " @" + temp.name + "(");
		if(!temp.name.equals("main")){									// Other than main function, each function has first parameter as class object
			out.print("%class.Main* %obj");
			flag = true;
		}
		for(int j = 0; j < temp.formals.size(); j++){					// Adding function parameters
			AST.formal temp2 = temp.formals.get(j);
			if(flag)
				out.print(", ");
			else
				flag = true;
			if(temp2.typeid.equals("Object"))
				s = "void";
			else if(temp2.typeid.equals("Int"))
				s = "i32";
			else if(temp2.typeid.equals("Bool"))
				s = "i8";
			out.print(s + " %" + temp2.name);						
		}
		out.print(") {");
		out.println("\nentry:");										// Entry block of functiono
		if(temp.name.equals("main")){									// In main function, class object is created. In other functions, it is passed as variable and loaded as pointer type
			out.println("\t%this1 = alloca %class.Main, align 4");		
		}
		else{
			out.println("\t%this_ = alloca %class.Main*, align 8");
			out.println("\tstore %class.Main* %obj, %class.Main** %this_, align 8");
			out.println("\t%this1 = load %class.Main*, %class.Main** %this_, align 8");
		}
		for(AST.formal e : temp.formals){								// Allocating memory for formal parameters of method
			int size = e.typeid.equals("Int") ? 32 : 8;
			out.println("\t%" + e.name + "_ = alloca i" + size + ", align " + (size/8));
			out.println("\tstore i" + size + " %" + e.name + ", i"+size+"* %" + e.name + "_, align " + (size/8));
		}
		if(temp.name.equals("main")){									// In main function, class variables are initialised
			for(AST.attr exp : atrExp){
				AST.assign as = new AST.assign(exp.name, exp.value, 0);
				as.type = exp.typeid;
				ProcessStr(as);
			}
		}
		ProcessStr(expr);												// Executing body of method
		ret_val = var_counter - 1;										// Storing return value
		if(!temp.typeid.equals("Object"))
			out.println("\tret i32 %" + ret_val);
		else
			out.println("\tret void");
		out.println("}\n");
	}

	private void ProcessStr(AST.expression expr) {
		
		if(expr.getClass() == AST.assign.class)
			ProcessStr((AST.assign)expr);
		else if(expr.getClass() == AST.int_const.class)
			ProcessStr((AST.int_const)expr);
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
	}

	private void ProcessStr(AST.plus x){
		ProcessStr(x.e1);
		int i = var_counter - 1;									// %i stores left hand expression
		ProcessStr(x.e2);
		int j = var_counter - 1;									// %j stores left hand expression
		out.println("\t"+"%" + var_counter + " = add i32 %" + i + ", %" + j);	// printing add instruction. Similar for other arithmetic operators
		var_counter++;
	}

	private void ProcessStr(AST.sub x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = sub i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.mul x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = mul nsw i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.divide x){
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = sdiv i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.object x) {
		int index = attributes.indexOf(x.name);
		if(index!=-1){										// If object is a class variable, get pointer of it and then access value
			if(x.type.equals("Int")){
				out.println("\t"+"%"+var_counter+" = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 "+index);
				var_counter++;
				out.println("\t"+"%" + var_counter + " = load i32, i32* %" + (var_counter-1) + ", align 4");
			}
			else if(x.type.equals("Bool")){
				out.println("\t"+"%"+var_counter+" = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 "+index);
				var_counter++;
				out.println("\t"+"%" + var_counter + " = load i8, i8* %" + (var_counter-1) + ", align 4");
			}
		}
		else{												// Else if it is in formal parameter, directly load from it.
			if(x.type.equals("Int"))
				out.println("\t"+"%" + var_counter + " = load i32, i32* %" + x.name + "_" + ", align 4");
			else if(x.type.equals("Bool"))
				out.println("\t"+"%" + var_counter + " = load i8, i8* %" + x.name + "_" + ", align 4");
		}
		var_counter++;
	}
	
	private void ProcessStr(AST.int_const x) {
		out.println("\t"+"%" + var_counter + " = mul nsw i32 1, " + x.value );	// multiplying 1 by value in expr to store value in register
		var_counter++;
	}

	private void ProcessStr(AST.bool_const x) {
		int temp = x.value? 1 : 0;
		out.println("\t"+"%" + var_counter + " = icmp eq i8 1, " + temp);		// Returns same value as of expr in reg
		var_counter++;
		out.println("\t"+"%" + var_counter + " = zext i1 %" + (var_counter-1) + " to i8");	// Type extension from 1 bit to 8
		var_counter++;
	}
	private void ProcessStr(AST.no_expr x, int size) {
		out.println("\t"+"%" + var_counter + " = mul nsw i"+size+" 0, 0");		// Store 0(default value) in register
		var_counter++;
	}
	
	private void ProcessStr(AST.assign x) {
		int size;
		if(x.type.equals("Int"))
			size = 32;
		else
			size = 1;
		if(x.e1.getClass() == AST.no_expr.class)								// Executing expression in =
			ProcessStr((AST.no_expr)x.e1, size);
		else
			ProcessStr(x.e1);
		int index = attributes.indexOf(x.name);
		int cur = 0;
		int i = var_counter - 2;
		if(x.type.equals("Int")){												// Similar to that of AST.object
			if(index!=-1){
				cur = var_counter;
				out.println("\t"+"%"+var_counter+" = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 "+index);
				var_counter++;
			}
			out.println("\t"+"store i32 %" + (var_counter-2) + ", i32* %" + (var_counter-1) + ", align 4");
		}
		else if(x.type.equals("Bool")){
			if(x.e1.getClass() != AST.object.class && x.e1.getClass() != AST.bool_const.class ){
				out.println("\t"+"%" + var_counter + " = zext i1 %" + (var_counter-1) + " to i8");
				var_counter++;
			}
			if(index!=-1){
				cur = var_counter;
				out.println("\t"+"%"+var_counter+" = getelementptr inbounds %class.Main, %class.Main* %this1, i32 0, i32 "+index);
				var_counter++;
			}	
			out.println("\t"+"store i8 %" + (var_counter-2) + ", i8* %" + cur + ", align 4");
		}
	}
	
	private void ProcessStr(AST.lt x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = icmp slt i32 %" + i + ", %" + j);
		var_counter++;
	}
	private void ProcessStr(AST.leq x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = icmp sle i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.eq x) {
		ProcessStr(x.e1);
		int i = var_counter - 1;
		ProcessStr(x.e2);
		int j = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = icmp eq i32 %" + i + ", %" + j);
		var_counter++;
	}

	private void ProcessStr(AST.comp x) {	
		ProcessStr(x.e1);								// Evaluating right hand side of expression
		int i = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = trunc i8 %" + i + " to i1");	// Expression is in i8. First convert it to i1. Then take xor with 1 to return negation value 
		var_counter++;
		out.println("\t"+"%" + var_counter + " = xor i1 %" + (var_counter-1) + ", 1");
		var_counter++;
	}

	private void ProcessStr(AST.neg x) {		
		ProcessStr(x.e1);
		int i = var_counter - 1;
		out.println("\t"+"%" + var_counter + " = sub i32 0, %" + i);
		var_counter++;		
	}

	private void ProcessStr(AST.block x){
		for(AST.expression e : x.l1)
			ProcessStr(e);
	}

	private void ProcessStr(AST.cond x){				// Explained in README
		int l1, l2, l3;
		l1 = ++label_ct;
		l2 = ++label_ct;
		l3 = ++label_ct;
		ProcessStr(x.predicate);
		out.println("\t"+"br i1 %" + (var_counter-1) + ", label %Label" +  l1 + ", label %Label" +  l2);
		out.println("\nLabel" + l1 + ":");
		ProcessStr(x.ifbody);
		out.println("\t"+"br label %Label" + l3);
		out.println("\nLabel" + l2 + ":");
		ProcessStr(x.elsebody);
		out.println("\t"+"br label %Label" + l3);
		out.println("\nLabel" + l3 + ":");
	}

	private void ProcessStr(AST.loop x){				// Explained in README
		int l1, l2, l3;
		l1 = ++label_ct;
		l2 = ++label_ct;
		l3 = ++label_ct;
		out.println("\t"+"br label %Label" + l1);
		out.println("\nLabel" + l1 + ":");
		ProcessStr(x.predicate);
		out.println("\t"+"br i1 %" + (var_counter-1) + ", label %Label" +  l2 + ", label %Label" +  l3);
		out.println("\nLabel" + l2 + ":");
		ProcessStr(x.body);
		out.println("\t"+"br label %Label" + l1);
		out.println("\nLabel" + l3 + ":");
	}

}