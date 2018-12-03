class Main {
	i:Int <- 46;
	k:Int;
	main():IO {
		{
			new IO.out_string("Hello world!\n");
			if i=2 then k<-0 else k<-1 ;		--here the fi is missing and also the block is not closed
		}
	};
};
