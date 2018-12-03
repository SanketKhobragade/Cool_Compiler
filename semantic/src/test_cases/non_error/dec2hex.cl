class Main inherits IO {
	s : String <- "";
	s1: String <- "";
	a: Int;
	n : Int;
	
	mod (a: Int, b: Int) : Int {
		if a<b then a else (a - b*(a/b))
		fi
	};
	
	convert2string(n: Int): String{
		if n=0 then "0" else
		if n=1 then "1" else
		if n=2 then "2" else
		if n=3 then "3" else
		if n=4 then "4" else
		if n=5 then "5" else
		if n=6 then "6" else
		if n=7 then "7" else
		if n=8 then "8" else
		if n=9 then "9" else
		if n=10 then "A" else
		if n=11 then "B" else
		if n=12 then "C" else
		if n=13 then "D" else
		if n=14 then "E" else
		"F" 
		fi fi fi fi fi fi fi fi fi fi fi fi fi fi fi 
	};
	
	convert() : Object {
		while 0<n loop {
			a <- mod(n,16);
			s1 <- convert2string(a);
			s <- s1.concat(s);
			n <-n/16;	
		}
		pool
	};
	   
	main(): Object {
		{
		   	 out_string("Enter a number => ");
			 n <- in_int();	 
			 out_string("Ans => ");
			 convert();
			 out_string(s);
			 out_string("\n");
		}	
	};
};
