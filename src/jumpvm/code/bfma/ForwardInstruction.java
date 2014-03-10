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
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Label;
import jumpvm.vm.BfMa;

/**
 * If the current cell is zero, jump forward to matching "]".
 * 
 * <pre>
 * if (!*ptr) goto matching "]";
 * </pre>
 */
public class ForwardInstruction extends BfMaInstruction {
    /** Matching "]". */
    private final Label label;

    /**
     * Create a new ForwardInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param label matching "]"
     */
    public ForwardInstruction(final BfMaAstNode sourceNode, final Label label) {
        super(sourceNode);
        this.label = label;
    }

    @Override
    protected final void execute(final BfMa vm) throws ExecutionException {
        if (0 == getValue(vm)) {
            vm.getProgramCounter().setValue(label.getAddress());
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "If the current cell is zero, jump forward to matching \"]\"";
    }

    @Override
    public final String getMnemonic() {
        return "[";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(label.getAddress());
    }
}
