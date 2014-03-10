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
 * Unification with unbound variable.
 * 
 * <pre>
 * case MODUS of
 * read:
 *     ST[FP + 1] := deref(ST[SP]);
 *     break;
 * write:
 *     H[ST[SP]] := ST[FP + i] := new(REF : HP);
 *     break;
 * esac;
 * SP := SP -1;
 * </pre>
 */
public class UVarInstruction extends WiMaInstruction {
    /** Position in current frame. */
    private final int i;

    /** Identifier. */
    private final String identifier;

    /**
     * Create a new UVarInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param i position in current frame
     * @param identifier identifier
     */
    public UVarInstruction(final WiMaAstNode sourceNode, final int i, final String identifier) {
        super(sourceNode);
        this.i = i;
        this.identifier = identifier;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unification with unbound variable " + identifier;
    }

    @Override
    public final String getMnemonic() {
        return "uvar";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(i);
    }
}
