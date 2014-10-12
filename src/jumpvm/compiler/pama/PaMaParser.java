/*
 * JumpVM: The Java Unified Multi Paradigm Virtual Machine.
 * Copyright (C) 2013 Tim Wiederhake
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package jumpvm.compiler.pama;

import java.util.ArrayList;

import jumpvm.ast.pama.CaseLimb;
import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.DesignatorPart;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.Program;
import jumpvm.ast.pama.Range;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.Type;
import jumpvm.ast.pama.declarations.FuncDecl;
import jumpvm.ast.pama.declarations.ProcDecl;
import jumpvm.ast.pama.declarations.TypeDecl;
import jumpvm.ast.pama.declarations.VarDecl;
import jumpvm.ast.pama.designatorpart.IndirectionDesignatorPart;
import jumpvm.ast.pama.designatorpart.ListAccessDesignatorPart;
import jumpvm.ast.pama.designatorpart.SelectorDesignatorPart;
import jumpvm.ast.pama.expressions.BooleanExpression;
import jumpvm.ast.pama.expressions.CallExpression;
import jumpvm.ast.pama.expressions.ConjunctionExpression;
import jumpvm.ast.pama.expressions.ConjunctionExpression.ConjunctionOperator;
import jumpvm.ast.pama.expressions.DisjunctionExpression;
import jumpvm.ast.pama.expressions.DisjunctionExpression.DisjunctionOperator;
import jumpvm.ast.pama.expressions.HighExpression;
import jumpvm.ast.pama.expressions.IntegerExpression;
import jumpvm.ast.pama.expressions.LowExpression;
import jumpvm.ast.pama.expressions.NilExpression;
import jumpvm.ast.pama.expressions.RelationalExpression;
import jumpvm.ast.pama.expressions.RelationalExpression.RelationalOperator;
import jumpvm.ast.pama.expressions.UnaryExpression;
import jumpvm.ast.pama.expressions.UnaryExpression.UnaryOperator;
import jumpvm.ast.pama.statements.AssignmentStatement;
import jumpvm.ast.pama.statements.CallStatement;
import jumpvm.ast.pama.statements.CaseStatement;
import jumpvm.ast.pama.statements.ForStatement;
import jumpvm.ast.pama.statements.IfStatement;
import jumpvm.ast.pama.statements.NewStatement;
import jumpvm.ast.pama.statements.ReadlnStatement;
import jumpvm.ast.pama.statements.RepeatStatement;
import jumpvm.ast.pama.statements.WhileStatement;
import jumpvm.ast.pama.statements.WritelnStatement;
import jumpvm.ast.pama.types.ArrayType;
import jumpvm.ast.pama.types.BooleanType;
import jumpvm.ast.pama.types.CustomType;
import jumpvm.ast.pama.types.FunctionType;
import jumpvm.ast.pama.types.IntegerType;
import jumpvm.ast.pama.types.PointerType;
import jumpvm.ast.pama.types.ProcedureType;
import jumpvm.ast.pama.types.RecordType;
import jumpvm.compiler.Location;
import jumpvm.compiler.Parser;
import jumpvm.exception.ParseException;

/**
 * PaMachine {@link Parser}.
 *
 * Grammar is in <a href="http://www.antlr.org">antlr</a> format.
 */
public class PaMaParser extends Parser<PaMaLexer, PaMaToken> {
    /**
     * Create a new PaMaParser over the given lexer.
     *
     * @param lexer lexer
     */
    public PaMaParser(final PaMaLexer lexer) {
        super(lexer);
    }

    @Override
    public final Program parse() throws ParseException {
        try {
            return parseProgram();
        } catch (final RuntimeException e) {
            throw new ParseException(getLocation(), e);
        }
    }

