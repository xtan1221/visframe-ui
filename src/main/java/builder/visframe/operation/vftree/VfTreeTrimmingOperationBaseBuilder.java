package builder.visframe.operation.vftree;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.AbstractOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import metadata.graph.vftree.VfTreeDataMetadata;
import operation.vftree.VfTreeTrimmingOperationBase;
import rdb.table.data.DataTableColumnName;

import static operation.vftree.VfTreeTrimmingOperationBase.*;

/**
 * 
 * @author tanxu
 *
 * @param <T>
 */
public abstract class VfTreeTrimmingOperationBaseBuilder<T extends VfTreeTrimmingOperationBase> extends AbstractOperationBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected VfTreeTrimmingOperationBaseBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected Set<DataType> getSelectedDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.vfTREE);
		
		return ret;
	}
	
	/////////////////////////////////
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Map<SimpleName, Object> buildVfTreeTrimmingOperationBaseLevelSpecificParameterNameValueObjectMap() throws SQLException{
		MetadataID inputVfTreeMetadataID = (MetadataID) this.getChildrenNodeBuilderNameMap().get(INPUT_VFTREE_METADATAID.getName().getStringValue()).getCurrentValue();
		@SuppressWarnings("unchecked")
		LinkedHashSet<DataTableColumnName> inputNodeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData =
				(LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).getCurrentValue();
		@SuppressWarnings("unchecked")
		LinkedHashSet<DataTableColumnName> inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData =
				(LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).getCurrentValue();
		MetadataName outputVfTreeDataName = (MetadataName) this.getChildrenNodeBuilderNameMap().get(OUTPUT_VFTREE_METADATA_ID.getName().getStringValue()).getCurrentValue();
		
		//
		VfTreeDataMetadata vftree = (VfTreeDataMetadata)this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputVfTreeMetadataID);
		
		return VfTreeTrimmingOperationBase.buildVfTreeTrimmingOperationBaseLevelSpecificParameterNameValueObjectMap(
				inputVfTreeMetadataID, 
				vftree.getNodeRecordMetadataID(),
				vftree.getEdgeRecordMetadataID(),
				inputNodeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData, 
				inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData, 
				outputVfTreeDataName);
	}
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		//INPUT_VFTREE_METADATAID
		this.addChildNodeBuilder(
				new MetadataIDSelector(
						INPUT_VFTREE_METADATAID.getName().getStringValue(), INPUT_VFTREE_METADATAID.getDescriptiveName(), 
						INPUT_VFTREE_METADATAID.canHaveNullValueObject(this.isForReproducing()), this, 
					this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
					this.getSelectedDataTypeSet(),
					null//
				));
		
		//INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue(), 
				INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getDescriptiveName(), 
				INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		//INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue(), 
				INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getDescriptiveName(), 
				INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		//OUTPUT_VFTREE_METADATA_NAME
		this.addChildNodeBuilder(new VfNameStringBuilder<MetadataName>(
				OUTPUT_VFTREE_METADATA_ID.getName().getStringValue(), OUTPUT_VFTREE_METADATA_ID.getDescriptiveName(),
				OUTPUT_VFTREE_METADATA_ID.canHaveNullValueObject(this.isForReproducing()), this,
				MetadataName.class
				));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		MetadataIDSelector inputVfTreeMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_VFTREE_METADATAID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeRecordNonMandatoryAdditionalFeatureColumnSetSelector = 
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetSelector =
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue());
		
		
		//when INPUT_VFTREE_METADATAID changed
		//INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP and INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP need to change accordingly
		Runnable inputVfTreeMetadataIDSelectorStatusChangeEventAction = ()->{
			if(inputVfTreeMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
				inputNodeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(new ArrayList<>());//set to an empty collection of columns
				inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(new ArrayList<>());
				
			}else if(inputVfTreeMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
				//skip since this will never happen;
			}else {//non-null valid value
				//
				try {
					VfTreeDataMetadata selectedVfTreeDataMetadata = (VfTreeDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputVfTreeMetadataIDSelector.getCurrentValue());
					
					inputNodeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(selectedVfTreeDataMetadata.getGraphVertexFeature().getNonMandatoryAdditionalColumnNameSet()); 
					inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(selectedVfTreeDataMetadata.getGraphEdgeFeature().getNonMandatoryAdditionalColumnNameSet()); 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
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
			this.getChildrenNodeBuilderNameMap().get(INPUT_VFTREE_METADATAID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(OUTPUT_VFTREE_METADATA_ID.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T vfTreeTrimmingOperationBase = (T)value;
				//need to first set the pools
				try {
					//first retrieve the RecordDataMetadata of the input edge record data
					VfTreeDataMetadata selectedVfTreeDataMetadata = (VfTreeDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().
							lookup(vfTreeTrimmingOperationBase.getInputVfTreeMetadataID());//!!!!!!!!!!!!
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeRecordNonMandatoryAdditionalFeatureColumnSetSelector = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetSelector =
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue());
					
					///set pools
					inputNodeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(selectedVfTreeDataMetadata.getGraphVertexFeature().getNonMandatoryAdditionalColumnNameSet()); 
					inputEdgeRecordNonMandatoryAdditionalFeatureColumnSetSelector.setPool(selectedVfTreeDataMetadata.getGraphEdgeFeature().getNonMandatoryAdditionalColumnNameSet()); 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				this.getChildrenNodeBuilderNameMap().get(INPUT_VFTREE_METADATAID.getName().getStringValue()).setValue(
						vfTreeTrimmingOperationBase.getInputVfTreeMetadataID(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).setValue(
						vfTreeTrimmingOperationBase.getInputNodeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData().getSet(), isEmpty);//!!!!!
				this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_NON_MANDATORY_ADDITIONAL_FEATURE_COLUMN_SET_TO_KEEP.getName().getStringValue()).setValue(
						vfTreeTrimmingOperationBase.getInputEdgeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData().getSet(), isEmpty);//!!!!!
				this.getChildrenNodeBuilderNameMap().get(OUTPUT_VFTREE_METADATA_ID.getName().getStringValue()).setValue(
						vfTreeTrimmingOperationBase.getOutputVfTreeDataID().getName(), isEmpty);
			}
		}
		
		return changed;
	}
	
	@Override
	protected abstract T build() throws SQLException;
}
