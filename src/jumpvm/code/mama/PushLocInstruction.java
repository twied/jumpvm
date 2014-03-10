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
 * Stack pointer to value of formal parameter or local variable.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := ST[SP - j];
 * </pre>
 */
public class PushLocInstruction extends MaMaInstruction {
    /** Index. */
    private final int j;

    /**
     * Create new PushLoc instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * 
     * @param j index
     */
    public PushLocInstruction(final MaMaAstNode sourceNode, final int j) {
        super(sourceNode);
        this.j = j;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Stack pointer to value of formal parameter or local variable";
    }

    @Override
    public final String getMnemonic() {
        return "pushloc";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(j);
    }
}
