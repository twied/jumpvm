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
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.vm.PaMa;

/**
 * Set Stack Pointer.
 *
 * <pre>
 * SP := MP + size - 1;
 * </pre>
 */
public class SspInstruction extends PaMaInstruction {
    /** Stack size. */
    private final int size;

    /** Frame name. */
    private final String identifier;

    /**
     * Create a new SspInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param size Stack size
     * @param identifier frame name
     */
    public SspInstruction(final PaMaAstNode sourceNode, final int size, final String identifier) {
        super(sourceNode);
        this.size = size;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int s = (vm.getMarkPointer().getValue() + size) - 1;

        if ((s - vm.getStackPointer().getValue()) > (PaMa.FRAME_SIZE - 1)) {
            vm.startFrame();
            vm.push(new BasicValueObject(0, "RVAL", "result of " + identifier));
            vm.push(new BasicValueObject(0, "SPD", "Static predecessor"));
            vm.push(new BasicValueObject(0, "DPD", "Dynamic predecessor"));
            vm.push(new BasicValueObject(0, "EP", "Extreme pointer"));
            vm.push(new BasicValueObject(0, "RA", "return address"));
        }

        while (vm.getStackPointer().getValue() < s) {
            vm.push(new BasicValueObject(0, null, null));
        }
        while (vm.getStackPointer().getValue() > s) {
            vm.pop();
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Set Stack Pointer";
    }

    @Override
    public final String getMnemonic() {
        return "ssp";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(size);
    }
}
