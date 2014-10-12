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

/** While {@link Statement}. */
public class WhileStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Condition. */
    private final Expression condition;

    /** List of statements. */
    private final ArrayList<Statement> statementList;

    /**
     * Create a new WhileStatement.
     *
     * @param begin begin
     * @param end end
     * @param condition condition
     * @param statementList list of statements
     */
    public WhileStatement(final Location begin, final Location end, final Expression condition, final ArrayList<Statement> statementList) {
        super(begin, end);
        this.condition = condition;
        this.statementList = statementList;

        final DefaultMutableTreeNode statements = new DefaultMutableTreeNode("Statements");
        add(condition);
        add(statements);
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
        return "While";
    }
}
