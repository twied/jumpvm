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
import jumpvm.vm.PaMa;

/**
 * Move (copy, really) dynamic data.
 *
 * <pre>
 * for i := 1 to STORE[MP + q + 1] do
 *     STORE[SP + i] := STORE[STORE[MP + q] + STORE[MP + q + 2] + i - 1];
 * end for
 * STORE[MP + q] := SP + 1 - STORE[MP + q + 2];
 * SP := SP + STORE[MP + q + 1];
 * </pre>
 */
public class MovdInstruction extends PaMaInstruction {
    /** Relative descriptor address. */
    private final int q;

    /** Element name. */
    private final String identifier;

    /**
     * Create a new MovdInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param q Relative descriptor address
     * @param identifier element name
     */
    public MovdInstruction(final PaMaAstNode sourceNode, final int q, final String identifier) {
        super(sourceNode);
        this.q = q;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Move dynamic data for " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "movd";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(q);
    }

}
