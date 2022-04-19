package builder.visframe.visinstance.run;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import builder.visframe.visinstance.run.utils.IndependentFreeInputVariableTypeValueAssignerManager;
import context.project.VisProjectDBContext;
import core.builder.LeafNodeBuilder;
import dependency.cfd.SimpleCFDGraph;
import dependency.cfd.SimpleCFDGraphBuilder;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import visinstance.VisInstance;
import visinstance.VisInstanceID;
import visinstance.run.VisInstanceRun;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunBuilder extends LeafNodeBuilder<VisInstanceRun, VisInstanceRunBuilderEmbeddedUIContentController>{
	public static final String NODE_NAME = "VisInstanceRun";
	public static final String NODE_DESCRIPTION = "VisInstanceRun";
	
	////////////
	private final VisProjectDBContext hostVisProjectDBContext;
	private final VisInstanceID visInstanceID;
	private final int runUID;
	/////////////////////////
	private VisInstance visInstance;
	
	private Map<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeValueAssignerManager> independentFreeInputVariableTypeValueAssignerManagerMap;
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws SQLException 
	 */
	public VisInstanceRunBuilder(
			VisProjectDBContext hostVisProjectDBContext, 
			VisInstanceID visInstanceID, 
			int runUID) throws SQLException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, VisInstanceRunBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.visInstanceID = visInstanceID;
		this.runUID = runUID;
		
		////////////////////
		this.initialize();
	}
	
	/**
	 * initialize {@link #independentFreeInputVariableTypeValueAssignerManagerMap}
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		this.visInstance = this.getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceManager().lookup(this.visInstanceID);
		
		//
		this.independentFreeInputVariableTypeValueAssignerManagerMap = new LinkedHashMap<>();
		
		SimpleCFDGraphBuilder builder = new SimpleCFDGraphBuilder(
				this.getHostVisProjectDBContext(), 
				this.getVisInstance().getCoreShapeCFGCFIDSet());
		
		SimpleCFDGraph cfdGraph= builder.getBuiltGraph();
		
		Set<IndependentFreeInputVariableTypeID> indieFIVTypeIDSet = cfdGraph.getAllEmployedIndependentFreeInputVariableTypeIDSet();
		for(IndependentFreeInputVariableTypeID id:indieFIVTypeIDSet){
			IndependentFreeInputVariableType type = 
					this.getHostVisProjectDBContext().getIndependentFreeInputVariableTypeLookup().lookup(id);
			
			//note that each IndependentFreeInputVariableTypeValueAssignerManager's controller's valueStringTextField's focus property change event will trigger the 
			//updateNonNullValueFromContentController() method of the owner builder of this controller;
			//see {@link IndependentFreeInputVariableTypeValueAssignerController#setManager()} method
			IndependentFreeInputVariableTypeValueAssignerManager manager = new IndependentFreeInputVariableTypeValueAssignerManager(this, type);
			
			this.independentFreeInputVariableTypeValueAssignerManagerMap.put(type, manager);
			
			
		}
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
	 * @return the independentFreeInputVariableTypeValueAssignerManagerMap
	 */
	public Map<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeValueAssignerManager> getIndependentFreeInputVariableTypeValueAssignerManagerMap() {
		return independentFreeInputVariableTypeValueAssignerManagerMap;
	}

	/**
	 * @return the runUID
	 */
	public int getRunUID() {
		return runUID;
	}

}
