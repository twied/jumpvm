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

import jumpvm.compiler.Token;

/** PaMa lexer token. */
public enum PaMaToken implements Token {
    /** Unknown token. */
    UNKNOWN("unknown"),

    /** End of file token. */
    EOF("eof"),

    /** Left round bracket. */
    BRACKET_ROUND_LEFT("("),

    /** Right round bracket. */
    BRACKET_ROUND_RIGHT(")"),

    /** Left square bracket. */
    BRACKET_SQUARE_LEFT("["),

    /** Right square bracket. */
    BRACKET_SQUARE_RIGHT("]"),

    /** Keyword "and". */
    KEYWORD_AND("and"),

    /** Keyword "array". */
    KEYWORD_ARRAY("array"),

    /** Keyword "begin". */
    KEYWORD_BEGIN("begin"),

    /** Keyword "boolean". */
    KEYWORD_BOOLEAN("boolean"),

    /** Keyword "case". */
    KEYWORD_CASE("case"),

    /** Keyword "do". */
    KEYWORD_DO("do"),

    /** Keyword "else". */
    KEYWORD_ELSE("else"),

    /** Keyword "end". */
    KEYWORD_END("end"),

    /** Keyword "false". */
    KEYWORD_FALSE("false"),

    /** Keyword "for". */
    KEYWORD_FOR("for"),

    /** Keyword "function". */
    KEYWORD_FUNCTION("function"),

    /** Keyword "high". */
    KEYWORD_HIGH("high"),

    /** Keyword "if". */
    KEYWORD_IF("if"),

    /** Keyword "integer". */
    KEYWORD_INTEGER("integer"),

    /** Keyword "low". */
    KEYWORD_LOW("low"),

    /** Keyword "new". */
    KEYWORD_NEW("new"),

    /** Keyword "nil". */
    KEYWORD_NIL("nil"),

    /** Keyword "not". */
    KEYWORD_NOT("not"),

    /** Keyword "of". */
    KEYWORD_OF("of"),

    /** Keyword "or". */
    KEYWORD_OR("or"),

    /** Keyword "procedure". */
    KEYWORD_PROCEDURE("procedure"),

    /** Keyword "program". */
    KEYWORD_PROGRAM("program"),

    /** Keyword "readln". */
    KEYWORD_READLN("readln"),

    /** Keyword "record". */
    KEYWORD_RECORD("record"),

    /** Keyword "repeat". */
    KEYWORD_REPEAT("repeat"),

    /** Keyword "then". */
    KEYWORD_THEN("then"),

    /** Keyword "to". */
    KEYWORD_TO("to"),

    /** Keyword "true". */
    KEYWORD_TRUE("true"),

    /** Keyword "type". */
    KEYWORD_TYPE("type"),

    /** Keyword "until". */
    KEYWORD_UNTIL("until"),

    /** Keyword "var". */
    KEYWORD_VAR("var"),

    /** Keyword "while". */
    KEYWORD_WHILE("while"),

    /** Keyword "writeln". */
    KEYWORD_WRITELN("writeln"),

    /** Token ":=". */
    TOKEN_ASSIGNMENT(":="),

    /** Token "^". */
    TOKEN_CARET("^"),

    /** Token ":". */
    TOKEN_COLON(":"),

    /** Token ",". */
    TOKEN_COMMA(","),

    /** Token "=". */
    TOKEN_EQUAL("="),

    /** Token ">". */
    TOKEN_GREATER(">"),

    /** Token ">=". */
    TOKEN_GREATEREQUAL(">="),

    /** Token "&lt;". */
    TOKEN_LESS("<"),

    /** Token "&lt;=". */
    TOKEN_LESSEQUAL("<="),

    /** Token "-". */
    TOKEN_MINUS("-"),

    /** Token "/=". */
    TOKEN_NOTEQUAL("/="),

    /** Token ".". */
    TOKEN_PERIOD("."),

    /** Token "+". */
    TOKEN_PLUS("+"),

    /** Token "..". */
    TOKEN_RANGE(".."),

    /** Token ";". */
    TOKEN_SEMICOLON(";"),

    /** Token "/". */
    TOKEN_SLASH("/"),

    /** Token "*". */
    TOKEN_STAR("*"),

    /** Identifier token. */
    IDENTIFIER("identifier"),

    /** Numeral token. */
    NUMERAL("numeral");

    /** Display name. */
    private final String name;

    /**
     * Create new PaMaToken.
     *
     * @param name display name
     */
    private PaMaToken(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
