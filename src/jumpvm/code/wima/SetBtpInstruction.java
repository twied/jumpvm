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
import jumpvm.memory.Label;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.vm.WiMa;

/**
 * Create back track point.
 * 
 * <pre>
 * ST[FP + 1] := BTP;
 * ST[FP + 2] := TP;
 * ST[FP + 3] := HP;
 * ST[FP + 4] := l;
 * BTP := FP;
 * </pre>
 */
public class SetBtpInstruction extends WiMaInstruction {
    /** Next alternative. */
    private final Label label;

    /**
     * Create new SetBtpInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param label next alternative
     */
    public SetBtpInstruction(final WiMaAstNode sourceNode, final Label label) {
        super(sourceNode);
        this.label = label;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register btp = vm.getBackTrackPointer();
        final Register hp = vm.getHeapPointer();
        final Register tp = vm.getTrailPointer();

        final int fp = vm.getFramePointer().getValue();
        stack.setElementAt(fp + WiMa.OFFSET_REG_BTP, new BasicValueObject(btp));
        stack.setElementAt(fp + WiMa.OFFSET_REG_TP, new BasicValueObject(tp));
        stack.setElementAt(fp + WiMa.OFFSET_REG_HP, new BasicValueObject(hp));
        stack.setElementAt(fp + WiMa.OFFSET_ADDR_NEG, new PointerObject(label));
        btp.setValue(fp);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create back track point with next alternative at instruction " + label.getAddress() + " (" + label.getName() + ")";
    }

    @Override
    public final String getMnemonic() {
        return "setbtp";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(label.getAddress());
    }
}
