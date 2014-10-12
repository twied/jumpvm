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
import jumpvm.memory.Register;
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.vm.PaMa;

/**
 * Create object on the heap.
 *
 * <pre>
 * if NP - STORE[SP] <= EP then
 *     error("store overflow");
 * else
 *     NP := NP - STORE[SP];
 *     STORE[STORE[SP - 1]] := NP;
 *     SP := SP - 2;
 * end if
 * </pre>
 */
public class NewInstruction extends PaMaInstruction {
    /** Variable name. */
    private final String identifier;

    /**
     * Create a new NewInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param identifier variable name
     */
    public NewInstruction(final PaMaAstNode sourceNode, final String identifier) {
        super(sourceNode);
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final Register np = vm.getNewPointer();
        final int size = vm.pop().getIntValue();
        final int address = vm.pop().getIntValue();

        if ((np.getValue() - size) <= vm.getExtremePointer().getValue()) {
            throw new ExecutionException(this, "Store overflow");
        }

        np.setValue(np.getValue() - size);
        vm.setElementAt(address, new PointerObject(np.getValue(), PointerObject.Type.POINTER_STACK, "→ " + identifier, "Pointer to " + identifier));

        for (int i = 0; i < size; ++i) {
            vm.setElementAt(np.getValue() + i, new NilPointerObject());
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create object on the heap";
    }

    @Override
    public final String getMnemonic() {
        return "new";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
