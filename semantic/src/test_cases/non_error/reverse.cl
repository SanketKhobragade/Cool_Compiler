class Main inherits IO {
	s : String;
	rev : String <- "";
	len : Int;
	
	reverse(): Object{
		 while 0<len loop
		 {
		 	rev <- rev.concat(s.substr(s.length()-1,1));
		 	s <- s.substr(0,s.length()-1);
		 	len <- len-1;
		 }
		 pool
	};  
	   
	main(): Object {
		{
		   	 out_string("Enter a string => ");
			 s <- in_string();		 
			 len <- s.length();
			 reverse();
			 out_string("reverse => ");
			 out_string(rev);
			 out_string("\n");
		}	
	};
};
