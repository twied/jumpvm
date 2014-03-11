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

package jumpvm.gui;

import javax.swing.ImageIcon;

import jumpvm.Main;
import jumpvm.compiler.DotBackend;
import jumpvm.compiler.mama.MaMaCompiler;
import jumpvm.compiler.mama.MaMaDotBackend;
import jumpvm.compiler.mama.MaMaLexer;
import jumpvm.compiler.mama.MaMaParser;
import jumpvm.vm.MaMa;

/**
 * MaMachine JumpTab.
 */
public class MaMaTab extends JumpTab {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new MaMaTab.
     */
    public MaMaTab() {
        super(new MaMa());
        setName("Untitled MaMa");
    }

    @Override
    protected final MaMaCompiler createCompiler() {
        return new MaMaCompiler();
    }

    @Override
    protected final DotBackend createDotBackend() {
        return new MaMaDotBackend();
    }

    @Override
    protected final MaMaParser createParser() {
        return new MaMaParser(new MaMaLexer(createReader()));
    }

    @Override
    public final ImageIcon getIconBig() {
        return Main.getImageIconResource("/icon32/accessories-calculator.png");
    }

    @Override
    public final ImageIcon getIconSmall() {
        return Main.getImageIconResource("/icon16/accessories-calculator.png");
    }
}
