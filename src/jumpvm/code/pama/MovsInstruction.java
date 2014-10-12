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
 * Move (copy, really) static data.
 *
 * <pre>
 * for i := q - 1 downto 0 do
 *     STORE[SP + i] := STORE[STORE[SP] + i];
 * end for
 * SP := SP + q - 1;
 * </pre>
 */
public class MovsInstruction extends PaMaInstruction {
    /** Block size. */
    private final int q;

    /** Element name. */
    private final String identifier;

    /**
     * Create a new MovsInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param q Size of the memory block to copy
     * @param identifier element name
     */
    public MovsInstruction(final PaMaAstNode sourceNode, final int q, final String identifier) {
        super(sourceNode);
        this.q = q;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Move static data " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "movs";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(q);
    }
}
