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
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jumpvm.memory.Register;

/**
 * JumpRegisterPanel.
 */
public class JumpRegisterPanel extends JPanel implements Observer {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** GUI element: Register value text fields. */
    private final HashMap<Register, JTextField> registerMap;

    /**
     * Create a new JumpRegisterPanel.
     * 
     * @param tab JumpTab
     */
    public JumpRegisterPanel(final JumpTab tab) {
        this.registerMap = new HashMap<Register, JTextField>();

        setBorder(new TitledBorder("Registers"));
        setLayout(new GridLayout(0, 2, JumpGui.DEFAULT_SPACING, 0));

        for (final Register register : tab.getVm().getDisplayRegisters()) {
            final JLabel label = new JLabel(register.getShortName() + ":");
            label.setToolTipText(register.getLongName());

            final JTextField textField = new JTextField();
            textField.setHorizontalAlignment(JTextField.RIGHT);
            textField.setEditable(false);
            textField.setToolTipText(register.getLongName());

            add(label);
            add(textField);
            register.addObserver(this);
            registerMap.put(register, textField);
            update(register, null);
        }
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        final JTextField textField = registerMap.get(o);

        if (textField == null) {
            return;
        }

        textField.setText(((Register) o).getDisplayValue());
    }
}
