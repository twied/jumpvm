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

package jumpvm.exception;

import jumpvm.ast.AstNode;

/**
 * Exception indicating failure to compile an ast node.
 */
public class CompileException extends Exception {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Offending node. */
    private final AstNode<?> node;

    /**
     * Create a new CompileException.
     * 
     * @param node offending ast node
     */
    public CompileException(final AstNode<?> node) {
        super();
        this.node = node;
    }

    /**
     * Create a new CompileException.
     * 
     * @param node offending ast node
     * @param cause the cause for the exception
     */
    public CompileException(final AstNode<?> node, final RuntimeException cause) {
        super(cause);
        this.node = node;
    }

    /**
     * Create a new CompileException.
     * 
     * @param node offending ast node
     * @param message additional message
     */
    public CompileException(final AstNode<?> node, final String message) {
        super(message);
        this.node = node;
    }

    /**
     * Returns the offending ast node.
     * 
     * @return the offending ast node
     */
    public final AstNode<?> getNode() {
        return node;
    }
}
