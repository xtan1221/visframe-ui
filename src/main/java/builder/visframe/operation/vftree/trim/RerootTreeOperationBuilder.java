package builder.visframe.operation.vftree.trim;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.generic.tree.trim.helper.InteractiveRectangleVfTreeGraphics;
import builder.visframe.generic.tree.trim.helper.VfTreePositionSelector;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.vftree.VfTreeTrimmingOperationBaseBuilder;
import context.project.VisProjectDBContext;
import generic.tree.trim.helper.PositionOnTree;
import operation.vftree.trim.RerootTreeOperation;

import static operation.vftree.VfTreeTrimmingOperationBase.INPUT_VFTREE_METADATAID;
import static operation.vftree.trim.RerootTreeOperation.*;

/**
 * 
 * @author tanxu
 *
 */
public final class RerootTreeOperationBuilder extends VfTreeTrimmingOperationBaseBuilder<RerootTreeOperation>{
	public static final String NODE_NAME = "RerootTreeOperation";
	public static final String NODE_DESCRIPTION = "RerootTreeOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public RerootTreeOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	private Map<SimpleName, Object> buildRerootTreeOperationLevelSpecificParameterNameValueObjectMap(){
		PositionOnTree newRootPositionOnTree = (PositionOnTree) this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).getCurrentValue();
		
		return RerootTreeOperation.buildRerootTreeOperationLevelSpecificParameterNameValueObjectMap(newRootPositionOnTree);
	}
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//NEW_ROOT_POSITION_ON_TREE
		this.addChildNodeBuilder(new VfTreePositionSelector(
				NEW_ROOT_POSITION_ON_TREE.getName().getStringValue(), NEW_ROOT_POSITION_ON_TREE.getDescriptiveName(),
				NEW_ROOT_POSITION_ON_TREE.canHaveNullValueObject(this.isForReproducing()), this
				));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		
		///prepare
		MetadataIDSelector inputVfTreeMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_VFTREE_METADATAID.getName().getStringValue());
		
		VfTreePositionSelector newRootPositionOnTreeSelector = (VfTreePositionSelector) this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue());
		
		
		//when super.INPUT_VFTREE_METADATAID is changed, the NEW_ROOT_POSITION_ON_TREE need to change accordingly
		Runnable inputVfTreeMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputVfTreeMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					newRootPositionOnTreeSelector.setInteractiveTreeGraphics(null);
					
					newRootPositionOnTreeSelector.setToDefaultEmpty();
					
				}else if(inputVfTreeMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					try {
						InteractiveRectangleVfTreeGraphics treeGraphics = 
								new InteractiveRectangleVfTreeGraphics(
										this.getHostVisProjectDBContext(), 
										inputVfTreeMetadataIDSelector.getCurrentValue(),
										false,//allowingRootNodeMarked
										false,//boolean allowingToMarkMultipleNodes
										true,//allowingSingleChildNodeMarked, 
										true//boolean allowingLeafNodeMarked
										);
						
						newRootPositionOnTreeSelector.setInteractiveTreeGraphics(treeGraphics);
						
					} catch (SQLException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		inputVfTreeMetadataIDSelector.addStatusChangedAction(
				inputVfTreeMetadataIDSelectorStatusChangeEventAction);
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				RerootTreeOperation rerootTreeOperation = (RerootTreeOperation)value;
				
				/////create InteractiveRectangleVfTreeGraphics
				InteractiveRectangleVfTreeGraphics treeGraphics = 
						new InteractiveRectangleVfTreeGraphics(this.getHostVisProjectDBContext(), 
								rerootTreeOperation.getInputVfTreeMetadataID(), 
								false, false, true, true);
			
				VfTreePositionSelector newRootPositionOnTreeSelector = (VfTreePositionSelector) this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue());
				
				newRootPositionOnTreeSelector.setInteractiveTreeGraphics(treeGraphics);
				
				//////////
				this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).setValue(
						rerootTreeOperation.getNewRootPositionOnTree(), isEmpty);
				
				//////////////////////////////////////////////
				/////
				this.checkIfForReproducing(rerootTreeOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	
	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() throws SQLException, IOException {
		this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).setModifiable(true);
	}
	
	@Override
	protected RerootTreeOperation build() throws SQLException {
		
		return new RerootTreeOperation(
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildVfTreeTrimmingOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildRerootTreeOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				); 
	}

}
