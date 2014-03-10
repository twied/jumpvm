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

package jumpvm.code.wima;

import jumpvm.ast.wima.WiMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.vm.WiMa;

/**
 * Delete back track point.
 * 
 * <pre>
 * BTP := ST[FP + 1];
 * </pre>
 */
public class DelBtpInstruction extends WiMaInstruction {
    /**
     * Create a new DelBtpInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public DelBtpInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register fp = vm.getFramePointer();
        final Register btp = vm.getBackTrackPointer();

        btp.setValue(stack.getElementAt(fp.getValue() + WiMa.OFFSET_REG_BTP));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Delete back track point";
    }

    @Override
    public final String getMnemonic() {
        return "delbtp";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
