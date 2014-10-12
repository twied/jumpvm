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
import jumpvm.vm.PaMa;

/**
 * Get upper bound of dimension in array.
 *
 * <pre>
 * STORE[SP - 1] := STORE[STORE[SP - 2] + 2 * STORE[SP - 1] + 2];
 * SP := SP - 1;
 * </pre>
 */
public class HighInstruction extends PaMaInstruction {
    /**
     * Create a new HighInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public HighInstruction(final PaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        final int dimension = vm.pop().getIntValue();
        final int descriptor = vm.pop().getIntValue();
        vm.push(vm.getElementAt(descriptor + dimension + dimension + 2));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Get upper bound of dimension in array";
    }

    @Override
    public final String getMnemonic() {
        return "high";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
