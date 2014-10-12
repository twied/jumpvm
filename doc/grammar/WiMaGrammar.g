grammar WiMaGrammar;

options {
  language = Java;
  output = AST;
  ASTLabelType = CommonTree;
}

program
    : clause* query EOF
    ;

clause
    : predicate (':-' predicate_list)? '.'
    ;

query
    : '?-' predicate_list '.'
    ;

predicate_list
    : predicate (',' predicate)*
    ;

predicate
    : Atom
    | structure
    ;

structure
	: Atom '(' term (',' term)* ')'
	;

term
    : predicate
    | Variable
    | Numeral
    ;

Atom
    : LowercaseLetter Character*
    ;

Variable
    : UppercaseLetter Character*
    ;

Numeral
    : Digit+
    ;

fragment Character
    : LowercaseLetter
    | UppercaseLetter
    | Digit
    ;

fragment LowercaseLetter
    : 'a'..'z'
    ;
 
fragment UppercaseLetter
    : 'A'..'Z'
    ;

fragment Digit
    : '0'..'9'
    ;

Whitespace
    :  (' ' | '\f' | '\n' | '\r'| '\t')+ {$channel = HIDDEN;}
    ;

Comment
    : '\%' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? {$channel = HIDDEN;}
    ;
