class A inherits B{
	obj1 : B <-new B;
	func1() : Int {
		0
	};
};

class B inherits C {
	obj2 : A <- new A;
	func2() : String {
		"1"
	};
};

class C inherits A {
	obj3 : A <- new A;
	func3() : Int {
		7
	};
	func3() : Bool {
		true
	};
};

class Main inherits IO {
	main() : Int {
		1
	};
};
