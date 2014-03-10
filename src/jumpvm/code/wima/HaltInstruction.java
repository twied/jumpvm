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

import java.io.PrintWriter;

import jumpvm.ast.wima.WiMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Heap;
import jumpvm.memory.Register;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.StackObject;
import jumpvm.memory.objects.StructureObject;
import jumpvm.vm.JumpVM;
import jumpvm.vm.WiMa;

/**
 * Start backtracking or halt the virtual machine.
 * 
 * <pre>
 * if BTP > FP then
 *     goto backtrack;
 * else
 *     stop;
 * fi
 * </pre>
 */
public class HaltInstruction extends WiMaInstruction {
    /**
     * Create a new HaltInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     */
    public HaltInstruction(final WiMaAstNode sourceNode) {
        super(sourceNode);
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register fp = vm.getFramePointer();
        final Register btp = vm.getBackTrackPointer();
        final PrintWriter writer = vm.getWriter();

        boolean yes = true;

        for (int i = WiMa.FRAME_SIZE + 1; i < stack.getSize(); ++i) {
            final StackObject stackObject = stack.getElementAt(i);

            if (!(stackObject instanceof PointerObject)) {
                break;
            }

            if (((PointerObject) stackObject).getType() != Type.POINTER_HEAP) {
                break;
            }

            yes = false;
            final int address = deref(vm, stackObject.getIntValue());
            writer.print(getName(vm, address));
            writer.print(" ");
            writer.print(getType(vm, address));
            writer.println();
        }

        if (yes) {
            writer.println("yes");
        }

        if (btp.getValue() > fp.getValue()) {
            backtrack(vm);
            return;
        } else {
            vm.getStatus().setValue(JumpVM.STATUS_STOP);
        }
    }

    @Override
    public final String getDisplayHoverText() {
        return "Start backtracking or halt the virtual machine";
    }

    @Override
    public final String getMnemonic() {
        return "halt";
    }

    /**
     * Returns the name of a cell.
     * 
     * @param vm WiMa
     * @param value address
     * @return the name of the cell
     */
    private String getName(final WiMa vm, final int value) {
        final Heap heap = vm.getHeap();
        final int address = deref(vm, value);
        final MemoryObject o = heap.getElementAt(address);

        if (!(o instanceof StructureObject)) {
            return o.getDisplayValue();
        }

        final StructureObject s = (StructureObject) o;
        if (s.getArity() == 0) {
            return s.getIdentifier() + "()";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s.getIdentifier());
        stringBuilder.append("(");

        for (int i = 0; i < (s.getArity() - 1); ++i) {
            stringBuilder.append(getName(vm, address + i + 1));
            stringBuilder.append(", ");
        }

        stringBuilder.append(getName(vm, address + s.getArity()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public final String getParameter() {
        return null;
    }

    /**
     * Returns the type of a cell.
     * 
     * @param vm WiMa
     * @param address address
     * @return the type of the cell
     */
    private String getType(final WiMa vm, final int address) {
        return "[" + vm.getHeap().getElementAt(deref(vm, address)).getDisplayType() + "]";
    }
}
