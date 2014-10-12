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

import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** For {@link Statement}. */
public class ForStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Counter variable. */
    private final Designator designator;

    /** Start value. */
    private final Expression fromExpression;

    /** End value. */
    private final Expression toExpression;

    /** List of statements. */
    private final ArrayList<Statement> statementList;

    /**
     * Create a new ForStatement.
     *
     * @param begin begin
     * @param end end
     * @param designator counter variable
     * @param fromExpression start value
     * @param toExpression end value
     * @param statementList list of statements
     */
    public ForStatement(final Location begin, final Location end, final Designator designator, final Expression fromExpression, final Expression toExpression, final ArrayList<Statement> statementList) {
        super(begin, end);
        this.designator = designator;
        this.fromExpression = fromExpression;
        this.toExpression = toExpression;
        this.statementList = statementList;

        final DefaultMutableTreeNode statements = new DefaultMutableTreeNode("Statements");
        add(designator);
        add(fromExpression);
        add(toExpression);
        add(statements);
        for (final Statement statement : statementList) {
            statements.add(statement);
        }
    }

    /**
     * Returns the counter variable.
     *
     * @return the counter variable
     */
    public final Designator getDesignator() {
        return designator;
    }

    /**
     * Returns the start value expression.
     *
     * @return the start value expression
     */
    public final Expression getFromExpression() {
        return fromExpression;
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
     * Returns the end value expression.
     *
     * @return the end value expression
     */
    public final Expression getToExpression() {
        return toExpression;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "For";
    }
}
