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

import java.io.File;
import java.io.FileNotFoundException;

import jumpvm.compiler.Lexer;
import jumpvm.compiler.LocatedReader;

/** JumpVM WiMa lexer. */
public class WiMaLexer extends Lexer<WiMaToken> {
    /**
     * Create a new WiMa lexer.
     * 
     * @param file source
     * @throws FileNotFoundException on failure
     */
    public WiMaLexer(final File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * Create a new WiMa lexer.
     * 
     * @param reader source
     */
    public WiMaLexer(final LocatedReader reader) {
        super(reader);
    }

    /**
     * Parse an atom token.
     * 
     * @return next token
     */
    private WiMaToken nextAtom() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isLetterOrDigit(getLastChar()));

        setIdentifier(sb.toString());

        return WiMaToken.ATOM;
    }

    /**
     * Parse an numeral token.
     * 
     * @return next token
     */
    private WiMaToken nextNumeral() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isDigit(getLastChar()));

        setIdentifier(sb.toString());

        return WiMaToken.NUMERAL;
    }

    @Override
    public final WiMaToken nextToken() {
        /* remove whitespace */
        while (Character.isWhitespace(getLastChar())) {
            getNextChar();
        }

        /* end of file */
        if (getLastChar() == -1) {
            markBegin();
            return WiMaToken.EOF;
        }

        /* comments */
        if (getLastChar() == '%') {
            while (true) {
                getNextChar();
                switch (getLastChar()) {
                case -1:
                    markBegin();
                    return WiMaToken.EOF;
                case '\n':
                    return nextToken();
                default:
                    break;
                }
            }
        }

        if ((getLastChar() >= 'a') && (getLastChar() <= 'z')) {
            return nextAtom();
        }

        if ((getLastChar() >= 'A') && (getLastChar() <= 'Z')) {
            return nextVariable();
        }

        if ((getLastChar() >= '0') && (getLastChar() <= '9')) {
            return nextNumeral();
        }

        markBegin();
        final int currentChar = getLastChar();
        getNextChar();

        switch (currentChar) {
        case '.':
            return WiMaToken.TOKEN_FULLSTOP;

        case ',':
            return WiMaToken.TOKEN_COMMA;

        case '(':
            return WiMaToken.BRACKET_LEFT;

        case ')':
            return WiMaToken.BRACKET_RIGHT;

        case ':':
            if (getLastChar() == '-') {
                getNextChar();
                return WiMaToken.TOKEN_IF;
            } else {
                return WiMaToken.UNKNOWN;
            }
        case '?':
            if (getLastChar() == '-') {
                getNextChar();
                return WiMaToken.TOKEN_PROMPT;
            } else {
                return WiMaToken.UNKNOWN;
            }

        default:
            return WiMaToken.UNKNOWN;
        }
    }

    /**
     * Parse an variable token.
     * 
     * @return next token
     */
    private WiMaToken nextVariable() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isLetterOrDigit(getLastChar()));

        setIdentifier(sb.toString());

        return WiMaToken.VARIABLE;
    }
}
