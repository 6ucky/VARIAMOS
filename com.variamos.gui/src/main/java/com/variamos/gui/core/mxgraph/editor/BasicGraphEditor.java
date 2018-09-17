package com.variamos.gui.core.mxgraph.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import com.variamos.common.core.exceptions.TechnicalException;
import com.variamos.gui.core.io.ConsoleTextArea;
import com.variamos.gui.maineditor.AbstractGraphEditorFunctions;
import com.variamos.gui.maineditor.MainFrame;
import com.variamos.gui.perspeditor.PerspEditorMenuBar;
import com.variamos.gui.util.ResourcesPathsUtil;

/**
 * Support the structure of VARIAMOS GUI. This class handles actions such as update the title, split the content 
 * inside different panels, create a status bar.
 * This class support the Java Swing library version of mxGrap
 * @author  mxgraph library
 */

public class BasicGraphEditor extends JPanel {
	
	private static final long serialVersionUID = 186586L;

	protected JFrame frame;
	protected mxGraphComponent graphComponent;
	protected mxGraphOutline graphOutline;
	protected JTabbedPane modelsTabPane;
	protected JTabbedPane libraryPane;
	protected mxUndoManager undoManager;
	protected String appTitle;
	protected JLabel statusBar;
	protected File currentFile;

	/**
	 * Flag indicating whether the current graph has been modified
	 */
	protected boolean modified = false;
	protected mxRubberband rubberband;
	protected mxKeyboardHandler keyboardHandler;
	protected AbstractGraphEditorFunctions graphEditorFunctions;

	protected JSplitPane upperPart;
	protected JSplitPane right;
	protected JSplitPane center;
	protected JSplitPane graphAndRight;

	protected int perspective = 2;

	/**
	 * Adds required resources for i18n
	 */
	static {
		try {
			mxResources.add(ResourcesPathsUtil.I18COMMON_USER_INTERFACE_PATH);
		} catch (java.util.MissingResourceException e) {
			throw new TechnicalException("Technical exception: Can't find a resources bundle file in the path "+ResourcesPathsUtil.I18COMMON_USER_INTERFACE_PATH);
			
		}
	}
	
	/**
	 * 
	 */

	public BasicGraphEditor() {

	}

	/**
	 * Constructor for all perspectives
	 * 
	 * @param appTitle
	 * @param component
	 * @param perspective
	 */
	public BasicGraphEditor(MainFrame mainFrame, String appTitle,
			mxGraphComponent component, int perspective) {

		this.frame = mainFrame;
		// Stores and updates the frame title
		this.appTitle = appTitle;

		// Stores a reference to the graph and creates the command history
		graphComponent = component;

		this.perspective = perspective;
		undoManager();
		modelsTabPane = new JTabbedPane();

		// Creates the graph outline component
		graphOutline = new mxGraphOutline(graphComponent);
		graphOutline.setMinimumSize(new Dimension(0, 70));

		// Creates the library pane that contains the tabs with the palettes
		libraryPane = new JTabbedPane();

		libraryPane.setMinimumSize(new Dimension(0, 70));

		libraryPane.setPreferredSize(new Dimension(0, 170));
		libraryPane.setMaximumSize(new Dimension(0, 270));
		// Creates the inner split 1 pane that contains the library with the
		// palettes and the graph outline on the left side of the window

		// add(modelsTabPane, BorderLayout.NORTH);
		center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, modelsTabPane,
				graphComponent);
		center.setDividerLocation(0);
		center.setResizeWeight(0);
		center.setDividerSize(1);
		center.setBorder(null);

