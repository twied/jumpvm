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

package jumpvm.ast.pama;

import java.util.ArrayList;

import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** One limb in a CaseStatement. */
public class CaseLimb extends PaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Value. */
    private final int value;

    /** List of statements. */
    private final ArrayList<Statement> statementList;

    /**
     * Create a new CaseLimb.
     *
     * @param begin begin
     * @param end end
     * @param value value
     * @param statementList list of statements
     */
    public CaseLimb(final Location begin, final Location end, final int value, final ArrayList<Statement> statementList) {
        super(begin, end);
        this.value = value;
        this.statementList = statementList;

        for (final Statement statement : statementList) {
            add(statement);
        }
    }

    /**
     * Returns the list of statements.
     *
     * @return the list of statements
     */
    public final ArrayList<Statement> getStatementList() {
        return statementList;
    }

    /**
     * Returns the value of the limb.
     *
     * @return the value of the limb
     */
    public final int getValue() {
        return value;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return String.valueOf(value);
    }
}
