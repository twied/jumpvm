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
 * Unification with structure.
 * 
 * <pre>
 * case modus of
 * read:
 *     v := deref(ST[SP]);
 *     case H[v] of
 *     (STRUCT: f/n):
 *         ST[SP] := v;
 *     (REF: _):
 *         ST[SP] := modus;
 *         SP := SP + 1;
 *         h := new((STRUCT: f/n), NIL, ..., NIL);
 *         ST[SP] := h;
 *         H[v] := (REF: h);
 *         modus := write;
 *         trail(v);
 *     otherwise:
 *         goto backtrack;
 *     esac     
 * write:
 *     H[ST[SP]] := ST[SP + 1] := new((STRUCT: f/n), NIL, ..., NIL);
 *     ST[SP] := modus;
 *     SP := SP + 1;
 * esac;
 * </pre>
 */
public class UStructInstruction extends WiMaInstruction {
    /** Identifier. */
    private final String f;

    /** Arity. */
    private final int n;

    /**
     * Create a new UStructInstruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param f identifier
     * @param n arity
     */
    public UStructInstruction(final WiMaAstNode sourceNode, final String f, final int n) {
        super(sourceNode);
        this.f = f;
        this.n = n;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unification with structure";
    }

    @Override
    public final String getMnemonic() {
        return "ustruct";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(f) + " / " + String.valueOf(n);
    }
}
