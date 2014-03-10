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
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.VectorObject;
import jumpvm.vm.MaMa;

/**
 * Put reference to global on the stack.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := HP[GP].v[j];
 * </pre>
 */
public class PushGlobInstruction extends MaMaInstruction {
    /** Index. */
    private final int j;

    /** Global name. */
    private final String name;

    /**
     * Create new PushGlob instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param j index
     * @param name global name
     */
    public PushGlobInstruction(final MaMaAstNode sourceNode, final int j, final String name) {
        super(sourceNode);
        this.j = j;
        this.name = name;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final Register gp = vm.getGlobalPointer();
        final MemoryObject object = hp.getElementAt(gp);

        if (object instanceof VectorObject) {
            /* "j - 1": index starts at 0, not at 1! */
            final int address = ((VectorObject) object).getVector().get(j - 1);
            st.push(new PointerObject(address, Type.POINTER_HEAP, "→" + name, "Reference to global " + name));
        } else {
            throw new ExecutionException(this, "not vector value");
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Put reference to global " + name + " on the stack";
    }

    @Override
    public final String getMnemonic() {
        return "pushglob";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(j);
    }
}
