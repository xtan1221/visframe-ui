package builder.visframe.visinstance.run.layoutconfiguration.display;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import context.project.VisProjectDBContext;
import function.group.CompositionFunctionGroupID;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import visinstance.run.VisInstanceRun;
import visinstance.run.extractor.VisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunLayoutDisplayManager {
	private final VisProjectDBContext hostVisProjectDBContext;
	private final VisInstanceRun visInstanceRun;
	/**
	 * action to take AFTER the layout is successfully displayed
	 */
	private final Runnable afterLayoutDoneAction;
	
	/**
	 * order list of core ShapeCFGs to be laid out;
	 * cannot be empty;
	 * note that not necessarily all core ShapeCFGs of the VisInstanceRun are to be included in this list;
	 */
	private final List<CompositionFunctionGroupID> coreShapeCFGLayoutOrderList;
	
	/**
	 * must be at the upper-left side of layoutRegionEndCoord;
	 */
	private final Point2D layoutRegionUpperLeftCoord;
	private final Point2D layoutRegionBottomRightCoord;
	
	////////////////////////
	private VisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor visInstanceRunCoreShapeCFGCalculatedTargetValueExtractor;
	private VisInstanceRunLayoutDisplayController controller;
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param visInstanceRun
	 * @param coreShapeCFGLayoutOrderList
	 * @param layoutRegionUpperLeftCoord
	 * @param layoutRegionBottomRightCoord
	 * @throws SQLException 
	 */
	public VisInstanceRunLayoutDisplayManager(
			VisProjectDBContext hostVisProjectDBContext,
			VisInstanceRun visInstanceRun,
			Runnable afterLayoutDoneAction,
			List<CompositionFunctionGroupID> coreShapeCFGLayoutOrderList,
			Point2D layoutRegionUpperLeftCoord,
			Point2D layoutRegionBottomRightCoord
			) throws SQLException {
		//validations
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		if(visInstanceRun==null)
			throw new IllegalArgumentException("given visInstanceRun cannot be null!");
		if(afterLayoutDoneAction==null)
			throw new IllegalArgumentException("given afterLayoutDoneAction cannot be null!");
		if(coreShapeCFGLayoutOrderList==null||coreShapeCFGLayoutOrderList.isEmpty())
			throw new IllegalArgumentException("given coreShapeCFGLayoutOrderList cannot be null or empty!");
		if(layoutRegionUpperLeftCoord==null)
			throw new IllegalArgumentException("given layoutRegionUpperLeftCoord cannot be null!");
		if(layoutRegionBottomRightCoord==null)
			throw new IllegalArgumentException("given layoutRegionBottomRightCoord cannot be null!");
		
		
		
		////////////////////////////
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.visInstanceRun = visInstanceRun;
		this.afterLayoutDoneAction = afterLayoutDoneAction;
		
		this.coreShapeCFGLayoutOrderList = coreShapeCFGLayoutOrderList;
		this.layoutRegionUpperLeftCoord = layoutRegionUpperLeftCoord;
		this.layoutRegionBottomRightCoord = layoutRegionBottomRightCoord;
		
		
		////////////////////
		this.visInstanceRunCoreShapeCFGCalculatedTargetValueExtractor = 
				new VisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor(this.getHostVisProjectDBContext(), this.getVisInstanceRun());
	}
	
	
	/**
	 * @return the controller
	 * @throws SQLException 
	 */
	public VisInstanceRunLayoutDisplayController getController() throws SQLException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(VisInstanceRunLayoutDisplayController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		return controller;
	}
	
	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void setModifiable(boolean modifiable) throws IOException, SQLException {
		if(modifiable)
			throw new UnsupportedOperationException("not supported?");
		
		this.getController().setModifiable(modifiable);
	}
	
	/////////////////////////////////////
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

	/**
	 * @return the visInstanceRun
	 */
	public VisInstanceRun getVisInstanceRun() {
		return visInstanceRun;
	}
	
	/**
	 * @return the coreShapeCFGLayoutOrderList
	 */
	public List<CompositionFunctionGroupID> getCoreShapeCFGLayoutOrderList() {
		return coreShapeCFGLayoutOrderList;
	}

	/**
	 * @return the layoutRegionStartCoord
	 */
	public Point2D getLayoutRegionUpperLeftCoord() {
		return layoutRegionUpperLeftCoord;
	}
	
	/**
	 * @return the layoutRegionEndCoord
	 */
	public Point2D getLayoutRegionBottomRightCoord() {
		return layoutRegionBottomRightCoord;
	}

	/**
	 * @return the visInstanceRunCoreShapeCFGCalculatedTargetValueExtractor
	 */
	public VisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor getVisInstanceRunCoreShapeCFGCalculatedTargetValueExtractor() {
		return visInstanceRunCoreShapeCFGCalculatedTargetValueExtractor;
	}


	/**
	 * @return the afterLayoutDoneAction
	 */
	public Runnable getAfterLayoutDoneAction() {
		return afterLayoutDoneAction;
	}

	public double getLayoutRegionWidth() {
		return this.getLayoutRegionBottomRightCoord().getX()-this.getLayoutRegionUpperLeftCoord().getX();
	}
	
	public double getLayoutRegionHeight() {
		return this.getLayoutRegionBottomRightCoord().getY()-this.getLayoutRegionUpperLeftCoord().getY();
	}
}
