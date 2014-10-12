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
 * Store object indirect.
 *
 * <pre>
 * STORE[STORE[SP - 1]] := STORE[SP];
 * SP := SP - 2;
 * </pre>
 */
public class StoInstruction extends PaMaInstruction {
    /** Target name. */
    private final String identifier;

    /**
     * Create a new StoInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param identifier target name
     */
    public StoInstruction(final PaMaAstNode sourceNode, final String identifier) {
        super(sourceNode);
        this.identifier = identifier;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Store object indirect " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "sto";
    }

    @Override
    public final String getParameter() {
        return null;
    }
}
