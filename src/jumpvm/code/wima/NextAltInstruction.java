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
import jumpvm.memory.Label;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.PointerObject;
import jumpvm.vm.WiMa;

/**
 * Continue with next alternative.
 * 
 * <pre>
 * ST[FP + 4] := l;
 * </pre>
 */
public class NextAltInstruction extends WiMaInstruction {
    /** Next alternative. */
    private final Label label;

    /**
     * Create a new NextAltInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param label next alternative
     */
    public NextAltInstruction(final WiMaAstNode sourceNode, final Label label) {
        super(sourceNode);
        this.label = label;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register fp = vm.getFramePointer();

        stack.setElementAt(fp.getValue() + WiMa.OFFSET_ADDR_NEG, new PointerObject(label));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Continue with next alternative";
    }

    @Override
    public final String getMnemonic() {
        return "nextalt";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(label.getAddress());
    }
}
