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
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.vm.MaMa;

/**
 * Detect end of list.
 * 
 * <pre>
 * if HP[ST[SP]].tag = NIL then
 *     ST[SP] := 1;
 * else
 *     ST[SP] := 0;
 * fi
 * </pre>
 */
public class IsNilInstruction extends MaMaInstruction {
    /**
     * Create a new IsNilInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public IsNilInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();

        if (hp.getElementAt(st.pop()) instanceof NilPointerObject) {
            st.push(new BasicValueObject(1, "true", "true"));
        } else {
            st.push(new BasicValueObject(0, "false", "false"));
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Detect end of list";
    }

    @Override
    public final String getMnemonic() {
        return "isnil";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
