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

package jumpvm.code.bfma;

import jumpvm.ast.bfma.BfMaAstNode;
import jumpvm.code.Instruction;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.vm.BfMa;
import jumpvm.vm.JumpVM;

/**
 * BfMa instruction.
 */
public abstract class BfMaInstruction extends Instruction {
    /**
     * Create a new BfMaInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public BfMaInstruction(final BfMaAstNode sourceNode) {
        super(sourceNode);
    }

    /**
     * Execute the instruction on the given BfMachine.
     * 
     * @param vm BfMachine
     * @throws ExecutionException on failure
     */
    protected abstract void execute(final BfMa vm) throws ExecutionException;

    @Override
    public final void execute(final JumpVM jumpVM) throws ExecutionException {
        if (jumpVM instanceof BfMa) {
            execute((BfMa) jumpVM);
        } else {
            throw new ExecutionException(this, "Wrong VM");
        }
    }

    /**
     * Convenience method to get the value in the current cell.
     * 
     * @param vm BfMachine
     * @return value in the current cell
     */
    protected final int getValue(final BfMa vm) {
        try {
            return vm.getStack().getElementAt(vm.getCellPointer()).getIntValue();
        } catch (final IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Convenience method to set the value in the current cell.
     * 
     * @param vm BfMachine
     * @param value new value
     */
    protected final void setValue(final BfMa vm, final int value) {
        vm.getStack().setElementAt(vm.getCellPointer(), new BasicValueObject(value, null, null));
    }
}
