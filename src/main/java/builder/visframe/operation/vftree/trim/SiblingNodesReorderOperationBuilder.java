package builder.visframe.operation.vftree.trim;

import static operation.vftree.VfTreeTrimmingOperationBase.INPUT_VFTREE_METADATAID;
import static operation.vftree.trim.SiblingNodesReorderOperation.SIBLING_REORDER_PATTERN;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.generic.tree.trim.helper.InteractiveRectangleVfTreeGraphics;
import builder.visframe.generic.tree.trim.helper.SiblingReorderPatternBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.vftree.VfTreeTrimmingOperationBaseBuilder;
import context.project.VisProjectDBContext;
import generic.tree.trim.helper.SiblingReorderPattern;
import operation.vftree.trim.SiblingNodesReorderOperation;

/**
 * 
 * @author tanxu
 *
 */
public final class SiblingNodesReorderOperationBuilder extends VfTreeTrimmingOperationBaseBuilder<SiblingNodesReorderOperation>{
	public static final String NODE_NAME = "SiblingNodesReorderOperation";
	public static final String NODE_DESCRIPTION = "SiblingNodesReorderOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SiblingNodesReorderOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	////////////////////////////////////////////
	private Map<SimpleName, Object> buildSiblingNodesReorderOperationLevelSpecificParameterNameValueObjectMap(){
		SiblingReorderPattern SiblingReorderPattern = (generic.tree.trim.helper.SiblingReorderPattern) this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue()).getCurrentValue();
		
		return SiblingNodesReorderOperation.buildSiblingNodesReorderOperationLevelSpecificParameterNameValueObjectMap(SiblingReorderPattern);
	}
	
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//SIBLING_REORDER_PATTERN
		this.addChildNodeBuilder(new SiblingReorderPatternBuilder(
				SIBLING_REORDER_PATTERN.getName().getStringValue(), SIBLING_REORDER_PATTERN.getDescriptiveName(),
				SIBLING_REORDER_PATTERN.canHaveNullValueObject(this.isForReproducing()), this
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
		
		SiblingReorderPatternBuilder silbingReorderPatternBuilder = 
				(SiblingReorderPatternBuilder) this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue());
		
		
		//when super.INPUT_VFTREE_METADATAID is changed, the SIBLING_REORDER_PATTERN need to change accordingly
		Runnable inputVfTreeMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputVfTreeMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					silbingReorderPatternBuilder.setInteractiveTreeGraphics(null);
					
					silbingReorderPatternBuilder.setToDefaultEmpty();
					
				}else if(inputVfTreeMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					try {
						InteractiveRectangleVfTreeGraphics treeGraphics = 
								new InteractiveRectangleVfTreeGraphics(
										this.getHostVisProjectDBContext(), 
										inputVfTreeMetadataIDSelector.getCurrentValue(),
										true, 
										true,//boolean allowingToMarkMultipleNodes
										false,
										false
										);
						
						silbingReorderPatternBuilder.setInteractiveTreeGraphics(treeGraphics);
						
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
			this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
			
				SiblingNodesReorderOperation siblingNodesReorderOperation = (SiblingNodesReorderOperation)value;
				
				/////create InteractiveRectangleVfTreeGraphics
				InteractiveRectangleVfTreeGraphics treeGraphics = 
						new InteractiveRectangleVfTreeGraphics(
								this.getHostVisProjectDBContext(), siblingNodesReorderOperation.getInputVfTreeMetadataID(), 
								true, true, false, false);
			
				SiblingReorderPatternBuilder silbingReorderPatternBuilder = 
						(SiblingReorderPatternBuilder) this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue());
				
				silbingReorderPatternBuilder.setInteractiveTreeGraphics(treeGraphics);
				
				//////////
				this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue()).setValue(
						siblingNodesReorderOperation.getSilbingReorderPattern(), isEmpty);
					
				//////////////////////////////////////////////
				/////
				this.checkIfForReproducing(siblingNodesReorderOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable()
			throws SQLException, IOException {
		this.getChildrenNodeBuilderNameMap().get(SIBLING_REORDER_PATTERN.getName().getStringValue()).setModifiable(true);
	}
	
	@Override
	protected SiblingNodesReorderOperation build() throws SQLException {
		return new SiblingNodesReorderOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildVfTreeTrimmingOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildSiblingNodesReorderOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}
	
}
