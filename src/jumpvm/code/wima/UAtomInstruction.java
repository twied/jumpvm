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
 * Unification with atom.
 * 
 * <pre>
 * case modus of
 * read:
 *     v := deref(ST[SP]);
 *     SP := SP - 1;
 *     case H[v] of
 *     (ATOM: a):
 *         ;
 *     (REF: _):
 *         H[v] := (REF: new(ATOM: a));
 *         trail(v);
 *     otherwise:
 *         goto backtrack;
 *     esac
 * write:
 *     H[ST[SP]] := new(ATOM: a);
 *     SP := SP - 1;
 * esac
 * </pre>
 */
public class UAtomInstruction extends WiMaInstruction {
    /** Atom identifier. */
    private final String atom;

    /**
     * Create new UAtomInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param atom atom identifier
     */
    public UAtomInstruction(final WiMaAstNode sourceNode, final String atom) {
        super(sourceNode);
        this.atom = atom;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unification with atom " + atom;
    }

    @Override
    public final String getMnemonic() {
        return "uatom";
    }

    @Override
    public final String getParameter() {
        return atom;
    }
}
