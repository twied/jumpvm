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
 * Conditional jump.
 * 
 * <pre>
 * if ST[SP] = 0 then
 *     PC := l
 * fi;
 * SP := SP - 1;
 * </pre>
 */
public class JFalseInstruction extends MaMaInstruction {
    /** Label. */
    private final Label l;

    /**
     * Create new JFalse instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param l label
     */
    public JFalseInstruction(final MaMaAstNode sourceNode, final Label l) {
        super(sourceNode);
        this.l = l;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Conditional jump (" + l.getName() + ")";
    }

    @Override
    public final String getMnemonic() {
        return "jfalse";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(l.getAddress());
    }
}
