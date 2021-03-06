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
import jumpvm.memory.objects.FunValObject;
import jumpvm.vm.MaMa;

/**
 * Create functional heap object.
 * 
 * <pre>
 * ST[SP - 2] := new(FUNVAL: ST[SP], ST[SP - 1], ST[SP - 2]);
 * SP := SP - 2;
 * </pre>
 */
public class MkFunvalInstruction extends MaMaInstruction {
    /** Function name. */
    private final String name;

    /**
     * Create a new MkFunvalInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param name function name
     */
    public MkFunvalInstruction(final MaMaAstNode sourceNode, final String name) {
        super(sourceNode);
        this.name = name;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final int cf = st.pop().getIntValue();
        final int fap = st.pop().getIntValue();
        final int fgp = st.pop().getIntValue();
        pushAlloc(vm, new FunValObject(cf, fap, fgp, name), "→" + name, "Reference to " + name);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Create functional heap object";
    }

    @Override
    public final String getMnemonic() {
        return "mkfunval";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
