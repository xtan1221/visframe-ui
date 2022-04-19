package builder.visframe.visinstance.run.layoutconfiguration.previewconfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import function.group.ShapeCFG;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * manager for the UI related functionalities of a single opened project in a VFSessionManager
 * @author tanxu
 *
 */
public final class CoreShapeCFGEntryManager {
	static final double LAYOUT_CIRCLE_RADIUS = 3;
	
	////////////////////
	private final VisInstanceRunLayoutPreviewAndConfigManager ownerVisInstanceRunLayoutPreviewAndConfigManager;
	private final ShapeCFG coreShapeCFG;
	//////////////////////
	private CoreShapeCFGEntryController controller;
	
	//
	private Set<Point2D> layoutCoordSet;
	
	private Set<Circle> layoutCircleSet;
	
	private Color color;
	
	private boolean includedInLayout;
	
	private boolean calculated = false;
	/**
	 * constructor
	 * @param visProject
	 */
	public CoreShapeCFGEntryManager(
			VisInstanceRunLayoutPreviewAndConfigManager ownerVisInstanceRunSummaryAndLayoutPreprocessManager,
			ShapeCFG coreShapeCFG
			){
		if(ownerVisInstanceRunSummaryAndLayoutPreprocessManager==null)
			throw new IllegalArgumentException("given ownerVisInstanceRunSummaryAndLayoutPreprocessManager cannot be null!");
		if(coreShapeCFG==null)
			throw new IllegalArgumentException("given coreShapeCFG cannot be null!");
		
		//////////////////
		this.ownerVisInstanceRunLayoutPreviewAndConfigManager = ownerVisInstanceRunSummaryAndLayoutPreprocessManager;
		this.coreShapeCFG = coreShapeCFG;
		////
		this.setColor(randomColor());
		this.layoutCoordSet = new HashSet<>();
		this.includedInLayout = true; //default is on canvass
	}
	
	/**
	 * whether the layout circles have been created and calculated;
	 */
	public boolean isCalculated() {
		return this.calculated;
	}
	/**
	 * 
	 * @param xScalingFactor
	 * @param yScalingFactor
	 */
	public void makeLayoutCircles(double startX, double startY, double xScalingFactor, double yScalingFactor) {
		this.layoutCircleSet = new HashSet<>();
		this.layoutCoordSet.forEach(c->{
			Circle circle = new Circle(
					c.getX()*xScalingFactor+startX,
					c.getY()*yScalingFactor+startY,
					LAYOUT_CIRCLE_RADIUS);
			
			//bind fill color to the color picker
			circle.fillProperty().bind(this.getController().coreShapeCFGColorPicker.valueProperty());
			
			this.layoutCircleSet.add(circle);
		});
		
		this.calculated = true;
	}
	
	
	
	/**
	 * @return the layoutCoordSet
	 */
	public Set<Point2D> getLayoutCoordSet() {
		return layoutCoordSet;
	}


	/**
	 * @return the layoutCircleSet
	 */
	public Set<Circle> getLayoutCircleSet() {
		return layoutCircleSet;
	}

	////////////////////////////////
	public CoreShapeCFGEntryController getController() {
		if(this.controller == null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(CoreShapeCFGEntryController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}


	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		if(modifiable)
			throw new UnsupportedOperationException("not supported?");
		
		this.getController().setModifiable(modifiable);
	}

	
	///////////////////////////////////
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}


	/**
	 * set the color
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		this.getController().coreShapeCFGColorPicker.setValue(this.color);
	}

	
	static Color randomColor() {
		Random rand = new Random();
		
		return new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1);
	}
	
	
	/**
	 * @return the ownerVisInstanceRunSummaryAndLayoutPreprocessManager
	 */
	public VisInstanceRunLayoutPreviewAndConfigManager getOwnerPreviewAndConfigManager() {
		return ownerVisInstanceRunLayoutPreviewAndConfigManager;
	}


	/**
	 * @return the onCanvass
	 */
	public boolean isIncludedInLayout() {
		return includedInLayout;
	}
	
	
	/**
	 * @param onCanvass the onCanvass to set
	 */
	public void setIncludedInLayout(boolean includedInLayout) {
		this.includedInLayout = includedInLayout;
		this.getController().toIncludeInLayoutCheckBox.setSelected(includedInLayout);
	}
	
	/**
	 * @return the coreShapeCFG
	 */
	public ShapeCFG getCoreShapeCFG() {
		return coreShapeCFG;
	}
}
