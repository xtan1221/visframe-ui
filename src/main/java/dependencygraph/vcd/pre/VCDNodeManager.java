package dependencygraph.vcd.pre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import dependency.cfd.SimpleCFDGraph;
import dependency.cfd.SimpleCFDGraphBuilder;
import dependency.dos.SimpleDOSGraph;
import dependency.dos.SimpleDOSGraphBuilder;
import dependency.vcd.VCDNodeImpl;
import dependencygraph.DAGNodeManager;
import function.composition.CompositionFunctionID;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

public class VCDNodeManager extends DAGNodeManager<VCDNodeImpl, VCDNodeController> {
	
	private SimpleCFDGraph cfdGraph;
	private SimpleDOSGraph dosGraph;
	
	
	protected VCDNodeManager(
			VCDGraphMainManager mainManager, 
			VCDNodeImpl node, 
			Point2D centerCoord,
			double nodeRootNodeHeight, double nodeRootNodeWidth) {
		super(mainManager, node, centerCoord, nodeRootNodeHeight, nodeRootNodeWidth);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected VCDNodeController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(VCDNodeController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}
	
	@Override
	public VCDGraphMainManager getMainManager() {
		return (VCDGraphMainManager) mainManager;
	}
	
	
	/**
	 * @return the cfdGraph
	 */
	public SimpleCFDGraph getCfdGraph() {
		if(this.cfdGraph==null) {
			//initial cf set are the cf of the core shapecfgs;
			Set<CompositionFunctionID> initialCFIDSet = new HashSet<>();
			
			this.getNode().getVSComponent().getCoreShapeCFGIDSet().forEach(cfgid->{
				initialCFIDSet.addAll(
						this.getMainManager().getVisframeContext().getCompositionFunctionIDSetOfGroupID(cfgid));
			});
			
			try {
				SimpleCFDGraphBuilder builder = new SimpleCFDGraphBuilder(
						this.getMainManager().getVisframeContext(), initialCFIDSet);
				
				this.cfdGraph = builder.getBuiltGraph();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return cfdGraph;
	}

	/**
	 * @return the dosGraph
	 */
	public SimpleDOSGraph getDosGraph() {
		if(this.dosGraph==null) {
			
			SimpleDOSGraphBuilder builder = new SimpleDOSGraphBuilder(
					this.getMainManager().getVisframeContext(), 
					this.getCfdGraph().getDependedRecordMetadataIDInputVariableDataTableColumnNameSetMap().keySet()//inducingMetadataIDSet
					);
			
			this.dosGraph = builder.getBuiltGraph();
			
		}
		
		
		return dosGraph;
	}

}
