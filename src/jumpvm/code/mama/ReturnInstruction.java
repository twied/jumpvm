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
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.StackObject;
import jumpvm.memory.objects.VectorObject;
import jumpvm.vm.MaMa;

/**
 * Finishing treatment of functions and closures.
 * 
 * <pre>
 * if SP = FP + 1 + n then
 *     PC := ST[FP - 2];
 *     GP := ST[FP];
 *     ST[FP - 2] := ST[SP];
 *     SP := FP - 2;
 *     FP := ST[FP - 1];
 * else
 *     if HP[ST[SP]].tag != FUNVAL then error fi;
 *     let
 *         (FUNVAL: cf, fap, fgp) = HP[ST[SP]]
 *     in
 *         PC := cf;
 *         GP := fgp;
 *         SP := SP - n - 1;
 *         for i := 1 to size(HP[fap].v) do
 *             SP := SP + 1;
 *             ST[SP] := HP[fap].v[i];
 *         od
 *     tel
 * fi
 * </pre>
 */
public class ReturnInstruction extends MaMaInstruction {
    /** Offset. */
    private final int n;

    /**
     * Create a new Return instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param n offset
     */
    public ReturnInstruction(final MaMaAstNode sourceNode, final int n) {
        super(sourceNode);
        this.n = n;
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final Heap hp = vm.getHeap();
        final Register pc = vm.getProgramCounter();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();
        final Register gp = vm.getGlobalPointer();

        if (sp.getValue() == (fp.getValue() + 1 + n)) {
            pc.setValue(st.getElementAt(fp.getValue() - 2));
            gp.setValue(st.getElementAt(fp));
            st.setElementAt(fp.getValue() - 2, st.peek());
            /* if sp is decreased too early the fp value is lost. */
            final StackObject newFP = st.getElementAt(fp.getValue() - 1);
            sp.setValue(fp.getValue() - 2);
            fp.setValue(newFP);
        } else {
            final FunValObject funval = (FunValObject) hp.getElementAt(st.peek());

            pc.setValue(funval.getCf());
            gp.setValue(funval.getFgp());
            sp.setValue(sp.getValue() - n - 1);

            final VectorObject vector = (VectorObject) hp.getElementAt(funval.getFap());
            for (int i = 0; i < vector.getVector().size(); ++i) {
                st.push(new PointerObject(vector.getVector().get(i), Type.POINTER_HEAP, "Arg " + i, "Argument " + i));
            }
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Finishing treatment of functions and closures";
    }

    @Override
    public final String getMnemonic() {
        return "return";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(n);
    }
}
