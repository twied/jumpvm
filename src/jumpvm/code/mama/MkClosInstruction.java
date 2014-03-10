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

import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.ClosureObject;
import jumpvm.vm.MaMa;

/**
 * Create closure.
 * 
 * <pre>
 * ST[SP - 1] := new(CLOSURE: ST[SP], ST[SP - 1]);
 * SP := SP - 1;
 * </pre>
 */
public class MkClosInstruction extends MaMaInstruction {
    /** Name of the closure. */
    private final String closureName;

    /**
     * Create a new MkClosInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param closureName closure name
     */
    public MkClosInstruction(final MaMaAstNode sourceNode, final String closureName) {
        super(sourceNode);
        this.closureName = closureName;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final int cpValue = st.pop().getIntValue();
        final int gpValue = st.pop().getIntValue();
        pushAlloc(vm, new ClosureObject(cpValue, gpValue, closureName), "→" + closureName, "Reference to " + closureName);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create closure";
    }

    @Override
    public final String getMnemonic() {
        return "mkclos";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
