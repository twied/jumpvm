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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import jumpvm.memory.Memory;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.AtomObject;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.memory.objects.ClosureObject;
import jumpvm.memory.objects.ConsObject;
import jumpvm.memory.objects.FunValObject;
import jumpvm.memory.objects.MemoryObject;
import jumpvm.memory.objects.NilPointerObject;
import jumpvm.memory.objects.PointerObject;
import jumpvm.memory.objects.PointerObject.Type;
import jumpvm.memory.objects.StackObject;
import jumpvm.memory.objects.StructureObject;
import jumpvm.memory.objects.VectorObject;

/**
 * Jump modify memory panel.
 */
public class JumpModifyMemoryPanel extends JTabbedPane {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Pattern: Integer value. */
    private static final Pattern PATTERN_INTEGER = Pattern.compile("^(-?\\d+)$");

    /** Pattern: String value. */
    private static final Pattern PATTERN_STRING = Pattern.compile("^\\w+$");

    /** Pattern: Structure name (i.e. "foo/2"). */
    private static final Pattern PATTERN_STRUCTURE = Pattern.compile("^(\\w+)/(-?\\d+)$");

    /** Pattern: Closure (i.e. "cp=4, gp=5"). */
    private static final Pattern PATTERN_CLOSURE = Pattern.compile("^cp=(-?\\d+),gp=(-?\\d+)$");

    /** Pattern: Function (i.e. "cf=4, fap=5, fgp=6"). */
    private static final Pattern PATTERN_FUNCTION = Pattern.compile("^cf=(-?\\d+),fap=(-?\\d+),fgp=(-?\\d+)$");

    /** Pattern: Vector of Integer (i.e. "[]", "[7]", "[3,4,5]"). */
    private static final Pattern PATTERN_VECTOR = Pattern.compile("^\\[((-?\\d+)(,-?\\d+)*)?\\]$");

    /** Pattern: List constructor (i.e. "hd=4, tl=5"). */
    private static final Pattern PATTERN_CONS = Pattern.compile("hd=(-?\\d+),tl=(-?\\d+)$");

    /** Parent component. */
    private final JumpTab parent;

    /** Map of changed contents. */
    private final HashMap<Memory<MemoryObject>, String[][]> dataMap;

