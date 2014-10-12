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

package jumpvm.ast.pama.designatorpart;

import jumpvm.ast.pama.DesignatorPart;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Selector designator part. */
public class SelectorDesignatorPart extends DesignatorPart {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Selector name. */
    private final String identifier;

    /**
     * Create a new SelectorDesignatorPart.
     *
     * @param begin begin
     * @param end end
     * @param identifier selector name
     */
    public SelectorDesignatorPart(final Location begin, final Location end, final String identifier) {
        super(begin, end);
        this.identifier = identifier;
    }

    /**
     * Returns the selector name.
     *
     * @return the selector name
     */
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public final int getMaxStackSize() {
        return 1;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "." + identifier;
    }
}
