import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SlideshowController {

	private SlideshowModel model;
	private SlideshowView view;
	private LinkedList<Action> undo = new LinkedList<>();
	private static int MAX_UNDOS = 10;

	public SlideshowController(SlideshowModel model, SlideshowView view) {
		this.model = model;
		this.view = view;
	}

	public ActionListener getFileNewActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.newSlideshow();
				view.updateView();
				undo.clear();
			}
		};
	}

	public ActionListener getFileOpenActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"slideshow", "slideshow");
				chooser.setFileFilter(filter);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

					try (Scanner in = new Scanner(chooser.getSelectedFile())) {
						// Clear everything
						model.newSlideshow();
						model.openSlideshow(in);
						model.setFilePath(chooser.getSelectedFile().getPath());
						view.updateView();
						undo.clear();
						in.close();
					} catch (FileNotFoundException ex) {
					}
				}
			}
		};
	}

	public ActionListener getFileSaveActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"slideshow", "slideshow");
				chooser.setFileFilter(filter);

				 String prevFilePath = model.getFilePath();
				 if (prevFilePath != null)
				 chooser.setSelectedFile(new File(prevFilePath));

				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

					// Create the path
					String path = chooser.getSelectedFile().getPath();
					if (!path.endsWith(".slideshow")) {
						path += ".slideshow";
					}

					File file = new File(path);
					file.delete();
					try (PrintWriter out = new PrintWriter(file)) {
						model.saveSlideshow(out);
						model.setFilePath(path);
						undo.clear();
						out.close();
					} catch (FileNotFoundException ex) {
					}
				}
			}
		};
	}

	public ActionListener getFileExitActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
	}

	public ActionListener getBrowseButtonActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (model.getSelectedSlide() == null) {
					return;
				}

				String filePath = null;

				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"image", "png", "jpg");
				chooser.setFileFilter(filter);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					filePath = chooser.getSelectedFile().getPath();

					// Clear browse textfield if invalid file; otherwise, show
					// image
					// in viewer
					String extension = filePath.substring(
							filePath.lastIndexOf("."), filePath.length());
					if (!extension.equals(".image")
							&& !extension.equals(".png")
							&& !extension.equals(".jpg")) {

					} else {
						String prevImagePath = model.getSelectedSlide().imagePath;
						int prevIndex = model.getCurrentIndex();
						undo.push(new UndoFilePathAction(prevImagePath,
								prevIndex));
						if (undo.size() > MAX_UNDOS)
							undo.removeFirst();

						model.setImagePath(filePath);
						view.updateView();
					}
				}
			}
		};
	}

	public ActionListener getSaveButtonActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (model.getSelectedSlide() == null) {
					return;
				}

				String prevCaption = model.getSelectedSlide().caption;
				int prevIndex = model.getCurrentIndex();
				undo.push(new UndoCaptionAction(prevCaption, prevIndex));
				if (undo.size() > MAX_UNDOS)
					undo.removeFirst();

				String caption = view.getCaptionTextField().getText();
				model.setCaption(caption);
				view.updateView();
			}
		};
	}

	public ActionListener getNewButtonActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.addSlide();
				model.selectSlide(model.getAllSlides().size() - 1);
				view.updateView();
			}
		};
	}

	public ActionListener getSelectButtonActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (model.getSelectedSlide() == null) {
					return;
				}

				// Find the selected button
				List<JToggleButton> imageButtons = view.getImageButtons();
				for (int i = 0; i < imageButtons.size(); i++) {

					JToggleButton imageButton = imageButtons.get(i);
					if (imageButton.isSelected()) {
						model.selectSlide(i);
					}
				}

				view.updateView();
			}
		};
	}

	public MouseAdapter getCaptionMouseAdapter() {
		return new MouseAdapter() {

			private int dragEventCount = 0;
			private int xCoord = 0;
			private int yCoord = 0;
			private int topBound = 0;
			private int leftBound = 0;
			private int bottomBound = 0;
			private int rightBound = 0;
			private JLabel imageLabel;
			private JLabel captionLabel;
			private boolean drag = false;

			@Override
			public void mousePressed(MouseEvent e) {

				// System.out.println("Pressed: (" +
				// view.getCaptionLabel().getX() + "," +
				// view.getCaptionLabel().getY() + ")");

				if (model.getSelectedSlide() == null) {
					return;
				}

				if (e.getSource() == view.getCaptionLabel()) {

					drag = true;
					imageLabel = view.getImageLabel();
					captionLabel = view.getCaptionLabel();

					// Set bounds
					xCoord = e.getX();
					yCoord = e.getY();
					topBound = imageLabel.getY();
					leftBound = imageLabel.getX();
					rightBound = imageLabel.getWidth()
							- captionLabel.getWidth();
					bottomBound = imageLabel.getHeight()
							- captionLabel.getHeight();

					// Set caption border
					captionLabel.setBorder(BorderFactory
							.createDashedBorder(Color.BLACK));
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				// System.out.println("Released: (" +
				// view.getCaptionLabel().getX() + "," +
				// view.getCaptionLabel().getY() + ")");

				if (model.getSelectedSlide() == null) {
					return;
				}

				drag = false;

				// Save the old x,y
				int prevX = model.getSelectedSlide().x;
				int prevY = model.getSelectedSlide().y;
				int prevIndex = model.getCurrentIndex();
				undo.push(new UndoXYAction(prevX, prevY, prevIndex));
				if (undo.size() > MAX_UNDOS)
					undo.removeFirst();

				// Save the new x,y
				//model.setSlideXY(captionLabel.getX(), captionLabel.getY());
				
				model.setSlideXY(captionLabel.getX(), captionLabel.getY());
				view.updateView();
				// Remove caption border
				captionLabel.setBorder(null);
			}

			@Override
			public void mouseDragged(MouseEvent e) {

				dragEventCount++;

				if (dragEventCount > 10 && drag == true) {

					dragEventCount = 0;
					JComponent component = (JComponent) e.getSource();

					// Adjust to valid coordinates within viewer panel
					if (component.getX() + e.getX() - xCoord < leftBound
							&& component.getY() + e.getY() - yCoord < topBound) {
						view.getCaptionLabel().setLocation(leftBound, topBound); // Top-left
																					// corner
					} else if (component.getX() + e.getX() - xCoord > rightBound
							&& component.getY() + e.getY() - yCoord < topBound) {
						view.getCaptionLabel()
								.setLocation(rightBound, topBound); // Top-right
																	// corner
					} else if (component.getX() + e.getX() - xCoord > rightBound
							&& component.getY() + e.getY() - yCoord > bottomBound) {
						view.getCaptionLabel().setLocation(rightBound,
								bottomBound); // Bottom-right corner
					} else if (component.getX() + e.getX() - xCoord < leftBound
							&& component.getY() + e.getY() > bottomBound) {
						view.getCaptionLabel().setLocation(leftBound,
								bottomBound); // Bottom-left corner
					} else if (component.getX() + e.getX() - xCoord < leftBound) {
						view.getCaptionLabel().setLocation(leftBound,
								component.getY() + e.getY() - yCoord); // Left
																		// bound
					} else if (component.getY() + e.getY() - yCoord < topBound) {
						view.getCaptionLabel().setLocation(
								component.getX() + e.getX() - xCoord, topBound); // Top
																					// bound
					} else if (component.getX() + e.getX() - xCoord > rightBound) {
						view.getCaptionLabel().setLocation(rightBound,
								component.getY() + e.getY() - yCoord); // Right
																		// bound
					} else if (component.getY() + e.getY() > bottomBound) {
						view.getCaptionLabel().setLocation(
								component.getX() + e.getX() - xCoord,
								bottomBound); // Bottom bound
					} else {
						view.getCaptionLabel().setLocation(
								component.getX() + e.getX() - xCoord,
								component.getY() + e.getY() - yCoord); // Within
																		// bounds
					}

				}
			}
		};
	}

	public MenuListener getEditMenuActionListener() {
		return new MenuListener() {

			@Override
			public void menuCanceled(MenuEvent e) {
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuSelected(MenuEvent e) {
				if (undo.isEmpty()) {
					view.getEditUndo().setEnabled(false);
				} else {
					view.getEditUndo().setEnabled(true);
				}
			}
		};
	}

	public ActionListener getEditUndoActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Action undoAction = undo.pop();
				undoAction.actionPerformed(null);

				// Check if undo is still viable
				if (undo.isEmpty()) {
					view.getEditUndo().setEnabled(false);
				} else {
					view.getEditUndo().setEnabled(true);
				}
			}
		};
	}

	public class UndoFilePathAction extends AbstractAction {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private String filePath;
		private int index;

		public UndoFilePathAction(String filePath, int index) {
			this.filePath = filePath;
			this.index = index;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentIndex = model.getCurrentIndex();
			model.selectSlide(index);
			model.setImagePath(filePath);
			model.selectSlide(currentIndex);
			view.updateView();
		}
	}

	public class UndoCaptionAction extends AbstractAction {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private String caption;
		private int index;

		public UndoCaptionAction(String caption, int index) {
			this.caption = caption;
			this.index = index;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentIndex = model.getCurrentIndex();
			model.selectSlide(index);
			model.setCaption(caption);
			model.selectSlide(currentIndex);
			view.updateView();
		}
	}

	public class UndoXYAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int x;
		private int y;
		private int index;

		public UndoXYAction(int x, int y, int index) {
			this.x = x;
			this.y = y;
			this.index = index;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentIndex = model.getCurrentIndex();
			model.selectSlide(index);
			model.setSlideXY(x, y);
			model.selectSlide(currentIndex);
			view.updateView();
		}
	}

	// Update viewer size
	public ComponentAdapter getJFrameResize() {
		return new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// Resize action panel and viewer panel to fit
				view.getSelectScrollPane().setPreferredSize(new Dimension(350, view.getSelectOuterPanel().getHeight()));
				view.updateView();
			}
		};
	}
}
