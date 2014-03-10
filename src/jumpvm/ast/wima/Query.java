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

import jumpvm.compiler.Location;
import jumpvm.compiler.wima.WiMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Query.
 */
public class Query extends WiMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Predicate list. */
    private final ArrayList<Predicate> predicateList;

    /**
     * Create a new Query.
     * 
     * @param begin begin in resource
     * @param end end in resource
     * @param predicateList predicate list
     */
    public Query(final Location begin, final Location end, final ArrayList<Predicate> predicateList) {
        super(begin, end);
        this.predicateList = predicateList;

        for (final Predicate predicate : predicateList) {
            add(predicate);
        }
    }

    /**
     * Returns the predicate list.
     * 
     * @return the predicate list
     */
    public final ArrayList<Predicate> getPredicateList() {
        return predicateList;
    }

    @Override
    public final void process(final WiMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Query";
    }
}
