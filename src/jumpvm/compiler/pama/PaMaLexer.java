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

import java.io.File;
import java.io.FileNotFoundException;

import jumpvm.compiler.Lexer;
import jumpvm.compiler.LocatedReader;

/** JumpVM PaMa lexer. */
public class PaMaLexer extends Lexer<PaMaToken> {
    /**
     * Create a new PaMa lexer.
     * 
     * @param file source
     * @throws FileNotFoundException on failure
     */
    public PaMaLexer(final File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * Create a new PaMa lexer.
     * 
     * @param reader source
     */
    public PaMaLexer(final LocatedReader reader) {
        super(reader);
    }

    /**
     * Parse an identifier token.
     * 
     * @return next token
     */
    private PaMaToken nextIdentifier() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isLetterOrDigit(getLastChar()));

        setIdentifier(sb.toString());

        if ("and".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_AND;
        } else if ("array".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_ARRAY;
        } else if ("begin".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_BEGIN;
        } else if ("boolean".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_BOOLEAN;
        } else if ("case".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_CASE;
        } else if ("do".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_DO;
        } else if ("else".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_ELSE;
        } else if ("end".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_END;
        } else if ("false".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_FALSE;
        } else if ("for".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_FOR;
        } else if ("function".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_FUNCTION;
        } else if ("high".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_HIGH;
        } else if ("if".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_IF;
        } else if ("integer".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_INTEGER;
        } else if ("low".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_LOW;
        } else if ("new".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_NEW;
        } else if ("nil".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_NIL;
        } else if ("not".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_NOT;
        } else if ("of".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_OF;
        } else if ("or".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_OR;
        } else if ("procedure".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_PROCEDURE;
        } else if ("program".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_PROGRAM;
        } else if ("readln".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_READLN;
        } else if ("record".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_RECORD;
        } else if ("repeat".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_REPEAT;
        } else if ("then".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_THEN;
        } else if ("to".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_TO;
        } else if ("true".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_TRUE;
        } else if ("type".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_TYPE;
        } else if ("until".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_UNTIL;
        } else if ("var".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_VAR;
        } else if ("while".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_WHILE;
        } else if ("writeln".equals(getIdentifier())) {
            return PaMaToken.KEYWORD_WRITELN;
        } else {
            return PaMaToken.IDENTIFIER;
        }
    }

    /**
     * Parse an numeral token.
     * 
     * @return next token
     */
    private PaMaToken nextNumeral() {
        final StringBuilder sb = new StringBuilder();
        markBegin();
        do {
            sb.appendCodePoint(getLastChar());
            getNextChar();
        } while (Character.isDigit(getLastChar()));

        setIdentifier(sb.toString());
        return PaMaToken.NUMERAL;
    }

    @Override
    public final PaMaToken nextToken() {
        /* remove whitespace */
        while (Character.isWhitespace(getLastChar())) {
            getNextChar();
        }

        /* end of file */
        if (getLastChar() == -1) {
            markBegin();
            return PaMaToken.EOF;
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
        case '[':
            return PaMaToken.BRACKET_SQUARE_LEFT;

        case ']':
            return PaMaToken.BRACKET_SQUARE_RIGHT;

        case '^':
            return PaMaToken.TOKEN_CARET;

        case ',':
            return PaMaToken.TOKEN_COMMA;

        case ';':
            return PaMaToken.TOKEN_SEMICOLON;

        case '=':
            return PaMaToken.TOKEN_EQUAL;

        case '+':
            return PaMaToken.TOKEN_PLUS;

        case '-':
            return PaMaToken.TOKEN_MINUS;

        case '*':
            return PaMaToken.TOKEN_STAR;

        case ':':
            if (getLastChar() == '=') {
                getNextChar();
                return PaMaToken.TOKEN_ASSIGNMENT;
            } else {
                return PaMaToken.TOKEN_COLON;
            }

        case '>':
            if (getLastChar() == '=') {
                getNextChar();
                return PaMaToken.TOKEN_GREATEREQUAL;
            } else {
                return PaMaToken.TOKEN_GREATER;
            }

        case '<':
            if (getLastChar() == '=') {
                getNextChar();
                return PaMaToken.TOKEN_LESSEQUAL;
            } else {
                return PaMaToken.TOKEN_LESS;
            }

        case '/':
            if (getLastChar() == '=') {
                getNextChar();
                return PaMaToken.TOKEN_NOTEQUAL;
            } else {
                return PaMaToken.TOKEN_SLASH;
            }

        case '.':
            if (getLastChar() == '.') {
                getNextChar();
                return PaMaToken.TOKEN_RANGE;
            } else {
                return PaMaToken.TOKEN_PERIOD;
            }

        case '(':
            if (getLastChar() == '*') {
                int charA = ' ';
                int charB = ' ';

                while ((charA != '*') && (charB != ')')) {
                    charA = charB;
                    charB = getNextChar();
                }

                getNextChar();
                return nextToken();
            } else {
                return PaMaToken.BRACKET_ROUND_LEFT;
            }

        case ')':
            return PaMaToken.BRACKET_ROUND_RIGHT;

        default:
            return PaMaToken.UNKNOWN;
        }
    }
}
