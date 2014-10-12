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
 * Check index for dimension.
 *
 * <pre>
 * if (STORE[SP] < STORE[STORE[SP - 3] + 2 * dim + 1] || STORE[SP] > STORE[STORE[SP - 3] + 2 * dim + 2]) then
 *     error("Index out of range");
 * end if
 * </pre>
 */
public class ChdInstruction extends PaMaInstruction {
    /** Relative address of array descriptor. */
    private static final int DESCRIPTOR_ADDRESS = 3;

    /** Dimension. */
    private final int d;

    /**
     * Create a new ChdInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param d Dimension
     */
    public ChdInstruction(final PaMaAstNode sourceNode, final int d) {
        super(sourceNode);
        this.d = d;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int value = vm.peek().getIntValue();
        final int address = vm.getElementAt(vm.getStackPointer().getValue() - DESCRIPTOR_ADDRESS).getIntValue();
        if ((value < vm.getElementAt(address + d + d + 1).getIntValue()) || (value > vm.getElementAt(address + d + d + 2).getIntValue())) {
            throw new ExecutionException(this, "Index out of range");
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Check index for dimension " + d;
    }

    @Override
    public final String getMnemonic() {
        return "chd";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(d);
    }
}
