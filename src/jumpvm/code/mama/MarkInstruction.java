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
import jumpvm.memory.Label;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.vm.MaMa;

/**
 * Create stack frame and fill organizational cells.
 * 
 * <pre>
 * ST[SP + 1] := l;
 * ST[SP + 2] := FP;
 * ST[SP + 3] := GP;
 * SP := SP + 3;
 * FP := SP;
 * </pre>
 */
public class MarkInstruction extends MaMaInstruction {
    /** Label. */
    private final Label l;

    /**
     * Create new Mark instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * 
     * @param l label
     */
    public MarkInstruction(final MaMaAstNode sourceNode, final Label l) {
        super(sourceNode);
        this.l = l;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();
        final Register gp = vm.getGlobalPointer();

        st.startFrame(MaMa.FRAME_SIZE);
        st.push(new PointerObject(l));
        st.push(new BasicValueObject(fp));
        st.push(new BasicValueObject(gp));
        fp.setValue(sp);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create stack frame and fill organizational cells";
    }

    @Override
    public final String getMnemonic() {
        return "mark";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(l.getAddress());
    }
}
