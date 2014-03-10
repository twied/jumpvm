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
import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.FunValObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.VectorObject;
import jumpvm.vm.MaMa;

/**
 * Apply function.
 * 
 * <pre>
 * if HP[ST[SP]].tag != FUNVAL then
 *     error
 * fi;
 * let (FUNVAL: cf, fap, fgp) := HP[ST[SP]] in
 *     PC := cf;
 *     GP := fgp;
 *     SP := SP - 1;
 *     for i := 1 to size(HP[fap].v) do
 *         SP := SP + 1;
 *         ST[SP] := HP[fap].v[i];
 *     od
 * tel
 * </pre>
 */
public class ApplyInstruction extends MaMaInstruction {
    /**
     * Create a new ApplyInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public ApplyInstruction(final MaMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final Register pc = vm.getProgramCounter();
        final Register gp = vm.getGlobalPointer();
        final MemoryObject object = hp.getElementAt(st.pop());
        if (object instanceof FunValObject) {
            final FunValObject funval = (FunValObject) object;

            pc.setValue(funval.getCf());
            gp.setValue(funval.getFgp());

            final MemoryObject arguments = hp.getElementAt(funval.getFap());
            if (arguments instanceof VectorObject) {
                final VectorObject vector = (VectorObject) arguments;
                for (int i = 0; i < vector.getVector().size(); ++i) {
                    st.push(new PointerObject(vector.getVector().get(i), Type.POINTER_HEAP, "Arg " + i, "Argument " + i));
                }
            } else {
                throw new ExecutionException(this, "not vector value");
            }
        } else {
            throw new ExecutionException(this, "not funval value");
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Apply function";
    }

    @Override
    public final String getMnemonic() {
        return "apply";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
