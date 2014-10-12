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
 * Calculate indexed address.
 *
 * <pre>
 * STORE[SP - 1] := STORE[SP - 1] + STORE[SP] * offset;
 * SP := SP - 1;
 * </pre>
 */
public class IxaInstruction extends PaMaInstruction {
    /** Size of sub field. */
    private final int offset;

    /**
     * Create a new IxaInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param offset size of sub field
     */
    public IxaInstruction(final PaMaAstNode sourceNode, final int offset) {
        super(sourceNode);
        this.offset = offset;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Indexed Access";

    }

    @Override
    public final String getMnemonic() {
        return "ixa";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(offset);
    }
}
