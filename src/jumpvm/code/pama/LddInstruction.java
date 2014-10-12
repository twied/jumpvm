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

package jumpvm.code.pama;

import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.objects.StackObject;
import jumpvm.vm.PaMa;

/**
 * Load from descriptor.
 *
 * <pre>
 * SP := SP + 1;
 * STORE[SP] := STORE[STORE[SP - 3] + offset];
 * </pre>
 */
public class LddInstruction extends PaMaInstruction {
    /** Descriptor offset. */
    private final int offset;

    /**
     * Create a new LddInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param offset Descriptor offset
     */
    public LddInstruction(final PaMaAstNode sourceNode, final int offset) {
        super(sourceNode);
        this.offset = offset;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int address = vm.getElementAt(vm.getStackPointer().getValue() - 2).getIntValue();
        final StackObject object = vm.getElementAt(address + offset);
        vm.push(object);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Load from descriptor " + offset;
    }

    @Override
    public final String getMnemonic() {
        return "ldd";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(offset);
    }
}