    /**
     * Parse an {@code assignment_statement}.
     *
     * <pre>
     * assignment_statement
     *     : designator ':=' expression
     *     ;
     * </pre>
     *
     * @param begin begin
     * @param identifier identifier of designator
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseAssignmentStatment(final Location begin, final String identifier) throws ParseException {
        final Designator designator = parseDesignator(begin, identifier);
        eat(PaMaToken.TOKEN_ASSIGNMENT);
        final Expression expression = parseExpression();
        final Location end = getLocation();
        return new AssignmentStatement(begin, end, designator, expression);
    }

    /**
     * Parse a {@code call_statement}.
     *
     * <pre>
     * call_statement
     *     : call
     *     ;
     * </pre>
     *
     * @param begin begin
     * @param identifier function name
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseCallStatement(final Location begin, final String identifier) throws ParseException {
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final ArrayList<Expression> expressionList;
        if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
            expressionList = new ArrayList<Expression>();
        } else {
            expressionList = parseExpressionList();
        }
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        final Location end = getLocation();
        return new CallStatement(begin, end, identifier, expressionList);
    }

    /**
     * Parse a {@code case_limb}.
     *
     * <pre>
     * case_limb
     *     : Integer ':' statement_list
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private CaseLimb parseCaseLimb() throws ParseException {
        final Location begin = getLocation();
        final int value = parseNumeral();
        eat(PaMaToken.TOKEN_COLON);
        final ArrayList<Statement> statementList = parseStatementList();
        final Location end = getLocation();
        return new CaseLimb(begin, end, value, statementList);
    }

    /**
     * Parse a {@code case_statement}.
     *
     * <pre>
     * case_statement
     *     : 'case' expression 'of' case_limb+ 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseCaseStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_CASE);
        final Expression expression = parseExpression();
        eat(PaMaToken.KEYWORD_OF);
        final ArrayList<CaseLimb> caseLimbList = new ArrayList<CaseLimb>();
        caseLimbList.add(parseCaseLimb());
        while (!isToken(PaMaToken.KEYWORD_END)) {
            caseLimbList.add(parseCaseLimb());
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new CaseStatement(begin, end, expression, caseLimbList);
    }

    /**
     * Parse an {@code addition}.
     *
     * <pre>
     * operand
     *     : term (addition term)?
     *     ;
     * addition
     *     : '+'
     *     | '-'
     *     | 'or'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseConjunctionExpression() throws ParseException {
        final Location begin = getLocation();
        final Expression lhs = parseDisjunctionExpression();
        final ConjunctionOperator operator = ConjunctionOperator.fromToken(getToken());
        if (operator == null) {
            return lhs;
        }

        eat(getToken());
        final Expression rhs = parseDisjunctionExpression();
        final Location end = getLocation();
        return new ConjunctionExpression(begin, end, operator, lhs, rhs);
    }

    /**
     * Parse {@code declarations}.
     *
     * <pre>
     * declarations
     *     : ('type' type_decl_list)? ('var' vardecl_list)? proc_decl_list?
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Declarations parseDeclarations() throws ParseException {
        final Location begin = getLocation();
        final ArrayList<TypeDecl> typeDeclList;
        if (isToken(PaMaToken.KEYWORD_TYPE)) {
            eat(PaMaToken.KEYWORD_TYPE);
            typeDeclList = parseTypeDeclList();
        } else {
            typeDeclList = new ArrayList<TypeDecl>();
        }
        final ArrayList<VarDecl> varDeclList;
        if (isToken(PaMaToken.KEYWORD_VAR)) {
            eat(PaMaToken.KEYWORD_VAR);
            varDeclList = parseVarDeclList();
        } else {
            varDeclList = new ArrayList<VarDecl>();
        }
        final ArrayList<FuncDecl> procDeclList;
        if (isToken(PaMaToken.KEYWORD_PROCEDURE) || isToken(PaMaToken.KEYWORD_FUNCTION)) {
            procDeclList = parseProcDeclList();
        } else {
            procDeclList = new ArrayList<FuncDecl>();
        }
        final Location end = getLocation();
        return new Declarations(begin, end, typeDeclList, varDeclList, procDeclList);
    }

    /**
     * Parse a {@code designator}.
     *
     * <pre>
     * designator
     *     : Identifier ('.' Identifier | '[' expression_list ']' | '^')*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Designator parseDesignator() throws ParseException {
        final Location begin = getLocation();
        final String identifier = parseIdentifier();
        return parseDesignator(begin, identifier);
    }

    /**
     * Parse a {@code designator}.
     *
     * <pre>
     * designator
     *     : Identifier ('.' Identifier | '[' expression_list ']' | '^')*
     *     ;
     * </pre>
     *
     * @param begin begin
     * @param identifier first identifier
     * @return next AST node
     * @throws ParseException on failure
     */
    private Designator parseDesignator(final Location begin, final String identifier) throws ParseException {
        final ArrayList<DesignatorPart> designatorPartList = new ArrayList<DesignatorPart>();
        while (isToken(PaMaToken.TOKEN_PERIOD) || isToken(PaMaToken.BRACKET_SQUARE_LEFT) || isToken(PaMaToken.TOKEN_CARET)) {
            final Location partBegin = getLocation();
            if (isToken(PaMaToken.TOKEN_PERIOD)) {
                eat(PaMaToken.TOKEN_PERIOD);
                final String selector = parseIdentifier();
                designatorPartList.add(new SelectorDesignatorPart(begin, getLocation(), selector));
            } else if (isToken(PaMaToken.BRACKET_SQUARE_LEFT)) {
                eat(PaMaToken.BRACKET_SQUARE_LEFT);
                final ArrayList<Expression> expressionList = parseExpressionList();
                eat(PaMaToken.BRACKET_SQUARE_RIGHT);
                designatorPartList.add(new ListAccessDesignatorPart(partBegin, getLocation(), expressionList));
            } else {
                eat(PaMaToken.TOKEN_CARET);
                designatorPartList.add(new IndirectionDesignatorPart(partBegin, getLocation()));
            }
        }
        final Location end = getLocation();
        return new Designator(begin, end, identifier, designatorPartList);
    }

