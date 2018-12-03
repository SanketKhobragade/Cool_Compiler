# COOL Compiler #

Token Matching-> See test3.cl

The program correctly matches valid tokens and report errors. This is made possible by following specifications->
1) Keywords are checked for before Type id and object id since keywords also satisfy the property of it.
2) Keywords are checked for both small and caps(except bool const)
3) If a character is found after digits, it will report as invalid type.
4) If an invalid symbol is found after digits, it will print error of only invalid token.
5) Invalid token takes only 1 character (.)(not matched by any other rule) and prints the error. So lexing will resume from next character.
6) Whitespaces are skipped.
7) Comment lines are also skipped.
8) On finding * , we find next character. If it is ) then it is considered as comment close. Else considered as multiplication symbol.

Comment Lines-> See test1.cl

The program skips single line comments and nested comments.
1) When a '--' is found, take all characters until a \n or \r. Skip all these characters.
2) When a '(*' is found. It will take any character or another comment block. It will terminate if *) is found or EOF is reached. Print an error in latter case.

String Processing-> See test2.cl

The program does string matching adequrtely.
1. Define set of valid characters that can come in string(anything except quotes, newline or NULL)
2. Start taking in characters after " opens.
3. Take an escape sequence or a valid character. If none is found, then program reaches some invalid state.
4. Now check next character. If it is '\u0000' it will report NULL char error. If it is EOF, it will report EOF error. If it is \n it will report unterminated string constant. Else it will process the input string.
5. Processing is done by removing beginning and ending quotes. Then all escape sequences are replaced by proper values. If final length is greater than 1024 print an error else set token str token.
6. Proceed to next character.

test4.cl -> sudo -n "\"EOF in string will check EOF in string case.
tees5.cl takes a valid cool program. Note that there are no errors. 
