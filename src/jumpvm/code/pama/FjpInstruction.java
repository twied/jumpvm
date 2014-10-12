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
 * False jump.
 *
 * <pre>
 * if STORE[SP] == false then
 *     PC := label;
 * end if
 * SP := SP - 1;
 * </pre>
 */
public class FjpInstruction extends PaMaInstruction {
    /** Jump target. */
    private final Label label;

    /**
     * Create a new FjpInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param label Jump target
     */
    public FjpInstruction(final PaMaAstNode sourceNode, final Label label) {
        super(sourceNode);
        this.label = label;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int value = vm.pop().getIntValue();
        if (value == 0) {
            vm.getProgramCounter().setValue(label.getAddress());
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Conditional jump to " + label.getName();
    }

    @Override
    public final String getMnemonic() {
        return "fjp";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(label.getAddress());
    }
}
