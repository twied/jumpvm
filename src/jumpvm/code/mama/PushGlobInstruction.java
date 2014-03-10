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
 * Put reference to global on the stack.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := HP[GP].v[j];
 * </pre>
 */
public class PushGlobInstruction extends MaMaInstruction {
    /** Index. */
    private final int j;

    /** Global name. */
    private final String name;

    /**
     * Create new PushGlob instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param j index
     * @param name global name
     */
    public PushGlobInstruction(final MaMaAstNode sourceNode, final int j, final String name) {
        super(sourceNode);
        this.j = j;
        this.name = name;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Put reference to global " + name + " on the stack";
    }

    @Override
    public final String getMnemonic() {
        return "pushglob";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(j);
    }
}
