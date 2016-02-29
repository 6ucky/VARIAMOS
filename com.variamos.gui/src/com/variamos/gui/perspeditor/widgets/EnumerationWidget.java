package com.variamos.gui.perspeditor.widgets;

import java.awt.BorderLayout;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.mxgraph.view.mxGraph;
import com.variamos.perspsupport.instancesupport.InstAttribute;
import com.variamos.perspsupport.perspmodel.ModelInstance;
import com.variamos.perspsupport.syntaxsupport.EditableElementAttribute;
import com.variamos.perspsupport.types.EnumerationSingleSelectionType;

/**
 * A class to support enumeration widgets on the interface. Inspired on other
 * widgets from ProductLine. Part of PhD work at University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-12-01
 * @see com.variamos.gui.pl.editor.widgets
 */
@SuppressWarnings("serial")
public class EnumerationWidget extends WidgetR {

	private JComboBox<String> txtValue;
	private Object[] enumeration;

	public EnumerationWidget() {
		super();

		setLayout(new BorderLayout());
		txtValue = new JComboBox<String>();
		add(txtValue, BorderLayout.CENTER);
		revalidate();
	}

	@Override
	public void configure(EditableElementAttribute v, mxGraph graph,
			ModelInstance semanticModel, boolean showSimulationCustomizationBox) {
		super.configure(v, graph, semanticModel, showSimulationCustomizationBox);
		ClassLoader classLoader = EnumerationSingleSelectionType.class
				.getClassLoader();
		@SuppressWarnings("rawtypes")
		Class aClass = null;
		InstAttribute instAttribute = (InstAttribute) v;
		try {
			aClass = classLoader.loadClass(instAttribute.getAttribute()
					.getClassCanonicalName());
			// System.out.println("aClass.getName() = " + aClass.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		enumeration = aClass.getEnumConstants();
		for (int i = 0; i < enumeration.length; i++) {
			String patternString = "([_])";
			Pattern p = Pattern.compile(patternString);

			String[] split = p.split(enumeration[i].toString());
			String out = split[0] + " ";
			for (int j = 1; j < split.length; j++)
				out += split[j].toLowerCase() + " ";
			txtValue.addItem(out.trim());
			if (instAttribute.getValue() != null
					&& out.equals(instAttribute.getValue()))
				txtValue.setSelectedItem(out);
		}
		if (instAttribute.getValue() == null) {
			txtValue.setSelectedIndex(0);
			instAttribute.setValue((String) txtValue.getSelectedItem());
		}
		revalidate();
		repaint();
	}

	@Override
	protected boolean pushValue(EditableElementAttribute v) {
		txtValue.setSelectedItem((String) v.getValue());
		group.setText((String) v.getGroup());
		revalidate();
		repaint();
		return false;
	}

	@Override
	protected void pullValue(EditableElementAttribute v) {
		v.setValue((String) txtValue.getSelectedItem());
		v.setGroup(group.getText());
	}

	@Override
	public JComponent getEditor() {
		return txtValue;
	}

	@Override
	public JComponent getGroup() {
		return group;
	}
}