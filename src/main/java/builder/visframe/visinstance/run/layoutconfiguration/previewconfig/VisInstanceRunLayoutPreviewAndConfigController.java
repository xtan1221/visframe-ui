package builder.visframe.visinstance.run.layoutconfiguration.previewconfig;

import java.io.IOException;
import java.util.Optional;

import org.hipparchus.util.Precision;

import com.google.common.base.Objects;

import graphics.shape.shape2D.fx.utils.VfStrokeDashType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import utils.AlertUtils;
import utils.FXUtils;

public final class VisInstanceRunLayoutPreviewAndConfigController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/layoutconfiguration/previewconfig/VisInstanceRunLayoutPreviewAndConfig.fxml";
	static final double DENSITY_HISTOGRAM_CIRCLE_RADIUS = 2;
	
	////////////////////////////
	private VisInstanceRunLayoutPreviewAndConfigManager manager;
	/**
	 * 
	 * @param manager
	 */
	void setManager(VisInstanceRunLayoutPreviewAndConfigManager manager) {
		this.manager = manager;
		///////
		this.setHeightAndWidthLabel();//
		this.setFullRegionTextFields();
		
		//
		this.prepareCalculation();
		this.addLayoutRegionBoundaryLines();
		this.setRegionCoordinateLabels();
		this.setXAxisDensityHistogram();
		this.setYAxisDensityHistogram();
		
		//
		this.updateCoreShapeCFGListVBox();
		this.layoutSelectedCoreShapeCFGEntities();
		
		this.setSelectFullPreviewRegionCheckBoxSelectedPropertyListener();
		/**
		 * select region for layout
		 */
		this.setCanvassAnchorPaneClickEventHandler();
		
		
		//this will trigger the action that selected region be the full region
		//
		this.selectFullPreviewRegionCheckBox.setSelected(true);
		FXUtils.set2Disable(this.cancelButton, true);//cannot cancel when initialized
		FXUtils.set2Disable(this.finishButton, false);
	}
	
	/**
	 * height and width label for the real pixel size of the full layout region
	 */
	private void setHeightAndWidthLabel() {
		this.heightLabel.setText("Height=".concat(Double.toString(this.getManager().getFullLayoutRegionHeight())));
		this.widthLabel.setText("Width=".concat(Double.toString(this.getManager().getFullLayoutRegionWidth())));
	}
	

	/**
	 * set the TextFields for the start and end x and y coordinate of full layout region in pixel;
	 */
	private void setFullRegionTextFields() {
		this.fullRegionStartXTextField.setText(Double.toString(this.getManager().getFullRegionStartX()));
		this.fullRegionStartYTextField.setText(Double.toString(this.getManager().getFullRegionStartY()));
		
		this.fullRegionEndXTextField.setText(Double.toString(this.getManager().getFullRegionEndX()));
		this.fullRegionEndYTextField.setText(Double.toString(this.getManager().getFullRegionEndY()));
	}
	
	
	////////////
	private double xAxisDensityScalingFactor;
	private double yAxisDensityScalingFactor;
	
	private double xAxisBinLength;
	private double yAxisBinLength;
	
	private double canvassXScalingFactor;
	private double canvassYScalingFactor;
	
	private double startX;
	private double startY;
	//
	private void prepareCalculation() {
		this.xAxisDensityScalingFactor = this.xAxisDensityHistogramAnchorPane.getPrefHeight()/this.getManager().getLargestBinDensity();
		this.yAxisDensityScalingFactor = this.yAxisDensityHistogramAnchorPane.getPrefWidth()/this.getManager().getLargestBinDensity();
		
		this.canvassXScalingFactor = this.canvassAnchorPane.getPrefWidth()/this.getManager().getTotalWidth();
		this.canvassYScalingFactor = this.canvassAnchorPane.getPrefHeight()/this.getManager().getTotalHeight();
	
		this.xAxisBinLength = this.canvassXScalingFactor*this.getManager().getxBinSize();
		this.yAxisBinLength = this.canvassYScalingFactor*this.getManager().getyBinSize();
		
		startX = this.getManager().getBorderLength()*this.canvassXScalingFactor;
		startY = this.getManager().getBorderLength()*this.canvassYScalingFactor;
	}
	
	private void setXAxisDensityHistogram() {
		double previousCenterX = 0;
		double previousCenterY = 0;
		for(int i=0;i<this.getManager().getXAxisBinDensityList().size();i++){
			//
			double centerX = i*this.xAxisBinLength+startX;
			double centerY = 
					this.xAxisDensityHistogramAnchorPane.getPrefHeight()-this.getManager().getXAxisBinDensityList().get(i)*yAxisDensityScalingFactor;
			
			Circle circle = new Circle(centerX, centerY, DENSITY_HISTOGRAM_CIRCLE_RADIUS);
			circle.setFill(Color.RED);
			this.xAxisDensityHistogramAnchorPane.getChildren().add(circle);
			
			if(i>0) {
				Line line = new Line(previousCenterX, previousCenterY, centerX, centerY);
				this.xAxisDensityHistogramAnchorPane.getChildren().add(line);
			}
			
			previousCenterX = centerX;
			previousCenterY = centerY;
		}
	}
	
	private void setYAxisDensityHistogram() {
		double previousCenterX = 0;
		double previousCenterY = 0;
		for(int i=0;i<this.getManager().getYAxisBinDensityList().size();i++){
			
			double centerX = this.getManager().getYAxisBinDensityList().get(i)*xAxisDensityScalingFactor;
			double centerY = i*this.yAxisBinLength+startY;
			
			Circle circle = new Circle(centerX, centerY, DENSITY_HISTOGRAM_CIRCLE_RADIUS);
			circle.setFill(Color.RED);
			this.yAxisDensityHistogramAnchorPane.getChildren().add(circle);
			
			if(i>0) {
				Line line = new Line(previousCenterX, previousCenterY, centerX, centerY);
				this.yAxisDensityHistogramAnchorPane.getChildren().add(line);
			}
			
			previousCenterX = centerX;
			previousCenterY = centerY;
		}
	}	
	
	/////////////////////
	//////on canvass AnchorPane
	private void addLayoutRegionBoundaryLines() {
		Line vLeft = new Line(
				startX, startY, 
				startX, startY+this.canvassYScalingFactor*this.getManager().getFullLayoutRegionHeight());
		vLeft.getStrokeDashArray().addAll(VfStrokeDashType.PATTERN_4.getDashArrayList());
		Line vRight = new Line(
				startX+this.canvassXScalingFactor*this.getManager().getFullLayoutRegionWidth(), startY, 
				startX+this.canvassXScalingFactor*this.getManager().getFullLayoutRegionWidth(), startY+this.canvassYScalingFactor*this.getManager().getFullLayoutRegionHeight());
		vRight.getStrokeDashArray().addAll(VfStrokeDashType.PATTERN_4.getDashArrayList());
		
		Line hUpper = new Line(
				startX, startY,
				startX+this.canvassXScalingFactor*this.getManager().getFullLayoutRegionWidth(), startY);
		hUpper.getStrokeDashArray().addAll(VfStrokeDashType.PATTERN_4.getDashArrayList());
		Line hBottom = new Line(
				startX, startY+this.canvassYScalingFactor*this.getManager().getFullLayoutRegionHeight(),
				startX+this.canvassXScalingFactor*this.getManager().getFullLayoutRegionWidth(), startY+this.canvassYScalingFactor*this.getManager().getFullLayoutRegionHeight());
		hBottom.getStrokeDashArray().addAll(VfStrokeDashType.PATTERN_4.getDashArrayList());
		
		this.canvassAnchorPane.getChildren().add(vLeft);
		this.canvassAnchorPane.getChildren().add(vRight);
		this.canvassAnchorPane.getChildren().add(hUpper);
		this.canvassAnchorPane.getChildren().add(hBottom);
	}
	
	/**
	 * create and add the layout region start and end coordinate label on the canvass AnchorPane
	 */
	private void setRegionCoordinateLabels() {
		Label startCoordinateLabel = new Label();
		startCoordinateLabel.setText(
				"("
				.concat(Double.toString(Precision.round(this.getManager().getFullRegionStartX(), 3)))
				.concat(",")
				.concat(Double.toString(Precision.round(this.getManager().getFullRegionStartY(), 3)))
				.concat(")")
				);
		startCoordinateLabel.setLayoutX(startX);
		startCoordinateLabel.setLayoutY(startY);
		
		this.canvassAnchorPane.getChildren().add(startCoordinateLabel);
		
		////
		Label endCoordinateLabel = new Label();
		endCoordinateLabel.setText(
				"("
				.concat(Double.toString(Precision.round(this.getManager().getFullRegionEndX(), 3)))
				.concat(",")
				.concat(Double.toString(Precision.round(this.getManager().getFullRegionEndY(), 3)))
				.concat(")")
				);
		endCoordinateLabel.setLayoutX(startX+this.canvassXScalingFactor*this.getManager().getFullLayoutRegionWidth());
		endCoordinateLabel.setLayoutY(startY+this.canvassYScalingFactor*this.getManager().getFullLayoutRegionHeight());
		
		this.canvassAnchorPane.getChildren().add(endCoordinateLabel);
	}
	
	//*******************************above are one-time settings, once set, do not change*****************************************
	
	
	/////////////////////////////////
	/**
	 * set or reset the core shapeCFG list based on the CoreShapeCFGIDLayoutOrderList of manager;
	 */
	void updateCoreShapeCFGListVBox() {
		this.coreShapeCFGListVBox.getChildren().clear();
		this.getManager().getCoreShapeCFGIDLayoutOrderList().forEach(cfgid->{
			this.coreShapeCFGListVBox.getChildren().add(this.getManager().getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(cfgid).getController().getRootNodePane());
		});
	}
	
	/**
	 * layout shape entity of each core ShapeCFG on the canvass based on the order of ShapeCFG layout and whether the ShapeCFG is selected to be shown;
	 * should be invoked whenever
	 * 1. a core ShapeCFG is selected or de-selected
	 * 2. layout order of one or more core ShapeCFGs are changed;
	 * 3. 
	 */
	void layoutSelectedCoreShapeCFGEntities() {
		//first remove all existing shape entities on the canvass
		this.canvassAnchorPane.getChildren().clear();
		//need to re-add the layout boundary lines and the coordinate
		this.addLayoutRegionBoundaryLines();
		this.setRegionCoordinateLabels();
		//
		this.getManager().getCoreShapeCFGIDLayoutOrderList().forEach(cfgID->{
			CoreShapeCFGEntryManager manager = this.getManager().getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(cfgID);
			if(manager.isCalculated()) {
				//do nothing
			}else {
				manager.makeLayoutCircles(startX, startY, canvassXScalingFactor, canvassYScalingFactor);
			}
			
			if(manager.isIncludedInLayout()) {
				this.canvassAnchorPane.getChildren().addAll(manager.getLayoutCircleSet());
			}
		});
	}
	
	
	////////////////////////////
	///real coordinate of the selected region (might be negative)
	Double selectedRegionStartX;
	Double selectedRegionStartY;
	Double selectedRegionEndX;
	Double selectedRegionEndY;
	
	void setSelectedRegion(Double x1, Double y1, Double x2, Double y2) {
		this.selectedRegionStartX = x1;
		this.selectedRegionStartY = y1;
		this.selectedRegionEndX = x2;
		this.selectedRegionEndY = y2;
		
		this.selectedRegionStartXTextField.setText(x1==null?"":Double.toString(this.selectedRegionStartX));
		this.selectedRegionStartYTextField.setText(y1==null?"":Double.toString(this.selectedRegionStartY));
		this.selectedRegionEndXTextField.setText(x2==null?"":Double.toString(this.selectedRegionEndX));
		this.selectedRegionEndYTextField.setText(y2==null?"":Double.toString(this.selectedRegionEndY));
	}
	
	/**
	 * 
	 */
	private void setSelectFullPreviewRegionCheckBoxSelectedPropertyListener() {
		this.selectFullPreviewRegionCheckBox.selectedProperty().addListener((o,ov,nv)->{
			//always remove any selected region on canvass first
			this.firstCoord = null;
			this.secondCoord = null;
			if(this.firstCoordCircle!=null) {
				this.canvassAnchorPane.getChildren().remove(this.firstCoordCircle);
				this.firstCoordCircle = null;
			}
			if(this.secondCoordCircle!=null) {
				this.canvassAnchorPane.getChildren().remove(this.secondCoordCircle);
				this.secondCoordCircle = null;
			}
			if(this.regionRect!=null) {
				this.canvassAnchorPane.getChildren().remove(this.regionRect);
				this.regionRect = null;
			}
			//
			if(nv) {//selected, thus use the full region
				this.setSelectedRegion(
						this.getManager().getFullRegionStartX()-this.getManager().getBorderLength(),
						this.getManager().getFullRegionStartY()-this.getManager().getBorderLength(),
						this.getManager().getFullRegionEndX()+this.getManager().getBorderLength(),
						this.getManager().getFullRegionEndY()+this.getManager().getBorderLength()
						);
				
				this.enableFinishAndCancelButton();
			}else {//deselected
				this.setSelectedRegion(null, null, null, null);
			}
		});
	}
	
	/////////////////////
	///fields for select a region on the canvass;
	private Point2D firstCoord;
	private Point2D secondCoord;
	private Circle firstCoordCircle;
	private Circle secondCoordCircle;
	private Rectangle regionRect;
	private double regionRectLayoutX;
	private double regionRectLayoutY;
	private double regionRectWidth;
	private double regionRectHeight;
	
	/**
	 * facilitate to select a layout region;
	 */
	private void setCanvassAnchorPaneClickEventHandler() {
		this.canvassAnchorPane.setOnMouseClicked(e->{
			if(this.selectFullPreviewRegionCheckBox.isSelected()) {
				return;
			}
			if(e.getClickCount()==1) {//single click
				if(this.firstCoord!=null&&this.secondCoord==null) {//second one
					this.secondCoord = new Point2D(e.getX(), e.getY());
					
					this.setSecondCoordCircleAndAddToCanvass();
					
					this.setSelectedRegionRectangleAndAddToCanvass();
					
					//set the selected region TextFields
					double selectedX1 = this.getManager().getFullRegionStartX()-this.getManager().getBorderLength() + this.regionRectLayoutX/this.canvassXScalingFactor;
					double selectedY1 = this.getManager().getFullRegionStartY()-this.getManager().getBorderLength() + this.regionRectLayoutY/this.canvassYScalingFactor;
					double selectedX2 = selectedX1+this.regionRectWidth/this.canvassXScalingFactor;
					double selectedY2 = selectedY1+this.regionRectHeight/this.canvassYScalingFactor;
					
					this.setSelectedRegion(selectedX1,selectedY1,selectedX2,selectedY2);
					
					//
					this.enableFinishAndCancelButton();
					
				}else if(this.firstCoord==null){//first coordinate
					this.firstCoord = new Point2D(e.getX(), e.getY());
					
					setfirstCoordCircleAndAddToCanvass();
				}else {
					//do nothing
				}
				
			}else if(e.getClickCount()==2){//double click to remove any selected coordinates
				this.firstCoord = null;
				this.secondCoord = null;
				if(this.firstCoordCircle!=null) {
					this.canvassAnchorPane.getChildren().remove(this.firstCoordCircle);
					this.firstCoordCircle = null;
				}
				if(this.secondCoordCircle!=null) {
					this.canvassAnchorPane.getChildren().remove(this.secondCoordCircle);
					this.secondCoordCircle = null;
				}
				if(this.regionRect!=null) {
					this.canvassAnchorPane.getChildren().remove(this.regionRect);
					this.regionRect = null;
				}
				
				/////////////////
				this.setSelectedRegion(null, null, null, null);
				
				this.enableFinishAndCancelButton();
			}
			
		});
		
	}
	private void setfirstCoordCircleAndAddToCanvass() {
		this.firstCoordCircle = new Circle(this.firstCoord.getX(), this.firstCoord.getY(), 5);
		this.canvassAnchorPane.getChildren().add(this.firstCoordCircle);
	}
	private void setSecondCoordCircleAndAddToCanvass() {
		this.secondCoordCircle = new Circle(this.secondCoord.getX(), this.secondCoord.getY(), 5);
		this.canvassAnchorPane.getChildren().add(this.secondCoordCircle);
	}
	
	private void setSelectedRegionRectangleAndAddToCanvass() {
		this.regionRect = new Rectangle();
		this.regionRect.setFill(Color.TRANSPARENT);
		this.regionRect.setStroke(Color.RED);
		
		if(this.firstCoord.getX()<this.secondCoord.getX()) {
			regionRectLayoutX = this.firstCoord.getX();
			regionRectWidth = this.secondCoord.getX() - this.firstCoord.getX();
		}else {
			regionRectLayoutX = this.secondCoord.getX();
			regionRectWidth = this.firstCoord.getX() - this.secondCoord.getX();
		}
		
		if(this.firstCoord.getY()<this.secondCoord.getY()) {
			regionRectLayoutY = this.firstCoord.getY();
			regionRectHeight = this.secondCoord.getY()-this.firstCoord.getY();
		}else {
			regionRectLayoutY = this.secondCoord.getY();
			regionRectHeight = this.firstCoord.getY()-this.secondCoord.getY();
		}
		
		this.regionRect.setLayoutX(this.regionRectLayoutX);
		this.regionRect.setLayoutY(this.regionRectLayoutY);
		this.regionRect.setWidth(this.regionRectWidth);
		this.regionRect.setHeight(this.regionRectHeight);
		this.canvassAnchorPane.getChildren().add(this.regionRect);
	}
	
	
	/**
	 * simply add the corresponding shapes to the canvass based on the given region
	 * 
	 * specifically build and add the following shapes to the canvass
	 * 		{@link #firstCoordCircle}
	 * 		{@link #secondCoordCircle}
	 * 		{@link #regionRect}
	 * 
	 */
	void addSelectedRegionShapesOnCanvass(double x1, double y1, double x2, double y2) {
//		double selectedX1 = this.getManager().getFullRegionStartX()-this.getManager().getBorderLength() + this.regionRectLayoutX/this.canvassXScalingFactor;
//		double selectedY1 = this.getManager().getFullRegionStartY()-this.getManager().getBorderLength() + this.regionRectLayoutY/this.canvassYScalingFactor;
//		double selectedX2 = selectedX1+this.regionRectWidth/this.canvassXScalingFactor;
//		double selectedY2 = selectedY1+this.regionRectHeight/this.canvassYScalingFactor;
		
		//calculate the firstCoord and secondCoord on the canvass - reverse of the above calculation
		//see setCanvassAnchorPaneClickEventHandler() 
		double regionRectLayoutX = (x1-this.getManager().getFullRegionStartX() + this.getManager().getBorderLength())*this.canvassXScalingFactor; //this.regionRectLayoutX
		double regionRectLayoutY = (y1-this.getManager().getFullRegionStartY() + this.getManager().getBorderLength())*this.canvassYScalingFactor; //this.regionRectLayoutY
		double regionRectWidth = (x2-x1)*this.canvassXScalingFactor;
		double regionRectHeight = (y2-y1)*this.canvassYScalingFactor;
		this.firstCoord = new Point2D(regionRectLayoutX,regionRectLayoutY);
		this.secondCoord = new Point2D(regionRectLayoutX+regionRectWidth,regionRectLayoutY+regionRectHeight);
		
		
		//remove any existing shapes for previously selected region from the canvass;
		if(this.firstCoordCircle!=null && this.canvassAnchorPane.getChildren().contains(this.firstCoordCircle))
			this.canvassAnchorPane.getChildren().remove(this.firstCoordCircle);
		
		if(this.secondCoordCircle!=null && this.canvassAnchorPane.getChildren().contains(this.secondCoordCircle))
			this.canvassAnchorPane.getChildren().remove(this.secondCoordCircle);
		
		if(this.regionRect!=null && this.canvassAnchorPane.getChildren().contains(this.regionRect))
			this.canvassAnchorPane.getChildren().remove(this.regionRect);
		
		
		///////////////
		this.setfirstCoordCircleAndAddToCanvass();
		this.setSecondCoordCircleAndAddToCanvass();
		this.setSelectedRegionRectangleAndAddToCanvass();
	}
	
	/////////////////////////
	VisInstanceRunLayoutPreviewAndConfigManager getManager() {
		return this.manager;
	}
	
	/**
	 * note that by design, there is always a valid region selected;
	 * 
	 * @return
	 */
	boolean validRegionIsSelected() {
		return this.selectedRegionStartX!=null && this.selectedRegionStartY!=null
				&&this.selectedRegionEndX!=null && this.selectedRegionEndY !=null;
	}
	
	public Parent getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	AnchorPane getCanvassAnchorPane() {
		return this.canvassAnchorPane;
	}
	
	/**
	 * invoked whenever 
	 * 1. the selected region is changed 
	 * 2. core shapeCFG list is changed (either selected or unselected or order changed)
	 */
	public void enableFinishAndCancelButton() {
		FXUtils.set2Disable(this.finishButton, false);
		FXUtils.set2Disable(this.cancelButton, false);
	}
	
	public void disableFinishAndCancelButton() {
		FXUtils.set2Disable(this.finishButton, true);
		FXUtils.set2Disable(this.cancelButton, true);
	}
	
	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		if(modifiable) {
			throw new UnsupportedOperationException("not supported?");
		}else {
			this.disableFinishAndCancelButton();
		
			this.canvassAnchorPane.setMouseTransparent(true);
			
			this.selectFullPreviewRegionCheckBox.setMouseTransparent(true);
			
			FXUtils.set2Disable(this.clearSelectedRegionButton, true);
		}
	}
	
	
	////////
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private HBox controlButtonHBox;
	@FXML
	private Button cancelButton;
	@FXML
	private Button finishButton;
	@FXML
	private AnchorPane canvassAnchorPane;
	@FXML
	private Label heightLabel;
	@FXML
	private AnchorPane xAxisDensityHistogramAnchorPane;
	@FXML
	private Label widthLabel;
	@FXML
	private AnchorPane yAxisDensityHistogramAnchorPane;
	@FXML
	private TextField fullRegionStartXTextField;
	@FXML
	private TextField fullRegionStartYTextField;
	@FXML
	private TextField fullRegionEndXTextField;
	@FXML
	private TextField fullRegionEndYTextField;
	@FXML
	VBox coreShapeCFGListVBox;
	@FXML
	CheckBox selectFullPreviewRegionCheckBox;
	@FXML
	private TextField selectedRegionStartXTextField;
	@FXML
	private TextField selectedRegionStartYTextField;
	@FXML
	private TextField selectedRegionEndXTextField;
	@FXML
	private TextField selectedRegionEndYTextField;
	@FXML
	private Button clearSelectedRegionButton;
	
	// Event Listener on Button[#clearSelectedRegionButton].onAction
	@FXML
	public void clearSelectedRegionButtonOnAction(ActionEvent event) {
		this.selectFullPreviewRegionCheckBox.setSelected(false);
		
		//remove the selected region
		this.firstCoord = null;
		this.secondCoord = null;
		if(this.firstCoordCircle!=null) {
			this.canvassAnchorPane.getChildren().remove(this.firstCoordCircle);
			this.firstCoordCircle = null;
		}
		if(this.secondCoordCircle!=null) {
			this.canvassAnchorPane.getChildren().remove(this.secondCoordCircle);
			this.secondCoordCircle = null;
		}
		if(this.regionRect!=null) {
			this.canvassAnchorPane.getChildren().remove(this.regionRect);
			this.regionRect = null;
		}

		this.selectedRegionStartX = null;
		this.selectedRegionStartY = null;
		this.selectedRegionEndX = null;
		this.selectedRegionEndY = null;
		
		this.selectedRegionStartXTextField.setText("");
		this.selectedRegionStartYTextField.setText("");
		this.selectedRegionEndXTextField.setText("");
		this.selectedRegionEndYTextField.setText("");
		//
		this.enableFinishAndCancelButton();
	}
	
	
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) throws IOException {
		this.getManager().setConfiguration(this.getManager().getMostRecentlySavedConfiguration());
		
		this.disableFinishAndCancelButton();
	}
	
	// Event Listener on Button[#finishButton].onAction
	@FXML
	public void finishButtonOnAction(ActionEvent event) throws IOException {
		if(!this.getManager().currentConfigurationIsFinishable()) {
			AlertUtils.popAlert("ERROR", "Configuration is not finishable!");
			return;
		}else {
			VisInstanceRunLayoutConfigurationWrapper  currentNodeCopyIndexNodeCopyMap = this.getManager().getCurrentVisInstanceRunLayoutConfigurationWrapper();
			if(!Objects.equal(currentNodeCopyIndexNodeCopyMap,this.getManager().getMostRecentlySavedConfiguration())) {
				//current configuration is different from most recently saved one;
				//pop up warning and react accordingly to the response
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Warning!");
				alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//save the currently finished assignment to override the previously saved one;
					this.getManager().setMostRecentlySavedConfiguration(currentNodeCopyIndexNodeCopyMap);
					this.getManager().setMostRecentlySavedConfigurationFinished(true);
					
					//invoke after change made action
					if(this.getManager().getAfterChangeMadeAction()!=null)
						this.getManager().getAfterChangeMadeAction().run();
					
				} else {//action denied, do nothing and return;
				    return;
				}
			}else {
				//
				AlertUtils.popAlert("Warning", "no change has been made since last time saved!");
			}
			
			this.disableFinishAndCancelButton();
		}
	}
}
