package builder.visframe.visinstance.run.layoutconfiguration.previewconfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import context.project.VisProjectDBContext;
import function.group.CompositionFunctionGroupID;
import function.group.ShapeCFG;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import visinstance.VisInstance;
import visinstance.run.VisInstanceRun;
import visinstance.run.extractor.CoreShapeCFGCalculatedLayoutCoordinateExtractor;
import visinstance.run.extractor.VisInstanceRunCoreShapeCFGLayoutCoordinateExtractor;

/**
 * manager class for creating previous of a VisInstanceRun and set up configuration for layout on canvass;
 * 
 * 1. preview (VisInstanceRun specific)
 * 		1. histogram of shape entities along the x and y axis;
 * 		2. distribution of shape entities on the 2D space;
 * 		3. find out the estimated(*) full layout region for the full set of core ShapeCFGs;
 * 
 * 2. layout configuration
 * 		1. select layout region
 * 		2. select set of core ShapeCFGs to be included
 * 		3. build the layout order of selected core ShapeCFGs;
 * 
 * @author tanxu
 *
 */
public final class VisInstanceRunLayoutPreviewAndConfigManager {
	public static final int BIN_NUM = 100;
	static final double BORDER_RATIO = 0.1; //of the longer side (of width and height)
	
	/////////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private final VisInstance visInstance;
	private final VisInstanceRun visInstanceRun;
	
	///////////////////////////////////
	/**
	 * 
	 */
	private final String beforeChangeMadeWarningInforText;
	
	/**
	 * action to take AFTER a new copy number assignment of all nodes is saved or reset to initial empty value;
	 */
	private final Runnable afterChangeMadeAction;
	
	/**
	 * the assignment most recently saved; could be a finished one or a default empty one;
	 * 
	 * if empty, a clear all action has been taken and no finished assignment has been saved afterwards;
	 * 
	 * null only if this manager is just initialized;
	 */
	private VisInstanceRunLayoutConfigurationWrapper mostRecentlySavedConfiguration;
	
	/**
	 * whether or not the {@link #mostRecentlySavedNodeCopyNumberAssignment} is a finished assignment or not;
	 */
	private Boolean mostRecentlySavedConfigurationFinished;
	
	////////////
	private VisInstanceRunCoreShapeCFGLayoutCoordinateExtractor visInstanceRunCoreShapeCFGLayoutCoordinateExtractor;
	

	
	//////////////////////////////
	
	/**
	 * the order of each core ShapeCFG in the {@link #coreShapeCFGIDCoreShapeCFGEntryManagerMap};
	 * 
	 * include both these selected and unselected;
	 * 
	 * the order should be updated whenever a swap occurred
	 * 		{@link #swapCoreShapeCFG(int, int)}
	 * 
	 * the order should be consistent with the order in the {@link VisInstanceRunLayoutPreviewAndConfigController#coreShapeCFGListVBox}
	 */
	private List<CompositionFunctionGroupID> coreShapeCFGIDLayoutOrderList;
	
	/**
	 * map from the core ShapeCFG ID to the corresponding CoreShapeCFGEntryManager for all core ShapeCFGs in the VisInstance;
	 */
	private Map<CompositionFunctionGroupID, CoreShapeCFGEntryManager> coreShapeCFGIDCoreShapeCFGEntryManagerMap;
	
	
	///////////////////////////////
	private VisInstanceRunLayoutPreviewAndConfigController controller;

	//***********fields related with summary of the VisInstanceRun; once calculated, will not change;
	
	//the coordinate range for all shape entities' layout 
	private double fullRegionStartX;
	private double fullRegionStartY;
	private double fullRegionEndX;
	private double fullRegionEndY;
	
	private int totalShapeEntityNum = 0;
	
	//
	private double borderLength;//border length with the same scale of the layout; equal to the longer side of layout * BORDER_RATIO
	
	private double totalWidth;//border length * 2 + layout width;
	private double totalHeight;//border length * 2 + layout height;
	
	//////////////////////////////////////
	//size of bin at x axis of the real layout scale
	private double xBinSize;
	private double yBinSize;
	
	/**
	 * list of number of shape entities on BINs on x axis
	 * x axis is divided into BIN_NUM bins;
	 */
	private List<Integer> xAxisBinCountList;
	
