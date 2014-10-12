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

package jumpvm.code.pama;

import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.vm.PaMa;

/**
 * Call user procedure indirectly.
 *
 * <pre>
 *     STORE[MP + 4] := PC;
 *     PC := STORE[base(p, STORE[MP + 2]) + q];
 * </pre>
 */
public class CupiInstruction extends PaMaInstruction {
    /** Nesting depth. */
    private final int p;

    /** Address. */
    private final int q;

    /** Procedure name or name of the variable. */
    private final String identifier;

    /**
     * Create a new CupiInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param p nesting depth
     * @param q address
     * @param identifier procedure name or name of the variable
     */
    public CupiInstruction(final PaMaAstNode sourceNode, final int p, final int q, final String identifier) {
        super(sourceNode);
        this.p = p;
        this.q = q;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        vm.setElementAt(vm.getMarkPointer().getValue() + PaMa.OFFSET_RSA, new BasicValueObject(vm.getProgramCounter()));
        vm.getProgramCounter().setValue(vm.getElementAt(base(vm, p, vm.getElementAt(vm.getMarkPointer().getValue() + 2).getIntValue()) + q));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Call User Procedure \"" + identifier + "\" Indirectly";
    }

    @Override
    public final String getMnemonic() {
        return "cupi";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(p) + " " + String.valueOf(q);
    }
}
