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
 * Test number of arguments.
 * 
 * <pre>
 * if SP - FP < n then
 *     h := ST[FP - 2];
 *     ST[FP - 2] := new(FUNVAL: PC - 1,
 *         new(VECTOR: [ST[FP + 1], ST[FP + 2], ... , ST[SP]]), GP);
 *     GP := ST[FP];
 *     SP := FP - 2;
 *     FP := ST[FP -1];
 *     PC := h;
 * fi
 * </pre>
 */
public class TargInstruction extends MaMaInstruction {
    /** Number of arguments. */
    private final int n;

    /** Function name. */
    private final String name;

    /**
     * Create new Targ instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param n number of arguments
     * @param name function name
     */
    public TargInstruction(final MaMaAstNode sourceNode, final int n, final String name) {
        super(sourceNode);
        this.n = n;
        this.name = name;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Test number of arguments";
    }

    @Override
    public final String getMnemonic() {
        return "targ";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(n);
    }
}
