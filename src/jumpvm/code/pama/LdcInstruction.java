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
 * Load constant value.
 *
 * <pre>
 * SP := SP + 1;
 * STORE[SP] := value;
 * </pre>
 */
public class LdcInstruction extends PaMaInstruction {
    /** Value to load. */
    private final int value;

    /** Short description. */
    private final String descriptionShort;

    /** Long description. */
    private final String descriptionLong;

    /**
     * Create a new LdcInstruction.
     *
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param value value to load
     * @param descriptionShort short description
     * @param descriptionLong long description
     */
    public LdcInstruction(final PaMaAstNode sourceNode, final int value, final String descriptionShort, final String descriptionLong) {
        super(sourceNode);
        this.value = value;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
    }

    @Override
    public final void execute(final PaMa vm) throws ExecutionException {
    }

    @Override
    public final String getDisplayHoverText() {
        if (descriptionLong != null) {
            return "Load constant (" + descriptionLong + ")";
        } else if (descriptionShort != null) {
            return "Load constant (" + descriptionShort + ")";
        }
        return "Load constant " + value;
    }

    @Override
    public final String getMnemonic() {
        return "ldc";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(value);
    }
}
