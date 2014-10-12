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
import jumpvm.memory.Label;
import jumpvm.vm.PaMa;

/**
 * Call user procedure.
 *
 * <pre>
 * MP := SP - (size + 4);
 * STORE[MP + 4] := PC;
 * PC := address;
 * </pre>
 */
public class CupInstruction extends PaMaInstruction {
    /** Parameter size. */
    private final int size;

    /** Procedure address. */
    private final Label address;

    /**
     * Create a new CupInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param size Parameter size
     * @param address Procedure address
     */
    public CupInstruction(final PaMaAstNode sourceNode, final int size, final Label address) {
        super(sourceNode);
        this.size = size;
        this.address = address;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Call User Procedure \"" + address.getName() + "\"";
    }

    @Override
    public final String getMnemonic() {
        return "cup";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(size) + " " + String.valueOf(address.getAddress());
    }
}
