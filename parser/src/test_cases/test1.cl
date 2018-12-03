class Main inherits IO {

	convert_char_to_digit(str : String) : Int {
		if str = "0" then 0 else
		if str = "1" then 1 else
		if str = "2" then 2 else
		if str = "3" then 3 else
		if str = "4" then 4 else
		if str = "5" then 5 else
		if str = "6" then 6 else
		if str = "7" then 7 else
		if str = "8" then 8 else
			9
		fi fi fi fi fi fi fi fi fi
	};
	
	convert_string_to_int(str : String) : Int {
		if str.length() = 1 then convert_char_to_digit(str) else
			((convert_string_to_int(str.substr(0,str.length()-1))) * 10 + (convert_char_to_digit(str.substr(str.length()-1,1))))
		fi
	};
 
	main() : Object { 
		{
			out_int(convert_string_to_int("45637"));
			out_string("\n");
		}
	} ; 
} ; 
