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
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.vm.WiMa;

/**
 * Create bound variable reference on stack.
 * 
 * <pre>
 * SP := SP + 1;
 * ST[SP] := ST[FP + i];
 * </pre>
 */
public class PutRefInstruction extends WiMaInstruction {
    /** Offset. */
    private final int offset;

    /** Variable name. */
    private final String identifier;

    /**
     * Create new PutRef instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param offset Offset
     * @param identifier Variable name
     */
    public PutRefInstruction(final WiMaAstNode sourceNode, final int offset, final String identifier) {
        super(sourceNode);
        this.offset = offset;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register fp = vm.getFramePointer();

        stack.push(stack.getElementAt(fp.getValue() + offset));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Put bound variable " + identifier + " on the stack";

    }

    @Override
    public final String getMnemonic() {
        return "putref";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(offset);
    }
}
