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
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.vm.WiMa;

/**
 * Create room for k organizational cells, arguments and variables.
 * 
 * <pre>
 * SP := FP + k;
 * </pre>
 */
public class PushEnvInstruction extends WiMaInstruction {
    /** Number of arguments and variables. */
    private final int k;

    /**
     * Create new PushEnv instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param k number of arguments and variables
     */
    public PushEnvInstruction(final WiMaAstNode sourceNode, final int k) {
        super(sourceNode);
        this.k = k;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();

        while (sp.getValue() < (fp.getValue() + k)) {
            stack.push(new NilPointerObject("↛", "Reserved space for " + (k - (WiMa.FRAME_SIZE - 1)) + " arguments and globals"));
        }
        while (sp.getValue() > (fp.getValue() + k)) {
            stack.pop();
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create room for 4 organizational cells and " + (k - (WiMa.FRAME_SIZE - 1)) + " arguments and globals";
    }

    @Override
    public final String getMnemonic() {
        return "pushenv";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(k);
    }
}
