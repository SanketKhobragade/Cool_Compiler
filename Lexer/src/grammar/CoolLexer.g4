lexer grammar CoolLexer;

tokens{
	ERROR,
	TYPEID,
	OBJECTID,
	BOOL_CONST,
	INT_CONST,
	STR_CONST,
	LPAREN,
	RPAREN,
	COLON,
	ATSYM,
	SEMICOLON,
	COMMA,
	PLUS,
	MINUS,
	STAR,
	SLASH,
	TILDE,
	LT,
	EQUALS,
	LBRACE,
	RBRACE,
	DOT,
	DARROW,
	LE,
	ASSIGN,
	CLASS,
	ELSE,
	FI,
	IF,
	IN,
	INHERITS,
	LET,
	LOOP,
	POOL,
	THEN,
	WHILE,
	CASE,
	ESAC,
	OF,
	NEW,
	ISVOID,
	NOT
}

/*
  DO NOT EDIT CODE ABOVE THIS LINE
*/

@members{

	/*
		YOU CAN ADD YOUR MEMBER VARIABLES AND METHODS HERE
	*/
	StringBuilder buf = new StringBuilder(); // can't make locals in lexer rules
		
	public void invalidToken(){
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
		reportError(text);
	}
		
	/**
	* Function to report errors.
	* Use this function whenever your lexer encounters any erroneous input
	* DO NOT EDIT THIS FUNCTION
	*/
	public void reportError(String errorString){
		setText(errorString);
		setType(ERROR);
	}

	public void processString() {
		Token t = _factory.create(_tokenFactorySourcePair, _type, _text, _channel, _tokenStartCharIndex, getCharIndex()-1, _tokenStartLine, _tokenStartCharPositionInLine);
		String text = t.getText();
		

		//write your code to test strings here
		
		
		text = text.substring(1,text.length() - 1);	// removing Ist and last quotes.
		text = text.replace("\\b","\b").replace("\\n","\n").replace("\\t","\t").replace("\\f","\f").replace("\\\n","\n").replaceAll("\\\\(.)","$1"); // Checking for escaped sequences
		if(text.length() > 1024){
			reportError("String constant too long");	// String const can have maximum size of 1024
			return;
		}
		setText(text);
	}
}

/*
	WRITE ALL LEXER RULES BELOW
*/
 
fragment VALID : ~('\n' | '"' | [\u0000]);
fragment ESCAPE: '\\' .;

STR_CONST :   '"' (ESCAPE|VALID)*? 
		('\n'	{reportError("Unterminated string constant");}
		| [\u0000] {reportError("String contains NULL character");}
		| '"'	{processString();}
		|EOF    {reportError("EOF in string");}
		)  ;

CLASS        : [Cc][Ll][Aa][Ss][Ss];
ELSE         : [Ee][Ll][Ss][Ee];
IF           : [Ii][Ff];
FI           : [Ff][Ii];
IN           : [Ii][Nn];
INHERITS     : [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss];
ISVOID       : [Ii][Ss][Vv][Oo][Ii][Dd];
LET          : [Ll][Ee][Tt];
LOOP         : [Ll][Oo][Oo][Pp];
POOL         : [Pp][Oo][Oo][Ll];
THEN         : [Tt][Hh][Ee][Nn];
WHILE        : [Ww][Hh][Ii][Ll][Ee];
CASE         : [Cc][Aa][Ss][Ee];
ESAC         : [Ee][Ss][Aa][Cc];
NEW          : [Nn][Ee][Ww];
OF           : [Oo][Ff];
NOT          : [Nn][Oo][Tt];
fragment TRUE	: [t][Rr][Uu][Ee];
fragment FALSE	: [f][Aa][Ll][Ss][Ee];
fragment OPEN	: '(*';
fragment CLOSE	: '*)'; 
COMMENT 	: '--'  ~('\n' | '\r')* -> skip;
MULTCOMMENT	: OPEN (MULTCOMMENT|.)*? 
		(CLOSE	{skip();}
		|EOF	{reportError("EOF in comment");}
		)  ;
COMMENTCLOSE	: '*)'  {reportError("Unmatched *)");} ;
LPAREN		: '(';
RPAREN		: ')';
COLON		: ':';
ATSYM		: '@';
SEMICOLON	: ';';
COMMA		: ',';
PLUS		: '+';
MINUS		: '-';
STAR		: '*';
SLASH		: '/';
TILDE		: '~';
LT		: '<';
EQUALS		: '=';
LBRACE		: '{';
RBRACE		: '}';
DOT		: '.';
DARROW		: '=>';
LE		: '<=';
ASSIGN		: '<-';
WS 		: [ \t\n\r\f]+ -> skip;
fragment SMALL 	: ('a'..'z');
fragment CAPITAL: ('A'..'Z');
fragment DIGIT	: '0'..'9';
INT_CONST	: DIGIT+;
INT_ERROR	: INT_CONST (SMALL|CAPITAL) {reportError("Invalid Type");};
BOOL_CONST	: TRUE|FALSE;
TYPEID       : [A-Z][A-Za-z0-9_]*;
OBJECTID     : [a-z][A-Za-z0-9_]*;
ERROR		: . {invalidToken();} ;		


