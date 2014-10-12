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
import jumpvm.memory.objects.StackObject;
import jumpvm.vm.PaMa;

/**
 * Helper class for binary operation instructions.
 */
public abstract class BinaryOperationInstruction extends PaMaInstruction {
    /**
     * Create a new BinaryOperationInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public BinaryOperationInstruction(final PaMaAstNode sourceNode) {
        super(sourceNode);
    }

    /**
     * Create a BasicValueObject with the boolean value of "false".
     *
     * @return BasicValueObject with the boolean value of "false"
     */
    protected final BasicValueObject createFalse() {
        return new BasicValueObject(0, "false", "false");
    }

    /**
     * Create a BasicValueObject with the boolean value of "true".
     *
     * @return BasicValueObject with the boolean value of "true"
     */
    protected final BasicValueObject createTrue() {
        return new BasicValueObject(1, "true", "true");
    }

    /**
     * Execute binary operation on given lhs and rhs values.
     *
     * @param lhs lhs value
     * @param rhs rhs value
     * @return result of the binary operation
     */
    protected abstract StackObject execute(int lhs, int rhs);

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
