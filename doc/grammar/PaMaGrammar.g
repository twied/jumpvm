grammar PaMaGrammar;

options {
  language = Java;
  output = AST;
  ASTLabelType = CommonTree;
}

program
    : 'program' declarations 'begin' statement_list? 'end' '.' EOF
    ;

declarations
    : ('type' type_decl_list)? ('var' var_decl_list)? proc_decl_list?
    ;

type_decl_list
    : type_decl (';' type_decl)*
    ;

type_decl
    : Identifier '=' type
    ;

var_decl_list
    : var_decl (';' var_decl)*
    ;

var_decl
    : Identifier ':' type
    ;

proc_decl_list
    : (proc_decl | func_decl) (';' (proc_decl | func_decl))*
    ;

proc_decl
    : 'procedure' Identifier '(' formal_parameter_list? ')' declarations 'begin' statement_list? 'end'
    ;

func_decl
    : 'function' Identifier '(' formal_parameter_list? ')' ':' type declarations 'begin' statement_list? 'end'
    ;

formal_parameter_list
    : formal_parameter (';' formal_parameter)*
    ;
    
formal_parameter
    : 'var'? Identifier ':' type
    ;

type_list
    : 'var'? type (';' 'var'? type)*
    ;

type
    : 'boolean'
    | 'integer'
    | '^' type
    | 'record' var_decl_list 'end'
    | 'array' '[' Integer ']' 'of' type
    | 'array' '[' range (',' range)* ']' 'of' type
    | Identifier
    | 'procedure' '(' type_list? ')'
    | 'function' '(' type_list? ')' ':' type
    ;

range
    : '-'? Integer '..' '-'? Integer
    ;

statement_list
    : statement (';' statement)*
    ;

statement
    : if_statement
    | while_statement
    | repeat_statement
    | for_statement
    | case_statement
    | new_statement
    | readln_statement
    | writeln_statement
    | assignment_statement
    | call_statement
    ;

assignment_statement
    : designator ':=' expression
    ;

if_statement
    : 'if' expression 'then' statement_list ('else' statement_list)? 'end'
    ;

while_statement
    : 'while' expression 'do' statement_list? 'end'
    ;

repeat_statement
    : 'repeat' statement_list? 'until' expression
    ;

for_statement
    : 'for' designator ':=' expression 'to' expression 'do' statement_list? 'end'
    ;

case_statement
    : 'case' expression 'of' case_limb+ 'end'
    ;

case_limb
    : Integer ':' statement_list
    ;

new_statement
    : 'new' '(' designator ')'
    ;

call_statement
    : call
    ;

readln_statement
    : 'readln' '(' designator ')'
    ;

writeln_statement
    : 'writeln' '(' expression ')'
    ;

expression_list
    : expression (',' expression)*
    ;

expression
    : operand (relation operand)?
    ;

operand
    : term (addition term)?
    ; 

term
    : unary (multiplication unary)?
    ;

unary
	: negation? factor
	;

factor
    : Integer
    | 'true'
    | 'false'
    | 'nil'
    | '(' expression ')'
    | 'high' '(' designator ',' Integer ')'
    | 'low' '(' designator ',' Integer ')'
    | call
    | designator
    ;

call
	: Identifier '(' expression_list? ')'
	;

designator
    : Identifier ('.' Identifier | '[' expression_list ']' | '^')*
    ;

relation
    : '='
    | '/='
    | '<'
    | '<='
    | '>'
    | '>='
    ;

addition
    : '+'
    | '-'
    | 'or'
    ;

multiplication
    : '*'
    | '/'
    | 'and'
    ;

negation
	: '-'
	| 'not'
	;

Identifier
    : Letter (Letter | Digit)*
    ;

Integer
    : Digit+
    ;

fragment Letter
    : 'a'..'z'
    | 'A'..'Z'
    ;

fragment Digit
    : '0'..'9'
    ;

Whitespace
    :  (' ' | '\f' | '\n' | '\r'| '\t')+ {$channel = HIDDEN;}
    ;

Comment
	: '(*' (options{greedy=false;}: .)* '*)' {$channel = HIDDEN;}
    ;
