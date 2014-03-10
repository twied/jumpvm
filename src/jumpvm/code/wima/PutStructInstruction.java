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

import java.util.ArrayList;
import java.util.Collections;

import jumpvm.ast.wima.WiMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Heap;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.vm.WiMa;

/**
 * Structure.
 * 
 * <pre>
 * SP := SP - n + 1;
 * ST[SP] := new(STRUCT : f / n, ST[SP], ... , ST[SP + n - 1]);
 * </pre>
 */
public class PutStructInstruction extends WiMaInstruction {
    /** Identifier. */
    private final String f;

    /** Arity. */
    private final int n;

    /**
     * Create new PutStruct instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param f Identifier
     * @param n Arity
     */
    public PutStructInstruction(final WiMaAstNode sourceNode, final String f, final int n) {
        super(sourceNode);
        this.f = f;
        this.n = n;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Heap heap = vm.getHeap();
        final ArrayList<MemoryObject> elements = new ArrayList<MemoryObject>();
        for (int i = 0; i < n; ++i) {
            elements.add(stack.pop());
        }

        Collections.reverse(elements);
        stack.push(allocateStructureObject(vm, f, n));

        for (final MemoryObject object : elements) {
            heap.allocate(object, null, null);
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Structure";
    }

    @Override
    public final String getMnemonic() {
        return "putstruct";
    }

    @Override
    public final String getParameter() {
        return f + " / " + String.valueOf(n);
    }
}
