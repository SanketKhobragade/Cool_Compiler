class A{
	val : Int <- 10;
};

class B{
	st : String <- "test";

};

class Main{
	z : Bool;
	main() : Object{
		(let obj1 :A  <- new A, obj2 : B<- new B in
			{
				z <- if obj1 < obj2 then true else false fi;
			}
		)
	};
};