	/**
	 * list of number of shape entities on BINs on y axis
	 * y axis is divided into BIN_NUM bins;
	 */
	private List<Integer> yAxisBinCountList;
	
	/**
	 * the largest bin density (on X AND Y axis) relative to the total count;
	 * sum of all densities of all bins on x/y axis is equal to 1;
	 */
	private double largestBinDensity;
	
	/**
	 * bin densities on X axis;
	 * sum of those values should be 1
	 */
	private List<Double> xAxisBinDensityList;
	
	/**
	 * bin densities on X axis;
	 * sum of those values should be 1
	 */
	private List<Double> yAxisBinDensityList;
	
	

	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @param visInstance
	 * @param visInstanceRun
	 * @param beforeChangeMadeWarningInforText
	 * @param afterChangeMadeAction
	 * @throws SQLException
	 * @throws IOException
	 */
	public VisInstanceRunLayoutPreviewAndConfigManager(
			VisProjectDBContext hostVisProjectDBContext,
			VisInstance visInstance,
			VisInstanceRun visInstanceRun,
			String beforeChangeMadeWarningInforText,
			Runnable afterChangeMadeAction
			) throws SQLException, IOException{
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		if(visInstance==null)
			throw new IllegalArgumentException("given visInstance cannot be null!");
		if(visInstanceRun==null)
			throw new IllegalArgumentException("given visInstanceRun cannot be null!");
		if(beforeChangeMadeWarningInforText==null)
			throw new IllegalArgumentException("given beforeChangeMadeWarningInforText cannot be null!");
		if(afterChangeMadeAction==null)
			throw new IllegalArgumentException("given afterChangeMadeAction cannot be null!");
		
		
		///////////////////////
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.visInstance = visInstance;
		this.visInstanceRun = visInstanceRun;
		this.beforeChangeMadeWarningInforText = beforeChangeMadeWarningInforText;
		this.afterChangeMadeAction = afterChangeMadeAction;
		
		/////////////////////////
		this.preprocess();
		
		this.extractLayoutCoordinates();
		this.calculateBorderAndTotalSize();
		this.countBins();
		this.calculateBinDensities();
		
		this.initializeConfiguration();
	}
	
