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
import jumpvm.memory.objects.ClosureObject;
import jumpvm.memory.objects.ConsObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.vm.MaMa;

/**
 * List constructor.
 * 
 * <pre>
 * if HP[ST[SP - 1]].tag == CONS || HP[ST[SP - 1]].tag == CLOSURE ||
 *         HP[ST[SP - 1]].tag == NIL then
 *     ST[SP - 1] := new(CONS: ST[SP], ST[SP - 1]);
 *     SP := SP - 1;
 * else
 *     error
 * fi
 * </pre>
 */
public class ConsInstruction extends MaMaInstruction {
    /**
     * Create a new ConsInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public ConsInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final int head = st.pop().getIntValue();
        final int body = st.pop().getIntValue();
        final MemoryObject object = hp.getElementAt(body);

        if ((object instanceof ConsObject) || (object instanceof ClosureObject) || (object instanceof NilPointerObject)) {
            st.push(hp.allocate(new ConsObject(head, body), "→cons", "Reference to List link"));
        } else {
            throw new ExecutionException(this, "not list object");
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "List constructor";
    }

    @Override
    public final String getMnemonic() {
        return "cons";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
