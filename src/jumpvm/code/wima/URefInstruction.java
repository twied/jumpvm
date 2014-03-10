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

/**
 * Unification with bound variable.
 * 
 * <pre>
 * case modus of
 * read:
 *     unify(ST[SP], ST[FP + i]);
 * write:
 *     H[ST[SP]] := ST[FP + i];
 * esac;
 * SP := SP - 1;
 * </pre>
 */
public class URefInstruction extends WiMaInstruction {
    /** Position in current frame. */
    private final int i;

    /** Identifier. */
    private final String identifier;

    /**
     * Create new URefInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param i position in current frame
     * @param identifier identifier
     */
    public URefInstruction(final WiMaAstNode sourceNode, final int i, final String identifier) {
        super(sourceNode);
        this.i = i;
        this.identifier = identifier;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unification with bound variable " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "uref";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(i);
    }
}
