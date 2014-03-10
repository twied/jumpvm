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

import java.util.ArrayList;

import jumpvm.ast.bfma.BfMaAstNode;
import jumpvm.ast.bfma.Input;
import jumpvm.ast.bfma.Left;
import jumpvm.ast.bfma.Loop;
import jumpvm.ast.bfma.Minus;
import jumpvm.ast.bfma.Output;
import jumpvm.ast.bfma.Plus;
import jumpvm.ast.bfma.Program;
import jumpvm.ast.bfma.Right;
import jumpvm.compiler.Location;
import jumpvm.compiler.Parser;
import jumpvm.exception.ParseException;

/**
 * BfMachine {@link Parser}.
 */
public class BfMaParser extends Parser<BfMaLexer, BfMaToken> {
    /**
     * Create a new BfMaParser.
     * 
     * @param lexer BfMaLexer to read the tokens
     */
    public BfMaParser(final BfMaLexer lexer) {
        super(lexer);
    }

    @Override
    public final Program parse() throws ParseException {
        try {
            final Location begin = getLocation();
            final ArrayList<BfMaAstNode> characters = new ArrayList<BfMaAstNode>();
            while (!isToken(BfMaToken.EOF)) {
                characters.add(parseCharacter());
            }
            final Location end = getLocation();
            return new Program(begin, end, characters);
        } catch (final RuntimeException e) {
            throw new ParseException(getLocation(), e);
        }
    }

    /**
     * Parse the next instruction.
     * 
     * @return the next instruction
     * @throws ParseException on failure
     */
    private BfMaAstNode parseCharacter() throws ParseException {
        final Location begin = getLocation();
        switch (getToken()) {
        case BRACKET_ANGLE_LEFT:
            eat(BfMaToken.BRACKET_ANGLE_LEFT);
            return new Left(begin, getLocation());

        case BRACKET_ANGLE_RIGHT:
            eat(BfMaToken.BRACKET_ANGLE_RIGHT);
            return new Right(begin, getLocation());

        case TOKEN_COMMA:
            eat(BfMaToken.TOKEN_COMMA);
            return new Input(begin, getLocation());

        case TOKEN_FULLSTOP:
            eat(BfMaToken.TOKEN_FULLSTOP);
            return new Output(begin, getLocation());

        case TOKEN_MINUS:
            eat(BfMaToken.TOKEN_MINUS);
            return new Minus(begin, getLocation());

        case TOKEN_PLUS:
            eat(BfMaToken.TOKEN_PLUS);
            return new Plus(begin, getLocation());

        case BRACKET_SQUARE_LEFT:
            return parseLoop();

        default:
            throw new ParseException(begin, null, getToken());
        }
    }

    /**
     * Parse a loop ("{@code []}").
     * 
     * @return a {@link Loop}
     * @throws ParseException on failure
     */
    private BfMaAstNode parseLoop() throws ParseException {
        final ArrayList<BfMaAstNode> characters = new ArrayList<BfMaAstNode>();

        final Location begin = getLocation();
        eat(BfMaToken.BRACKET_SQUARE_LEFT);

        while (!isToken(BfMaToken.BRACKET_SQUARE_RIGHT)) {
            if (isToken(BfMaToken.EOF)) {
                throw new ParseException(getLocation(), BfMaToken.BRACKET_SQUARE_RIGHT, BfMaToken.EOF);
            }
            characters.add(parseCharacter());
        }

        eat(BfMaToken.BRACKET_SQUARE_RIGHT);
        final Location end = getLocation();

        return new Loop(begin, end, characters);
    }
}
