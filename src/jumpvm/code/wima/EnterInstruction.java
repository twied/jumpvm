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
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.vm.WiMa;

/**
 * Create stack frame.
 * 
 * <pre>
 * SP := SP + 6;
 * ST[SP - 4] := FP;
 * </pre>
 */
public class EnterInstruction extends WiMaInstruction {
    /**
     * Create a new EnterInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public EnterInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        stack.startFrame(WiMa.FRAME_SIZE + 1);
        stack.push(new PointerObject(0, Type.POINTER_PROGRAM, "+PC", "positive return address"));
        stack.push(new BasicValueObject(vm.getFramePointer()));
        stack.push(new BasicValueObject(vm.getBackTrackPointer(), 0));
        stack.push(new BasicValueObject(vm.getTrailPointer(), 0));
        stack.push(new BasicValueObject(vm.getHeapPointer(), 0));
        stack.push(new PointerObject(0, Type.POINTER_PROGRAM, "-PC", "negative return address"));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create stack frame";
    }

    @Override
    public final String getMnemonic() {
        return "enter";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
