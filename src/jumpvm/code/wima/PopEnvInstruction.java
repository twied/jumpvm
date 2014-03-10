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
 * Drop stack frame if last alternative.
 * 
 * <pre>
 * if FP > BTP then
 *     SP := FP - 2;
 * fi;
 * PC := ST[FP - 1];
 * FP := ST[FP];
 * </pre>
 */
public class PopEnvInstruction extends WiMaInstruction {
    /**
     * Create a new PopEnvInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public PopEnvInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register pc = vm.getProgramCounter();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();
        final Register btp = vm.getBackTrackPointer();

        final int newPC = stack.getElementAt(fp.getValue() + WiMa.OFFSET_ADDR_POS).getIntValue();
        final int newFP = stack.getElementAt(fp.getValue() + WiMa.OFFSET_REG_FP).getIntValue();

        if (fp.getValue() > btp.getValue()) {
            sp.setValue(fp.getValue() - 2);
        }
        pc.setValue(newPC);
        fp.setValue(newFP);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Drop stack frame if last alternative";
    }

    @Override
    public final String getMnemonic() {
        return "popenv";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
