package cyou.keithhacks.ems;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.Hashtable;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Application extends JFrame {
	private static final long serialVersionUID = -2429084325927720912L;

	SoundEffects soundEffects;

	public Application() {
		super();

		soundEffects = SoundEffects.get();

		this.setTitle("EMS");
		this.setSize(960, 640);
		// this.setMinimumSize(new Dimension(960, 640));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		windows = new Hashtable<Integer, JInternalFrame>();

		build();

		this.setVisible(true);
	}

	JMenuBar menuBar;
	JMenu applicationMenu;
	JMenu windowsMenu;

	JMenu lookAndFeelMenu;

	JDesktopPane desktop;

	Hashtable<Integer, JInternalFrame> windows;

	JLabel status;

	void build() {
		desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		this.add(desktop);

		// Build menu bar
		menuBar = new JMenuBar();

		applicationMenu = (JMenu) menuBar.add(new JMenu("Application"));

		JMenuItem help = new JMenuItem("Help...");
		help.addActionListener((ActionEvent e) -> {
			JInternalFrame win = desktop.getSelectedFrame();
			if (win.isIcon())
				win = null;

			addWindow(new HelpWindow(this, win.getClass().getTypeName()));

		});
		applicationMenu.add(help);
		JMenuItem about = new JMenuItem("About...");
		about.addActionListener((ActionEvent e) -> {
			addWindow(new AboutDialog(), true);
		});
		applicationMenu.add(about);
		applicationMenu.addSeparator();

		lookAndFeelMenu = new JMenu("Set Look and Feel");
		buildLookAndFeelMenu();
		applicationMenu.add(lookAndFeelMenu);

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener((ActionEvent e) -> {
			dispose();
		});
		applicationMenu.add(quit);

		windowsMenu = (JMenu) menuBar.add(new JMenu("Windows"));
		buildWindowsMenu();

		// make a gap
		menuBar.add(new JLabel("  "));

		status = new JLabel("(no database open)");
		status.setEnabled(false);
		menuBar.add(status);

		this.setJMenuBar(menuBar);

		// Open main window
		// addWindow(new MainWindow(this));
		addWindow(new StartupWindow(this));
	}

	public void setStatus(String text) {
		status.setText(text);
	}

	void buildLookAndFeelMenu() {
		lookAndFeelMenu.removeAll();
		for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
			JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(lafInfo.getName(), false);
			if (lafInfo.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
				menuItem.setState(true);
			}
			menuItem.addActionListener((ActionEvent e) -> {
				try {
					UIManager.setLookAndFeel(lafInfo.getClassName());
					buildLookAndFeelMenu();
					SwingUtilities.updateComponentTreeUI(this);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
			});
			lookAndFeelMenu.add(menuItem);
		}
	}

	void buildWindowsMenu() {
		// Clear
		windowsMenu.removeAll();

		// Fallback if no windows are open
		if (windows.size() == 0) {
			windowsMenu.add(new JMenuItem("(no windows open)")).setEnabled(false);
			;
			return;
		}

		// getAllFrames() returns in stacking order
		for (JInternalFrame window : desktop.getAllFrames()) {
			int windowID = window.hashCode();

			JMenu windowMenu = new JMenu(window.getTitle());

			windowMenu.add(new JMenuItem(Integer.toString(windowID))).setEnabled(false);
			windowMenu.addSeparator();

			JMenuItem close = new JMenuItem("Close");
			close.setEnabled(window.isClosable());
			close.addActionListener((ActionEvent e) -> {
				try {
					window.setClosed(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			});
			windowMenu.add(close);

			JMenuItem iconify = new JMenuItem(window.isIcon() ? "Restore" : "Iconify");
			iconify.setEnabled(window.isIconifiable());
			iconify.addActionListener((ActionEvent e) -> {
				try {
					window.setIcon(!window.isIcon());
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			});
			windowMenu.add(iconify);

			JMenuItem focus = new JMenuItem("Focus");
			focus.addActionListener((ActionEvent e) -> {
				try {
					JInternalFrame oldWindow = desktop.getSelectedFrame();
					if (oldWindow != null)
						oldWindow.setSelected(false);
					desktop.setSelectedFrame(window);
					window.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			});
			windowMenu.add(focus);

			JMenuItem raise = new JMenuItem(desktop.getComponentZOrder(window) == 0 ? "Lower" : "Raise");
			raise.addActionListener((ActionEvent e) -> {
				if (desktop.getComponentZOrder(window) == 0)
					window.toBack();
				else
					window.toFront();
				buildWindowsMenu();
			});
			windowMenu.add(raise);

			windowsMenu.add(windowMenu);
		}
	}

	public long addWindow(JInternalFrame window, boolean isDialog) {
		int windowID = window.hashCode();

		// register window
		windows.put(windowID, window);
		desktop.add(window);

		// make sure removeWindow() is called, among other things
		window.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosed(InternalFrameEvent e) {
				removeWindow(windowID, isDialog);
			}

			public void internalFrameIconified(InternalFrameEvent e) {
				buildWindowsMenu();
				soundEffects.play(soundEffects.iconify);
			}

			public void internalFrameDeiconified(InternalFrameEvent e) {
				buildWindowsMenu();
				soundEffects.play(soundEffects.deiconify);
			}

			public void internalFrameActivated(InternalFrameEvent e) {
				buildWindowsMenu();
			}

			public void internalFrameDeactivated(InternalFrameEvent e) {
				buildWindowsMenu();
			}
		});

		soundEffects.play(isDialog ? soundEffects.dialogOpen : soundEffects.open);

		try {
			JInternalFrame oldWindow = desktop.getSelectedFrame();
			if (oldWindow != null)
				oldWindow.setSelected(false);
			desktop.setSelectedFrame(window);
			window.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		buildWindowsMenu();

		return windowID;
	}

	public long addWindow(JInternalFrame window) {
		return addWindow(window, false);
	}

	public void removeWindow(int windowID, boolean isDialog) {
		JInternalFrame window = windows.get(Integer.valueOf(windowID));
		if (window == null)
			return;

		windows.remove(Integer.valueOf(windowID));
		desktop.remove(window);

		soundEffects.play(isDialog ? soundEffects.dialogClose : soundEffects.close);

		buildWindowsMenu();
	}

	public void removeWindow(int windowID) {
		removeWindow(windowID, false);
	}

	public static void main(String[] args) {
		// Configure default Metal theme
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		try {
			UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Use the following theme if available:
		String preferredThemeName = "Nimbus";
		for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
			System.out.println(lafInfo.getName());
			try {
				if (lafInfo.getName().equals(preferredThemeName))
					UIManager.setLookAndFeel(lafInfo.getClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unused")
		Application app = new Application();
	}

}
