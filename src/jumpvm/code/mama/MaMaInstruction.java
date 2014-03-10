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

import jumpvm.ast.AstNode;
import jumpvm.code.Instruction;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.vm.JumpVM;
import jumpvm.vm.MaMa;

/**
 * MaMa instruction.
 */
public abstract class MaMaInstruction extends Instruction {
    /**
     * Convenience method to allocate an object on the heap an push the pointer to that object on the stack.
     * 
     * <pre>
     * SP := SP + 1;
     * ST[SP] := new(object);
     * </pre>
     * 
     * @param vm MaMa
     * @param object object to allocate on the heap
     * @param descriptionShort short description for the pointer
     * @param descriptionLong long description for the pointer
     */
    protected static void pushAlloc(final MaMa vm, final MemoryObject object, final String descriptionShort, final String descriptionLong) {
        vm.getStack().push(vm.getHeap().allocate(object, descriptionShort, descriptionLong));
    }

    /**
     * Create a new MaMaInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public MaMaInstruction(final AstNode<?> sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final JumpVM jumpVM) throws ExecutionException {
        if (!(jumpVM instanceof MaMa)) {
            throw new ExecutionException(this, "wrong VM");
        }

        execute((MaMa) jumpVM);
    }

    /**
     * Execute a MaMa instruction.
     * 
     * @param vm MaMa
     * @throws ExecutionException on failure
     */
    public abstract void execute(final MaMa vm) throws ExecutionException;
}
