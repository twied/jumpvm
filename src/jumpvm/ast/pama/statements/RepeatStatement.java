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

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Repeat {@link Statement}. */
public class RepeatStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** List of statements. */
    private final ArrayList<Statement> statementList;

    /** Condition. */
    private final Expression condition;

    /**
     * Create a new RepeatStatement.
     *
     * @param begin begin
     * @param end end
     * @param statementList list of statements
     * @param condition condition
     */
    public RepeatStatement(final Location begin, final Location end, final ArrayList<Statement> statementList, final Expression condition) {
        super(begin, end);
        this.statementList = statementList;
        this.condition = condition;

        final DefaultMutableTreeNode statements = new DefaultMutableTreeNode("Statements");
        add(statements);
        add(condition);
        for (final Statement statement : statementList) {
            statements.add(statement);
        }
    }

    /**
     * Returns the condition expression.
     *
     * @return the condition expression
     */
    public final Expression getCondition() {
        return condition;
    }

    @Override
    public final int getMaxStackSize() {
        int size = condition.getMaxStackSize();
        for (final Statement statement : statementList) {
            size = Math.max(size, statement.getMaxStackSize());
        }
        return size;
    }

    /**
     * Returns the list of statements.
     *
     * @return the list of statements
     */
    public final ArrayList<Statement> getStatementList() {
        return statementList;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Repeat";
    }
}
