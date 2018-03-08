package org.rapidprom.operators.padas;

import javax.swing.JPanel;

import com.rapidminer.gui.properties.PropertyDialog;
import com.rapidminer.gui.wizards.ConfigurationListener;
import com.rapidminer.parameter.ParameterType;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

import org.processmining.database.metamodel.dapoql.ui.components.DAPOQLGroovyQueryField;

import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.parameter.Parameters;
import com.rapidminer.parameter.UndefinedParameterError;


public class DAPOQLangDialog extends PropertyDialog {
	
	private static final long serialVersionUID = -1695902812139200491L;

	private boolean ok = false;

	private final ConfigurationListener listener;

	private DAPOQLGroovyQueryField qF = null;

	public DAPOQLangDialog(ParameterType type, ConfigurationListener listener) {
		super(type, "dapoql-query-editor");
		super.setModalityType(ModalityType.MODELESS);
		this.listener = listener;

		layoutDefault(createMainPanel(), createButtonPanel(), LARGE);
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		qF = new DAPOQLGroovyQueryField();
		String v = "";
		try {
			v = listener.getParameters().getParameter(DAPOQLangQueryOperator.PARAMETER_1);
		} catch (UndefinedParameterError e) {
			e.printStackTrace();
		}
		qF.setQuery(v);
		panel.add(qF);
		
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton okButton = new JButton(new ResourceAction("ok") {
			
			private static final long serialVersionUID = 5836090824300913876L;

			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		buttonPanel.add(okButton);
		
		JButton applyButton = new JButton(new ResourceAction("apply") {
			
			private static final long serialVersionUID = 5836090824300913876L;

			public void actionPerformed(ActionEvent e) {
				apply();
			}
		});
		buttonPanel.add(applyButton);
		
		JButton cancelButton = makeCancelButton("cancel");
		buttonPanel.add(cancelButton);
		getRootPane().setDefaultButton(okButton);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		return bottomPanel;
	}

	protected void ok() {
		ok = true;
		apply();
		dispose();
	}
	
	protected void apply() {
		Parameters parameters = listener.getParameters();
		parameters.setParameter(DAPOQLangQueryOperator.PARAMETER_1, qF.getQuery());
		listener.setParameters(parameters); 
	}

	protected void cancel() {
		ok = false;
		dispose();
	}

	public boolean isOk() {
		return ok;
	}
}
