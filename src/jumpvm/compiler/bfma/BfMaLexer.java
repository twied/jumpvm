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

import java.io.File;
import java.io.FileNotFoundException;

import jumpvm.compiler.Lexer;
import jumpvm.compiler.LocatedReader;

/**
 * BfMachine {@link Lexer}.
 */
public class BfMaLexer extends Lexer<BfMaToken> {
    /**
     * Create a new BfMa lexer.
     * 
     * @param file source
     * @throws FileNotFoundException on failure
     */
    public BfMaLexer(final File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * Create a new BfMa lexer.
     * 
     * @param reader source
     */
    public BfMaLexer(final LocatedReader reader) {
        super(reader);
    }

    @Override
    public final BfMaToken nextToken() {
        /* end of file */
        if (getLastChar() == -1) {
            markBegin();
            return BfMaToken.EOF;
        }

        markBegin();
        final int currentChar = getLastChar();
        getNextChar();

        switch (currentChar) {
        case '[':
            return BfMaToken.BRACKET_SQUARE_LEFT;

        case ']':
            return BfMaToken.BRACKET_SQUARE_RIGHT;

        case '<':
            return BfMaToken.BRACKET_ANGLE_LEFT;

        case '>':
            return BfMaToken.BRACKET_ANGLE_RIGHT;

        case ',':
            return BfMaToken.TOKEN_COMMA;

        case '.':
            return BfMaToken.TOKEN_FULLSTOP;

        case '-':
            return BfMaToken.TOKEN_MINUS;

        case '+':
            return BfMaToken.TOKEN_PLUS;

        default:
            return nextToken();
        }
    }
}
