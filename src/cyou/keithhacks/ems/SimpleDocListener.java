package cyou.keithhacks.ems;

import java.util.function.Consumer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SimpleDocListener implements DocumentListener {
	
	
	Consumer<DocumentEvent> cons;
	public SimpleDocListener(Consumer<DocumentEvent> cons) {
		this.cons = cons;
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		cons.accept(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		cons.accept(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		cons.accept(e);
	}

}
