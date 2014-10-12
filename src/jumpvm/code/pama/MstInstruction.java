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
 * Mark stack.
 *
 * <pre>
 * STORE[SP + 2] := base(p, MP);
 * STORE[SP + 3] := MP;
 * STORE[SP + 4] := EP;
 * SP := SP + 5;
 * </pre>
 */
public class MstInstruction extends PaMaInstruction {
    /** Depth difference. */
    private final int p;

    /** Frame name. */
    private final String identifier;

    /**
     * Create a new MstInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param p depth difference
     * @param identifier frame name
     */
    public MstInstruction(final PaMaAstNode sourceNode, final int p, final String identifier) {
        super(sourceNode);
        this.p = p;
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Mark stack";
    }

    @Override
    public final String getMnemonic() {
        return "mst";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(p);
    }
}
