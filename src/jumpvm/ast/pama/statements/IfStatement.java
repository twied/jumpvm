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

/** If {@link Statement}. */
public class IfStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Condition expression. */
    private final Expression condition;

    /** Statements for "then". */
    private final ArrayList<Statement> thenStatementList;

    /** Statements for "else". */
    private final ArrayList<Statement> elseStatementList;

    /**
     * Create a new IfStatement.
     *
     * @param begin begin
     * @param end end
     * @param condition condition
     * @param thenStatementList statements for "then"
     * @param elseStatementList statements for "else"
     */
    public IfStatement(final Location begin, final Location end, final Expression condition, final ArrayList<Statement> thenStatementList, final ArrayList<Statement> elseStatementList) {
        super(begin, end);
        this.condition = condition;
        this.thenStatementList = thenStatementList;
        this.elseStatementList = elseStatementList;

        final DefaultMutableTreeNode thenNode = new DefaultMutableTreeNode("then");
        final DefaultMutableTreeNode elseNode = new DefaultMutableTreeNode("else");
        add(condition);
        add(thenNode);
        add(elseNode);
        for (final Statement statement : thenStatementList) {
            thenNode.add(statement);
        }
        for (final Statement statement : elseStatementList) {
            elseNode.add(statement);
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

    /**
     * Returns the list of statements for "else".
     *
     * @return the list of statements for "else"
     */
    public final ArrayList<Statement> getElseStatementList() {
        return elseStatementList;
    }

    @Override
    public final int getMaxStackSize() {
        int size = condition.getMaxStackSize();
        for (final Statement statement : thenStatementList) {
            size = Math.max(size, statement.getMaxStackSize());
        }
        for (final Statement statement : elseStatementList) {
            size = Math.max(size, statement.getMaxStackSize());
        }
        return size;
    }

    /**
     * Returns the list of statements for "then".
     *
     * @return the list of statements for "then"
     */
    public final ArrayList<Statement> getThenStatementList() {
        return thenStatementList;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "If";
    }
}
