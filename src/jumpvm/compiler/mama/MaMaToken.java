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

import jumpvm.compiler.Token;

/**
 * MaMachine {@link Token}.
 */
public enum MaMaToken implements Token {
    /** Unknown token. */
    UNKNOWN("unknown"),

    /** End of file token. */
    EOF("eof"),

    /** Bracket "(". */
    BRACKET_ROUND_LEFT("("),

    /** Bracket ")". */
    BRACKET_ROUND_RIGHT(")"),

    /** Bracket "[". */
    BRACKET_SQUARE_LEFT("["),

    /** Bracket "]". */
    BRACKET_SQUARE_RIGHT("]"),

    /** Keyword "else". */
    KEYWORD_ELSE("else"),

    /** Keyword "head". */
    KEYWORD_HEAD("head"),

    /** Keyword "if". */
    KEYWORD_IF("if"),

    /** Keyword "in". */
    KEYWORD_IN("in"),

    /** Keyword "isnil". */
    KEYWORD_ISNIL("isnil"),

    /** Keyword "letrec". */
    KEYWORD_LETREC("letrec"),

    /** Keyword "tail". */
    KEYWORD_TAIL("tail"),

    /** Keyword "then". */
    KEYWORD_THEN("then"),

    /** Token "&&". */
    TOKEN_AND("&&"),

    /** Token "==". */
    TOKEN_ASSIGNMENT("=="),

    /** Token "\". */
    TOKEN_BACKSLASH("\\"),

    /** Token ":". */
    TOKEN_COLON(":"),

    /** Token ",". */
    TOKEN_COMMA(","),

    /** Token "=". */
    TOKEN_EQUALS("="),

    /** Token "!". */
    TOKEN_EXCLAMATIONMARK("!"),

    /** Token ".". */
    TOKEN_FULLSTOP("."),

    /** Token ">". */
    TOKEN_GREATER(">"),

    /** Token ">=". */
    TOKEN_GREATEREQUAL(">="),

    /** Token "<". */
    TOKEN_LESS("<"),

    /** Token "<=". */
    TOKEN_LESSEQUAL("<="),

    /** Token "-". */
    TOKEN_MINUS("-"),

    /** Token "!=". */
    TOKEN_NOTEQUAL("!="),

    /** Token "||". */
    TOKEN_OR("||"),

    /** Token "+". */
    TOKEN_PLUS("+"),

    /** Token ";". */
    TOKEN_SEMICOLON(";"),

    /** Token "/". */
    TOKEN_SLASH("/"),

    /** Token "*". */
    TOKEN_STAR("*"),

    /** Identifier. */
    IDENTIFIER("identifier"),

    /** Numeral. */
    NUMERAL("numeral");

    /** Display name. */
    private final String name;

    /**
     * Create new MaMaToken.
     * 
     * @param name display name
     */
    private MaMaToken(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
