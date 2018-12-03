parser grammar CoolParser;

options {
	tokenVocab = CoolLexer;
}

@header{
	import cool.AST;
	import java.util.List;
}

@members{
	String filename;
	public void setFilename(String f){
		filename = f;
	}

/*
	DO NOT EDIT THE FILE ABOVE THIS LINE
	Add member functions, variables below.
*/

}

/*
	Add Grammar rules and appropriate actions for building AST below.
*/

program returns [AST.program value] : 
	cl=class_list EOF 
	{
		$value = new AST.program($cl.value, $cl.value.get(0).lineNo);
	}
	;


class_list returns [ArrayList<AST.class_> value]
	@init
	{
		$value = new ArrayList<AST.class_>();
	}
	:
		(c = class_ SEMICOLON {$value.add($c.value);})+
	;

class_ returns  [AST.class_ value]  :
	
	c=CLASS type=TYPEID LBRACE fl=classMEMBER RBRACE
	{
		$value = new AST.class_($type.getText(), filename, "Object", $classMEMBER.value, $c.getLine());
	}
	
	| c=CLASS type=TYPEID INHERITS par=TYPEID LBRACE fl=classMEMBER RBRACE
	{
		$value = new AST.class_($type.getText(), filename, $par.getText(), $classMEMBER.value, $c.getLine());
	}
	;


classMEMBER returns [ArrayList<AST.feature> value]
	@init
	{
		$value = new ArrayList<AST.feature>();
	}
	:
		(c = member SEMICOLON {$value.add($c.value);})*
   ;

member returns [AST.feature value] :
	
	f = method
	{
		$value = $f.value;
	}
	| v = attr
	{
		$value = $v.value;
	}
	;

attr returns [AST.attr value]	:
	id=OBJECTID COLON type=TYPEID
	{
		$value = new AST.attr($id.getText(), $type.getText(), new AST.no_expr($id.getLine()), $id.getLine());
	}
	
	| id=OBJECTID COLON type=TYPEID ASSIGN expr=expression
	{
		$value = new AST.attr($id.getText(), $type.getText(), $expr.value, $id.getLine());
	}
	;

method returns [AST.method value] :
	id=OBJECTID LPAREN RPAREN COLON type=TYPEID LBRACE expr=expression RBRACE
	{
		$value = new AST.method($id.getText(), new ArrayList<AST.formal>(), $type.getText(), $expr.value, $id.getLine());
	}
	| id=OBJECTID LPAREN argl=arg_list RPAREN COLON type=TYPEID LBRACE expr=expression RBRACE
	{
		$value = new AST.method($id.getText(), $argl.value, $type.getText(), $expr.value, $id.getLine());
	}
	;


arg_list returns [ArrayList<AST.formal> value]
	@init
	{
		$value = new ArrayList<AST.formal>();
	}
	:
		c = arg {$value.add($c.value);} 

		(COMMA y = arg {$value.add($y.value);})*
	;


arg returns [AST.formal value]   :
	id=OBJECTID COLON type=TYPEID
	{
		$value = new AST.formal($id.getText(), $type.getText(), $id.getLine()) ;
	}
	;


expression_list returns [ArrayList<AST.expression> value]
	@init
	{
		$value = new ArrayList<AST.expression>();
	}
	:
		( expr = expression {$value.add($expr.value);} (COMMA expr = expression {$value.add($expr.value);})* )?
	;

block_expr_list returns [ArrayList<AST.expression> value]
	@init
	{
		$value = new ArrayList<AST.expression>();
	}
	:
		(expr = expression SEMICOLON {$value.add($expr.value);})+
	;

branch_list returns [ArrayList<AST.branch> value]
	@init
	{
		$value = new ArrayList<AST.branch>();
	}
	:
		(br = branch SEMICOLON {$value.add($br.value);})+
	;

