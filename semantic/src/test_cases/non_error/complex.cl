(* This code takes 2 compex numbers in the form a+ib 
and adds or subtracts them. Don't add blank spaces anywhere in input *)
class Main inherits IO {
	x1 : Int;
	y1 : Int;
	x2 : Int;
	y2 : Int;
	x : Int;
	y : Int;
	ch : Int;
	i : Int;
	j1 : Int;
	j2 : Int;
	sub : String;
	s1 : String;
	s2 : String;
	temp: Int;
	add() : Object {
		{
			x <- x1 + x2;
			y <- y1 + y2;
		}
	};
	
	sub() : Object {
		{
			x <- x1 - x2;
			y <- y1 - y2;
		}
	};
	
	choice(i : Int) : Object {
		if i = 1 then add() else
		if i = 2 then sub() else
		out_string("Invalid Choice\n")
		fi fi
	};
	
	convert1() : Object {
		
		while i<s1.length() loop
		{
			sub <- s1.substr(i,1);
			if sub = "+" then j1 <- i else out_string("")
			fi;
			i <- i + 1;
		}
		pool
	};
	
	convert2() : Object {
		while i<s2.length() loop
		{
			sub <- s2.substr(i,1);
			if sub = "+" then j2 <- i else out_string("")
			fi;
			i <- i + 1;
		}
		pool
	};
	
	convert2int(n: String): Int{
		if n="0" then 0 else
		if n="1" then 1 else
		if n="2" then 2 else
		if n="3" then 3 else
		if n="4" then 4 else
		if n="5" then 5 else
		if n="6" then 6 else
		if n="7" then 7 else
		if n="8" then 8 else
		if n="9" then 9 else
		0
		fi fi fi fi fi fi fi fi fi fi
	};
	
	find(st : String, x:Int, f:Int) : Int {
		if f < x then{
			sub <- st.substr(f,1);
			temp <- 10*temp + convert2int(sub);
			find(st,x,f+1);
		}
		else temp
		fi
	};
			
	
	print() : Object{
		{
			out_string("Result-> ");
			out_int(x);
			out_string(" + ");
			out_int(y);
			out_string("i");
			out_string("\n");
		}
	};
	
	main(): Object {
		{
		   	 out_string("Enter 2 complex numbers\n");
			 s1 <- in_string();
			 s2 <- in_string();
			 s1 <- s1.substr(0,s1.length()-1);
			 s2 <- s2.substr(0,s2.length()-1);
			 i<-0;
			 convert1();
			 i<-0;
			 convert2();	 
			 out_string("1. Add\n2. Sub\n");
			 
			 x1 <- find(s1.substr(0,j1), j1, 0);
			 temp <- 0;

			 y1 <- find(s1.substr(j1+1, s1.length()-j1-1),s1.length()-j1-1, 0);
			 temp <- 0;

			 x2 <- find(s2.substr(0,j2), j2, 0);
			 temp <- 0;
			 			
			 y2 <- find(s2.substr(j2+1, s2.length()-j2-1),s2.length()-j2-1, 0);
			 temp <- 0;
			 			 
			 ch <- in_int();
			 choice(ch);
			 print();
		}	
	};
};
