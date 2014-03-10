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
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.vm.WiMa;

/**
 * Jump to code for procedure.
 * 
 * <pre>
 * FP := SP - (n + 4);
 * ST[FP - 1] := PC;
 * PC := Adr(Code(p/n));
 * </pre>
 */
public class CallInstruction extends WiMaInstruction {
    /** Address of procedure. */
    private final Label address;

    /** Arity of procedure. */
    private final int arity;

    /**
     * Create new Call instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param address Address of procedure
     * @param arity Arity of procedure
     */
    public CallInstruction(final WiMaAstNode sourceNode, final Label address, final int arity) {
        super(sourceNode);
        this.address = address;
        this.arity = arity;
    }

    @Override
    public final void execute(final WiMa vm) throws ExecutionException {
        final Stack stack = vm.getStack();
        final Register pc = vm.getProgramCounter();
        final Register sp = vm.getStackPointer();
        final Register fp = vm.getFramePointer();

        fp.setValue(sp.getValue() - arity - (WiMa.FRAME_SIZE - 1));
        stack.setElementAt(fp.getValue() + WiMa.OFFSET_ADDR_POS, new PointerObject(pc.getValue(), Type.POINTER_PROGRAM, "+PC", "positive return address"));
        pc.setValue(address);
    }

    @Override
    public final String getDisplayHoverText() {
        return "Jump to code for procedure " + address.getName();
    }

    @Override
    public final String getMnemonic() {
        return "call";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(address.getAddress()) + " / " + String.valueOf(arity);
    }
}
