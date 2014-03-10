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
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.StackObject;
import jumpvm.vm.WiMa;

/**
 * Unify next term in Structure.
 * 
 * <pre>
 * case modus of
 * 'read':
 *     ST[SP + 1] := H[ST[SP] + i];
 *     break;
 * 'write':
 *     ST[SP + 1] := ST[SP] + i;
 *     break;
 * esac;
 * SP := SP + 1;
 * </pre>
 */
public class BrotherInstruction extends WiMaInstruction {
    /** Index. */
    private final int i;

    /** Structure name. */
    private final String name;

    /**
     * Create new Brother instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param i index
     * @param name structure name
     */
    public BrotherInstruction(final WiMaAstNode sourceNode, final int i, final String name) {
        super(sourceNode);
        this.i = i;
        this.name = name;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Heap heap = vm.getHeap();
        final Register modus = vm.getModus();

        final int value = stack.peek().getIntValue() + i;

        if (modus.getValue() == WiMa.MODUS_READ) {
            stack.push((StackObject) heap.getElementAt(value));
        } else {
            stack.push(new BasicValueObject(value, "element " + value, "Unify with element " + value + " of " + name));
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unify next term in structure";
    }

    @Override
    public final String getMnemonic() {
        return "brother";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(i);
    }
}
