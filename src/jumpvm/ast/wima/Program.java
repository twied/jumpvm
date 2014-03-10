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
 * Program.
 */
public class Program extends WiMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Clause list. */
    private final ArrayList<Clause> clauseList;

    /** Query. */
    private final Query query;

    /**
     * Create a new Program.
     * 
     * @param begin begin in resource
     * @param end end in resource
     * @param clauseList clause list
     * @param query query
     */
    public Program(final Location begin, final Location end, final ArrayList<Clause> clauseList, final Query query) {
        super(begin, end);
        this.clauseList = clauseList;
        this.query = query;

        final DefaultMutableTreeNode clauseListNode = new DefaultMutableTreeNode("Clause list");
        final DefaultMutableTreeNode queryNode = new DefaultMutableTreeNode("Queries");
        add(clauseListNode);
        add(queryNode);
        for (final Clause clause : clauseList) {
            clauseListNode.add(clause);
        }
        queryNode.add(query);
    }

    /**
     * Returns the clause list.
     * 
     * @return the clause list
     */
    public final ArrayList<Clause> getClauseList() {
        return clauseList;
    }

    /**
     * Returns the query.
     * 
     * @return the query
     */
    public final Query getQuery() {
        return query;
    }

    @Override
    public final void process(final WiMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Program";
    }
}
