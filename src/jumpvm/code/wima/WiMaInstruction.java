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

import java.util.ArrayList;

import jumpvm.ast.AstNode;
import jumpvm.code.Instruction;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.AtomObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.StackObject;
import jumpvm.memory.objects.StructureObject;
import jumpvm.vm.JumpVM;
import jumpvm.vm.WiMa;

/**
 * WiMa instruction.
 */
public abstract class WiMaInstruction extends Instruction {
    /**
     * Convenience method to push an atom to the heap and get a pointer to that object.
     * 
     * <pre>
     * new(ATOM : identifier);
     * </pre>
     * 
     * @param vm WiMa
     * @param identifier atom's identifier
     * @return reference to that atom on the heap
     */
    protected static PointerObject allocateAtomObject(final WiMa vm, final String identifier) {
        return vm.getHeap().allocate(new AtomObject(identifier), "→" + identifier, "Reference to atom " + identifier);
    }

    /**
     * Convenience method to push an structure to the heap and get a pointer to that object.
     * 
     * <pre>
     * new(STRUCT : identifier / arity);
     * </pre>
     * 
     * @param vm WiMa
     * @param identifier structure's identifier
     * @param arity structure's arity
     * @return reference to that structure on the heap
     */
    protected static PointerObject allocateStructureObject(final WiMa vm, final String identifier, final int arity) {
        return vm.getHeap().allocate(new StructureObject(identifier, arity), "→" + identifier + "/" + arity, "Reference to structure " + identifier + "/" + arity);
    }

    /**
     * Reset to next alternative.
     * 
     * @param vm WiMa
     */
    protected static void backtrack(final WiMa vm) {
        final Stack stack = vm.getStack();
        final Heap heap = vm.getHeap();
        final Heap trail = vm.getTrail();
        final Register pc = vm.getProgramCounter();
        final Register fp = vm.getFramePointer();
        final Register btp = vm.getBackTrackPointer();
        final Register hp = vm.getHeapPointer();
        final Register tp = vm.getTrailPointer();

        final int newFP = btp.getValue();
        final int newHP = stack.getElementAt(newFP + WiMa.OFFSET_REG_HP).getIntValue();
        final int newTP = stack.getElementAt(newFP + WiMa.OFFSET_REG_TP).getIntValue();
        final int newPC = stack.getElementAt(newFP + WiMa.OFFSET_ADDR_NEG).getIntValue();

        if (newPC == 0) {
            fail(vm);
            return;
        }

        fp.setValue(newFP);
        /* Remove excessive elements from the heap. */
        if (newHP < hp.getValue()) {
            final ArrayList<MemoryObject> content = heap.getContent();
            content.subList(newHP, content.size()).clear();
            heap.reset(content);
        }
        hp.setValue(newHP);

        reset(vm, stack.getElementAt(newFP + WiMa.OFFSET_REG_TP).getIntValue(), tp.getValue());
        /* Remove excessive elements from the trail. */
        if (newTP < tp.getValue()) {
            final ArrayList<MemoryObject> content = trail.getContent();
            content.subList(newTP + 1, content.size()).clear();
            trail.reset(content);
        }

        tp.setValue(newTP);
        pc.setValue(newPC);
    }

    /**
     * Dereference a chain of pointers.
     * 
     * @param vm WiMa
     * @param address starting address
     * @return address of target element
     */
    protected static int deref(final WiMa vm, final int address) {
        final MemoryObject object = vm.getHeap().getElementAt(address);

        if (!(object instanceof PointerObject)) {
            return address;
        }

        final PointerObject pointer = (PointerObject) object;

        if (pointer.getType() != Type.POINTER_HEAP) {
            return address;
        }

        if (pointer.getIntValue() == address) {
            /* free variable */
            return address;
        } else {
            /* bound variable */
            return deref(vm, pointer.getIntValue());
        }
    }

    /**
     * Stop execution with no more alternatives to follow.
     * 
     * @param vm WiMa
     */
    private static void fail(final WiMa vm) {
        vm.getStatus().setValue(JumpVM.STATUS_STOP);
        vm.getWriter().println("no");
    }

    /**
     * Reset the variables in the given range of trail memory.
     * 
     * @param vm WiMa
     * @param from from index
     * @param to to index
     */
    protected static void reset(final WiMa vm, final int from, final int to) {
        for (int i = from + 1; i <= to; ++i) {
            final StackObject trailElement = (StackObject) vm.getTrail().getElementAt(i);
            vm.getHeap().setElementAt(trailElement, trailElement);
        }
    }

    /**
     * Track variable bindings in current frame.
     * 
     * @param vm WiMa
     * @param a stack address of the variable
     * @param description name of the variable
     */
    protected static void trail(final WiMa vm, final int a, final String description) {
        final Stack stack = vm.getStack();
        final Heap trail = vm.getTrail();
        final Register btp = vm.getBackTrackPointer();
        final Register tp = vm.getTrailPointer();

        if (a < stack.getElementAt(btp.getValue() + WiMa.OFFSET_REG_HP).getIntValue()) {
            tp.increment();
            trail.setElementAt(tp, new PointerObject(a, Type.POINTER_HEAP, description, null));
        }
    }

