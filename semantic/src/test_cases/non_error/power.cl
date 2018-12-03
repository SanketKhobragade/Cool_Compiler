
class Main inherits IO {
	a : Int;
	ans : Int<-0;
	b: Int;
	i : Int <-0 ;
	
	power(a:Int,b:Int) : Int{
		if b = 0 then 1 else
			if b = 1 then a else
			a*power(a,b-1)
			fi
		fi
	};
	
	(*bin(): Object{
		while 0<a loop 
		{
			b <- a/2;
			ans <- (power(10,i))*(a - b*2) + ans;
			a <- b;
		}
		pool
	};*)
	   
	main(): Object {
		{
		   	 out_string("Enter a number => ");
			 a <- in_int();
			 out_string("Enter a number => ");
			 b <-in_int();
			 ans <-power(a,b);
			 out_string("Ans => ");
			 out_int(ans);
			 out_string("\n");
		}	
	};
};
