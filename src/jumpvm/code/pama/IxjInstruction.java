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
import jumpvm.memory.Label;
import jumpvm.vm.PaMa;

/**
 * Indexed jump.
 *
 * <pre>
 * PC := STORE[SP] + jumptable;
 * SP := SP - 1;
 * </pre>
 */
public class IxjInstruction extends PaMaInstruction {
    /** Jump offset. */
    private final Label jumptable;

    /**
     * Create a new IxjInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param jumptable Jump table
     */
    public IxjInstruction(final PaMaAstNode sourceNode, final Label jumptable) {
        super(sourceNode);
        this.jumptable = jumptable;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        vm.getProgramCounter().setValue((vm.pop().getIntValue() + jumptable.getAddress()) - 1);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Indexed Jump to jump table";
    }

    @Override
    public final String getMnemonic() {
        return "ixj";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(jumptable.getAddress() - 1);
    }
}
