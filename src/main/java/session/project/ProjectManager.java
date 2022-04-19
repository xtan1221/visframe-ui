package session.project;

import java.io.IOException;
import java.sql.SQLException;

import context.project.VisProjectDBContext;
import core.table.process.ProcessLogTableViewerManager;
import dependencygraph.cfd.CFDNodeOrderingComparatorFactory;
import dependencygraph.cfd.SimpleCFDGraphViewerManager;
import dependencygraph.dos.DOSNodeOrderingComparatorFactory;
import dependencygraph.dos.SimpleDOSGraphViewerManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

/**
 * manager for the UI related functionalities of a single opened project in a VFSessionManager
 * @author tanxu
 *
 */
public class ProjectManager {
	private final VisProjectDBContext visProjectDBContext;
	
	//////////////////////
	private ProjectController controller;
	
	private Tab projectTab;
	
	/////////////////
	private ProcessLogTableViewerManager processLogTableViewerManager;
	private SimpleCFDGraphViewerManager simpleCFDGraphViewerManager;
	private SimpleDOSGraphViewerManager simpleDOSGraphViewerManager;
	
	/**
	 * constructor
	 * @param visProject
	 * @throws SQLException 
	 */
	public ProjectManager(VisProjectDBContext visProject) throws SQLException{
		this.visProjectDBContext = visProject;
		
		////////////////////////
		this.processLogTableViewerManager = new ProcessLogTableViewerManager(this.getVisProjectDBContext().getProcessLogTableAndProcessPerformerManager());

		Runnable r1 = ()->{
			this.processLogTableViewerManager.getTableViewDelegate().refresh();
		};
		this.getProcessLogTableViewerManager().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager()
		.addToProcessInsertedOrStatusColumnChangeEventRunnable(r1);
		this.getProcessLogTableViewerManager().getProcessLogTableAndProcessPerformerManager().getVSAArchiveReproducerAndInserterManager()
		.addToProcessInsertedOrStatusColumnChangeEventRunnable(r1);
		
		//////////////////
		this.simpleCFDGraphViewerManager = new SimpleCFDGraphViewerManager(
				this.getVisProjectDBContext().getCFDGraph().getUnderlyingGraph(),//SimpleDirectedGraph<CFDNodeImpl, CFDEdgeImpl> underlyingSimpleCFDGraph,
				CFDNodeOrderingComparatorFactory.getComparator(),//Comparator<CFDNodeImpl> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.getCFID().toString();}//Function<CFDNodeImpl,String> dagNodeInforStringFunction
				);
		Runnable r2 = ()->{
			try {
				this.simpleCFDGraphViewerManager = new SimpleCFDGraphViewerManager(
						this.getVisProjectDBContext().getCFDGraph().getUnderlyingGraph(),//SimpleDirectedGraph<CFDNodeImpl, CFDEdgeImpl> underlyingSimpleCFDGraph,
						CFDNodeOrderingComparatorFactory.getComparator(),//Comparator<CFDNodeImpl> nodeOrderComparator,
						300,//double distBetweenLevels,
						200,//double distBetweenNodesOnSameLevel,
						(a)->{return a.getCFID().toString();}//Function<CFDNodeImpl,String> dagNodeInforStringFunction
						);
				this.getController().updateCFDGraph();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		};
		this.getProcessLogTableViewerManager().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager()
		.addToCFTypeProcessInsertedOrStatusColumnChangeEventRunnable(r2);
		
		//////////////////
		this.simpleDOSGraphViewerManager = new SimpleDOSGraphViewerManager(
				this.getVisProjectDBContext().getDOSGraph().getUnderlyingGraph(),//SimpleDirectedGraph<DOSNodeImpl, DOSEdgeImpl> underlyingSimpleDOSGraph,
				DOSNodeOrderingComparatorFactory.getComparator(),//Comparator<DOSNodeImpl> nodeOrderComparator,
				300,//double distBetweenLevels,
				200,//double distBetweenNodesOnSameLevel,
				(a)->{return a.getMetadataID().toString();}//Function<DOSNodeImpl,String> dagNodeInforStringFunction
				);
		Runnable r3 = ()->{
			try {
				this.simpleDOSGraphViewerManager = new SimpleDOSGraphViewerManager(
						this.getVisProjectDBContext().getDOSGraph().getUnderlyingGraph(),//SimpleDirectedGraph<DOSNodeImpl, DOSEdgeImpl> underlyingSimpleDOSGraph,
						DOSNodeOrderingComparatorFactory.getComparator(),//Comparator<DOSNodeImpl> nodeOrderComparator,
						300,//double distBetweenLevels,
						200,//double distBetweenNodesOnSameLevel,
						(a)->{return a.getMetadataID().toString();}//Function<DOSNodeImpl,String> dagNodeInforStringFunction
						);
				this.getController().updateDOSGraph();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		};
		this.getProcessLogTableViewerManager().getProcessLogTableAndProcessPerformerManager().getSimpleProcessManager()
		.addToMetadataProducingTypeProcessInsertedOrStatusColumnChangeEventRunnableSet(r3);
		this.getProcessLogTableViewerManager().getProcessLogTableAndProcessPerformerManager().getVSAArchiveReproducerAndInserterManager()
		.addToMetadataProducingTypeProcessInsertedOrStatusColumnChangeEventRunnableSet(r3);
		
	}
	
	
	/**
	 * @return the processLogTableViewerManager
	 */
	public ProcessLogTableViewerManager getProcessLogTableViewerManager() {
		return processLogTableViewerManager;
	}


	/**
	 * @return the simpleCFDGraphViewerManager
	 */
	public SimpleCFDGraphViewerManager getSimpleCFDGraphViewerManager() {
		return simpleCFDGraphViewerManager;
	}


	/**
	 * @return the simpleDOSGraphViewerManager
	 */
	public SimpleDOSGraphViewerManager getSimpleDOSGraphViewerManager() {
		return simpleDOSGraphViewerManager;
	}


	public VisProjectDBContext getVisProjectDBContext() {
		return visProjectDBContext;
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void closeDBConnection() throws SQLException {
		this.getVisProjectDBContext().disconnect();
	}
	
	public Tab getProjectTab() {
		return projectTab;
	}

	public void setProjectTab(Tab projectTab) {
		this.projectTab = projectTab;
	}
	
	//////////////////////////////////
	public ProjectController getController() {
		if(this.controller == null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ProjectController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
				
				this.controller = loader.getController();
				
				this.controller.setManager(this);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return this.controller;
	}
	

}
