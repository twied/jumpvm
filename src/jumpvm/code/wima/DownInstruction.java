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
import jumpvm.memory.Heap;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.StackObject;
import jumpvm.vm.WiMa;

/**
 * Unify children.
 * 
 * <pre>
 * case modus of
 * read:
 *     ST[SP + 1] := H[ST[SP] + 1];
 * write:
 *     ST[SP + 1] := ST[SP] + 1;
 * esac;
 * SP := SP + 1;
 * </pre>
 */
public class DownInstruction extends WiMaInstruction {
    /** Structure name. */
    private final String name;

    /**
     * Create a new DownInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param name structure name
     */
    public DownInstruction(final WiMaAstNode sourceNode, final String name) {
        super(sourceNode);
        this.name = name;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Heap heap = vm.getHeap();
        final int value = stack.peek().getIntValue() + 1;

        if (vm.getModus().getValue() == WiMa.MODUS_READ) {
            stack.push((StackObject) heap.getElementAt(value));
        } else {
            stack.push(new BasicValueObject(value, "element 1", "Unify with element 1 of " + name));
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unify children";
    }

    @Override
    public final String getMnemonic() {
        return "down";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