    /**
     * Create a new JumpModifyMemoryPanel.
     * 
     * @param parent parent component
     */
    @SuppressWarnings("unchecked")
    public JumpModifyMemoryPanel(final JumpTab parent) {
        this.parent = parent;
        this.dataMap = new HashMap<Memory<MemoryObject>, String[][]>();

        final JComboBox<String> typeSelectorBox = new JComboBox<String>(new String[] {"NOTHING", "→P ", "→S ", "→H ", " ↛ ", " # ", " ○ ", " Σ ", "clo", "fun", "vec", "Cons"});
        for (final Memory<?> memory : parent.getVm().getDisplayMemories()) {
            if (memory == parent.getVm().getProgram()) {
                continue;
            }

            /* Columns: Index, Type, Value. */
            final String[][] data = new String[memory.getSize() + 2][1 + 1 + 1];

            /* Fill array. */
            for (int i = 0; i < memory.getSize(); ++i) {
                final MemoryObject object = memory.getElementAt(i);
                data[i][0] = String.valueOf(i);
                data[i][1] = object.getDisplayType();
                data[i][2] = object.getDisplayValue();
            }

            /* Append two empty rows. */
            for (int i = memory.getSize(); i < (memory.getSize() + 2); ++i) {
                data[i][0] = String.valueOf(i);
                data[i][1] = "NOTHING";
                data[i][2] = "";
            }

            /* Create and add a table for that array. */
            final JTable table = new JTable(data, new String[] {"Address", "Type", "Value"});
            table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(typeSelectorBox));
            table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            this.addTab(memory.getName(), new JScrollPane(table));
            dataMap.put((Memory<MemoryObject>) memory, data);
        }
    }

    /**
     * Apply the changes to the temporary copy of the memorys.
     */
    public final void apply() {
        for (final Entry<Memory<MemoryObject>, String[][]> entry : dataMap.entrySet()) {
            try {
                apply(entry.getKey(), entry.getValue());
            } catch (final IllegalArgumentException e) {
                JumpGui.showExceptionDialog(parent, e, e.getMessage(), "Unable to change memory");
            }
        }
    }

    /**
     * Apply the changes to one of the memories.
     * 
     * @param memory memory to change
     * @param data temporary, possibly changed copy of that memory
     */
    private void apply(final Memory<MemoryObject> memory, final String[][] data) {
        /* List of MemoryObjects to remove. */
        final ArrayList<MemoryObject> remove = new ArrayList<MemoryObject>();

        for (int i = 0; i < data.length; ++i) {
            /* these contain the possibly changed values. */
            final String typeString = data[i][1];
            final String valueString = data[i][2];

            /* these contain the original values. */
            final String memoryType;
            final String memoryValue;
            if (i >= memory.getSize()) {
                memoryType = "NOTHING";
                memoryValue = "";
            } else {
                final MemoryObject object = memory.getElementAt(i);
                memoryType = object.getDisplayType();
                memoryValue = object.getDisplayValue();
            }

            /* Ignore unchanged rows. */
            if (typeString.equals(memoryType) && valueString.equals(memoryValue)) {
                continue;
            }

            if ("NOTHING".equals(typeString)) {
                remove.add(memory.getElementAt(i));
            } else if ("→P ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString, PATTERN_INTEGER);
                setElement(memory, i, new PointerObject(Integer.valueOf(m.group(0)), Type.POINTER_PROGRAM, null, null));
            } else if ("→S ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString, PATTERN_INTEGER);
                setElement(memory, i, new PointerObject(Integer.valueOf(m.group(0)), Type.POINTER_STACK, null, null));
            } else if ("→H ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString, PATTERN_INTEGER);
                setElement(memory, i, new PointerObject(Integer.valueOf(m.group(0)), Type.POINTER_HEAP, null, null));
            } else if (" ↛ ".equals(typeString)) {
                setElement(memory, i, new NilPointerObject());
            } else if (" # ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString, PATTERN_INTEGER);
                setElement(memory, i, new BasicValueObject(Integer.valueOf(m.group(0)), null, null));
            } else if (" ○ ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString, PATTERN_STRING);
                setElement(memory, i, new AtomObject(m.group(0)));
            } else if (" Σ ".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString.replaceAll("\\s", ""), PATTERN_STRUCTURE);
                setElement(memory, i, new StructureObject(m.group(1), Integer.valueOf(m.group(1 + 1))));
            } else if ("clo".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString.replaceAll("\\s", ""), PATTERN_CLOSURE);
                setElement(memory, i, new ClosureObject(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(1 + 1)), null));
            } else if ("fun".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString.replaceAll("\\s", ""), PATTERN_FUNCTION);
                setElement(memory, i, new FunValObject(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(1 + 1)), Integer.valueOf(m.group(1 + 2)), null));
            } else if ("vec".equals(typeString)) {
                getMatch(memory, i, valueString, PATTERN_VECTOR);
                final ArrayList<Integer> array = new ArrayList<Integer>();
                final String[] values = valueString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split("\\,");
                for (final String value : values) {
                    if (value.isEmpty()) {
                        continue;
                    }
                    array.add(Integer.parseInt(value));
                }
                setElement(memory, i, new VectorObject(array, null));
            } else if ("Cons".equals(typeString)) {
                final Matcher m = getMatch(memory, i, valueString.replaceAll("\\s", ""), PATTERN_CONS);
                setElement(memory, i, new ConsObject(Integer.valueOf(m.group(0)), Integer.valueOf(m.group(1))));
            } else {
                throw new IllegalArgumentException("Unknown type \"" + typeString + "\" in element " + i + " in " + memory.getName());
            }
        }

        final ArrayList<MemoryObject> newMemory = memory.getContent();
        /* Remove deleted rows. */
        newMemory.removeAll(remove);

        /* Fill possible gaps. */
        for (int i = 0; i < newMemory.size(); ++i) {
            if (null == newMemory.get(i)) {
                newMemory.set(i, new BasicValueObject(0, null, null));
            }
        }
        memory.reset(newMemory);
    }

    /**
     * Get a Matcher for the given Pattern or throw an IllegalArgumentException on failure.
     * 
     * @param memory the current memory
     * @param address the current position in the memory
     * @param valueString the desired value
     * @param pattern the pattern this value must fulfill
     * @return a Matcher for the value or an Exception
     */
    private Matcher getMatch(final Memory<MemoryObject> memory, final int address, final String valueString, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(valueString);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Value \"" + valueString + "\" does not match the pattern \"" + pattern.pattern() + "\" in element " + address + " in " + memory.getName());
        }
        return matcher;
    }

    /**
     * Try to set an element in the memory or throw an IllegalArgumentException on failure.
     * 
     * @param memory the current memory
     * @param address the current position in the memory
     * @param object the new object
     */
    private void setElement(final Memory<MemoryObject> memory, final int address, final MemoryObject object) {
        if (memory.getClass() == Stack.class) {
            if (object instanceof StackObject) {
                memory.setElementAt(address, object);
            } else {
                throw new IllegalArgumentException("Non-stack element in stack at address " + address);
            }
        } else {
            memory.setElementAt(address, object);
        }
    }
}
