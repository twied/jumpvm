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

package jumpvm.code.mama;

import jumpvm.ast.mama.MaMaAstNode;

/**
 * Load basic object from heap to stack.
 * 
 * <pre>
 * if HP[ST[SP]].tag != BASIC then
 *     error
 * fi;
 * ST[SP] := HP[ST[SP]].value;
 * </pre>
 */
public class GetBasicInstruction extends MaMaInstruction {
    /**
     * Create a new GetBasicInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public GetBasicInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Load basic object from heap to stack";
    }

    @Override
    public final String getMnemonic() {
        return "getbasic";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
