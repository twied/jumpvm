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

import jumpvm.ast.pama.Range;
import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa array type. */
public class ArrayType extends Type {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Dimensions. */
    private final ArrayList<Range> dimensions;

    /** Base type. */
    private final Type baseType;

    /**
     * Create a new ArrayType.
     *
     * @param begin begin
     * @param end end
     * @param dimensions dimensions
     * @param baseType base type
     */
    public ArrayType(final Location begin, final Location end, final ArrayList<Range> dimensions, final Type baseType) {
        super(begin, end);
        this.dimensions = dimensions;
        this.baseType = baseType;

        if (!dimensions.isEmpty() && (dimensions.get(0) == null)) {
            add(new DefaultMutableTreeNode("[" + dimensions.size() + "]"));
        } else {
            for (final Range range : dimensions) {
                add(range);
            }
        }
        add(baseType);
    }

    /**
     * Returns the element type.
     *
     * @return the element type
     */
    public final Type getBaseType() {
        return baseType;
    }

    /**
     * Returns the size of a descriptor for this array.
     *
     * @return the size of a descriptor for this array
     */
    public final int getDescriptorSize() {
        return (/* d */dimensions.size() + /* u, o */(dimensions.size() * 2)) + 2;
    }

    /**
     * Returns the dimensions.
     * 
     * @return the dimensions
     */
    public final ArrayList<Range> getDimensions() {
        return dimensions;
    }

    @Override
    public final Type getResolvedType() {
        return this;
    }

    @Override
    public final int getSize() {
        int size = baseType.getSize();
        for (final Range range : dimensions) {
            size *= (range.getHigh() - range.getLow()) + 1;
        }
        return size;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Array";
    }
}
