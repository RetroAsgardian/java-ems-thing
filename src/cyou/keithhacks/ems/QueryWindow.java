package cyou.keithhacks.ems;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import cyou.keithhacks.ems.query.DoubleQueryClause;
import cyou.keithhacks.ems.query.IDQueryClause;
import cyou.keithhacks.ems.query.Query;
import cyou.keithhacks.ems.query.QueryClause;
import cyou.keithhacks.ems.query.QueryClause.QueryField;
import cyou.keithhacks.ems.query.TextQueryClause;

public class QueryWindow extends JInternalFrame {
	private static final long serialVersionUID = -4466607445779607993L;
	
	Application app;
	EmployeeTableModel model;
	Query query;
	public QueryWindow(Application app, EmployeeTableModel model) {
		super("Search database", false, true, false, false);
		this.app = app;
		this.model = model;
		this.query = new Query();
		this.query.clauses.add(new IDQueryClause(IDQueryClause.QueryType.Equals, 0));
		
		build();
		
		this.pack();
		this.setSize(640, this.getSize().height);
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	void rebuild() {
		buildClauses();

		this.pack();
		this.setSize(640, this.getSize().height);
		this.setMinimumSize(this.getSize());
	}
	
	enum MatchType {
		any,
		all
	}
	
	JComboBox<MatchType> queryMatchType;
	JPanel clausesPanel;
	JButton addBtn;
	JButton removeBtn;
	
	void build() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER; 
		
		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEADING));
		this.add(labelContainer, gbc);
		
		labelContainer.add(new JLabel("Find employees that match "));
		
		queryMatchType = new JComboBox<MatchType>(MatchType.class.getEnumConstants());
		if (query.matchAny)
			queryMatchType.setSelectedItem(MatchType.any);
		else
			queryMatchType.setSelectedItem(MatchType.all);
		queryMatchType.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() != ItemEvent.SELECTED)
				return;
			
			if (e.getItem().equals(MatchType.any) != query.matchAny) {
				query.matchAny = e.getItem().equals(MatchType.any);
				// rebuild();
			}
		});
		labelContainer.add(queryMatchType);
		
		labelContainer.add(new JLabel(" of these conditions:"));
		
		clausesPanel = new JPanel();
		clausesPanel.setLayout(new GridBagLayout());
		gbc.weightx = 1.0;
		this.add(clausesPanel, gbc);
		buildClauses();
		
		JPanel addRemoveContainer = new JPanel();
		addRemoveContainer.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(addRemoveContainer, gbc);
		addBtn = new JButton("+");
		addBtn.addActionListener((ActionEvent e) -> {
			query.clauses.add(new IDQueryClause(IDQueryClause.QueryType.Equals, 0));
			rebuild();
		});
		addRemoveContainer.add(addBtn);
		
		removeBtn = new JButton("-");
		removeBtn.addActionListener((ActionEvent e) -> {
			if (query.clauses.size() > 0)
				query.clauses.remove(query.clauses.size() - 1);
			rebuild();
		});
		addRemoveContainer.add(removeBtn);
	}
	
	void buildClauses() {
		clausesPanel.removeAll();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		
		GridBagConstraints gbc2 = (GridBagConstraints) gbc.clone();
		gbc2.gridwidth = GridBagConstraints.REMAINDER;
		gbc2.weightx = 1.0;
		
		for (int i = 0; i < query.clauses.size(); i++) {
			QueryClause clause = query.clauses.get(i);
			// HACK java won't let me put 'i' into a listener unless it's final
			final int finalI = i;
			JComboBox<QueryClause.QueryField> field = new JComboBox<QueryClause.QueryField>(QueryClause.QueryField.class.getEnumConstants());
			field.setSelectedItem(QueryClause.getFieldForQuery(clause));
			field.addItemListener((ItemEvent e) -> {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				
				query.clauses.set(finalI, QueryClause.convertQueryClause(clause, (QueryField) field.getSelectedItem()));
				rebuild();
			});
			clausesPanel.add(field, gbc);
			
			if (clause instanceof IDQueryClause) {
				IDQueryClause idClause = (IDQueryClause) clause;
				
				JComboBox<IDQueryClause.QueryType> type = new JComboBox<IDQueryClause.QueryType>(IDQueryClause.QueryType.class.getEnumConstants());
				type.setSelectedItem(idClause.type);
				type.addItemListener((ItemEvent e) -> {
					if (e.getStateChange() != ItemEvent.SELECTED)
						return;
					
					idClause.type = (IDQueryClause.QueryType) type.getSelectedItem();
				});
				clausesPanel.add(type, gbc);
				
				JSpinner value = new JSpinner(new SpinnerNumberModel(
						Integer.valueOf(idClause.val),
						Integer.valueOf(0),
						Integer.valueOf(999999),
						Integer.valueOf(1)
				));
				value.addChangeListener((ChangeEvent e) -> {
					idClause.val = ((Integer) value.getValue()).intValue();
				});
				clausesPanel.add(value, gbc2);
			} else if (clause instanceof TextQueryClause) {
				TextQueryClause textClause = (TextQueryClause) clause;
				
				JComboBox<TextQueryClause.QueryType> type = new JComboBox<TextQueryClause.QueryType>(TextQueryClause.QueryType.class.getEnumConstants());
				type.setSelectedItem(textClause.type);
				type.addItemListener((ItemEvent e) -> {
					if (e.getStateChange() != ItemEvent.SELECTED)
						return;
					
					textClause.type = (TextQueryClause.QueryType) type.getSelectedItem();
				});
				clausesPanel.add(type, gbc);
				
				JTextField value = new JTextField(textClause.str);
				value.getDocument().addDocumentListener(new SimpleDocListener((DocumentEvent e) -> {
					textClause.str = value.getText();
				}));
				clausesPanel.add(value, gbc2);
			} else if (clause instanceof DoubleQueryClause) {
				DoubleQueryClause doubleClause = (DoubleQueryClause) clause;
				
				JComboBox<DoubleQueryClause.QueryType> type = new JComboBox<DoubleQueryClause.QueryType>(DoubleQueryClause.QueryType.class.getEnumConstants());
				type.setSelectedItem(doubleClause.type);
				type.addItemListener((ItemEvent e) -> {
					if (e.getStateChange() != ItemEvent.SELECTED)
						return;
					
					doubleClause.type = (DoubleQueryClause.QueryType) type.getSelectedItem();
				});
				clausesPanel.add(type, gbc);
				
				JSpinner value = new JSpinner(new SpinnerNumberModel(
						Double.valueOf(doubleClause.val),
						null,
						null,
						Double.valueOf(1.0)
				));
				value.addChangeListener((ChangeEvent e) -> {
					doubleClause.val = ((Double) value.getValue()).doubleValue();
				});
				clausesPanel.add(value, gbc2);
			} else {
				clausesPanel.add(new JLabel("(internal error)"), gbc2);
			}
		}
	}
	
}