		// Creates the inner split 2 pane that contains the library with the
		// palettes and the graph outline on the left side of the window
		right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, libraryPane,
				graphOutline);
		right.setDividerLocation(230);
		right.setResizeWeight(1);
		right.setDividerSize(6);
		right.setBorder(null);

		// Creates the outer split pane that contains the inner split 2 pane and
		// the inner split 1 on the right side of the window
		if (perspective == 4)
			graphAndRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center,
					graphOutline);
		else
			graphAndRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, center,
					right);
		graphAndRight.setOneTouchExpandable(true);
		graphAndRight.setDividerLocation(800);
		graphAndRight.setResizeWeight(1);
		graphAndRight.setDividerSize(6);
		graphAndRight.setBorder(null);

		// Creates another split for the west component
		upperPart = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getLeftComponent(), graphAndRight);
		upperPart.setOneTouchExpandable(false);
		upperPart.setDividerLocation(650);
		upperPart.setDividerSize(6);
		upperPart.setBorder(null);

		JSplitPane everything = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				upperPart, getExtensionsTab());
		everything.setOneTouchExpandable(false);
		everything.setDividerLocation(300);
		everything.setResizeWeight(1);
		everything.setDividerSize(6);
		everything.setBorder(null);
		upperPart.setPreferredSize(new Dimension(600, 600));

		// Creates the status bar
		statusBar = createStatusBar();

		// Display some useful information about repaint events
		installRepaintListener();

		// Puts everything together
		setLayout(new BorderLayout());
		add(everything, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		installToolBar(mainFrame, perspective);

		// Installs rubberband selection and handling for some special
		// keystrokes such as F2, Control-C, -V, X, A etc.
		installHandlers();
		installListeners();
		updateTitle();
	}
	
	public void setLayout(int perspective) {
		switch (perspective) {
		case 0:
			center.setDividerLocation(0);
			upperPart.setDividerLocation(150);
			graphAndRight.setDividerLocation(400);
			graphComponent.setBackgroundImage(null);
			frame.setJMenuBar(new PerspEditorMenuBar(this));
			break;
		case 1:
			center.setDividerLocation(0);
			upperPart.setDividerLocation(0);
			graphAndRight.setDividerLocation(700);
			graphComponent.setBackgroundImage(null);
			frame.setJMenuBar(new PerspEditorMenuBar(this));
			break;
		case 2:
			center.setDividerLocation(25);
			upperPart.setDividerLocation(0);
			graphAndRight.setDividerLocation(700);
			frame.setJMenuBar(new PerspEditorMenuBar(this));
			// frame.setJMenuBar(new ProductLineMenuBar(this));
			graphComponent.setBackgroundImage(null);
			break;
		case 3:
			center.setDividerLocation(0);
			upperPart.setDividerLocation(0);
			graphAndRight.setDividerLocation(700);
			// frame.setJMenuBar(new RequirementsMenuBar(this));
			frame.setJMenuBar(new PerspEditorMenuBar(this));
			break;
		case 4:
			center.setDividerLocation(0);
			upperPart.setDividerLocation(0);
			graphAndRight.setDividerLocation(700);
			// frame.setJMenuBar(new RequirementsMenuBar(this));
			frame.setJMenuBar(new PerspEditorMenuBar(this));
			break;
		}

	}

	public void reloadMenus() {
		frame.setJMenuBar(new PerspEditorMenuBar(this));
	}

	
	//Listeners
	
	protected mxIEventListener undoHandler = new mxIEventListener() {
		@Override
		public void invoke(Object source, mxEventObject evt) {
			undoManager.undoableEditHappened((mxUndoableEdit) evt
					.getProperty("edit"));
		}
	};

	/**
	 * 
	 */
	protected mxIEventListener changeTracker = new mxIEventListener() {
		@Override
		public void invoke(Object source, mxEventObject evt) {
			setModified(true);
		}
	};

	

	

	protected void undoManager() {
		final mxGraph graph = graphComponent.getGraph();

		undoManager = createUndoManager();

		// Do not change the scale and translation after files have been loaded
		// graph.setResetViewOnRootChange(false);

		// Updates the modified flag if the graph model changes
		graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

		// Adds the command history to the model and view
		graph.getModel().addListener(mxEvent.UNDO, undoHandler);
		graph.getView().addListener(mxEvent.UNDO, undoHandler);

		// Keeps the selection in sync with the command history
		mxIEventListener undoHandler = new mxIEventListener() {
			@Override
			public void invoke(Object source, mxEventObject evt) {
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt
						.getProperty("edit")).getChanges();
				graph.setSelectionCells(graph
						.getSelectionCellsForChanges(changes));
			}
		};

		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);
	}

	

	/**
	 * 
	 */
	public void installHandlers() {
		rubberband = new mxRubberband(graphComponent);
		//Shortcuts for handle some actions inside of the GUI
		keyboardHandler = new EditorKeyboardHandler(graphComponent);
	}

	/**
	 * 
	 */
	protected JToolBar installToolBar(MainFrame mainFrame, int perspective) {
		EditorToolBar out = new EditorToolBar(this, JToolBar.HORIZONTAL);
		add(out, BorderLayout.NORTH);
		return out;
	}

	/**
	 * 
	 */
	protected JLabel createStatusBar() {
		JLabel statusBar = new JLabel(mxResources.get("ready"));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

		return statusBar;
	}

	/**
	 * 
	 */
	protected void installRepaintListener() {
		graphComponent.getGraph().addListener(mxEvent.REPAINT,
				new mxIEventListener() {
					@Override
					public void invoke(Object source, mxEventObject evt) {
						String buffer = (graphComponent.getTripleBuffer() != null) ? ""
								: " (unbuffered)";
						mxRectangle dirty = (mxRectangle) evt
								.getProperty("region");

						if (dirty == null) {
							status("Repaint all" + buffer);
						} else {
							status("Repaint: x=" + (int) (dirty.getX()) + " y="
									+ (int) (dirty.getY()) + " w="
									+ (int) (dirty.getWidth()) + " h="
									+ (int) (dirty.getHeight()) + buffer);
						}
					}
				});
	}

	/**
	 * 
	 */
	public EditorPalette[] insertPalettes(String title) {
		String[] paletteNames = title.split(";");
		EditorPalette[] out = new EditorPalette[paletteNames.length];
		for (int i = 0; i < paletteNames.length; i++) {
			final EditorPalette palette = new EditorPalette();
			palette.setName(paletteNames[i]);
			final JScrollPane scrollPane = new JScrollPane(palette);
			scrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			libraryPane.add(paletteNames[i], scrollPane);
			// Updates the widths of the palette if the container size changes
			libraryPane.addComponentListener(new ComponentAdapter() {
				/**
			 * 
			 */
				@Override
				public void componentResized(ComponentEvent e) {
					int w = scrollPane.getWidth()
							- scrollPane.getVerticalScrollBar().getWidth();
					palette.setPreferredWidth(w);
				}

			});
			out[i] = palette;
		}
		return out;
	}

	/**
	 * 
	 */
	public EditorPalette insertPalette(String title) {
		final EditorPalette palette = new EditorPalette();
		palette.setName(title);
		final JScrollPane scrollPane = new JScrollPane(palette);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryPane.add(title, scrollPane);
		// libraryPane.add("2", scrollPane2);
		// Updates the widths of the palettes if the container size changes
		libraryPane.addComponentListener(new ComponentAdapter() {
			/**
			 * 
			 */
			@Override
			public void componentResized(ComponentEvent e) {
				int w = scrollPane.getWidth()
						- scrollPane.getVerticalScrollBar().getWidth();
				palette.setPreferredWidth(w);
			}

		});

		return palette;
	}

	public void clearPalettes() {
		libraryPane.removeAll();
	}

	/**
	 * 
	 */
	protected void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			graphComponent.zoomIn();
		} else {
			graphComponent.zoomOut();
		}

		status(mxResources.get("scale") + ": "
				+ (int) (100 * graphComponent.getGraph().getView().getScale())
				+ "%");
	}

	/**
	 * 
	 */
	protected void showOutlinePopupMenu(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(
				mxResources.get("magnifyPage"));
		item.setSelected(graphOutline.isFitPage());

		item.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				graphOutline.setFitPage(!graphOutline.isFitPage());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(
				mxResources.get("showLabels"));
		item2.setSelected(graphOutline.isDrawLabels());

		item2.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
				graphOutline.repaint();
			}
		});

		JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(
				mxResources.get("buffering"));
		item3.setSelected(graphOutline.isTripleBuffered());

		item3.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
				graphOutline.repaint();
			}
		});

		JPopupMenu menu = new JPopupMenu();
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}

	/**
	 * 
	 */
	protected void showGraphPopupMenu(MouseEvent e) {
		graphEditorFunctions.showGraphPopupMenu(e, graphComponent, this);
		
	}

	/**
	 * @param palette
	 *            Load palette according to instantiated class
	 */

	/**
	 * 
	 */
	protected void mouseLocationChanged(MouseEvent e) {
		status(e.getX() + ", " + e.getY());
	}

	/**
	 * 
	 */
	protected void installListeners() {
		installGraphComponentListeners();
		// Installs mouse wheel listener for zooming
		MouseWheelListener wheelTracker = new MouseWheelListener() {
			/**
			 * 
			 */
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline
						|| e.isControlDown()) {
					BasicGraphEditor.this.mouseWheelMoved(e);
				}
			}

		};

		// Handles mouse wheel events in the outline and graph component
		graphOutline.addMouseWheelListener(wheelTracker);
		// graphComponent.addMouseWheelListener(wheelTracker);

		// Installs the popup menu in the outline
		graphOutline.addMouseListener(new MouseAdapter() {

			/**
			 * 
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				// Handles context menu on the Mac where the trigger is on
				// mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showOutlinePopupMenu(e);
				}
			}

		});

	}

	protected void installGraphComponentListeners() {
		MouseWheelListener wheelTracker = new MouseWheelListener() {
			/**
		 * 
		 */
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphOutline
						|| e.isControlDown()) {
					BasicGraphEditor.this.mouseWheelMoved(e);
				}
			}

		};
		// Handles mouse wheel events in the graph component
		graphComponent.addMouseWheelListener(wheelTracker);

		// Installs the popup menu in the graph component
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			/**
			 * 
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				// Handles context menu on the Mac where the trigger is on
				// mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showGraphPopupMenu(e);
				}
			}

		});

		// Installs a mouse motion listener to display the mouse location
		graphComponent.getGraphControl().addMouseMotionListener(
				new MouseMotionListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * java.awt.event.MouseMotionListener#mouseDragged(java.
					 * awt.event.MouseEvent)
					 */
					@Override
					public void mouseDragged(MouseEvent e) {
						mouseLocationChanged(e);
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * java.awt.event.MouseMotionListener#mouseMoved(java.awt
					 * .event.MouseEvent)
					 */
					@Override
					public void mouseMoved(MouseEvent e) {
						mouseDragged(e);
					}

				});

	}

	/**
	
	/**
	 * 
	 * @param name
	 * @param action
	 * @return a new Action bound to the specified string name
	 */
	public Action bind(String name, final Action action) {
		return bind(name, action, null);
	}

	public Action bind(final JMenuItem menuItem, String identifier,
			final Action action, String iconUrl) {
		AbstractAction newAction = new AbstractAction(identifier,
				(iconUrl != null) ? new ImageIcon(
						BasicGraphEditor.class.getResource(iconUrl)) : null) {
			/**
							 * 
							 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(menuItem, e.getID(), e
						.getActionCommand()));
			}
		};

		newAction.putValue(Action.SHORT_DESCRIPTION, identifier);

		return newAction;
	}

	

	/**
	 * 
	 * @param name
	 * @param action
	 * @return a new Action bound to the specified string name and icon
	 */

	public Action bind(String name, final Action action, String iconUrl) {
		AbstractAction newAction = new AbstractAction(name,
				(iconUrl != null) ? new ImageIcon(
						BasicGraphEditor.class.getResource(iconUrl)) : null) {
			/**
							 * 
							 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(getGraphComponent(), e
						.getID(), e.getActionCommand()));
			}
		};

		newAction.putValue(Action.SHORT_DESCRIPTION,
				action.getValue(Action.SHORT_DESCRIPTION));

		return newAction;
	}

	
	/**
	 * 
	 */
	public void updateTitle() {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null) {
			String title = (currentFile != null) ? currentFile
					.getAbsolutePath() : mxResources.get("newDiagram");

			if (modified) {
				title += "*";
			}

			frame.setTitle(title + " - " + appTitle);
		}
	}

	

	/**
	 * 
	 */
	public void exit() {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null) {
			frame.dispose();
		}
	}

	/**
	 * 
	 */
	public void setLookAndFeel(String clazz) {
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null) {
			try {
				UIManager.setLookAndFeel(clazz);
				SwingUtilities.updateComponentTreeUI(frame);

				// Needs to assign the key bindings again
				keyboardHandler = new EditorKeyboardHandler(graphComponent);
			} catch (Exception e) {
				ConsoleTextArea.addText(e.getStackTrace());
			}
		}
	}

	

	public JFrame createFrame(JMenuBar menuBar) {
		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (menuBar == null)
			setLayout(2);
		else
			frame.setJMenuBar(menuBar);
		// width and height
		frame.setSize(1070, 740);

		// Updates the frame title
		updateTitle();

		return frame;
	}

	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Creates an action that executes the specified layout.
	 * 
	 * @param key
	 *            Key to be used for getting the label from mxResources and also
	 *            to create the layout instance for the commercial graph editor
	 *            example.
	 * @return an action that executes the specified layout
	 */

	public Action graphLayout(final String key, boolean animate) {
		final mxIGraphLayout layout = createLayout(key, animate);

		if (layout != null) {
			return new AbstractAction(mxResources.get(key)) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					final mxGraph graph = graphComponent.getGraph();
					Object cell = graph.getSelectionCell();

					if (cell == null
							|| graph.getModel().getChildCount(cell) == 0) {
						cell = graph.getDefaultParent();
					}

					graph.getModel().beginUpdate();
					try {
						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0)
								+ " ms");
					} finally {
						mxMorphing morph = new mxMorphing(graphComponent, 20,
								1.2, 20);

						morph.addListener(mxEvent.DONE, new mxIEventListener() {
							@Override
							public void invoke(Object sender, mxEventObject evt) {
								graph.getModel().endUpdate();
							}

						});

						morph.startAnimation();
					}

				}

			};
		} else {
			return new AbstractAction(mxResources.get(key)) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(graphComponent,
							mxResources.get("noLayout"));
				}

			};
		}
	}

	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical")) {
				layout = new mxHierarchicalLayout(graph);
			} else if (ident.equals("horizontalHierarchical")) {
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			} else if (ident.equals("verticalTree")) {
				layout = new mxCompactTreeLayout(graph, false);
			} else if (ident.equals("horizontalTree")) {
				layout = new mxCompactTreeLayout(graph, true);
			} else if (ident.equals("parallelEdges")) {
				layout = new mxParallelEdgeLayout(graph);
			} else if (ident.equals("placeEdgeLabels")) {
				layout = new mxEdgeLabelLayout(graph);
			} else if (ident.equals("organicLayout")) {
				layout = new mxOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition")) {
				layout = new mxPartitionLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					@Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalPartition")) {
				layout = new mxPartitionLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					@Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("verticalStack")) {
				layout = new mxStackLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					@Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalStack")) {
				layout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					@Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("circleLayout")) {
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}

	public void setDefaultButton() {
		if (modelsTabPane.getTabCount() > 0)
			modelsTabPane.setSelectedIndex(0);
	}

	public void setSelectedTab(int selected) {
		if (modelsTabPane.getTabCount() > selected)
			modelsTabPane.setSelectedIndex(selected);
	}

	
	
	public int getPerspective() {
		return perspective;
	}

	public void setPerspective(int perspective) {
		this.perspective = perspective;
	}
	protected Component getLeftComponent() {
		return new JPanel();
	}

	protected Component getExtensionsTab() {
		return new JPanel();
	}

	/**
	 * 
	 */
	protected mxUndoManager createUndoManager() {
		return new mxUndoManager();
	}
	

	public void setCurrentFile(File file) {
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);

		if (oldValue != file) {
			updateTitle();
		}
	}

	
	public File getCurrentFile() {
		return currentFile;
	}

	/**
	 * 
	 * @param modified
	 */
	public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;

		firePropertyChange("modified", oldValue, modified);

		if (oldValue != modified) {
			updateTitle();
		}
	}

	/**
	 * 
	 * @return whether or not the current graph has been modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * 
	 */
	public mxGraphComponent getGraphComponent() {
		return graphComponent;
	}

	/**
	 * 
	 */
	public mxGraphOutline getGraphOutline() {
		return graphOutline;
	}

	/**
	 * 
	 */
	public JTabbedPane getLibraryPane() {
		return libraryPane;
	}

	/**
	 * 
	 */
	public mxUndoManager getUndoManager() {
		return undoManager;
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void status(String msg) {
		statusBar.setText(msg);
	}


}
