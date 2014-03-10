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

package jumpvm.compiler.bfma;

import jumpvm.compiler.Token;

/**
 * BfMachine {@link Token}.
 */
public enum BfMaToken implements Token {
    /** Unknown token. */
    UNKNOWN("unknown"),

    /** End of file token. */
    EOF("eof"),

    /** Bracket angle left. */
    BRACKET_ANGLE_LEFT("<"),

    /** Bracket angle right. */
    BRACKET_ANGLE_RIGHT(">"),

    /** Bracket square left. */
    BRACKET_SQUARE_LEFT("["),

    /** Bracket square right. */
    BRACKET_SQUARE_RIGHT("]"),

    /** Token ",". */
    TOKEN_COMMA(","),

    /** Token ".". */
    TOKEN_FULLSTOP("."),

    /** Token "-". */
    TOKEN_MINUS("-"),

    /** Token "+". */
    TOKEN_PLUS("+");

    /** Display name. */
    private final String name;

    /**
     * Create new BfMaToken.
     * 
     * @param name display name
     */
    private BfMaToken(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