    /**
     * Create a new WiMaInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public WiMaInstruction(final AstNode<?> sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final JumpVM jumpVM) throws ExecutionException {
        if (!(jumpVM instanceof WiMa)) {
            throw new ExecutionException(this, "wrong VM");
        }

        final WiMa wima = (WiMa) jumpVM;
        execute(wima);

        /* adjust heap pointer: hp points to the next free cell. */
        wima.getHeapPointer().setValue(wima.getHeap().getSize());
    }

    /**
     * Execute a WiMa instruction.
     * 
     * @param vm WiMa
     * @throws ExecutionException on failure
     */
    public abstract void execute(final WiMa vm) throws ExecutionException;

    /**
     * Unify two elements on the heap.
     * 
     * @param vm WiMa
     * @param a first element
     * @param b second element
     * @throws ExecutionException on failure
     */
    protected final void unify(final WiMa vm, final int a, final int b) throws ExecutionException {
        final Heap heap = vm.getHeap();

        final MemoryObject t1 = heap.getElementAt(a);
        final MemoryObject t2 = heap.getElementAt(b);

        if (t1 instanceof PointerObject) {
            if (((PointerObject) t1).getType() != Type.POINTER_HEAP) {
                throw new ExecutionException(this, "t1 of unexpected type");
            }
        }

        if (t2 instanceof PointerObject) {
            if (((PointerObject) t2).getType() != Type.POINTER_HEAP) {
                throw new ExecutionException(this, "t2 of unexpected type");
            }
        }

        if (t1 instanceof AtomObject) {
            if (t2 instanceof AtomObject) {
                /* Atom & atom. */
                if (!((AtomObject) t1).getIdentifier().equals(((AtomObject) t2).getIdentifier())) {
                    fail(vm);
                }
            } else if (t2 instanceof PointerObject) {
                /* Atom & variable. */
                heap.setElementAt(b, new PointerObject(a, Type.POINTER_HEAP, "→" + t1.getDisplayValue(), "Reference to " + t1.getDisplayValue()));
            } else if (t2 instanceof StructureObject) {
                /* Atom & structure. */
                fail(vm);
            } else {
                throw new ExecutionException(this, "t2 of unexpected type");
            }
        } else if (t1 instanceof PointerObject) {
            if (t2 instanceof AtomObject) {
                /* Variable & atom. */
                heap.setElementAt(a, new PointerObject(b, Type.POINTER_HEAP, "→" + t2.getDisplayValue(), "Reference to " + t2.getDisplayValue()));
            } else if (t2 instanceof PointerObject) {
                /* Variable & variable. */
                if (((PointerObject) t1).getIntValue() != ((PointerObject) t2).getIntValue()) {
                    heap.setElementAt(a, new PointerObject(b, Type.POINTER_HEAP, "→" + t2.getDisplayValue(), "Reference to " + t2.getDisplayValue()));
                }
            } else if (t2 instanceof StructureObject) {
                /* Variable & structure. */
                heap.setElementAt(a, new PointerObject(b, Type.POINTER_HEAP, "→" + t2.getDisplayValue(), "Reference to " + t2.getDisplayValue()));
            } else {
                throw new ExecutionException(this, "t2 of unexpected type");
            }
        } else if (t1 instanceof StructureObject) {
            if (t2 instanceof AtomObject) {
                /* Structure & atom. */
                fail(vm);
            } else if (t2 instanceof PointerObject) {
                /* Structure & variable. */
                heap.setElementAt(b, new PointerObject(a, Type.POINTER_HEAP, "→" + t1.getDisplayValue(), "Reference to " + t1.getDisplayValue()));
            } else if (t2 instanceof StructureObject) {
                /* Structure & structure. */
                unifyList(vm, a, b);
            } else {
                throw new ExecutionException(this, "t2 of unexpected type");
            }
        } else {
            throw new ExecutionException(this, "t1 of unexpected type");
        }
    }

    /**
     * Unify two structures.
     * 
     * @param vm WiMa
     * @param a first structure
     * @param b second structure
     * @throws ExecutionException on failure
     */
    protected final void unifyList(final WiMa vm, final int a, final int b) throws ExecutionException {
        final Heap heap = vm.getHeap();

        final StructureObject t = (StructureObject) heap.getElementAt(a);
        final StructureObject s = (StructureObject) heap.getElementAt(b);

        if (!t.getIdentifier().equals(s.getIdentifier())) {
            fail(vm);
        }

        if (t.getArity() != s.getArity()) {
            fail(vm);
        }

        for (int i = 0; i < t.getArity(); ++i) {
            unify(vm, deref(vm, a + i + 1), deref(vm, b + i + 1));
        }
    }
}
