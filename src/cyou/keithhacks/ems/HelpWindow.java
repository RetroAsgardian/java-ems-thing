package cyou.keithhacks.ems;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HelpWindow extends JInternalFrame {

	private static final long serialVersionUID = 6121276283616556408L;

	Application app;

	public HelpWindow(Application app, String id) {
		super("Help", true, true, true, true);
		this.app = app;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		build();

		// System.out.println(id);
		updateDocument("Welcome.htm");
		openByID(id);

		this.pack();
		this.setSize(600, 400);
		this.setVisible(true);
	}

	public HelpWindow(Application app) {
		this(app, "default");
	}

	JTree topics;

	JEditorPane contentPane;

	void build() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		buildTopics();
		splitPane.add(new JScrollPane(topics));

		topics.addTreeSelectionListener((TreeSelectionEvent e) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) topics.getLastSelectedPathComponent();
			if (node == null)
				return;
			Topic topic = (Topic) node.getUserObject();
			if ("".equals(topic.file))
				return;

			updateDocument(topic.file);
		});

		contentPane = new JEditorPane();
		contentPane.setContentType("text/html");
		contentPane.setEditable(false);

		contentPane.addHyperlinkListener((HyperlinkEvent e) -> {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				updateDocument(e.getURL());
			}
		});

		splitPane.add(new JScrollPane(contentPane));

		this.add(splitPane);
	}

	protected class Topic {
		String name;
		String file;
		String id;

		public Topic(String name, String file, String id) {
			this.name = name;
			this.file = file;
			this.id = id;
		}

		public String toString() {
			return this.name;
		}
	}

	void buildTopics() {
		URL helptoc = getClass().getClassLoader().getResource("cyou/keithhacks/ems/help/_helptoc.xml");

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(helptoc.toURI().toString());

			doc.getDocumentElement().normalize();

			DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Topic("Help", "", ""));
			addTreeNodes(doc.getDocumentElement().getChildNodes(), root);

			topics = new JTree(root);
		} catch (IOException | ParserConfigurationException | SAXException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	void addTreeNodes(NodeList xmlNodes, DefaultMutableTreeNode treeNode) {
		for (int i = 0; i < xmlNodes.getLength(); i++) {
			Node xmlNode = xmlNodes.item(i);
			if (xmlNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element xmlElement = (Element) xmlNode;

			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new Topic(xmlElement.getAttribute("name"),
					xmlElement.getAttribute("href"), xmlElement.getAttribute("id")));
			if (xmlElement.hasChildNodes()) {
				addTreeNodes(xmlElement.getChildNodes(), childNode);
			}
			treeNode.add(childNode);
		}
	}

	void updateDocument(String file) {
		updateDocument(getClass().getClassLoader().getResource("cyou/keithhacks/ems/help/" + file));
	}

	void updateDocument(URL url) {
		try {
			contentPane.setPage(url);
		} catch (IOException e) {
			contentPane.setText("<html><body><h1>Error</h1><p>Unable to open the requested help topic.</p><p>Path: "
					+ url.toString() + "</p></body></html>");
		}
	}

	void openByID(String id) {
		if ("".equals(id))
			return;
		var top = findByID(id);
		if (top != null)
			updateDocument(top.file);
	}

	Topic findByID(String id) {
		return findByID(id, (DefaultMutableTreeNode) topics.getModel().getRoot());
	}

	Topic findByID(String id, DefaultMutableTreeNode rootNode) {
		Topic top = null;
		Object uo = rootNode.getUserObject();
		if (uo instanceof Topic) {
			top = (Topic) uo;
			if (id.equals(top.id)) {
				return top;
			}
			top = null;
		}
		for (int i = 0; i < rootNode.getChildCount() && top == null; i++) {
			top = findByID(id, (DefaultMutableTreeNode) rootNode.getChildAt(i));
		}
		return top;
	}
}
