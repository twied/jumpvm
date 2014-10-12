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
import jumpvm.memory.objects.PointerObject;
import jumpvm.vm.PaMa;

/**
 * Load address.
 *
 * <pre>
 * SP := SP + 1;
 * STORE[SP] := base(p, MP) + q;
 * </pre>
 */
public class LdaInstruction extends PaMaInstruction {
    /** Depth. */
    private final int p;

    /** Relative address. */
    private final int q;

    /** Object name. */
    private final String identifier;

    /**
     * Create a new LdaInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param p depth
     * @param q relative address
     * @param identifier object / address name
     */
    public LdaInstruction(final PaMaAstNode sourceNode, final int p, final int q, final String identifier) {
        super(sourceNode);
        this.p = p;
        this.q = q;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
        vm.push(new PointerObject(base(vm, p, vm.getMarkPointer().getValue()) + q, PointerObject.Type.POINTER_STACK, "→ " + identifier, "Pointer to " + identifier));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Load address of " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "lda";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(p) + " " + String.valueOf(q);
    }
}
