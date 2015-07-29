import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class SlideshowView extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private SlideshowModel model;
	private SlideshowController controller;
	//private JMenuItem fileNew = new JMenuItem("New");
	private JMenuItem fileOpen = new JMenuItem("Open");
	//private JMenuItem fileSave = new JMenuItem("Save");
	private JMenuItem fileExit = new JMenuItem("Exit");
	//private JMenuItem editUndo = new JMenuItem("Undo");
	private JTextField browseTextField = new JTextField();
	private JTextField captionTextField = new JTextField();
	private JButton browseButton = new JButton("Browse");
	private JButton saveButton = new JButton("Save");
	private JButton addNewButton = new JButton("Add New");
	private JLabel imageLabel = new JLabel();
	private JLabel imageLabel2 = new JLabel();
	private JLabel imageLabel3 = new JLabel();
	private JLabel captionLabel = new JLabel();
	private ButtonGroup imageButtonGroup = new ButtonGroup();
	private JPanel selectInnerPanel = new JPanel();
	private JPanel viewerPanel = new JPanel();
	private JScrollPane selectScrollPane = new JScrollPane(selectInnerPanel);
	private JPanel selectOuterPanel = new JPanel();
	
	private JButton playButton = new JButton ();
	private JButton previousButton = new JButton("Previous");
	private JButton nextButton = new JButton("Next");
	private JSlider delaySlider = new JSlider (JSlider.HORIZONTAL, 10, 10000, 5000);
	private JLabel delayLabel = new JLabel("Delay");
	

	private JToggleButton currentImageButton = null;
	private List<JToggleButton> imageButtons = new ArrayList<>();
	
	private String playButtonText;
	
	public int widthPanel;
	public int heightPanel;

	public SlideshowView() {

		this.model = new SlideshowModel();
		this.controller = new SlideshowController(model, this);

		
		
		setTitle("Slideshow Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1200, 675));
		setMinimumSize(new Dimension(800, 365));

		// Create our Panels
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
		//actionPanel.setPreferredSize(new Dimension (360, 670));
		viewerPanel.setLayout(new BorderLayout());
		JPanel browsePanel = new JPanel(
				new FlowLayout(FlowLayout.CENTER, 0, 10));
		browsePanel.setMaximumSize(new Dimension(500, 60));
		browsePanel.setMinimumSize(new Dimension(500, 60));
		JPanel captionPanel = new JPanel(
				new FlowLayout(FlowLayout.CENTER, 0, 10));
		captionPanel.setMaximumSize(new Dimension(500, 60));
		captionPanel.setMinimumSize(new Dimension(500, 60));
		JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
		savePanel.setMaximumSize(new Dimension(500, 80));
		savePanel.setMinimumSize(new Dimension(500, 80));
		selectOuterPanel.setLayout(new FlowLayout(FlowLayout.CENTER,
				15, 0));
		JPanel addDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,
				15, 10));
		addDeletePanel.setMaximumSize(new Dimension(500, 60));
		addDeletePanel.setMinimumSize(new Dimension(500, 60));
		selectInnerPanel.setLayout(new BoxLayout(selectInnerPanel,
				BoxLayout.Y_AXIS));

		// Create the Menu Bar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		//fileMenu.add(fileNew);
		fileMenu.add(fileOpen);
		//fileMenu.add(fileSave);
		fileMenu.add(fileExit);
		//JMenu editMenu = new JMenu("Edit");
		//menuBar.add(editMenu);
		//editMenu.add(editUndo);
		setJMenuBar(menuBar);
		
		//Create play panel
		JPanel playPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
		playPanel.setMaximumSize(new Dimension(360, 60));
		playButton.setPreferredSize(new Dimension (340, 45));
		
		playButtonText = "Play";
		playButton.setText(playButtonText);
		
		playPanel.add(playButton);
		
		//Create Previous/Next panel
		JPanel nextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 25));
		nextPanel.setMaximumSize(new Dimension(360, 50));
		nextButton.setPreferredSize(new Dimension (164, 45));
		previousButton.setPreferredSize(new Dimension (164, 45));
		
		
		nextPanel.add(previousButton);
		nextPanel.add(nextButton);
		
		
		//Create Panel for "Delay" Label
		JPanel delayLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,  3, 15));
		delayLabelPanel.setMaximumSize(new Dimension (360, 30));
		delayLabelPanel.add(delayLabel);
		
		//Create Panel for slider
		JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		delaySlider.setPreferredSize(new Dimension (330, 20));
		sliderPanel.add(delaySlider);

		// Create the Action Panel
		
		actionPanel.add(playPanel);
		actionPanel.add(nextPanel);
		actionPanel.add(delayLabelPanel);
		actionPanel.add(sliderPanel);
		

		// Create viewer label for image
		imageLabel.setPreferredSize(new Dimension(500, 500));
		imageLabel2.setPreferredSize(new Dimension(500, 500)); //**
		imageLabel3.setPreferredSize(new Dimension(500, 500));
		
		
		imageLabel.setOpaque(true);
		imageLabel2.setOpaque(true);
		imageLabel3.setOpaque(true);
		
		imageLabel.setBackground(Color.WHITE);
		imageLabel2.setBackground(Color.BLACK);  //**
		imageLabel3.setBackground(Color.BLACK);  //**
		
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		imageLabel2.setHorizontalAlignment(JLabel.CENTER); //**
		imageLabel3.setHorizontalAlignment(JLabel.CENTER); //**

		// Create the caption label
		captionLabel.setHorizontalAlignment(JLabel.CENTER);
		captionLabel.setBounds(225, 500, 350, 100);
		captionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Create the Viewer Panel
		
		//viewerPanel.setComponentZOrder(imageLabel2, 0); //**
		viewerPanel.setComponentZOrder(imageLabel, 0);
		
		viewerPanel.setComponentZOrder(captionLabel, 1);
		viewerPanel.add(captionLabel, BorderLayout.CENTER);
		
		//viewerPanel.add(imageLabel2, BorderLayout.CENTER); //**
		viewerPanel.add(imageLabel, BorderLayout.CENTER);

		// Create the Content Pane
		viewerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		getContentPane().add(actionPanel, BorderLayout.WEST);
		getContentPane().add(viewerPanel, BorderLayout.CENTER);

		
		// Create action listeners
		//fileNew.addActionListener(controller.getFileNewActionListener());
		fileOpen.addActionListener(controller.getFileOpenActionListener());
		//fileSave.addActionListener(controller.getFileSaveActionListener());
		fileExit.addActionListener(controller.getFileExitActionListener());
		browseButton.addActionListener(controller
				.getBrowseButtonActionListener());
		saveButton.addActionListener(controller.getSaveButtonActionListener());
		addNewButton.addActionListener(controller.getNewButtonActionListener());

		MouseAdapter captionMouseAdapter = controller.getCaptionMouseAdapter();
		captionLabel.addMouseListener(captionMouseAdapter);
		captionLabel.addMouseMotionListener(captionMouseAdapter);

		//editMenu.addMenuListener(controller.getEditMenuActionListener());
		//editUndo.addActionListener(controller.getEditUndoActionListener());

		// Check for JFrame resize
		addComponentListener(controller.getJFrameResize());
		
		playButton.addActionListener(controller.getPlayButtonActionListener());
		nextButton.addActionListener(controller.getNextButtonActionListener());
		previousButton.addActionListener(controller.getPreviousButtonActionListener());
		delaySlider.addChangeListener(controller.getDelayChangeListener());
		
		
	}


	
	
	public void updateView() {

		// Clear everything
		
		/**
		currentImageButton = null;
		imageButtons.clear();
		selectInnerPanel.removeAll();
		browseTextField.setText("");
		captionTextField.setText("");
		captionLabel.setText("");
		**/
		captionLabel.setLocation(Slide.DEFAULT_X, Slide.DEFAULT_Y);
		
		//imageLabel2.setIcon(null); //**
		imageLabel.setIcon(null);
		
	
		// Create the image buttons from the model
		for (Slide slide : model.getAllSlides()) {
			
			JToggleButton newButton = new JToggleButton();
			imageButtons.add(newButton);
			imageButtonGroup.add(newButton);
			selectInnerPanel.add(newButton);

			// Extract the caption
			String caption = "";
			if (slide.caption != null) {
				caption = slide.caption;
			}

			int x = slide.x;
			int y = slide.y;
			
			
			newButton.setText(String.format("Image %d <%d,%d>: %s",
					imageButtons.indexOf(newButton) + 1, x, y, caption));
			newButton.setHorizontalAlignment(SwingConstants.LEFT);
			newButton.setPreferredSize(new Dimension(325, 50));
			newButton.setMaximumSize(new Dimension(325, 50));
			newButton.addActionListener(controller
					.getSelectButtonActionListener());
			
		}
		
		/*
		 * When opening a slide show, load the first slide
		 */

		// Check to see if the newly opened Slide Show is empty.
		if (!imageButtons.isEmpty()) {
			int index = model.getCurrentIndex();
			currentImageButton = imageButtons.get(index);
			currentImageButton.setSelected(true);

			String caption = model.getSelectedSlide().caption;
			String imagePath = model.getSelectedSlide().imagePath;

			ImageIcon image = null;
			//Image scaledImage = null;
			if (imagePath != null) {
				image = new ImageIcon(imagePath);

				// Scale image icon
				//scaledImage = image.getImage().getScaledInstance(
					//	viewerPanel.getWidth(), viewerPanel.getHeight(),
						//Image.SCALE_SMOOTH);
				//image.setImage(scaledImage);
			}

			// Set the caption location
			int x = model.getSelectedSlide().x;
			int y = model.getSelectedSlide().y;
			captionLabel.setLocation(x, y);

			///*****************************////
			imageLabel.setIcon(image);//********//
			///****************************///
			captionLabel.setText(caption);
			//captionTextField.setText(caption);
			//browseTextField.setText(imagePath);
		}

		selectInnerPanel.repaint();
		viewerPanel.repaint(); // Repaint to fix Z-Order issue for imageLabel
								// and captionLabel
	}


	
	public void updateView2() {
		
		//viewerPanel.add(imageLabel2, BorderLayout.CENTER); //**
		//ImageIcon image = null;
		Image scaledImage = null;
		
		//captionLabel.setLocation(Slide.DEFAULT_X, Slide.DEFAULT_Y);
		
		String caption = model.getSelectedSlide().caption;
		String imagePath = model.getSelectedSlide().imagePath;
		
		ImageIcon image = new ImageIcon(imagePath);
		
		scaledImage = image.getImage().getScaledInstance(
				viewerPanel.getWidth(), viewerPanel.getHeight(),
				Image.SCALE_SMOOTH);
		
		
		
		image.setImage(scaledImage);
		
		int x = model.getSelectedSlide().x;
		int y = model.getSelectedSlide().y;
		captionLabel.setLocation(x, y);
		
		///*****************************////
		imageLabel.setIcon(image);//********//
		///****************************///
		captionLabel.setText(caption);
		
	}
	

	

	
	public void changePlayButton() {
		if (playButtonText.equals("Play"))
		{
			playButton.setText("Pause");
			playButtonText = "Pause";
		}
		else
		{
			playButton.setText("Play");
			playButtonText = "Play";
		}
	}
	
	public JTextField getCaptionTextField() {
		return captionTextField;
	}

	public List<JToggleButton> getImageButtons() {
		return imageButtons;
	}

	public JLabel getCaptionLabel() {
		return captionLabel;
	}

	public JLabel getImageLabel() {
		return imageLabel;
	}
	
	/*
	public JMenuItem getEditUndo() {
		return editUndo;
	}
	*/
	public String getPlayButtonText() {
		return playButtonText;
	}
	
	public JScrollPane getSelectScrollPane(){
		return selectScrollPane;
	}
	
	public JPanel getSelectOuterPanel(){
		return selectOuterPanel;
	}
	
	
	public int getTime () {
		return delaySlider.getValue();
	}
	
	//In addition to redrawing the button to a Specific String, also set the playButtonText variable equal 'text'
	public void redrawPlayButton(String text) {
			playButton.setText(text);
			playButtonText = text;
	}
	
	public Container getFrame () {
		//JFrame f = (JFrame) this.getContentPane();
		
		return this.getContentPane();
	}
	
}