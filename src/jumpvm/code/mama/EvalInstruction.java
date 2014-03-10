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

package jumpvm.code.mama;

import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.ClosureObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.vm.MaMa;

/**
 * Evaluate closure.
 * 
 * <pre>
 * if HP[ST[SP]].tag = CLOSURE then
 *     ST[SP + 1] := PC;
 *     ST[SP + 2] := FP;
 *     ST[SP + 3] := GP;
 *     GP := HP[ST[SP]].gp;
 *     PC := HP[ST[SP]].cp;
 *     SP := SP + 3;
 *     FP := SP;
 * fi
 * </pre>
 */
public class EvalInstruction extends MaMaInstruction {
    /**
     * Create a new EvalInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public EvalInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final Register pc = vm.getProgramCounter();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();
        final Register gp = vm.getGlobalPointer();
        final MemoryObject object = hp.getElementAt(st.peek());
        if (object instanceof ClosureObject) {
            final ClosureObject closure = (ClosureObject) object;

            st.startFrame(MaMa.FRAME_SIZE);
            st.push(new BasicValueObject(pc));
            st.push(new BasicValueObject(fp));
            st.push(new BasicValueObject(gp));

            gp.setValue(closure.getGp());
            pc.setValue(closure.getCp());
            fp.setValue(sp);
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Evaluate closure";
    }

    @Override
    public final String getMnemonic() {
        return "eval";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
