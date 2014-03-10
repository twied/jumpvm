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

package jumpvm.code.mama;

import jumpvm.ast.mama.MaMaAstNode;

/**
 * Finishing treatment of functions and closures.
 * 
 * <pre>
 * if SP = FP + 1 + n then
 *     PC := ST[FP - 2];
 *     GP := ST[FP];
 *     ST[FP - 2] := ST[SP];
 *     SP := FP - 2;
 *     FP := ST[FP - 1];
 * else
 *     if HP[ST[SP]].tag != FUNVAL then error fi;
 *     let
 *         (FUNVAL: cf, fap, fgp) = HP[ST[SP]]
 *     in
 *         PC := cf;
 *         GP := fgp;
 *         SP := SP - n - 1;
 *         for i := 1 to size(HP[fap].v) do
 *             SP := SP + 1;
 *             ST[SP] := HP[fap].v[i];
 *         od
 *     tel
 * fi
 * </pre>
 */
public class ReturnInstruction extends MaMaInstruction {
    /** Offset. */
    private final int n;

    /**
     * Create a new Return instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param n offset
     */
    public ReturnInstruction(final MaMaAstNode sourceNode, final int n) {
        super(sourceNode);
        this.n = n;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Finishing treatment of functions and closures";
    }

    @Override
    public final String getMnemonic() {
        return "return";
    }

    @Override
    public final String getParameter() {
        return String.valueOf(n);
    }
}
