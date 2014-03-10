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

import java.io.File;
import java.io.FileNotFoundException;

import jumpvm.compiler.Lexer;
import jumpvm.compiler.LocatedReader;

/**
 * MaMachine {@link Lexer}.
 */
public class MaMaLexer extends Lexer<MaMaToken> {
    /**
     * Create a new MaMa lexer.
     * 
     * @param file source
     * @throws FileNotFoundException on failure
     */
    public MaMaLexer(final File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * Create a new MaMa lexer.
     * 
     * @param reader source
     */
    public MaMaLexer(final LocatedReader reader) {
        super(reader);
    }

    /**
     * Parse an identifier token.
     * 
     * @return next token
     */
    private MaMaToken nextIdentifier() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isLetterOrDigit(getLastChar()));

        setIdentifier(sb.toString());

        if ("else".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_ELSE;
        } else if ("head".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_HEAD;
        } else if ("if".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_IF;
        } else if ("in".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_IN;
        } else if ("isnil".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_ISNIL;
        } else if ("letrec".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_LETREC;
        } else if ("tail".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_TAIL;
        } else if ("then".equals(getIdentifier())) {
            return MaMaToken.KEYWORD_THEN;
        } else if ("true".equals(getIdentifier())) {
            setIdentifier("1");
            return MaMaToken.NUMERAL;
        } else if ("false".equals(getIdentifier())) {
            setIdentifier("0");
            return MaMaToken.NUMERAL;
        } else {
            return MaMaToken.IDENTIFIER;
        }
    }

    /**
     * Parse an numeral token.
     * 
     * @return next token
     */
    private MaMaToken nextNumeral() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isDigit(getLastChar()));

        setIdentifier(sb.toString());
        return MaMaToken.NUMERAL;
    }

    @Override
    public final MaMaToken nextToken() {
        /* remove whitespace */
        while (Character.isWhitespace(getLastChar())) {
            getNextChar();
        }

        /* end of file */
        if (getLastChar() == -1) {
            markBegin();
            return MaMaToken.EOF;
        }

        /* comments */
        if (getLastChar() == '%') {
            while (true) {
                getNextChar();
                switch (getLastChar()) {
                case -1:
                    markBegin();
                    return MaMaToken.EOF;
                case '\n':
                    return nextToken();
                default:
                    break;
                }
            }
        }

        if (Character.isLetter(getLastChar())) {
            return nextIdentifier();
        }

        if (Character.isDigit(getLastChar())) {
            return nextNumeral();
        }

        markBegin();
        final int currentChar = getLastChar();
        getNextChar();

        switch (currentChar) {
        case '.':
            return MaMaToken.TOKEN_FULLSTOP;

        case '\\':
            return MaMaToken.TOKEN_BACKSLASH;

        case ',':
            return MaMaToken.TOKEN_COMMA;

        case ';':
            return MaMaToken.TOKEN_SEMICOLON;

        case ':':
            return MaMaToken.TOKEN_COLON;

        case '[':
            return MaMaToken.BRACKET_SQUARE_LEFT;

        case ']':
            return MaMaToken.BRACKET_SQUARE_RIGHT;

        case '(':
            return MaMaToken.BRACKET_ROUND_LEFT;

        case ')':
            return MaMaToken.BRACKET_ROUND_RIGHT;

        case '-':
            return MaMaToken.TOKEN_MINUS;

        case '+':
            return MaMaToken.TOKEN_PLUS;

        case '*':
            return MaMaToken.TOKEN_STAR;

        case '/':
            return MaMaToken.TOKEN_SLASH;

        case '|':
            if (getLastChar() == '|') {
                getNextChar();
                return MaMaToken.TOKEN_OR;
            } else {
                return MaMaToken.UNKNOWN;
            }

        case '&':
            if (getLastChar() == '&') {
                getNextChar();
                return MaMaToken.TOKEN_AND;
            } else {
                return MaMaToken.UNKNOWN;
            }

        case '<':
            if (getLastChar() == '=') {
                getNextChar();
                return MaMaToken.TOKEN_LESSEQUAL;
            } else {
                return MaMaToken.TOKEN_LESS;
            }

        case '>':
            if (getLastChar() == '=') {
                getNextChar();
                return MaMaToken.TOKEN_GREATEREQUAL;
            } else {
                return MaMaToken.TOKEN_GREATER;
            }

        case '=':
            if (getLastChar() == '=') {
                getNextChar();
                return MaMaToken.TOKEN_ASSIGNMENT;
            } else {
                return MaMaToken.TOKEN_EQUALS;
            }

        case '!':
            if (getLastChar() == '=') {
                getNextChar();
                return MaMaToken.TOKEN_NOTEQUAL;
            } else {
                return MaMaToken.TOKEN_EXCLAMATIONMARK;
            }

        default:
            return MaMaToken.UNKNOWN;
        }
    }
}
