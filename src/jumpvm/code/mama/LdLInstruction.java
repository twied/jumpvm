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
import jumpvm.memory.Label;

/**
 * Stack label.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] = l;
 * </pre>
 */
public class LdLInstruction extends MaMaInstruction {
    /** Label. */
    private final Label l;

    /**
     * Create new LdL instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param l label
     */
    public LdLInstruction(final MaMaAstNode sourceNode, final Label l) {
        super(sourceNode);
        this.l = l;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Stack label (" + l.getName() + ")";
    }

    @Override
    public final String getMnemonic() {
        return "ldl";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(l.getAddress());
    }
}
