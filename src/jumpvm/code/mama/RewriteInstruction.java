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
 * Overwrite heap object.
 * 
 * <pre>
 * HP[ST[SP - m]] := HP[ST[SP]];
 * SP := SP - 1;
 * </pre>
 */
public class RewriteInstruction extends MaMaInstruction {
    /** Index. */
    private final int m;

    /**
     * Create new Rewrite instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param m index
     */
    public RewriteInstruction(final MaMaAstNode sourceNode, final int m) {
        super(sourceNode);
        this.m = m;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Overwrite heap object.";
    }

    @Override
    public final String getMnemonic() {
        return "rewrite";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(m);
    }
}
