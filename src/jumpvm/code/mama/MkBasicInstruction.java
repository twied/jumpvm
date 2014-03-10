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
import jumpvm.memory.Stack;
import jumpvm.memory.objects.StackObject;
import jumpvm.vm.MaMa;

/**
 * Create basic heap object.
 * 
 * <pre>
 * ST[SP] := new(BASIC: ST[SP]);
 * </pre>
 */
public class MkBasicInstruction extends MaMaInstruction {
    /**
     * Create a new MkBasicInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public MkBasicInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final StackObject object = st.pop();

        st.push(hp.allocate(object, "→" + object.getDisplayValue(), "Reference to Value " + object.getDisplayValue()));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create basic heap object";
    }

    @Override
    public final String getMnemonic() {
        return "mkbasic";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
