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
import jumpvm.compiler.wima.WiMaCompiler;
import jumpvm.compiler.wima.WiMaDotBackend;
import jumpvm.compiler.wima.WiMaLexer;
import jumpvm.compiler.wima.WiMaParser;
import jumpvm.vm.WiMa;

/**
 * WiMachine JumpTab.
 */
public class WiMaTab extends JumpTab {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new WiMaTab.
     */
    public WiMaTab() {
        super(new WiMa());
        setName("untitled WiMa");
    }

    @Override
    protected final WiMaCompiler createCompiler() {
        return new WiMaCompiler();
    }

    @Override
    protected final DotBackend createDotBackend() {
        return new WiMaDotBackend();
    }

    @Override
    protected final WiMaParser createParser() {
        return new WiMaParser(new WiMaLexer(createReader()));
    }

    @Override
    public final ImageIcon getIconBig() {
        return Main.getImageIconResource("/icon32/internet-group-chat.png");
    }

    @Override
    public final ImageIcon getIconSmall() {
        return Main.getImageIconResource("/icon16/internet-group-chat.png");
    }
}
