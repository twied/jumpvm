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
import jumpvm.compiler.bfma.BfMaCompiler;
import jumpvm.compiler.bfma.BfMaDotBackend;
import jumpvm.compiler.bfma.BfMaLexer;
import jumpvm.compiler.bfma.BfMaParser;
import jumpvm.vm.BfMa;

/**
 * BfMachine JumpTab.
 */
public class BfMaTab extends JumpTab {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new BfMaTab.
     */
    public BfMaTab() {
        super(new BfMa());
        setName("Untitled BfMa");
    }

    @Override
    protected final BfMaCompiler createCompiler() {
        return new BfMaCompiler();
    }

    @Override
    protected final BfMaDotBackend createDotBackend() {
        return new BfMaDotBackend();
    }

    @Override
    protected final BfMaParser createParser() {
        return new BfMaParser(new BfMaLexer(createReader()));
    }

    @Override
    public final ImageIcon getIconBig() {
        return Main.getImageIconResource("/icon32/weather-storm.png");
    }

    @Override
    public final ImageIcon getIconSmall() {
        return Main.getImageIconResource("/icon16/weather-storm.png");
    }
}
