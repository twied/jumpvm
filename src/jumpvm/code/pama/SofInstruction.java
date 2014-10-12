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
 * Store with offset.
 *
 * <pre>
 * STORE[STORE[SP] + offset] := value;
 * </pre>
 */
public class SofInstruction extends PaMaInstruction {
    /** Offset. */
    private final int offset;

    /** Value. */
    private final int value;

    /** Short description. */
    private final String descriptionShort;

    /** Long description. */
    private final String descriptionLong;

    /**
     * Create a new SofInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param offset position to write
     * @param value value to write
     * @param descriptionShort short description
     * @param descriptionLong long description
     */
    public SofInstruction(final PaMaAstNode sourceNode, final int offset, final int value, final String descriptionShort, final String descriptionLong) {
        super(sourceNode);
        this.offset = offset;
        this.value = value;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        return "Store with offset: " + descriptionLong;
    }

    @Override
    public final String getMnemonic() {
        return "sof";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(offset) + " " + String.valueOf(value);
    }

}
