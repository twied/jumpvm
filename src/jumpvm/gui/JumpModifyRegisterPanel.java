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

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jumpvm.memory.Register;
import jumpvm.vm.JumpVM;

/**
 * Jump modify register panel.
 */
public class JumpModifyRegisterPanel extends JPanel {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Value map. */
    private final HashMap<Register, JSpinner> values;

    /**
     * Create a new JumpModifyRegisterPanel.
     * 
     * @param vm underlying JumpVM
     */
    public JumpModifyRegisterPanel(final JumpVM vm) {
        this.values = new HashMap<Register, JSpinner>();

        setLayout(new GridLayout(0, 2));
        add(new JLabel("Name"));
        add(new JLabel("Value", JLabel.RIGHT));

        for (final Register register : vm.getDisplayRegisters()) {
            final JSpinner spinner = new JSpinner(new SpinnerNumberModel(register.getValue(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            values.put(register, spinner);
            add(new JLabel(register.getShortName()));
            add(spinner);
        }
    }

    /**
     * Apply the changed values.
     */
    public final void apply() {
        for (final Entry<Register, JSpinner> entry : values.entrySet()) {
            entry.getKey().setValue(((Number) entry.getValue().getValue()).intValue());
        }
    }
}
