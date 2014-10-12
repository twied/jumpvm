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
 * Set MP.
 *
 * <pre>
 * MP := SP - (p + 4);
 * </pre>
 */
public class SmpInstruction extends PaMaInstruction {
    /** Mark pointer increment. */
    private final int p;

    /**
     * Create a new SmpInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param p mark pointer increment
     */
    public SmpInstruction(final PaMaAstNode sourceNode, final int p) {
        super(sourceNode);
        this.p = p;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        vm.getMarkPointer().setValue(vm.getStackPointer().getValue() - p - (PaMa.FRAME_SIZE - 1));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Set Mark Pointer";
    }

    @Override
    public final String getMnemonic() {
        return "smp";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(p);
    }
}
