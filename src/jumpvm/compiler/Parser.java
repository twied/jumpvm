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

package jumpvm.compiler;

import jumpvm.ast.AstNode;
import jumpvm.exception.ParseException;

/**
 * JumpVM parser classes.
 * 
 * @param <L> Lexer class
 * @param <T> Token class
 */
public abstract class Parser<L extends Lexer<T>, T extends Token> {
    /** {@link Lexer}. */
    private final L lexer;

    /** Current token. */
    private T token;

    /**
     * Create a new parser over the given lexer.
     * 
     * @param lexer lexer
     */
    public Parser(final L lexer) {
        this.lexer = lexer;
        this.token = lexer.nextToken();
    }

    /**
     * Discard {@code expectedToken} and read next token.
     * 
     * @param expectedToken expected token
     * @throws ParseException if current token != expected token
     */
    protected final void eat(final T expectedToken) throws ParseException {
        expect(expectedToken);
        token = lexer.nextToken();
    }

    /**
     * Throws Exceptions if current token != expected token.
     * 
     * @param expectedToken expected token
     * @throws ParseException if current token != expected token
     */
    protected final void expect(final T expectedToken) throws ParseException {
        if (token == expectedToken) {
            return;
        }

        throw new ParseException(lexer.getLocation(), expectedToken, token);
    }

    /**
     * Returns last identifier.
     * 
     * @see Lexer#getIdentifier
     * @return last identifier
     */
    protected final String getIdentifier() {
        return lexer.getIdentifier();
    }

    /**
     * Returns the begin of the current token.
     * 
     * @return the begin of the current token
     */
    protected final Location getLocation() {
        return lexer.getLocation();
    }

    /**
     * Returns the current token.
     * 
     * @return the current token
     */
    protected final T getToken() {
        return token;
    }

    /**
     * Returns whether or not current token == expected Token.
     * 
     * @param expectedToken expected Token
     * @return true if current token == expected Token, false otherwise
     */
    protected final boolean isToken(final T expectedToken) {
        return token == expectedToken;
    }

    /**
     * Start parsing.
     * 
     * @return an AST tree representing the program
     * @throws ParseException on failure
     */
    public abstract AstNode<?> parse() throws ParseException;
}
