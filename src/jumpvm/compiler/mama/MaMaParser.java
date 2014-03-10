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

package jumpvm.compiler.mama;

import java.util.ArrayList;

import jumpvm.ast.mama.BinOpExpression;
import jumpvm.ast.mama.BinOpExpression.BinaryOperator;
import jumpvm.ast.mama.CallExpression;
import jumpvm.ast.mama.ConsExpression;
import jumpvm.ast.mama.Expression;
import jumpvm.ast.mama.HeadExpression;
import jumpvm.ast.mama.IdentifierExpression;
import jumpvm.ast.mama.IfExpression;
import jumpvm.ast.mama.IsNilExpression;
import jumpvm.ast.mama.LambdaExpression;
import jumpvm.ast.mama.LetrecExpression;
import jumpvm.ast.mama.ListExpression;
import jumpvm.ast.mama.NumeralExpression;
import jumpvm.ast.mama.Program;
import jumpvm.ast.mama.TailExpression;
import jumpvm.ast.mama.UnOpExpression;
import jumpvm.ast.mama.UnOpExpression.UnaryOperator;
import jumpvm.ast.mama.VariableDeclaration;
import jumpvm.compiler.Location;
import jumpvm.compiler.Parser;
import jumpvm.exception.ParseException;

/**
 * MaMachine {@link Parser}.
 * 
 * Grammar is in <a href="http://www.antlr.org">antlr</a> format.
 */
