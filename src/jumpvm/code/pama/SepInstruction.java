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
 * Set Extreme stack Pointer.
 *
 * <pre>
 * EP := SP + size;
 * if EP >= NP then
 *     error("Store overflow");
 * end if
 * </pre>
 */
public class SepInstruction extends PaMaInstruction {
    /** EP increase. */
    private final int size;

    /**
     * Create a new SepInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param size EP increase
     */
    public SepInstruction(final PaMaAstNode sourceNode, final int size) {
        super(sourceNode);
        this.size = size;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Set Extreme stack Pointer";
    }

    @Override
    public final String getMnemonic() {
        return "sep";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(size);
    }
}