	///////////////////////////////////////
	/**
	 * @throws SQLException 
	 */
	private void preprocess() throws SQLException {
		
		this.visInstanceRunCoreShapeCFGLayoutCoordinateExtractor = 
				new VisInstanceRunCoreShapeCFGLayoutCoordinateExtractor(this.hostVisProjectDBContext, this.visInstanceRun);
		
		this.coreShapeCFGIDCoreShapeCFGEntryManagerMap = new HashMap<>();
		this.coreShapeCFGIDLayoutOrderList = new ArrayList<>();
		this.visInstance.getCoreShapeCFGIDSet().forEach(cfgid->{
			try {
				ShapeCFG coreShapeCFG = (ShapeCFG) this.hostVisProjectDBContext.getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(cfgid);
				this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.put(cfgid, new CoreShapeCFGEntryManager(this, coreShapeCFG));
				this.coreShapeCFGIDLayoutOrderList.add(cfgid);
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		});
	}

	/**
	 * for each core ShapeCFG, extract the layout properties values;
	 * 
	 * 1. for each core ShapeCFG
	 * 		1. find out the layout related targets
	 * 		2. find out the assigned CompositionFunction of those targets;
	 * 		3. build sql query string to select those calculated targets from the corresponding target value tables for each RUID value of the owner record data table;
	 * 		4. run the sql query and process layout related targets value for each RUID value;
	 * @throws SQLException 
	 * 
	 */
	private void extractLayoutCoordinates() throws SQLException {
		CoreShapeCFGCalculatedLayoutCoordinateExtractor shapeCFGExtractor;
		Point2D layoutCoord;
		while((shapeCFGExtractor=this.visInstanceRunCoreShapeCFGLayoutCoordinateExtractor.nextCoreShapeCFGExtractor())!=null) {
			while((layoutCoord=shapeCFGExtractor.nextRecord())!=null) {
				this.totalShapeEntityNum++;
				
				/////
				if(this.fullRegionStartX>layoutCoord.getX())
					this.fullRegionStartX = layoutCoord.getX();
				if(this.fullRegionStartY>layoutCoord.getY())
					this.fullRegionStartY = layoutCoord.getY();
				if(this.fullRegionEndX<layoutCoord.getX())
					this.fullRegionEndX = layoutCoord.getX();
				if(this.fullRegionEndY<layoutCoord.getY())
					this.fullRegionEndY = layoutCoord.getY();
				
				this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(shapeCFGExtractor.getCoreShapeCFG().getID()).getLayoutCoordSet().add(layoutCoord);
			}
		}
	}
	
	private void calculateBorderAndTotalSize() {
		double longerSideLen = this.getFullLayoutRegionHeight()>=this.getFullLayoutRegionWidth()?this.getFullLayoutRegionHeight():this.getFullLayoutRegionWidth();
		
		this.borderLength = longerSideLen*BORDER_RATIO;
		
		this.totalHeight = this.getFullLayoutRegionHeight()+this.borderLength*2;
		this.totalWidth = this.getFullLayoutRegionWidth()+this.borderLength*2;
	}
	
	/**
	 * count the number of shape entity layout coordinate falling into each bins and update them in the {@link #xAxisBinSizeList} and {@link #yAxisBinSizeList};
	 */
	private void countBins() {
		
		this.xBinSize = (this.fullRegionEndX-this.fullRegionStartX)/BIN_NUM;
		this.yBinSize = (this.fullRegionEndY-this.fullRegionStartY)/BIN_NUM;
		
		this.xAxisBinCountList = new ArrayList<>();
		this.yAxisBinCountList = new ArrayList<>();
		
		for(int i=0;i<=BIN_NUM;i++) {//there is one more bin than the BIN_NUM to hold any point on margin;
			this.xAxisBinCountList.add(0);
			this.yAxisBinCountList.add(0);
		}
		
		int xIndex;
		int yIndex;
		for(CompositionFunctionGroupID cfgID:this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().keySet()){
			for(Point2D p:this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(cfgID).getLayoutCoordSet()){
				xIndex = (int)((p.getX()-this.fullRegionStartX)/this.xBinSize);
				this.xAxisBinCountList.set(xIndex, this.xAxisBinCountList.get(xIndex)+1);
				
				yIndex = (int)((p.getY()-this.fullRegionStartY)/this.yBinSize);
				this.yAxisBinCountList.set(yIndex, this.yAxisBinCountList.get(yIndex)+1);
			}
		}
	}

	private void calculateBinDensities() {
		this.xAxisBinDensityList = new ArrayList<>();
		this.yAxisBinDensityList = new ArrayList<>();
		
		for(int i=0;i<BIN_NUM;i++) {
			double xAxisBinDensity = (double)this.xAxisBinCountList.get(i)/this.totalShapeEntityNum;
			double yAxisBinDensity = (double)this.yAxisBinCountList.get(i)/this.totalShapeEntityNum;
			
			if(xAxisBinDensity>this.largestBinDensity)
				this.largestBinDensity = xAxisBinDensity;
			if(yAxisBinDensity>this.largestBinDensity)
				this.largestBinDensity = yAxisBinDensity;
			
			this.xAxisBinDensityList.add(xAxisBinDensity);
			this.yAxisBinDensityList.add(yAxisBinDensity);
		}
	}
	
	/**
	 * initial configuration 
	 * 1. the full layout region are included;
	 * 
	 * 2. all core ShapeCFGs are included in the original order (as how they are retrieved from the VisInstance in {@link #preprocess()} method)
	 * @throws IOException 
	 */
	private void initializeConfiguration() throws IOException {
		//the initial configuration will be set in the controller class;
		this.getController();
		
		//save the initial configuration
		this.mostRecentlySavedConfiguration = 
				this.getCurrentVisInstanceRunLayoutConfigurationWrapper();
		
		
		this.mostRecentlySavedConfigurationFinished = true;
		
		
	}
	
	///////////////////////////////////////////////
	/**
	 * check if current configuration is finishable
	 * 
	 * @return
	 * @throws IOException 
	 */
	public boolean currentConfigurationIsFinishable() throws IOException {
		boolean noCoreShapeCFGSelected = true;
		for(CompositionFunctionGroupID coreShapeCFGID:this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().keySet()) {
			if(this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(coreShapeCFGID).isIncludedInLayout()) {
				noCoreShapeCFGSelected = false;
				break;
			}
		}
		
		return !noCoreShapeCFGSelected && this.getController().validRegionIsSelected();
	}

	/**
	 * extract and return the current VisInstanceRunLayoutConfigurationWrapper from the UI;
	 * only applicable if current configuration is valid
	 * @return
	 * @throws IOException 
	 */
	public VisInstanceRunLayoutConfigurationWrapper getCurrentVisInstanceRunLayoutConfigurationWrapper() throws IOException {
		if(!this.currentConfigurationIsFinishable())
			throw new UnsupportedOperationException("current configuration is not valid!");
		
		List<CompositionFunctionGroupID> coreShapeCFGIDListInLayoutOrder = new ArrayList<>();
		
		this.coreShapeCFGIDLayoutOrderList.forEach(e->{
			if(this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(e).isIncludedInLayout())
				coreShapeCFGIDListInLayoutOrder.add(e);
		});
		
		return new VisInstanceRunLayoutConfigurationWrapper(
				this.getController().selectFullPreviewRegionCheckBox.isSelected(),
				this.getController().selectedRegionStartX,
				this.getController().selectedRegionStartY,
				this.getController().selectedRegionEndX,
				this.getController().selectedRegionEndY,
				coreShapeCFGIDListInLayoutOrder
				);
	}

	
	/**
	 * @return the mostRecentlySavedConfiguration
	 */
	public VisInstanceRunLayoutConfigurationWrapper getMostRecentlySavedConfiguration() {
		return mostRecentlySavedConfiguration;
	}
	/**
	 * 
	 * @param config
	 */
	void setMostRecentlySavedConfiguration(VisInstanceRunLayoutConfigurationWrapper config) {
		this.mostRecentlySavedConfiguration = config;
	}
	
	public Boolean getMostRecentlySavedConfigurationFinished() {
		return this.mostRecentlySavedConfigurationFinished;
	}
	
	public void setMostRecentlySavedConfigurationFinished(Boolean finished) {
		this.mostRecentlySavedConfigurationFinished = finished;
	}
	
	////////////////////////////////////////////////

	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 * @throws IOException 
	 */
	public void setModifiable(boolean modifiable) throws IOException {
		if(modifiable)
			throw new UnsupportedOperationException("not supported?");
		
		//
		this.getController().setModifiable(modifiable);
		
		//
		this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.forEach((id,manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	
	/**
	 * @return the controller
	 * @throws IOException 
	 */
	public VisInstanceRunLayoutPreviewAndConfigController getController() throws IOException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(VisInstanceRunLayoutPreviewAndConfigController.FXML_FILE_DIR_STRING));
			
			loader.load();
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		return controller;
	}
	
	////////////////////////////////////
	/**
	 * simply remove the corresponding shape entities of the deselected core ShapeCFG from the canvass;
	 * @param coreShapeCFGID
	 * @throws IOException 
	 */
	public void removeCoreShapeCFGFromCanvass(CompositionFunctionGroupID coreShapeCFGID) throws IOException {
		//simply remove the corresponding shape entities of the given coreShapeCFGID from canvass; do not need to re-layout all core ShapeCFGs, which is time efficient;
		this.getController().getCanvassAnchorPane().getChildren().removeAll(this.getCoreShapeCFGIDCoreShapeCFGEntryManagerMap().get(coreShapeCFGID).getLayoutCircleSet());
	}
	
	/**
	 * invoked after a unselected core ShapeCFG is selected;
	 * this will result in 
	 *  	re-layout all shape entities of all selected core ShapeCFGs on the canvass based on current order;
	 * 		by invoking 
	 * @param coreShapeCFGID
	 * @throws IOException
	 */
	public void updateSelectedCoreShapeCFGOrderListAfterSelection() throws IOException {
		this.getController().layoutSelectedCoreShapeCFGEntities();
	}
	
	/**
	 * swap the two Core ShapeCFG at the two given order index;
	 * then update the UI;
	 * 1. update the list
	 * 2. re-add the shape entities to the canvass based on the new order and whether they are to be shown;
	 * @param index1
	 * @param index2
	 * @throws IOException 
	 */
	public void swapCoreShapeCFG(int index1, int index2) throws IOException {
		CompositionFunctionGroupID cfgID1 = this.coreShapeCFGIDLayoutOrderList.get(index1);
		CompositionFunctionGroupID cfgID2 = this.coreShapeCFGIDLayoutOrderList.get(index2);
		
		this.coreShapeCFGIDLayoutOrderList.set(index1, cfgID2);
		this.coreShapeCFGIDLayoutOrderList.set(index2, cfgID1);
		
		this.getController().updateCoreShapeCFGListVBox();
		this.getController().layoutSelectedCoreShapeCFGEntities();
	}
	
	////////////////////////////////////////////
	/**
	 * set the configuration as the given one;
	 * also update the UI;
	 * @param config
	 * @throws IOException 
	 */
	public void setConfiguration(VisInstanceRunLayoutConfigurationWrapper config) throws IOException {
		//first select the core ShapeCFGs
		this.setSelectedCoreShapeCFGList(config.getSelectedCoreShapeCFGIDListInLayoutOrder());
		//update the layout based on the selectec core ShapeCFGs and the layout order;
		this.updateSelectedCoreShapeCFGOrderListAfterSelection();
		
		
		//then set the selected region (always as the alternatively selected one rather than the full region)
		this.setSelectedRegion(config);
		
	}
	
	/**
	 * 
	 * @param selectedCoreShapeCFGIDListInLayoutOrder
	 * @throws IOException 
	 */
	private void setSelectedCoreShapeCFGList(List<CompositionFunctionGroupID> selectedCoreShapeCFGIDListInLayoutOrder) throws IOException {
		//1. deselect all CoreShapeCFGEntryManager
		this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.forEach((id,manager)->{
			manager.setIncludedInLayout(false);
		});
		
		//2. clear the coreShapeCFGIDLayoutOrderList and CoreShapeCFGEntryManager container VBox
		this.coreShapeCFGIDLayoutOrderList.clear();
		this.getController().coreShapeCFGListVBox.getChildren().clear();
		
		//3. add the selected core ShapeCFG's CoreShapeCFGEntryManager to the container VBox in the order as in the given VisInstanceRunLayoutConfigurationWrapper;
		for(CompositionFunctionGroupID id:selectedCoreShapeCFGIDListInLayoutOrder){
			CoreShapeCFGEntryManager manager = this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.get(id);
			manager.setIncludedInLayout(true);
			this.getController().coreShapeCFGListVBox.getChildren().add(manager.getController().getRootNodePane());
			this.coreShapeCFGIDLayoutOrderList.add(id);
		}
		
		//4. add the unselected core ShapeCFG's CoreShapeCFGEntryManager to the container VBox
		for(CompositionFunctionGroupID id:this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.keySet()) {
			if(!this.coreShapeCFGIDLayoutOrderList.contains(id)) {
				CoreShapeCFGEntryManager manager = this.coreShapeCFGIDCoreShapeCFGEntryManagerMap.get(id);
				this.getController().coreShapeCFGListVBox.getChildren().add(manager.getController().getRootNodePane());
				this.coreShapeCFGIDLayoutOrderList.add(id);
			}
		}
	}
	
	/**
	 * if full region selected, 
	 * 
	 * else
	 * 
	 * @param config
	 * @throws IOException 
	 */
	private void setSelectedRegion(VisInstanceRunLayoutConfigurationWrapper config) throws IOException {
		
		if(config.isFullRegionSelected()) {
			this.getController().selectFullPreviewRegionCheckBox.setSelected(true);
		}else {
			this.getController().selectFullPreviewRegionCheckBox.setSelected(false);
			
			this.getController().setSelectedRegion(config.getX1(), config.getY1(), config.getX2(), config.getY2());
			
			this.getController().addSelectedRegionShapesOnCanvass(config.getX1(), config.getY1(), config.getX2(), config.getY2());
		}
		
	}
	
	/////////////////////////////////////////////////
	/**
	 * @return the binNum
	 */
	public static int getBinNum() {
		return BIN_NUM;
	}

	/**
	 * @return the borderRatio
	 */
	public static double getBorderRatio() {
		return BORDER_RATIO;
	}

	/**
	 * @return the borderLength
	 */
	public double getBorderLength() {
		return borderLength;
	}

	/**
	 * @return the totalWidth
	 */
	public double getTotalWidth() {
		return totalWidth;
	}

	/**
	 * @return the totalHeight
	 */
	public double getTotalHeight() {
		return totalHeight;
	}

	/**
	 * @return the xAxisBinDensityList
	 */
	public List<Double> getxAxisBinDensityList() {
		return xAxisBinDensityList;
	}

	/**
	 * @return the yAxisBinDensityList
	 */
	public List<Double> getyAxisBinDensityList() {
		return yAxisBinDensityList;
	}

	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

	
	/**
	 * @return the visInstance
	 */
	public VisInstance getVisInstance() {
		return visInstance;
	}

	/**
	 * @return the visInstanceRun
	 */
	public VisInstanceRun getVisInstanceRun() {
		return visInstanceRun;
	}

	/**
	 * @return the visInstanceRunCoreShapeCFGLayoutCoordinateExtractor
	 */
	public VisInstanceRunCoreShapeCFGLayoutCoordinateExtractor getVisInstanceRunCoreShapeCFGLayoutCoordinateExtractor() {
		return visInstanceRunCoreShapeCFGLayoutCoordinateExtractor;
	}

	/**
	 * @return the coreShapeCFGIDLayoutOrderList
	 */
	public List<CompositionFunctionGroupID> getCoreShapeCFGIDLayoutOrderList() {
		return coreShapeCFGIDLayoutOrderList;
	}

	/**
	 * @return the coreShapeCFGIDCoreShapeCFGEntryManagerMap
	 */
	public Map<CompositionFunctionGroupID, CoreShapeCFGEntryManager> getCoreShapeCFGIDCoreShapeCFGEntryManagerMap() {
		return coreShapeCFGIDCoreShapeCFGEntryManagerMap;
	}

	/**
	 * @return the startX
	 */
	public double getFullRegionStartX() {
		return fullRegionStartX;
	}

	/**
	 * @return the startY
	 */
	public double getFullRegionStartY() {
		return fullRegionStartY;
	}

	/**
	 * @return the endX
	 */
	public double getFullRegionEndX() {
		return fullRegionEndX;
	}

	/**
	 * @return the endY
	 */
	public double getFullRegionEndY() {
		return fullRegionEndY;
	}
	
	/**
	 * @return the totalShapeEntityNum
	 */
	public int getTotalShapeEntityNum() {
		return totalShapeEntityNum;
	}

	/**
	 * @return the xBinSize
	 */
	public double getxBinSize() {
		return xBinSize;
	}

	/**
	 * @return the yBinSize
	 */
	public double getyBinSize() {
		return yBinSize;
	}

	/**
	 * @return the xAxisBinCountList
	 */
	public List<Integer> getxAxisBinCountList() {
		return xAxisBinCountList;
	}

	/**
	 * @return the yAxisBinCountList
	 */
	public List<Integer> getyAxisBinCountList() {
		return yAxisBinCountList;
	}

	/**
	 * @return the largestBinDensity
	 */
	public double getLargestBinDensity() {
		return largestBinDensity;
	}

	/**
	 * @return the xAxisBinDensityList
	 */
	public List<Double> getXAxisBinDensityList() {
		return xAxisBinDensityList;
	}

	/**
	 * @return the yAxisBinDensityList
	 */
	public List<Double> getYAxisBinDensityList() {
		return yAxisBinDensityList;
	}
	
	public double getFullLayoutRegionHeight() {
		return this.fullRegionEndY - this.fullRegionStartY;
	}
	
	public double getFullLayoutRegionWidth() {
		return this.fullRegionEndX - this.fullRegionStartX;
	}
	

	/**
	 * @return the beforeChangeMadeWarningInforText
	 */
	public String getBeforeChangeMadeWarningInforText() {
		return beforeChangeMadeWarningInforText;
	}

	/**
	 * @return the afterChangeMadeAction
	 */
	public Runnable getAfterChangeMadeAction() {
		return afterChangeMadeAction;
	}
	////////////////////////////////////

	
	
}
