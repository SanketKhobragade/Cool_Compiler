class Converter { -- Creating a class to convert string to integer and vice versa

	mod (a: Int, b: Int) : Int {
		if a<b then a else (a - b*(a/b))
		fi
	};
	
	char2digit(str : String) : Int {		-- converting single character to int
		if str = "0" then 0 else
		if str = "1" then 1 else
		if str = "2" then 2 else
		if str = "3" then 3 else
		if str = "4" then 4 else
		if str = "5" then 5 else
		if str = "6" then 6 else
		if str = "7" then 7 else
		if str = "8" then 8 else
			9
		fi fi fi fi fi fi fi fi fi
	};
	
	str2int(str : String) : Int {			--converting stirng to interger by converting each char to digit
		if str.length() = 1 then char2digit(str) else
			((str2int(str.substr(0,str.length()-1))) * 10 + (char2digit(str.substr(str.length()-1,1))))
		fi
	};
	
	digit2char(num : Int) : String{			--convert digits to char
		if num=0 then "0" else
		if num=1 then "1" else
		if num=2 then "2" else
		if num=3 then "3" else
		if num=4 then "4" else
		if num=5 then "5" else
		if num=6 then "6" else
		if num=7 then "7" else
		if num=8 then "8" else
			"9"
		fi fi fi fi fi fi fi fi fi
	};
	
	int2str(num : Int) : String{			--converting int to string($ is used to know the empty stack)
		if (num/10) = 0  then digit2char(num) else
			((int2str(num/10)).concat(digit2char(mod(num,10))))
		fi	
	};
	
};

class Stack inherits IO{			--creating a stack class for stack object
	str : String <- "$";			--using string to store values      
	
	find_comma(s:String): Int{		--Using "," as a separator and here finding index of first "," from the end of string
		( let flag : Bool <- true , i : Int <- (s.length()-1) in
			{
		
				while flag loop 
				{
					if s.substr(i,1) = "," then 
						{
							flag<-false;
						} 
					else if str.substr(i,1) = "$" then 
						{
							flag <- false;
							i <- 0;
						}
					else {
						i <- i-1;
					}
					fi fi;
				}
				pool;
				i; 
			}
		)
	};
	
	push(s: String): Object {		--Pushing value in stack
		{
			str <- str.concat(",");
			str <- str.concat(s);
		}
	};
	
	pop(): String{				--poping value from stack
		(let s : String, i : Int in 
 			if (str = "$") then "" else
			{
				i<-find_comma(str);
				s <- str.substr(i+1,(str.length() - i - 1));
				str <- str.substr(0,i);
				s;
			}
			fi
		)
		
	};
	
	disp_stack() : Object{			--display of stack
		(let s:String <- str, i:Int <-0 in
			while (not (s = "$")) loop 
			{
				i<-find_comma(s);
				out_string(s.substr(i+1,(s.length() - i - 1)));
				out_string("\n");
				s <- s.substr(0,i);	
			}
			pool
		)
	};
};

class StackCammands  {
	stack : Stack <- new Stack;		--creating stack and converter object
	con: Converter <- new Converter;
	
	process_cammand(s:String) : Object{ 	--method to process cammands
		if s = "d" then {stack.disp_stack();}
		else if s = "e" then {evaluate();}
		else {stack.push(s);}
		fi fi 
		 
	};
	
	evaluate() : Object {			--for evaluation
		(let s: String <- stack.pop() in 
			if s="+" then {add();}
			else if s="s" then {swap();}
			else {stack.push(s);}
			fi fi
		)
	};
	
	add() : Object{
		stack.push(con.int2str(con.str2int(stack.pop()) + con.str2int(stack.pop())))
	};
	
	swap() : Object{
		(let s1:String, s2:String in
			{
				s1 <- stack.pop();
				s2 <- stack.pop();
				stack.push(s1);
				stack.push(s2);
			}
		)
	};
	
};

class Main inherits IO { 
	stcmd : StackCammands <- new stackCammands;  --Missing new in the statement
	
	main() : Object {
		(let stcmd : StackCammands <- new stackCammands , flag:Bool <-true,s:String in
			while flag loop
			{
				out_string(">");
				s<-in_string();
				if s="x" then {	flag<-false;}
				else{
					stcmd.process_cammand(s);
				}
				fi;
			}
			pool
		)
	}; 
};