    /**
     * Parse a {@code term}.
     *
     * <pre>
     * term
     *     : unary (multiplication unary)?
     *     ;
     *
     * multiplication
     *     : '*'
     *     | '/'
     *     | 'and'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseDisjunctionExpression() throws ParseException {
        final Location begin = getLocation();
        final Expression lhs = parseUnaryExpression();
        final DisjunctionOperator operator = DisjunctionOperator.fromToken(getToken());
        if (operator == null) {
            return lhs;
        }

        eat(getToken());
        final Expression rhs = parseUnaryExpression();
        final Location end = getLocation();
        return new DisjunctionExpression(begin, end, operator, lhs, rhs);
    }

    /**
     * Alias for {@link #parseRelationalExpression()}.
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseExpression() throws ParseException {
        return parseRelationalExpression();
    }

    /**
     * Parse a {@code expression_list}.
     *
     * <pre>
     * expression_list
     *     : expression (',' expression)*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<Expression> parseExpressionList() throws ParseException {
        final ArrayList<Expression> expressionList = new ArrayList<Expression>();
        expressionList.add(parseExpression());
        while (isToken(PaMaToken.TOKEN_COMMA)) {
            eat(PaMaToken.TOKEN_COMMA);
            expressionList.add(parseExpression());
        }
        return expressionList;
    }

    /**
     * Parse a {@code factor}.
     *
     * <pre>
     * factor
     *     : Integer
     *     | 'true'
     *     | 'false'
     *     | 'nil'
     *     | '(' expression ')'
     *     | 'high' '(' designator ',' Integer ')'
     *     | 'low' '(' designator ',' Integer ')'
     *     | call
     *     | designator
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseFactorExpression() throws ParseException {
        final Location begin = getLocation();
        if (isToken(PaMaToken.NUMERAL)) {
            final int value = parseNumeral();
            final Location end = getLocation();
            return new IntegerExpression(begin, end, value);
        } else if (isToken(PaMaToken.KEYWORD_TRUE)) {
            eat(PaMaToken.KEYWORD_TRUE);
            final Location end = getLocation();
            return new BooleanExpression(begin, end, true);
        } else if (isToken(PaMaToken.KEYWORD_FALSE)) {
            eat(PaMaToken.KEYWORD_FALSE);
            final Location end = getLocation();
            return new BooleanExpression(begin, end, false);
        } else if (isToken(PaMaToken.KEYWORD_NIL)) {
            eat(PaMaToken.KEYWORD_NIL);
            final Location end = getLocation();
            return new NilExpression(begin, end);
        } else if (isToken(PaMaToken.BRACKET_ROUND_LEFT)) {
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final Expression expression = parseExpression();
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            return expression;
        } else if (isToken(PaMaToken.KEYWORD_HIGH)) {
            eat(PaMaToken.KEYWORD_HIGH);
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final Designator designator = parseDesignator();
            eat(PaMaToken.TOKEN_COMMA);
            final int dimension = parseNumeral();
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            final Location end = getLocation();
            return new HighExpression(begin, end, designator, dimension);
        } else if (isToken(PaMaToken.KEYWORD_LOW)) {
            eat(PaMaToken.KEYWORD_LOW);
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final Designator designator = parseDesignator();
            eat(PaMaToken.TOKEN_COMMA);
            final int dimension = parseNumeral();
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            final Location end = getLocation();
            return new LowExpression(begin, end, designator, dimension);
        }

        final String identifier = parseIdentifier();
        if (isToken(PaMaToken.BRACKET_ROUND_LEFT)) {
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final ArrayList<Expression> expressionList;
            if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
                expressionList = new ArrayList<Expression>();
            } else {
                expressionList = parseExpressionList();
            }
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            final Location end = getLocation();
            return new CallExpression(begin, end, identifier, expressionList);
        } else {
            return parseDesignator(begin, identifier);
        }
    }

    /**
     * Parse a {@code formal_parameter}.
     *
     * <pre>
     * formal_parameter
     *     : 'var'? Identifier ':' type
     *     ;
     * </pre>
     *
     * @param hasIdentifier if this formal_parameter must have an indentifer. Parameter declarations in type declarations for instance don't have names.
     * @return next AST node
     * @throws ParseException on failure
     */
    private FormalParameter parseFormalParameter(final boolean hasIdentifier) throws ParseException {
        final Location begin = getLocation();
        final boolean reference;
        if (isToken(PaMaToken.KEYWORD_VAR)) {
            eat(PaMaToken.KEYWORD_VAR);
            reference = true;
        } else {
            reference = false;
        }
        final String identifier;
        if (hasIdentifier) {
            identifier = parseIdentifier();
            eat(PaMaToken.TOKEN_COLON);
        } else {
            identifier = null;
        }
        final Type type = parseType();
        final Location end = getLocation();
        return new FormalParameter(begin, end, reference, identifier, type);
    }

