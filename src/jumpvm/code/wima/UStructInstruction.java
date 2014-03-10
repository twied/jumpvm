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
import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.StructureObject;
import jumpvm.vm.WiMa;

/**
 * Unification with structure.
 * 
 * <pre>
 * case modus of
 * read:
 *     v := deref(ST[SP]);
 *     case H[v] of
 *     (STRUCT: f/n):
 *         ST[SP] := v;
 *     (REF: _):
 *         ST[SP] := modus;
 *         SP := SP + 1;
 *         h := new((STRUCT: f/n), NIL, ..., NIL);
 *         ST[SP] := h;
 *         H[v] := (REF: h);
 *         modus := write;
 *         trail(v);
 *     otherwise:
 *         goto backtrack;
 *     esac     
 * write:
 *     H[ST[SP]] := ST[SP + 1] := new((STRUCT: f/n), NIL, ..., NIL);
 *     ST[SP] := modus;
 *     SP := SP + 1;
 * esac;
 * </pre>
 */
public class UStructInstruction extends WiMaInstruction {
    /** Identifier. */
    private final String f;

    /** Arity. */
    private final int n;

    /**
     * Create a new UStructInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param f identifier
     * @param n arity
     */
    public UStructInstruction(final WiMaAstNode sourceNode, final String f, final int n) {
        super(sourceNode);
        this.f = f;
        this.n = n;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Heap heap = vm.getHeap();
        final Register modus = vm.getModus();

        if (modus.getValue() == WiMa.MODUS_READ) {
            final int v = deref(vm, stack.pop().getIntValue());
            final MemoryObject o = heap.getElementAt(v);
            final String name = o.getDisplayDescription();

            if (o instanceof StructureObject) {
                /* _same_ structure. */
                final StructureObject s = (StructureObject) o;
                if (f.equals(s.getIdentifier()) && (s.getArity() == n)) {
                    stack.push(new PointerObject(v, Type.POINTER_HEAP, "→" + f + "/" + n, "Reference to structure " + f + "/" + n));
                } else {
                    backtrack(vm);
                    return;
                }
            } else if (o instanceof PointerObject) {
                if (v == ((PointerObject) o).getIntValue()) {
                    stack.push(new BasicValueObject(modus));
                    final PointerObject pointer = allocateStructureObject(vm, f, n);
                    for (int i = 0; i < n; ++i) {
                        heap.allocate(new NilPointerObject(), "→" + String.valueOf(i), "Reference to element # " + i + " of structure " + f + "/" + n);
                    }
                    stack.push(pointer);
                    heap.setElementAt(v, pointer);
                    modus.setValue(WiMa.MODUS_WRITE);
                    trail(vm, v, name);
                } else {
                    backtrack(vm);
                    return;
                }
            } else {
                backtrack(vm);
                return;
            }
        } else {
            final PointerObject pointer = allocateStructureObject(vm, f, n);
            for (int i = 0; i < n; ++i) {
                heap.allocate(new NilPointerObject(), "→" + String.valueOf(i), "Reference to element # " + i + " of structure " + f + "/" + n);
            }
            heap.setElementAt(stack.peek(), pointer);
            stack.push(pointer);
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unification with structure";
    }

    @Override
    public final String getMnemonic() {
        return "ustruct";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(f) + " / " + String.valueOf(n);
    }
}
