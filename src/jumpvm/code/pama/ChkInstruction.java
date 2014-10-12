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

package jumpvm.code.pama;

import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.vm.PaMa;

/**
 * Check index.
 *
 * <pre>
 * if (STORE[SP] < p) || (STORE[SP] > q) then
 *     error("Index out of range");
 * end if
 * </pre>
 */
public class ChkInstruction extends PaMaInstruction {
    /** Lower bound. */
    private final int p;

    /** Upper bound. */
    private final int q;

    /**
     * Create a new ChkInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param p Lower bound
     * @param q Upper bound
     */
    public ChkInstruction(final PaMaAstNode sourceNode, final int p, final int q) {
        super(sourceNode);
        this.p = p;
        this.q = q;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int value = vm.peek().getIntValue();
        if ((value < p) || (value > q)) {
            throw new ExecutionException(this, "Index out of range");
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Check if index lies between " + p + " and " + q;
    }

    @Override
    public final String getMnemonic() {
        return "chk";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(p) + " " + String.valueOf(q);
    }
}
