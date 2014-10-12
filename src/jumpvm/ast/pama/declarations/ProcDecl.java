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

package jumpvm.ast.pama.declarations;

import java.util.ArrayList;

import jumpvm.ast.pama.Declarations;
import jumpvm.ast.pama.FormalParameter;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;

/** PaMa procedure declaration. */
public class ProcDecl extends FuncDecl {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new ProcDecl.
     * 
     * @param begin begin
     * @param end end
     * @param identifier procedure name
     * @param formalParameterList list of formal parameters
     * @param declarations declarations
     * @param statementList list of statements
     */
    public ProcDecl(final Location begin, final Location end, final String identifier, final ArrayList<FormalParameter> formalParameterList, final Declarations declarations, final ArrayList<Statement> statementList) {
        super(begin, end, identifier, formalParameterList, null, declarations, statementList);
    }
}
