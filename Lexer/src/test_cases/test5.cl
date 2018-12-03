-- This is a valid COOL program. It prints no error.
class Main inherits IO{
	var : Int <- 1;
	fact : Int <- 1;
	calculate(): Object {
		while 0<var loop
		{
			fact <- fact * var;
			var <- var - 1;
		}
		pool
	};
	main(): SELF_TYPE {
		{
			var <- in_int();
			calculate();
		}
  	};
};
