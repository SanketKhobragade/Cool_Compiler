class A {
	val : Int <- 10;
	func(a :Int,b: Bool):Int {
		10
	};
};

class B inherits A{
	i : Int <- 5;
};

class Main{
	obj:B<-new B;
	v : Int;
	main() : Object{
		{
			v<- a@B.func(10,true);
		}
	};
};
