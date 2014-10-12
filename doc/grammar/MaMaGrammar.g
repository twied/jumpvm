grammar MaMaGrammar;

options {
  language = Java;
  output = AST;
  ASTLabelType = CommonTree;
}

program: expression EOF;

expression
    : lambdaExpression
    | letrecExpression
    | ifExpression
    | binOpExpression
    ;

lambdaExpression
    : '\\' Identifier* '.' expression
    ;

letrecExpression
    : 'letrec' variableDeclaration (';' variableDeclaration)* 'in' expression
    ;

variableDeclaration
    : Identifier '==' expression
    ;

ifExpression
    : 'if' expression 'then' expression 'else' expression
    ;

isNilExpression
	: 'isnil' atomicExpression
	;

headExpression
    : 'head' atomicExpression
    ;

tailExpression
    : 'tail' atomicExpression
    ;

binOpExpression
    : unOpExpression (binOp unOpExpression)?
    ;

unOpExpression
    : unOp? callExpression
    ;

callExpression
    : isNilExpression
    | headExpression
    | tailExpression
    | atomicExpression atomicExpression*
    ;

listExpression
    : '[' (expression (',' expression)*)? ']'
    ;

atomicExpression
    : '(' expression ')'
    | listExpression
    | Numeral
    | Identifier
    ;

unOp
    : '!'
    | '-'
    ;

binOp
    : '+'
    | '-'
    | '*'
    | '/'
    | '='
    | '!='
    | '<'
    | '<='
    | '>'
    | '>='
    | '&&'
    | '||'
    | ':'
    ;

Identifier
    : Letter (Letter | Digit)*
    ;

Numeral
    : Digit Digit*
    ;

fragment Letter
    : 'a'..'z'
    | 'A'..'Z';

fragment Digit
    : '0'..'9'
    ;
    
Whitespace
    :  (' ' | '\f' | '\n' | '\r'| '\t')+ {$channel = HIDDEN;}
    ;

Comment
    : '--' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? {$channel = HIDDEN;}
    ;