branch returns [AST.branch value] :
	id=OBJECTID COLON type=TYPEID DARROW expr=expression SEMICOLON
	{
		$value = new AST.branch($id.getText(), $type.getText(), $expr.value, $id.getLine());
	}
	;



let_list returns [ArrayList<AST.attr> value]
	@init
	{
		$value = new ArrayList<AST.attr>();
	}
	:
	at_un = attr { $value.add($at_un.value); }
	(COMMA at_deux = attr {$value.add($at_deux.value);})*
	;
						

expression returns [AST.expression value]   :
	
	expr=expression DOT id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.dispatch($expr.value, $id.getText(), $expr_list.value, $expr.value.lineNo);
	}
	
	| expr=expression ATSYM type=TYPEID DOT id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.static_dispatch($expr.value, $type.getText(), $id.getText(), $expr_list.value, $expr.value.lineNo);
	}
	
	| id=OBJECTID LPAREN expr_list = expression_list RPAREN
	{
		$value = new AST.dispatch(new AST.object("self" , $id.getLine()) , $id.getText() , $expr_list.value , $id.getLine()); 
	}
	
	| c=TILDE e1=expression
		{
			$value = new AST.comp($e1.value, $c.getLine());
		}
	
	| c=ISVOID expr=expression
		{
			$value = new AST.isvoid($expr.value, $c.getLine());
		}
	
	| e1=expression STAR e2=expression
		{
			$value = new AST.mul($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression SLASH e2=expression
		{
			$value = new AST.divide($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression PLUS e2=expression
		{
			$value = new AST.plus($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression MINUS e2=expression
		{
			$value = new AST.sub($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression LT e2=expression
		{
			$value = new AST.lt($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression LE e2=expression
		{
			$value = new AST.leq($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| e1=expression EQUALS e2=expression
		{
			$value = new AST.eq($e1.value, $e2.value, $e1.value.lineNo);
		}
	
	| c=NOT e1=expression
		{
			$value = new AST.neg($e1.value, $c.getLine());
		}
	
	| id=OBJECTID ASSIGN expr=expression
		{
			$value = new AST.assign($id.getText(), $expr.value, $id.getLine());  
		}
	
	| c=IF predicate=expression THEN ifbody=expression ELSE elsebody=expression FI 
		{
			$value = new AST.cond($predicate.value, $ifbody.value, $elsebody.value, $c.getLine());
		}
	
	| c=WHILE predicate=expression LOOP body=expression POOL
		{
			$value = new AST.loop($predicate.value, $body.value, $c.getLine());
		}
	
	| c=LBRACE el=block_expr_list RBRACE	// define
		{
			$value = new AST.block($el.value, $c.getLine());
		}
	
	| c=LET ll=let_list IN e1=expression
	{
		$value = $e1.value; 
		AST.attr this_attr;
		for(int i = $ll.value.size() - 1; i >= 0; --i) 
		{
			this_attr = $ll.value.get(i);
			$value = new AST.let(this_attr.name, this_attr.typeid, this_attr.value, $value, $c.getLine());
		}
	}
	
	| c=CASE predicate=expression OF bl=branch_list ESAC
		{	// define
			$value = new AST.typcase($predicate.value, $bl.value, $c.getLine());
		}
	
	| c=NEW type=TYPEID
		{
			$value = new AST.new_($type.getText(), $c.getLine());
		}
	
	| c=LPAREN e1=expression RPAREN
		{
			$value = $e1.value;
		}
		
	| id=OBJECTID
		{
			$value = new AST.object($id.getText(), $id.getLine());
		}
	
	| v=INT_CONST
		{
			$value = new AST.int_const(Integer.parseInt($v.getText()), $v.getLine());
		}
	
	| v=STR_CONST
		{
			$value = new AST.string_const($v.getText(), $v.getLine());
		}
	
	| v=BOOL_CONST
		{
			$value = new AST.bool_const($v.getText().charAt(0)=='t', $v.getLine());
		}
	;