public class MaMaParser extends Parser<MaMaLexer, MaMaToken> {
    /**
     * Create a new MaMaParser over the given lexer.
     * 
     * @param lexer lexer
     */
    public MaMaParser(final MaMaLexer lexer) {
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
     * Parse a {@code atomicExpression}.
     * 
     * <pre>
     * atomicExpression
     *     : '(' expression ')'
     *     | listExpression
     *     | Numeral
     *     | Identifier
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseAtomicExpression() throws ParseException {
        final Location begin = getLocation();
        if (isToken(MaMaToken.BRACKET_ROUND_LEFT)) {
            eat(MaMaToken.BRACKET_ROUND_LEFT);
            final Expression expression = parseExpression();
            eat(MaMaToken.BRACKET_ROUND_RIGHT);
            return expression;
        } else if (isToken(MaMaToken.BRACKET_SQUARE_LEFT)) {
            return parseListExpression();
        } else if (isToken(MaMaToken.NUMERAL)) {
            final Integer numeral = parseNumeral();
            final Location end = getLocation();
            return new NumeralExpression(begin, end, numeral);
        } else {
            final String identifier = parseIdentifier();
            final Location end = getLocation();
            return new IdentifierExpression(begin, end, identifier);
        }
    }

    /**
     * Parse a {@code binOpExpression}.
     * 
     * <pre>
     * binOpExpression
     *     : unOpExpression (BinOp unOpExpression)?
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseBinOpExpression() throws ParseException {
        final Location begin = getLocation();
        final Expression lhs = parseUnOpExpression();
        final BinaryOperator binOp = BinaryOperator.fromToken(getToken());
        if (binOp != null) {
            eat(getToken());
            final Expression rhs = parseUnOpExpression();
            final Location end = getLocation();
            return new BinOpExpression(begin, end, binOp, lhs, rhs);
        } else if (isToken(MaMaToken.TOKEN_COLON)) {
            eat(MaMaToken.TOKEN_COLON);
            final Expression rhs = parseUnOpExpression();
            final Location end = getLocation();
            return new ConsExpression(begin, end, lhs, rhs);
        } else {
            return lhs;
        }
    }

    /**
     * Parse a {code callExpression}.
     * 
     * <pre>
     * callExpression
     *     : isNilExpression
     *     | headExpression
     *     | tailExpression
     *     | atomicExpression atomicExpression*
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseCallExpression() throws ParseException {
        if (isToken(MaMaToken.KEYWORD_ISNIL)) {
            return parseIsnilExpression();
        } else if (isToken(MaMaToken.KEYWORD_HEAD)) {
            return parseHeadExpression();
        } else if (isToken(MaMaToken.KEYWORD_TAIL)) {
            return parseTailExpression();
        }

        final Location begin = getLocation();
        final Expression body = parseAtomicExpression();
        final ArrayList<Expression> arguments = new ArrayList<Expression>();

        while (true) {
            if (!isToken(MaMaToken.NUMERAL) && !isToken(MaMaToken.IDENTIFIER) && !isToken(MaMaToken.BRACKET_ROUND_LEFT) && !isToken(MaMaToken.BRACKET_SQUARE_LEFT)) {
                break;
            }

            arguments.add(parseAtomicExpression());
        }

        if (arguments.size() == 0) {
            return body;
        } else {
            final Location end = getLocation();
            return new CallExpression(begin, end, body, arguments);
        }
    }

    /**
     * Parse an {@code expression}.
     * 
     * <pre>
     * expression
     *     : lambdaExpression
     *     | letrecExpression
     *     | ifExpression
     *     | binOpExpression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseExpression() throws ParseException {
        if (isToken(MaMaToken.TOKEN_BACKSLASH)) {
            return parseLambdaExpression();
        } else if (isToken(MaMaToken.KEYWORD_LETREC)) {
            return parseLetrecExpression();
        } else if (isToken(MaMaToken.KEYWORD_IF)) {
            return parseIfExpression();
        } else {
            return parseBinOpExpression();
        }
    }

    /**
     * Parse an {@code headExpression}.
     * 
     * <pre>
     * headExpression
     *     : 'head' atomicExpression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private HeadExpression parseHeadExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.KEYWORD_HEAD);
        final Expression expression = parseAtomicExpression();
        final Location end = getLocation();
        return new HeadExpression(begin, end, expression);
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
        eat(MaMaToken.IDENTIFIER);
        return identifier;
    }

    /**
     * Parse an {@code ifExpression}.
     * 
     * <pre>
     * ifExpression
     *     : 'if' expression 'then' expression 'else' expression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private IfExpression parseIfExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.KEYWORD_IF);
        final Expression condition = parseExpression();
        eat(MaMaToken.KEYWORD_THEN);
        final Expression thenExpression = parseExpression();
        eat(MaMaToken.KEYWORD_ELSE);
        final Expression elseExpression = parseExpression();
        final Location end = getLocation();
        return new IfExpression(begin, end, condition, thenExpression, elseExpression);
    }

    /**
     * Parse an {@code isNilExpression}.
     * 
     * <pre>
     * isNilExpression
     *     : 'isnil' atomicExpression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private IsNilExpression parseIsnilExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.KEYWORD_ISNIL);
        final Expression expression = parseAtomicExpression();
        final Location end = getLocation();
        return new IsNilExpression(begin, end, expression);
    }

    /**
     * Parse a {@code lambdaExpression}.
     * 
     * <pre>
     * lambdaExpression
     *     : '\\' Identifier* '.' expression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private LambdaExpression parseLambdaExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.TOKEN_BACKSLASH);

        final ArrayList<String> identifiers = new ArrayList<String>();
        while (!isToken(MaMaToken.TOKEN_FULLSTOP)) {
            identifiers.add(parseIdentifier());
        }

        eat(MaMaToken.TOKEN_FULLSTOP);
        final Expression expression = parseExpression();
        final Location end = getLocation();

        return new LambdaExpression(begin, end, identifiers, expression);
    }

    /**
     * Parse a {@code letrecExpression}.
     * 
     * <pre>
     * letrecExpression
     *     : 'letrec' variableDeclaration (';' variableDeclaration)* 'in' expression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private LetrecExpression parseLetrecExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.KEYWORD_LETREC);
        final ArrayList<VariableDeclaration> declarations = new ArrayList<VariableDeclaration>();
        declarations.add(parseVariableDeclaration());
        while (isToken(MaMaToken.TOKEN_SEMICOLON)) {
            eat(MaMaToken.TOKEN_SEMICOLON);
            declarations.add(parseVariableDeclaration());
        }
        eat(MaMaToken.KEYWORD_IN);
        final Expression expression = parseExpression();
        final Location end = getLocation();
        return new LetrecExpression(begin, end, declarations, expression);
    }

    /**
     * Parse a {@code listExpression}.
     * 
     * <pre>
     * listExpression
     *     : '[' (expression (',' expression)*)? ']'
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private ListExpression parseListExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.BRACKET_SQUARE_LEFT);

        final ArrayList<Expression> expressions;
        if (isToken(MaMaToken.BRACKET_SQUARE_RIGHT)) {
            expressions = null;
        } else {
            expressions = new ArrayList<Expression>();
            expressions.add(parseExpression());
            while (isToken(MaMaToken.TOKEN_COMMA)) {
                eat(MaMaToken.TOKEN_COMMA);
                expressions.add(parseExpression());
            }
        }
        eat(MaMaToken.BRACKET_SQUARE_RIGHT);
        final Location end = getLocation();

        return new ListExpression(begin, end, expressions);
    }

    /**
     * Parse a {@code Numeral}.
     * 
     * <pre>
     * Numeral
     *     : Digit Digit*
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Integer parseNumeral() throws ParseException {
        final String identifier = getIdentifier();
        final Integer integer;

        try {
            integer = new Integer(identifier);
        } catch (final NumberFormatException e) {
            throw new ParseException(getLocation(), MaMaToken.NUMERAL, identifier + " (" + e + ")");
        }

        eat(MaMaToken.NUMERAL);
        return integer;
    }

    /**
     * Parse a {@code program}.
     * 
     * <pre>
     * program
     *     : expression EOF
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Program parseProgram() throws ParseException {
        final Location begin = getLocation();
        final Expression expression = parseExpression();
        expect(MaMaToken.EOF);
        final Location end = getLocation();
        return new Program(begin, end, expression);
    }

    /**
     * Parse a {@code tailExpression}.
     * 
     * <pre>
     * tailExpression
     *     : 'tail' atomicExpression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private TailExpression parseTailExpression() throws ParseException {
        final Location begin = getLocation();
        eat(MaMaToken.KEYWORD_TAIL);
        final Expression expression = parseAtomicExpression();
        final Location end = getLocation();
        return new TailExpression(begin, end, expression);
    }

    /**
     * Parse an {@code unOpExpression}.
     * 
     * <pre>
     * unOpExpression
     *     : UnOp? callExpression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Expression parseUnOpExpression() throws ParseException {
        final Location begin = getLocation();
        final UnaryOperator unOp = UnaryOperator.fromToken(getToken());
        if (unOp == null) {
            return parseCallExpression();
        }

        eat(getToken());
        final Expression expression = parseCallExpression();
        final Location end = getLocation();
        return new UnOpExpression(begin, end, unOp, expression);
    }

    /**
     * Parse a {@code variableDeclaration}.
     * 
     * <pre>
     * variableDeclaration
     *     : Identifier '==' expression
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private VariableDeclaration parseVariableDeclaration() throws ParseException {
        final Location begin = getLocation();
        final String identifier = parseIdentifier();
        eat(MaMaToken.TOKEN_ASSIGNMENT);
        final Expression expression = parseExpression();
        final Location end = getLocation();
        return new VariableDeclaration(begin, end, identifier, expression);
    }
}
