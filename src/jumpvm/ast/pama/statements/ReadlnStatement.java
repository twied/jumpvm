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

package jumpvm.ast.pama.statements;

import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Readln {@link Statement}. */
public class ReadlnStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Target variable. */
    private final Designator designator;

    /**
     * Create a new ReadlnStatement.
     *
     * @param begin begin
     * @param end end
     * @param designator target variable
     */
    public ReadlnStatement(final Location begin, final Location end, final Designator designator) {
        super(begin, end);
        this.designator = designator;
    }

    /**
     * Returns the target Variable.
     *
     * @return the target Variable
     */
    public final Designator getDesignator() {
        return designator;
    }

    @Override
    public final int getMaxStackSize() {
        return designator.getMaxStackSize();
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Readln " + designator;
    }
}
