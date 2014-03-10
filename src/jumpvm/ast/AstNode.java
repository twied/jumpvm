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

package jumpvm.ast;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.compiler.AstWalker;
import jumpvm.compiler.Location;
import jumpvm.exception.CompileException;

/**
 * A single node int the abstract syntax tree parsed from a resource.
 * 
 * @param <T> AstWalker used to process this node further
 */
public abstract class AstNode<T extends AstWalker> extends DefaultMutableTreeNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Begin of this node in the resource. */
    private final Location begin;

    /** End of this node in the resource. */
    private final Location end;

    /**
     * Create a new AstNode with given begin and end locations.
     * 
     * @param begin begin of this node in the resource
     * @param end end of this node in the resource
     */
    public AstNode(final Location begin, final Location end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * Returns the begin of this node in the resource.
     * 
     * @return the begin of this node in the resource
     */
    public final Location getBegin() {
        return begin;
    }

    /**
     * Returns the end of this node in the resource.
     * 
     * @return the end of this node in the resource
     */
    public final Location getEnd() {
        return end;
    }

    /**
     * Process this node with the given {@link AstWalker}.
     * 
     * @param treewalker AstWalker to process this node with
     * @throws CompileException on failure
     */
    public abstract void process(T treewalker) throws CompileException;

    @Override
    public abstract String toString();
}
