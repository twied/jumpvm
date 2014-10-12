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
import jumpvm.compiler.pama.PaMaCompiler;
import jumpvm.compiler.pama.PaMaDotBackend;
import jumpvm.compiler.pama.PaMaLexer;
import jumpvm.compiler.pama.PaMaParser;
import jumpvm.vm.PaMa;

/**
 * PaMachine JumpTab.
 */
public class PaMaTab extends JumpTab {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new PaMaTab.
     */
    public PaMaTab() {
        super(new PaMa());
        setName("Untitled PaMa");
    }

    @Override
    protected final PaMaCompiler createCompiler() {
        return new PaMaCompiler();
    }

    @Override
    protected final PaMaDotBackend createDotBackend() {
        return new PaMaDotBackend();
    }

    @Override
    protected final PaMaParser createParser() {
        return new PaMaParser(new PaMaLexer(createReader()));
    }

    @Override
    public final ImageIcon getIconBig() {
        return Main.getImageIconResource("/icon32/preferences-desktop-multimedia.png");
    }

    @Override
    public final ImageIcon getIconSmall() {
        return Main.getImageIconResource("/icon16/preferences-desktop-multimedia.png");
    }
}
