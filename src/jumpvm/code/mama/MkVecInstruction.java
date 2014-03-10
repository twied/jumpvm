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

import java.util.ArrayList;
import java.util.Collections;

import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.VectorObject;
import jumpvm.vm.MaMa;

/**
 * Make vector with n components.
 * 
 * <pre>
 * ST[SP - n + 1] := new(VECTOR: ST[SP - n + 1], ST[SP - n + 2], ... , ST[SP]);
 * SP := SP - n + 1;
 * </pre>
 */
public class MkVecInstruction extends MaMaInstruction {
    /** Components. */
    private final int n;

    /** Vector name. */
    private final String vectorName;

    /**
     * Create new MkVec instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param n components
     * @param vectorName vector name
     */
    public MkVecInstruction(final MaMaAstNode sourceNode, final int n, final String vectorName) {
        super(sourceNode);
        this.n = n;
        this.vectorName = vectorName;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final ArrayList<Integer> vector = new ArrayList<Integer>();
        for (int i = 0; i < n; ++i) {
            vector.add(st.pop().getIntValue());
        }

        Collections.reverse(vector);
        pushAlloc(vm, new VectorObject(vector, vectorName), "→" + vectorName, "Reference to " + vectorName + " vector");
    }

    @Override
    public final String getDisplayHoverText() {
        return "Make vector with n components";
    }

    @Override
    public final String getMnemonic() {
        return "mkvec";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(n);
    }
}