    /**
     * Parse a {@code formal_parameter_list}.
     *
     * <pre>
     * formal_parameter_list
     *     : formal_parameter (';' formal_parameter)*
     *     ;
     * </pre>
     *
     * @param hasIdentifier if this formal_parameter must have an indentifer. Parameter declarations in type declarations for instance don't have names.
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<FormalParameter> parseFormalParameterList(final boolean hasIdentifier) throws ParseException {
        final ArrayList<FormalParameter> formalParameterList = new ArrayList<FormalParameter>();
        formalParameterList.add(parseFormalParameter(hasIdentifier));
        while (isToken(PaMaToken.TOKEN_SEMICOLON)) {
            eat(PaMaToken.TOKEN_SEMICOLON);
            formalParameterList.add(parseFormalParameter(hasIdentifier));
        }
        return formalParameterList;
    }

    /**
     * Parse a {@code for_statement}.
     *
     * <pre>
     * for_statement
     *     : 'for' designator ':=' expression 'to' expression 'do' statement_list? 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseForStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_FOR);
        final Designator designator = parseDesignator();
        eat(PaMaToken.TOKEN_ASSIGNMENT);
        final Expression fromExpression = parseExpression();
        eat(PaMaToken.KEYWORD_TO);
        final Expression toExpression = parseExpression();
        eat(PaMaToken.KEYWORD_DO);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_END)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new ForStatement(begin, end, designator, fromExpression, toExpression, statementList);
    }

    /**
     * Parse a {@code func_decl}.
     *
     * <pre>
     * func_decl
     *     : 'function' Identifier '(' formal_parameter_list? ')' ':' type declarations 'begin' statement_list? 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private FuncDecl parseFuncDecl() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_FUNCTION);
        final String identifier = parseIdentifier();
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final ArrayList<FormalParameter> formalParameterList;
        if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
            formalParameterList = new ArrayList<FormalParameter>();
        } else {
            formalParameterList = parseFormalParameterList(true);
        }
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        eat(PaMaToken.TOKEN_COLON);
        final Type returnType = parseType();
        final Declarations declarations = parseDeclarations();
        eat(PaMaToken.KEYWORD_BEGIN);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_END)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new FuncDecl(begin, end, identifier, formalParameterList, returnType, declarations, statementList);
    }

    /**
     * Parse an {@code Identifier}.
     *
     * <pre>
     * Identifier
     *     : Letter (Letter | Digit)*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private String parseIdentifier() throws ParseException {
        final String identifier = getIdentifier();
        eat(PaMaToken.IDENTIFIER);
        return identifier;
    }

    /**
     * Parse an {@code if_statement}.
     *
     * <pre>
     * if_statement
     *     : 'if' expression 'then' statement_list ('else' statement_list)? 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseIfStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_IF);
        final Expression condition = parseExpression();
        eat(PaMaToken.KEYWORD_THEN);
        final ArrayList<Statement> thenStatementList = parseStatementList();
        final ArrayList<Statement> elseStatementList;
        if (isToken(PaMaToken.KEYWORD_ELSE)) {
            eat(PaMaToken.KEYWORD_ELSE);
            elseStatementList = parseStatementList();
        } else {
            elseStatementList = new ArrayList<Statement>();
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new IfStatement(begin, end, condition, thenStatementList, elseStatementList);
    }

    /**
     * Parse a {@code new_statement}.
     *
     * <pre>
     * new_statement
     *     : 'new' '(' designator ')'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseNewStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_NEW);
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final Designator designator = parseDesignator();
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        final Location end = getLocation();
        return new NewStatement(begin, end, designator);
    }

    /**
     * Parse an {@code integer}.
     *
     * <pre>
     * Integer
     *     : Digit+
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private int parseNumeral() throws ParseException {
        final String identifier = getIdentifier();
        final int integer;

        try {
            integer = Integer.parseInt(identifier);
        } catch (final NumberFormatException e) {
            throw new ParseException(getLocation(), PaMaToken.NUMERAL, identifier + " (" + e + ")");
        }

        eat(PaMaToken.NUMERAL);
        return integer;
    }

    /**
     * Parse a {@code proc_decl}.
     *
     * <pre>
     * proc_decl
     *     : 'procedure' Identifier '(' formal_parameter_list? ')' declarations 'begin' statement_list? 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private FuncDecl parseProcDecl() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_PROCEDURE);
        final String identifier = parseIdentifier();
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final ArrayList<FormalParameter> formalParameterList;
        if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
            formalParameterList = new ArrayList<FormalParameter>();
        } else {
            formalParameterList = parseFormalParameterList(true);
        }
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        final Declarations declarations = parseDeclarations();
        eat(PaMaToken.KEYWORD_BEGIN);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_END)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new ProcDecl(begin, end, identifier, formalParameterList, declarations, statementList);
    }

    /**
     * Parse a {@code proc_decl_list}.
     *
     * <pre>
     * proc_decl_list
     *     : (proc_decl | func_decl) (';' (proc_decl | func_decl))*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<FuncDecl> parseProcDeclList() throws ParseException {
        final ArrayList<FuncDecl> procDeclList = new ArrayList<FuncDecl>();
        if (isToken(PaMaToken.KEYWORD_FUNCTION)) {
            procDeclList.add(parseFuncDecl());
        } else {
            procDeclList.add(parseProcDecl());
        }
        while (isToken(PaMaToken.TOKEN_SEMICOLON)) {
            eat(PaMaToken.TOKEN_SEMICOLON);
            if (isToken(PaMaToken.KEYWORD_FUNCTION)) {
                procDeclList.add(parseFuncDecl());
            } else {
                procDeclList.add(parseProcDecl());
            }
        }
        return procDeclList;
    }

    /**
     * Parse a {@code program}.
     *
     * <pre>
     * program
     *     : 'program' declarations 'begin' statement_list? 'end' '.' EOF
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Program parseProgram() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_PROGRAM);
        final Declarations declarations = parseDeclarations();
        eat(PaMaToken.KEYWORD_BEGIN);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_END)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_END);
        eat(PaMaToken.TOKEN_PERIOD);
        expect(PaMaToken.EOF);
        final Location end = getLocation();
        return new Program(begin, end, declarations, statementList);
    }

    /**
     * Parse a {@code range}.
     *
     * <pre>
     * range
     *     : '-'? Integer '..' '-'? Integer
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Range parseRange() throws ParseException {
        final Location begin = getLocation();
        if (isToken(PaMaToken.TOKEN_MINUS)) {
            eat(PaMaToken.TOKEN_MINUS);
            final int low = -parseNumeral();
            return parseRange(begin, low);
        } else {
            final int low = parseNumeral();
            return parseRange(begin, low);
        }
    }

    /**
     * Parse a {@code range}.
     *
     * <pre>
     * range
     *     : '-'? Integer '..' '-'? Integer
     *     ;
     * </pre>
     *
     * @param begin begin
     * @param low lower bound
     * @return next AST node
     * @throws ParseException on failure
     */
    private Range parseRange(final Location begin, final int low) throws ParseException {
        eat(PaMaToken.TOKEN_RANGE);
        if (isToken(PaMaToken.TOKEN_MINUS)) {
            eat(PaMaToken.TOKEN_MINUS);
            final int high = -parseNumeral();
            final Location end = getLocation();
            return new Range(begin, end, low, high);
        } else {
            final int high = parseNumeral();
            final Location end = getLocation();
            return new Range(begin, end, low, high);
        }
    }

