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

int var_counter = 0;

public class Codegen{
	
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

	private int Analyse(AST.plus x){
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = add i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.sub x){
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = sub i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.mul x){
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = mul nsw i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.divide x){
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = sdiv i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.object x) {
		System.out.println(var_counter + " = load i32, i32* " + x.name + ", align 4");
		return var_counter++;
	}
	
	private int Analyse(AST.string_const x) {
		System.out.println(var_counter + " = i32 " + x.value );
		return var_counter++;
	}
	
	private int Analyse(AST.int_const x) {
		System.out.println("%" + var_counter + " = mul nsw i32 1, " + x.value );
		return var_counter++;
	}

	private int Analyse(AST.bool_const x) {
		System.out.println("%" + var_counter + " = icmp eq i8 1, " + x.value);
		var_counter++;
		System.out.println("%" + var_counter + " = zext i1 %" + var_counter-1 + " to i8");
		return var_counter++;
	}
	
	private int Analyse(AST.assign x) {
		int i = Analyse(assign.e1);
		System.out.println("store i32 " + i + ", i32* " + x.name + ", align 4");
		return var_counter;
	}
	
	private int Analyse(AST.lt x) {
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = icmp slt i32 %" + i + ", %" + j);
		return var_counter++;
	}
	private int Analyse(AST.leq x) {
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = icmp sle i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.eq x) {
		int i = Analyse(x.e1);
		int j = Analyse(x.e2);
		System.out.println("%" + var_counter + " = icmp eq i32 %" + i + ", %" + j);
		return var_counter++;
	}

	private int Analyse(AST.comp x) {	
		int i = Analyse(x.e1);
		System.out.println("%" + var_counter + " = trunc i8 %" + i + " to i1");
		var_counter++;
		System.out.println("%" + var_counter + " = xor i1 %" + (var_counter-1) + ", true");
		var_counter++;
		System.out.println("%" + var_counter + " = zext i1 %" + (var_counter-1) + " to i8");
		return var_counter++;
	}

	private int Analyse(AST.neg x) {		
		int i = Analyse(x.e1);
		System.out.println("%" + var_counter + " = sub i32 0, %" + i);
		return var_counter++;		
	}

	/*private void Analyse(AST.cond x){
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
	

	private void Analyse(AST.isvoid x) {
		x.type = "Bool";
	}*/
	
}
