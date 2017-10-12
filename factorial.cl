
class Main inherits IO {
	a : Int <- 4;
	b : Int;
	c : Int;
	d : Int;
	e : Int;
	f : Int;
	x1 : Bool;
	x2 : Bool;

	run(x : Int, y : Bool) : Int{
		{
			a <- 2*x + b;
			x1 <- y;
			5;
		}
	};

	check() : Object{
		{
			x1 <- not x2;
			x1 <- (a < b);
		}
	};
	
	power() : Object{
		if a = 3 then
			{
				if b = 4 then e <- 0 else e <- 1 fi;
				f <- 15;
			}
		else
			if c = 5 then{
				e <- 2;
				if d = 5 then e <- 10 else f <- 9 fi;
			}
			else e <- 20 fi
		fi

	};  
	looping() : Object{
		{
			check();
			a <- 0;
			while a < b loop{
				a <- a+1;
				if d = 5 then e <- 10 else f <- 9 fi;
				a <- a+2;
			}
			pool;
			a <- 0;
		}
	};
	main(ab : Int, cd : Bool): Int {
	   	5
	};
};

