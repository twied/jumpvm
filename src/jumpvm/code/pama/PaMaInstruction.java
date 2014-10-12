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
import jumpvm.code.Instruction;
import jumpvm.exception.ExecutionException;
import jumpvm.vm.JumpVM;
import jumpvm.vm.PaMa;

/** PaMachine {@link Instruction}. */
public abstract class PaMaInstruction extends Instruction {
    /**
     * Create a new PaMaInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public PaMaInstruction(final PaMaAstNode sourceNode) {
        super(sourceNode);
    }

    /**
     * Calculate the address of an object, given it's stacking depth and relative address.
     *
     * @param vm PaMachine
     * @param p stacking depth
     * @param a relative address
     * @return absolut address of the object
     */
    protected final int base(final PaMa vm, final int p, final int a) {
        if (p == 0) {
            return a;
        }

        return base(vm, p - 1, vm.getElementAt(a + 1).getIntValue());
    }

    @Override
    public final void execute(final JumpVM jumpVM) throws ExecutionException {
        if (jumpVM instanceof PaMa) {
            execute((PaMa) jumpVM);
        } else {
            throw new ExecutionException(this, "Wrong VM");
        }
    }

    /**
     * Execute a PaMa instruction.
     *
     * @param vm PaMachine
     * @throws ExecutionException on failure
     */
    public abstract void execute(PaMa vm) throws ExecutionException;
}
