package builder.visframe.operation.graph;

import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.AbstractOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.MetadataID;
import operation.graph.SingleGenericGraphAsInputOperation;

import static operation.graph.SingleGenericGraphAsInputOperation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;

public abstract class SingleGenericGraphAsInputOperationBuilder<T extends SingleGenericGraphAsInputOperation> extends AbstractOperationBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected SingleGenericGraphAsInputOperationBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
	}
	
	
	protected Set<DataType> getSelectedDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.GRAPH);
		ret.add(DataType.vfTREE);
		
		return ret;
	}
	
	
	/////////////////////////////////
	protected Map<SimpleName, Object> buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(){
		MetadataID inputGenericGraphMetadataID = (MetadataID) this.getChildrenNodeBuilderNameMap().get(INPUT_GENERIC_GRAPH_METADATAID.getName().getStringValue()).getCurrentValue();
		return SingleGenericGraphAsInputOperation.buildSingleGenericGraphAsInputOperationLevelSpecificParameterNameValueObjectMap(inputGenericGraphMetadataID);
	}
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		//INPUT_GENERIC_GRAPH_METADATAID
		this.addChildNodeBuilder(
				new MetadataIDSelector(
						INPUT_GENERIC_GRAPH_METADATAID.getName().getStringValue(), INPUT_GENERIC_GRAPH_METADATAID.getDescriptiveName(), 
						INPUT_GENERIC_GRAPH_METADATAID.canHaveNullValueObject(this.isForReproducing()), this, 
					this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
					this.getSelectedDataTypeSet(),
					null//
				));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		// TODO Auto-generated method stub
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(INPUT_GENERIC_GRAPH_METADATAID.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T singleGenericGraphAsInputOperation = (T)value;
				this.getChildrenNodeBuilderNameMap().get(INPUT_GENERIC_GRAPH_METADATAID.getName().getStringValue()).setValue(singleGenericGraphAsInputOperation.getInputGenericGraphMetadataID(), isEmpty);
			}
		}
		return changed;
	}
	
	@Override
	protected abstract T build();
}
