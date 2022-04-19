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
import operation.vftree.trim.SubTreeOperation;

import static operation.vftree.trim.SubTreeOperation.*;

/**
 * 
 * @author tanxu
 *
 */
public final class SubTreeOperationBuilder extends VfTreeTrimmingOperationBaseBuilder<SubTreeOperation>{
	public static final String NODE_NAME = "SubTreeOperation";
	public static final String NODE_DESCRIPTION = "SubTreeOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SubTreeOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	////////////////////////////////////////////
	private Map<SimpleName, Object> buildSubTreeOperationLevelSpecificParameterNameValueObjectMap(){
		PositionOnTree newRootPositionOnTree = (PositionOnTree) this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).getCurrentValue();
		
		return SubTreeOperation.buildSubTreeOperationLevelSpecificParameterNameValueObjectMap(newRootPositionOnTree);
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
									false, 
									false,//boolean allowingToMarkMultipleNodes
									true,
									true
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
				
				SubTreeOperation subTreeOperation = (SubTreeOperation)value;
				
				/////create InteractiveRectangleVfTreeGraphics
				InteractiveRectangleVfTreeGraphics treeGraphics = 
						new InteractiveRectangleVfTreeGraphics(
								this.getHostVisProjectDBContext(), subTreeOperation.getInputVfTreeMetadataID(), 
								false, false, true, true);
			
				VfTreePositionSelector newRootPositionOnTreeSelector = 
						(VfTreePositionSelector) this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue());
				
				newRootPositionOnTreeSelector.setInteractiveTreeGraphics(treeGraphics);
				
				//////////
				this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).setValue(
						subTreeOperation.getNewRootPositionOnTree(), isEmpty);
				
				//////////////////////////////////////////////
				/////
				this.checkIfForReproducing(subTreeOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable()
			throws SQLException, IOException {
		this.getChildrenNodeBuilderNameMap().get(NEW_ROOT_POSITION_ON_TREE.getName().getStringValue()).setModifiable(true);
	}
	
	@Override
	protected SubTreeOperation build() throws SQLException {
		return new SubTreeOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildVfTreeTrimmingOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildSubTreeOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}

}
