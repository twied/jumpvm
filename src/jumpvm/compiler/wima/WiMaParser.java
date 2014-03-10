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

package jumpvm.compiler.wima;

import java.util.ArrayList;

import jumpvm.ast.wima.Atom;
import jumpvm.ast.wima.Clause;
import jumpvm.ast.wima.Numeral;
import jumpvm.ast.wima.Predicate;
import jumpvm.ast.wima.Program;
import jumpvm.ast.wima.Query;
import jumpvm.ast.wima.Structure;
import jumpvm.ast.wima.Term;
import jumpvm.ast.wima.Variable;
import jumpvm.compiler.Location;
import jumpvm.compiler.Parser;
import jumpvm.exception.ParseException;

/**
 * WiMachine {@link Parser}.
 * 
 * Grammar is in <a href="http://www.antlr.org">antlr</a> format.
 */
public class WiMaParser extends Parser<WiMaLexer, WiMaToken> {
    /**
     * Create a new WiMaParser over the given lexer.
     * 
     * @param lexer lexer
     */
    public WiMaParser(final WiMaLexer lexer) {
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
     * Parse an {@code Atom}.
     * 
     * <pre>
     * Atom
     *     : LowercaseLetter Character*
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Atom parseAtom() throws ParseException {
        final Location begin = getLocation();
        final String identifier = getIdentifier();
        eat(WiMaToken.ATOM);
        final Location end = getLocation();
        return new Atom(begin, end, identifier);
    }

    /**
     * Parse an {@code clause}.
     * 
     * <pre>
     * clause
     *     : predicate (':-' predicate_list)? '.'
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Clause parseClause() throws ParseException {
        final Location begin = getLocation();
        final Predicate predicate = parsePredicate();
        final ArrayList<Predicate> predicateList;

        if (isToken(WiMaToken.TOKEN_IF)) {
            eat(WiMaToken.TOKEN_IF);
            predicateList = parsePredicateList();
        } else {
            predicateList = new ArrayList<Predicate>();
        }
        eat(WiMaToken.TOKEN_FULLSTOP);
        final Location end = getLocation();
        return new Clause(begin, end, predicate, predicateList);
    }

    /**
     * Parse a {@code Numeral}.
     * 
     * <pre>
     * Numeral
     *     : Digit+
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Numeral parseNumeral() throws ParseException {
        final Location begin = getLocation();
        final String identifier = getIdentifier();
        eat(WiMaToken.NUMERAL);
        final Location end = getLocation();
        return new Numeral(begin, end, identifier);
    }

    /**
     * Parse a {@code Predicate}.
     * 
     * <pre>
     * predicate
     *     : Atom
     *     | structure
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Predicate parsePredicate() throws ParseException {
        final Atom atom = parseAtom();
        if (isToken(WiMaToken.BRACKET_LEFT)) {
            return parseStructure(atom);
        } else {
            return atom;
        }
    }

    /**
     * Parse a {@code predicate_list}.
     * 
     * <pre>
     * predicate_list
     *     : predicate (',' predicate)*
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private ArrayList<Predicate> parsePredicateList() throws ParseException {
        final ArrayList<Predicate> predicateList = new ArrayList<Predicate>();
        predicateList.add(parsePredicate());
        while (isToken(WiMaToken.TOKEN_COMMA)) {
            eat(WiMaToken.TOKEN_COMMA);
            predicateList.add(parsePredicate());
        }
        return predicateList;
    }

    /**
     * Parse a {@code program}.
     * 
     * <pre>
     * program
     *     : clause* query EOF
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Program parseProgram() throws ParseException {
        final Location begin = getLocation();
        final ArrayList<Clause> clauses = new ArrayList<Clause>();
        while (!isToken(WiMaToken.TOKEN_PROMPT)) {
            clauses.add(parseClause());
        }
        final Query query = parseQuery();
        expect(WiMaToken.EOF);
        final Location end = getLocation();
        return new Program(begin, end, clauses, query);
    }

    /**
     * Parse a {@code query}.
     * 
     * <pre>
     * query
     *     : predicate (':-' predicate_list)? '.'
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Query parseQuery() throws ParseException {
        final Location begin = getLocation();
        eat(WiMaToken.TOKEN_PROMPT);
        final ArrayList<Predicate> predicatelist = parsePredicateList();
        eat(WiMaToken.TOKEN_FULLSTOP);
        final Location end = getLocation();
        return new Query(begin, end, predicatelist);
    }

    /**
     * Parse a {@code structure}.
     * 
     * <pre>
     * structure
     *     : Atom '(' term (',' term)* ')'
     *     ;
     * </pre>
     * 
     * @param atom lookahead symbol
     * @return next AST node
     * @throws ParseException on failure
     */
    private Structure parseStructure(final Atom atom) throws ParseException {
        eat(WiMaToken.BRACKET_LEFT);
        final ArrayList<Term> termList = new ArrayList<Term>();
        termList.add(parseTerm());
        while (isToken(WiMaToken.TOKEN_COMMA)) {
            eat(WiMaToken.TOKEN_COMMA);
            termList.add(parseTerm());
        }
        eat(WiMaToken.BRACKET_RIGHT);
        final Location end = getLocation();
        return new Structure(atom.getBegin(), end, atom, termList);
    }

    /**
     * Parse a {@code term}.
     * 
     * <pre>
     * term
     *     : predicate
     *     | Variable
     *     | Numeral
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Term parseTerm() throws ParseException {
        if (isToken(WiMaToken.NUMERAL)) {
            return parseNumeral();
        } else if (isToken(WiMaToken.VARIABLE)) {
            return parseVariable();
        } else {
            return parsePredicate();
        }
    }

    /**
     * Parse a {@code Variable}.
     * 
     * <pre>
     * Variable
     *     : UppercaseLetter Character*
     *     ;
     * </pre>
     * 
     * @return next AST node
     * @throws ParseException on failure
     */
    private Variable parseVariable() throws ParseException {
        final Location begin = getLocation();
        final String identifier = getIdentifier();
        eat(WiMaToken.VARIABLE);
        final Location end = getLocation();
        return new Variable(begin, end, identifier);
    }
}
