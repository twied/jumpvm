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
 * Read input.
 *
 * <pre>
 * SP := SP + 1;
 * STORE[SP] := &lt;input&gt;
 * </pre>
 */
public class ReadlnInstruction extends PaMaInstruction {
    /**
     * Create a new ReadlnInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public ReadlnInstruction(final PaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        while (true) {
            try {
                vm.push(new BasicValueObject(Integer.parseInt(vm.getInput()), "input", null));
                break;
            } catch (final NumberFormatException e) {
                continue;
            }
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Read input";
    }

    @Override
    public final String getMnemonic() {
        return "rdl";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
