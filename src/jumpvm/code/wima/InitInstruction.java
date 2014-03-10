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
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.vm.WiMa;

/**
 * Initialize the VM.
 * 
 * <pre>
 * SP := 5;
 * FP := 1;
 * HP := 0;
 * TP := -1;
 * BTP := 1;
 * ST[5] := 0;
 * ST[3] := -1;
 * ST[4] := -1;
 * </pre>
 */
public class InitInstruction extends WiMaInstruction {
    /**
     * Create a new InitInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public InitInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register fp = vm.getFramePointer();
        final Register btp = vm.getBackTrackPointer();
        final Register hp = vm.getHeapPointer();
        final Register tp = vm.getTrailPointer();

        fp.setValue(1);
        hp.setValue(0);
        tp.setValue(-1);
        btp.setValue(1);

        stack.startFrame(WiMa.FRAME_SIZE + 1);
        stack.push(new PointerObject(0, Type.POINTER_PROGRAM, "+PC", "positive return address"));
        stack.push(new BasicValueObject(fp, 0));
        stack.push(new BasicValueObject(btp, 0));
        stack.push(new BasicValueObject(tp, -1));
        stack.push(new BasicValueObject(hp, -1));
        stack.push(new PointerObject(0, Type.POINTER_PROGRAM, "-PC", "negative return address"));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Initialize the VM";
    }

    @Override
    public final String getMnemonic() {
        return "init";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
