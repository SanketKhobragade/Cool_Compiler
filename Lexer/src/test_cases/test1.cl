--This test case covers comment lines and nesting of comments
(* This is a valid (* Nested comment *). *)
Tokenize this line . This will print mismatched *)
-- Single line comment
This line will be printed.
(* This will give error EOF in comment
