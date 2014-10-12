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

package jumpvm.ast.pama.types;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa function type. */
public class FunctionType extends Type {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Function / procedure parameters. */
    private final ArrayList<FormalParameter> parameterList;

    /** Return type. */
    private final Type returnType;

    /**
     * Create a new FunctionType.
     *
     * @param begin begin
     * @param end end
     * @param parameterList list of parameters
     * @param returnType return type ({@code null} for procedures)
     */
    public FunctionType(final Location begin, final Location end, final ArrayList<FormalParameter> parameterList, final Type returnType) {
        super(begin, end);
        this.parameterList = parameterList;
        this.returnType = returnType;

        final DefaultMutableTreeNode parameters = new DefaultMutableTreeNode("Parameters");
        for (final FormalParameter type : parameterList) {
            parameters.add(type);
        }

        add(parameters);
        if (returnType != null) {
            add(returnType);
        }
    }

    /**
     * Returns the list of parameters.
     *
     * @return the list of parameters
     */
    public final ArrayList<FormalParameter> getParameterList() {
        return parameterList;
    }

    @Override
    public final Type getResolvedType() {
        return this;
    }

    /**
     * Returns the return type.
     * 
     * @return the return type or {@code null} for procedures.
     */
    public final Type getReturnType() {
        return returnType;
    }

    @Override
    public final int getSize() {
        return 2;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Function";
    }
}
