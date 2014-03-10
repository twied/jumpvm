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
 * Create empty closure.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := new(CLOSURE: NIL, NIL);
 * </pre>
 */
public class AllocInstruction extends MaMaInstruction {
    /** NIL pointer. */
    private static final int NIL = -1;

    /** Name of the closure. */
    private final String closureName;

    /**
     * Create a new AllocInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param closureName closure name
     */
    public AllocInstruction(final MaMaAstNode sourceNode, final String closureName) {
        super(sourceNode);
        this.closureName = closureName;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create empty closure";
    }

    @Override
    public final String getMnemonic() {
        return "alloc";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
