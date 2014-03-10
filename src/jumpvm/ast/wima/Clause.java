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

package jumpvm.ast.wima;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.compiler.Location;
import jumpvm.compiler.wima.WiMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Clause.
 */
public class Clause extends WiMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Head. */
    private final Predicate head;

    /** Body. */
    private final ArrayList<Predicate> body;

    /**
     * Create a new Clause.
     * 
     * @param begin begin in resource
     * @param end end in resource
     * @param head head
     * @param body body
     */
    public Clause(final Location begin, final Location end, final Predicate head, final ArrayList<Predicate> body) {
        super(begin, end);
        this.head = head;
        this.body = body;

        final DefaultMutableTreeNode headNode = new DefaultMutableTreeNode("Head");
        add(headNode);
        headNode.add(head);

        if (!body.isEmpty()) {
            final DefaultMutableTreeNode bodyNode = new DefaultMutableTreeNode("Body");
            add(bodyNode);
            for (final Predicate predicate : body) {
                bodyNode.add(predicate);
            }
        }
    }

    /**
     * Returns the body.
     * 
     * @return the body
     */
    public final ArrayList<Predicate> getBody() {
        return body;
    }

    /**
     * Returns the head.
     * 
     * @return the head
     */
    public final Predicate getHead() {
        return head;
    }

    @Override
    public final void process(final WiMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Clause";
    }
}
