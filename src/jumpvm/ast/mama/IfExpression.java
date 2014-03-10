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

package jumpvm.ast.mama;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.compiler.Location;
import jumpvm.compiler.mama.MaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * {@code if ... then ... else} {@link Expression}.
 */
public class IfExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Condition. */
    private final Expression condition;

    /** Then expression. */
    private final Expression thenExpression;

    /** Else expression. */
    private final Expression elseExpression;

    /**
     * Create a new IfExpression.
     * 
     * @param begin begin
     * @param end end
     * @param condition condition
     * @param thenExpression thenExpression
     * @param elseExpression elseExpression
     */
    public IfExpression(final Location begin, final Location end, final Expression condition, final Expression thenExpression, final Expression elseExpression) {
        super(begin, end);
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;

        final DefaultMutableTreeNode ifNode = new DefaultMutableTreeNode("if");
        final DefaultMutableTreeNode thenNode = new DefaultMutableTreeNode("then");
        final DefaultMutableTreeNode elseNode = new DefaultMutableTreeNode("else");
        add(ifNode);
        add(thenNode);
        add(elseNode);
        ifNode.add(condition);
        thenNode.add(thenExpression);
        elseNode.add(elseExpression);
    }

    /**
     * Returns the condition.
     * 
     * @return the condition
     */
    public final Expression getCondition() {
        return condition;
    }

    /**
     * Returns the else expression.
     * 
     * @return the else expression
     */
    public final Expression getElseExpression() {
        return elseExpression;
    }

    /**
     * Returns the then expression.
     * 
     * @return the then expression
     */
    public final Expression getThenExpression() {
        return thenExpression;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "If";
    }
}
