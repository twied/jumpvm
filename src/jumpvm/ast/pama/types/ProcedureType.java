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

import jumpvm.ast.pama.FormalParameter;
import jumpvm.compiler.Location;

/** PaMa procedure type. */
public class ProcedureType extends FunctionType {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new ProcedureType.
     * 
     * @param begin begin
     * @param end end
     * @param parameterList list of parameters
     */
    public ProcedureType(final Location begin, final Location end, final ArrayList<FormalParameter> parameterList) {
        super(begin, end, parameterList, null);
    }
}