    /**
     * Parse a range list.
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<Range> parseRangeList() throws ParseException {
        final ArrayList<Range> rangeList = new ArrayList<Range>();

        if (isToken(PaMaToken.TOKEN_MINUS)) {
            /* range */
            rangeList.add(parseRange());
            while (isToken(PaMaToken.TOKEN_COMMA)) {
                eat(PaMaToken.TOKEN_COMMA);
                rangeList.add(parseRange());
            }
        } else {
            /* simple or range */
            final Location begin = getLocation();
            final int value = parseNumeral();
            if (isToken(PaMaToken.BRACKET_SQUARE_RIGHT)) {
                /* simple */
                for (int i = 0; i < value; ++i) {
                    rangeList.add(null);
                }
            } else {
                /* range */
                rangeList.add(parseRange(begin, value));
                while (isToken(PaMaToken.TOKEN_COMMA)) {
                    eat(PaMaToken.TOKEN_COMMA);
                    rangeList.add(parseRange());
                }
            }
        }

        return rangeList;
    }

    /**
     * Parse a {@code readln_statement}.
     *
     * <pre>
     * readln_statement
     *     : 'readln' '(' designator ')'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseReadlnStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_READLN);
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final Designator designator = parseDesignator();
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        final Location end = getLocation();
        return new ReadlnStatement(begin, end, designator);
    }

    /**
     * Parse an {@code expression}.
     *
     * <pre>
     * expression
     *     : operand (relation operand)?
     *     ;
     *
     * relation
     *     : '='
     *     | '/='
     *     | '<'
     *     | '<='
     *     | '>'
     *     | '>='
     *     ;
     *     </pre>
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseRelationalExpression() throws ParseException {
        final Location begin = getLocation();
        final Expression lhs = parseConjunctionExpression();
        final RelationalOperator operator = RelationalOperator.fromToken(getToken());
        if (operator == null) {
            return lhs;
        }

        eat(getToken());
        final Expression rhs = parseConjunctionExpression();
        final Location end = getLocation();
        return new RelationalExpression(begin, end, operator, lhs, rhs);
    }

    /**
     * Parse a {@code repeat_statement}.
     *
     * <pre>
     * repeat_statement
     *     : 'repeat' statement_list? 'until' expression
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseRepeatStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_REPEAT);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_UNTIL)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_UNTIL);
        final Expression condition = parseExpression();
        final Location end = getLocation();
        return new RepeatStatement(begin, end, statementList, condition);
    }

    /**
     * Parse a {@code statement}.
     *
     * <pre>
     * statement
     *     : if_statement
     *     | while_statement
     *     | repeat_statement
     *     | for_statement
     *     | case_statement
     *     | new_statement
     *     | readln_statement
     *     | writeln_statement
     *     | assignment_statment
     *     | call_statement
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseStatement() throws ParseException {
        if (isToken(PaMaToken.KEYWORD_IF)) {
            return parseIfStatement();
        } else if (isToken(PaMaToken.KEYWORD_WHILE)) {
            return parseWhileStatement();
        } else if (isToken(PaMaToken.KEYWORD_REPEAT)) {
            return parseRepeatStatement();
        } else if (isToken(PaMaToken.KEYWORD_FOR)) {
            return parseForStatement();
        } else if (isToken(PaMaToken.KEYWORD_CASE)) {
            return parseCaseStatement();
        } else if (isToken(PaMaToken.KEYWORD_NEW)) {
            return parseNewStatement();
        } else if (isToken(PaMaToken.KEYWORD_READLN)) {
            return parseReadlnStatement();
        } else if (isToken(PaMaToken.KEYWORD_WRITELN)) {
            return parseWritelnStatement();
        }

        final Location begin = getLocation();
        final String identifier = parseIdentifier();
        if (isToken(PaMaToken.BRACKET_ROUND_LEFT)) {
            return parseCallStatement(begin, identifier);
        } else {
            return parseAssignmentStatment(begin, identifier);
        }
    }

    /**
     * Parse a {@code statement_list}.
     *
     * <pre>
     * statement_list
     *     : statement (';' statement)*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<Statement> parseStatementList() throws ParseException {
        final ArrayList<Statement> statementList = new ArrayList<Statement>();
        statementList.add(parseStatement());
        while (isToken(PaMaToken.TOKEN_SEMICOLON)) {
            eat(PaMaToken.TOKEN_SEMICOLON);
            statementList.add(parseStatement());
        }
        return statementList;
    }

    /**
     * Parse a {@code type}.
     *
     * <pre>
     * type
     *     : 'boolean'
     *     | 'integer'
     *     | '^' type
     *     | 'record' var_decl_list 'end'
     *     | 'array' '[' Integer ']' 'of' type
     *     | 'array' '[' range (',' range)* ']' 'of' type
     *     | Identifier
     *     | 'procedure' '(' type_list? ')'
     *     | 'function' '(' type_list? ')' ':' type
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Type parseType() throws ParseException {
        final Location begin = getLocation();
        if (isToken(PaMaToken.KEYWORD_BOOLEAN)) {
            eat(PaMaToken.KEYWORD_BOOLEAN);
            final Location end = getLocation();
            return new BooleanType(begin, end);
        } else if (isToken(PaMaToken.KEYWORD_INTEGER)) {
            eat(PaMaToken.KEYWORD_INTEGER);
            final Location end = getLocation();
            return new IntegerType(begin, end);
        } else if (isToken(PaMaToken.TOKEN_CARET)) {
            eat(PaMaToken.TOKEN_CARET);
            final Type type = parseType();
            final Location end = getLocation();
            return new PointerType(begin, end, type);
        } else if (isToken(PaMaToken.KEYWORD_RECORD)) {
            eat(PaMaToken.KEYWORD_RECORD);
            final ArrayList<VarDecl> varDeclList = parseVarDeclList();
            eat(PaMaToken.KEYWORD_END);
            final Location end = getLocation();
            return new RecordType(begin, end, varDeclList);
        } else if (isToken(PaMaToken.KEYWORD_ARRAY)) {
            eat(PaMaToken.KEYWORD_ARRAY);
            eat(PaMaToken.BRACKET_SQUARE_LEFT);
            final ArrayList<Range> dimensions = parseRangeList();
            eat(PaMaToken.BRACKET_SQUARE_RIGHT);
            eat(PaMaToken.KEYWORD_OF);
            final Type type = parseType();
            final Location end = getLocation();
            return new ArrayType(begin, end, dimensions, type);
        } else if (isToken(PaMaToken.KEYWORD_PROCEDURE)) {
            eat(PaMaToken.KEYWORD_PROCEDURE);
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final ArrayList<FormalParameter> formalParameterList;
            if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
                formalParameterList = new ArrayList<FormalParameter>();
            } else {
                formalParameterList = parseFormalParameterList(false);
            }
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            final Location end = getLocation();
            return new ProcedureType(begin, end, formalParameterList);
        } else if (isToken(PaMaToken.KEYWORD_FUNCTION)) {
            eat(PaMaToken.KEYWORD_FUNCTION);
            eat(PaMaToken.BRACKET_ROUND_LEFT);
            final ArrayList<FormalParameter> formalParameterList;
            if (isToken(PaMaToken.BRACKET_ROUND_RIGHT)) {
                formalParameterList = new ArrayList<FormalParameter>();
            } else {
                formalParameterList = parseFormalParameterList(false);
            }
            eat(PaMaToken.BRACKET_ROUND_RIGHT);
            eat(PaMaToken.TOKEN_COLON);
            final Type returnType = parseType();
            final Location end = getLocation();
            return new FunctionType(begin, end, formalParameterList, returnType);
        } else {
            final String identifier = parseIdentifier();
            final Location end = getLocation();
            return new CustomType(begin, end, identifier);
        }
    }

    /**
     * Parse a {@code type_decl}.
     *
     * <pre>
     * type_decl
     *     : Identifier '=' type
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private TypeDecl parseTypeDecl() throws ParseException {
        final Location begin = getLocation();
        final String identifier = parseIdentifier();
        eat(PaMaToken.TOKEN_EQUAL);
        final Type type = parseType();
        final Location end = getLocation();
        return new TypeDecl(begin, end, identifier, type);
    }

    /**
     * Parse a {@code type_decl_list}.
     *
     * <pre>
     * type_decl_list
     *     : type_decl (';' type_decl)*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<TypeDecl> parseTypeDeclList() throws ParseException {
        final ArrayList<TypeDecl> typeDeclList = new ArrayList<TypeDecl>();
        typeDeclList.add(parseTypeDecl());
        while (isToken(PaMaToken.TOKEN_SEMICOLON)) {
            eat(PaMaToken.TOKEN_SEMICOLON);
            typeDeclList.add(parseTypeDecl());
        }
        return typeDeclList;
    }

    /**
     * Parse an {@code unary}.
     *
     * <pre>
     * unary
     *     : negation? factor
     *     ;
     *
     * negation
     *     : '-'
     *     | 'not'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseUnaryExpression() throws ParseException {
        final Location begin = getLocation();
        final UnaryOperator operator = UnaryOperator.fromToken(getToken());
        if (operator == null) {
            return parseFactorExpression();
        }

        eat(getToken());
        final Expression expression = parseFactorExpression();
        final Location end = getLocation();
        return new UnaryExpression(begin, end, operator, expression);
    }

    /**
     * Parse a {@code var_decl}.
     *
     * <pre>
     * var_decl
     *     : Identifier ':' type
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private VarDecl parseVarDecl() throws ParseException {
        final Location begin = getLocation();
        final String identifier = parseIdentifier();
        eat(PaMaToken.TOKEN_COLON);
        final Type type = parseType();
        final Location end = getLocation();
        return new VarDecl(begin, end, identifier, type);
    }

    /**
     * Parse a {@code var_decl_list}.
     *
     * <pre>
     * var_decl_list
     *     : var_decl (';' var_decl)*
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<VarDecl> parseVarDeclList() throws ParseException {
        final ArrayList<VarDecl> varDeclList = new ArrayList<VarDecl>();
        varDeclList.add(parseVarDecl());
        while (isToken(PaMaToken.TOKEN_SEMICOLON)) {
            eat(PaMaToken.TOKEN_SEMICOLON);
            varDeclList.add(parseVarDecl());
        }
        return varDeclList;
    }

    /**
     * Parse a {@code while_statement}.
     *
     * <pre>
     * while_statement
     *     : 'while' expression 'do' statement_list? 'end'
     *     ;
     * </pre>
     *
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseWhileStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_WHILE);
        final Expression condition = parseExpression();
        eat(PaMaToken.KEYWORD_DO);
        final ArrayList<Statement> statementList;
        if (isToken(PaMaToken.KEYWORD_END)) {
            statementList = new ArrayList<Statement>();
        } else {
            statementList = parseStatementList();
        }
        eat(PaMaToken.KEYWORD_END);
        final Location end = getLocation();
        return new WhileStatement(begin, end, condition, statementList);
    }

    /**
     * Parse a {@code writeln_statement}.
     *
     * <pre>
     * writeln_statement
     *     : 'writeln' '(' expression ')'
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Statement parseWritelnStatement() throws ParseException {
        final Location begin = getLocation();
        eat(PaMaToken.KEYWORD_WRITELN);
        eat(PaMaToken.BRACKET_ROUND_LEFT);
        final Expression expression = parseExpression();
        eat(PaMaToken.BRACKET_ROUND_RIGHT);
        final Location end = getLocation();
        return new WritelnStatement(begin, end, expression);
    }
}
