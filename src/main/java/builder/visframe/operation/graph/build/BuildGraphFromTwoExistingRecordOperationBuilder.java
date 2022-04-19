package builder.visframe.operation.graph.build;

import static operation.graph.build.BuildGraphFromExistingRecordOperationBase.*;
import static operation.graph.build.BuildGraphFromTwoExistingRecordOperation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.misc.DoubleMapValueSelector;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import metadata.MetadataID;
import metadata.record.RecordDataMetadata;
import operation.graph.build.BuildGraphFromTwoExistingRecordOperation;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;
import utils.CollectionUtils;
import utils.Pair;

/**
 * builder for a {@link BuildGraphFromTwoExistingRecordOperation} that employs a {@link DoubleMapValueSelector} to build 
 * {@link BuildGraphFromTwoExistingRecordOperation#NODE_ID_COLUMN_NAME_EDGE_SOURCE_NODE_ID_COLUMN_NAME_MAP} and
 * {@link BuildGraphFromTwoExistingRecordOperation#NODE_ID_COLUMN_NAME_EDGE_SINK_NODE_ID_COLUMN_NAME_MAP} together;
 * 
 * the {@link DoubleMapValueSelector} will enforce the following properties:
 * 1. no duplicate column selected for both map;
 * 2. each pair of columns in a map entry will have the same SQLDataType;
 * 
 * 
 * @author tanxu
 *
 */
public class BuildGraphFromTwoExistingRecordOperationBuilder extends BuildGraphFromExistingRecordOperationBaseBuilder<BuildGraphFromTwoExistingRecordOperation>{
	public static final String NODE_NAME = "BuildGraphFromTwoExistingRecordOperation";
	public static final String NODE_DESCRIPTION = "BuildGraphFromTwoExistingRecordOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public BuildGraphFromTwoExistingRecordOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	/////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	private Map<SimpleName, Object> buildBuildGraphFromTwoExistingRecordOperationLevelSpecificParameterNameValueObjectMap(){
		MetadataID inputNodeRecordDataMetadataID = (MetadataID) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_METADATAID.getName().getStringValue()).getCurrentValue();
		
		
		LinkedHashSet<DataTableColumnName> inputNodeDataTableColumnSetAsNodeID = 
				(LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue()).getCurrentValue();
		
		LinkedHashSet<DataTableColumnName> inputNodeDataTableColumnSetAsAdditionalFeature = 
				(LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).getCurrentValue();
		
		//
		Pair<LinkedHashMap<DataTableColumnName,DataTableColumnName>, LinkedHashMap<DataTableColumnName,DataTableColumnName>> selectedColumns = 
				(Pair<LinkedHashMap<DataTableColumnName, DataTableColumnName>, LinkedHashMap<DataTableColumnName, DataTableColumnName>>) this.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap).getCurrentValue();
		
		LinkedHashMap<DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSourceNodeIDColumnNameMap = selectedColumns.getFirst();
		LinkedHashMap<DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSinkNodeIDColumnNameMap = selectedColumns.getSecond();
		
		boolean toAddDiscoveredVertexFromInputEdgeDataTable = (boolean) this.getChildrenNodeBuilderNameMap().get(TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.getName().getStringValue()).getCurrentValue();
		
		///
		return BuildGraphFromTwoExistingRecordOperation.buildBuildGraphFromTwoExistingRecordOperationLevelSpecificParameterNameValueObjectMap(
				inputNodeRecordDataMetadataID, inputNodeDataTableColumnSetAsNodeID, inputNodeDataTableColumnSetAsAdditionalFeature, 
				nodeIDColumnNameEdgeSourceNodeIDColumnNameMap, nodeIDColumnNameEdgeSinkNodeIDColumnNameMap, 
				toAddDiscoveredVertexFromInputEdgeDataTable);
		
	}
	
	
	/////////////////////////////////////////////
	protected static final String nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap = "nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap";
	protected static final String nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap_description = "nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap";

	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//INPUT_NODE_RECORD_METADATAID
		this.addChildNodeBuilder(
				new MetadataIDSelector(
					INPUT_NODE_RECORD_METADATAID.getName().getStringValue(), INPUT_NODE_RECORD_METADATAID.getDescriptiveName(), 
					INPUT_NODE_RECORD_METADATAID.canHaveNullValueObject(this.isForReproducing()), this, 
					this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
					this.getSelectedDataTypeSet(),
					null
				));
		
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue(), 
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getDescriptiveName(), 
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue(), 
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getDescriptiveName(), 
				INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		//NODE_ID_COLUMN_NAME_EDGE_SOURCE_NODE_ID_COLUMN_NAME_MAP and NODE_ID_COLUMN_NAME_EDGE_SINK_NODE_ID_COLUMN_NAME_MAP
		this.addChildNodeBuilder(new DoubleMapValueSelector<DataTableColumn,DataTableColumn,DataTableColumnName,DataTableColumnName>(
				nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap, nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap_description,
				false, this, 
				
				e->{return e.getName();},//Function<K1,K2> mapKeyFunction, DataTableColumn to DataTableColumnName
				e->{return e.getName();},//Function<V1,V2> mapValueFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<K1,String> inputMapKeyToStringFunction, DataTableColumn to string
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<V1,String> inputMapValueToStringFunction,
				e->{return e.getFirst().getSqlDataType().equals(e.getSecond().getSqlDataType());},//Predicate<Pair<K1,V1>> mapKeyValueConstraint, map key and value column's data type must be the same
				"columns of each map entry must have the same SQLDataType!"//String mapKeyValueConstraintDescription
				));
		
		
		//TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.getName().getStringValue(),
				TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.getDescriptiveName(),
				TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.canHaveNullValueObject(this.isForReproducing()),
				this));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID cannot be empty
		Set<String> set1 = new HashSet<>();
		set1.add(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<BuildGraphFromTwoExistingRecordOperation> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "inputNodeDataTableColumnSetAsNodeID cannot be empty set!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeDataTableColumnSetAsNodeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
					
					return !inputNodeDataTableColumnSetAsNodeIDBuilder.getCurrentValue().isEmpty();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE must be disjoint with INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID
		Set<String> set2 = new HashSet<>();
		set2.add(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
		set2.add(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<BuildGraphFromTwoExistingRecordOperation> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID must be disjoint with INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE!",
				set2, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> nodeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> additionalFeatureBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					
					return CollectionUtils.setsAreDisjoint(nodeIDBuilder.getCurrentValue(), additionalFeatureBuilder.getCurrentValue());
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		
		//if super.EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS is true, selected columns from input edge data as the source/sink node id columns must be disjoint with super.INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID
		//else, selected columns from input edge data as the source/sink node id columns must be a subset of super.INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID
		Set<String> set3 = new HashSet<>();
		set3.add(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue());
		set3.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
		set3.add(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
		GenricChildrenNodeBuilderConstraint<BuildGraphFromTwoExistingRecordOperation> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS is true, selected columns from input edge data as the source/sink node id columns must be disjoint with INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID; "
						+ "else, selected columns from input edge data as the source/sink node id columns must be a subset of INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID!",
				set3, 
				e->{
					BooleanTypeBuilder edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsBuilder = 
							(BooleanTypeBuilder) e.getChildrenNodeBuilderNameMap().get(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					DoubleMapValueSelector<DataTableColumn,DataTableColumn,DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder = 
							(DoubleMapValueSelector<DataTableColumn, DataTableColumn, DataTableColumnName, DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
					
					Set<DataTableColumnName> selectedEdgeColumnAsNodeIDs = new HashSet<>();
					selectedEdgeColumnAsNodeIDs.addAll(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.getCurrentValue().getFirst().values());
					selectedEdgeColumnAsNodeIDs.addAll(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.getCurrentValue().getSecond().values());
					
					if(edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsBuilder.getCurrentValue()) {//disjoint
						return CollectionUtils.setsAreDisjoint(selectedEdgeColumnAsNodeIDs, inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue());
					}else {//
						return CollectionUtils.set1IsSuperSetOfSet2(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), selectedEdgeColumnAsNodeIDs);
					}
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		
		
		//selected columns from input edge data as the source/sink node id columns must be disjoint with super.INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE;
		Set<String> set4 = new HashSet<>();
		set4.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
		set4.add(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
		GenricChildrenNodeBuilderConstraint<BuildGraphFromTwoExistingRecordOperation> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected columns from input edge data as the source/sink node id columns must be disjoint with super.INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE!",
				set4, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeatureSelector = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					DoubleMapValueSelector<DataTableColumn,DataTableColumn,DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder = 
							(DoubleMapValueSelector<DataTableColumn, DataTableColumn, DataTableColumnName, DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
					
					Set<DataTableColumnName> selectedEdgeColumnAsNodeIDs = new HashSet<>();
					selectedEdgeColumnAsNodeIDs.addAll(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.getCurrentValue().getFirst().values());
					selectedEdgeColumnAsNodeIDs.addAll(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.getCurrentValue().getSecond().values());
					
					return CollectionUtils.setsAreDisjoint(selectedEdgeColumnAsNodeIDs, inputEdgeDataTableColumnSetAsAdditionalFeatureSelector.getCurrentValue());
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c4);
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//prepare
		MetadataIDSelector inputEdgeRecordDataMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue());
		
		///////
		MetadataIDSelector inputNodeRecordDataMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_METADATAID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeDataTableColumnSetAsNodeIDBuilder = 
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeDataTableColumnSetAsAdditionalFeatureBuilder =
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
	
		
		@SuppressWarnings("unchecked")
		DoubleMapValueSelector<DataTableColumn,DataTableColumn,DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder = 
				(DoubleMapValueSelector<DataTableColumn, DataTableColumn, DataTableColumnName, DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
		
		
		//===============================
		//when super.INPUT_EDGE_RECORD_METADATAID changed, following need to change accordingly
		//nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap 
		Runnable inputEdgeRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {//default empty, the nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap cannot be started;
					
					nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setPool(new ArrayList<>(), new ArrayList<>());
					
					nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setToDefaultEmpty();
					
				}else if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					if(inputNodeDataTableColumnSetAsNodeIDBuilder.getCurrentStatus().hasValidValue()) {//edge record data is selected and node id columns from input node record data are selected, set the pool
						
						//
						try {
							
							RecordDataMetadata selectedEdgeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputEdgeRecordDataMetadataIDSelector.getCurrentValue());
	
							RecordDataMetadata selectedNodeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputNodeRecordDataMetadataIDSelector.getCurrentValue());
	
							List<DataTableColumn> mapKeyPool = new ArrayList<>();
							selectedNodeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
								if(inputNodeDataTableColumnSetAsNodeIDBuilder.getCurrentValue().contains(e.getName())) {
									mapKeyPool.add(e);
								}
							});
							
							List<DataTableColumn> mapValuePool = selectedEdgeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn();
							
							nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setPool(mapKeyPool, mapValuePool);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(1);
						}
					}else {//the node record data is not set yet, do not set the pool
						//
					}
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		};
		
		inputEdgeRecordDataMetadataIDSelector.addStatusChangedAction(
				inputEdgeRecordDataMetadataIDSelectorStatusChangeEventAction);
				
		//=================================
		//when INPUT_NODE_RECORD_METADATAID is changed, following need to change accordingly
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID, 
		//INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE, 
		Runnable inputNodeRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			if(inputNodeRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {//default empty
				inputNodeDataTableColumnSetAsNodeIDBuilder.setPool(new ArrayList<>());
				inputNodeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(new ArrayList<>());//set to an empty collection of columns
			}else if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
				//skip since this will never happen;
			}else {//non-null valid value
				//
				try {
					RecordDataMetadata selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputNodeRecordDataMetadataIDSelector.getCurrentValue());

					inputNodeDataTableColumnSetAsNodeIDBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					inputNodeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			
		};
		
		inputNodeRecordDataMetadataIDSelector.addStatusChangedAction(
				inputNodeRecordDataMetadataIDSelectorStatusChangeEventAction);
		
		
		//=================================
		//when INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID changed, following need to change accordingly
		//nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap
		Runnable inputNodeDataTableColumnSetAsNodeIDBuilderStatusChangeEventAction = ()->{
			try {
				if(inputNodeDataTableColumnSetAsNodeIDBuilder.getCurrentStatus().isDefaultEmpty()) {//default empty
					nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setPool(new ArrayList<>(), new ArrayList<>());
					
					nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setToDefaultEmpty();
					
				}else if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().hasValidValue()) {//edge record data is selected and node id columns from input node record data are selected, set the pool
						
						//
						try {
							
							RecordDataMetadata selectedEdgeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputEdgeRecordDataMetadataIDSelector.getCurrentValue());
	
							RecordDataMetadata selectedNodeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputNodeRecordDataMetadataIDSelector.getCurrentValue());
							
							List<DataTableColumn> mapKeyPool = new ArrayList<>();
							selectedNodeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
								if(inputNodeDataTableColumnSetAsNodeIDBuilder.getCurrentValue().contains(e.getName())) {
									mapKeyPool.add(e);
								}
							});
							
							List<DataTableColumn> mapValuePool = selectedEdgeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn();
							
							nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setPool(mapKeyPool, mapValuePool);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(1);
						}
					}else {//the node record data is not set yet, do not set the pool
						//
					}
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		inputNodeDataTableColumnSetAsNodeIDBuilder.addStatusChangedAction(
				inputNodeDataTableColumnSetAsNodeIDBuilderStatusChangeEventAction);
		
		
		
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_METADATAID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.getName().getStringValue()).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				BuildGraphFromTwoExistingRecordOperation buildGraphFromTwoExistingRecordOperation = (BuildGraphFromTwoExistingRecordOperation)value;
				
				//need to first set the pools of INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID, INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE, nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap
				try {
					RecordDataMetadata selectedEdgeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(buildGraphFromTwoExistingRecordOperation.getInputEdgeRecordDataMetadataID());
					RecordDataMetadata selectedNodeRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(buildGraphFromTwoExistingRecordOperation.getInputNodeRecordDataMetadataID());
					
					
					//set pools of inputNodeDataTableColumnSetAsNodeIDBuilder and inputNodeDataTableColumnSetAsAdditionalFeatureBuilder
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeDataTableColumnSetAsNodeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputNodeDataTableColumnSetAsAdditionalFeatureBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					//
					inputNodeDataTableColumnSetAsNodeIDBuilder.setPool(selectedNodeRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					inputNodeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(selectedNodeRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					
					
					
					////////////
					//set pool of nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder
					@SuppressWarnings("unchecked")
					DoubleMapValueSelector<DataTableColumn,DataTableColumn,DataTableColumnName,DataTableColumnName> nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder = 
							(DoubleMapValueSelector<DataTableColumn, DataTableColumn, DataTableColumnName, DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap);
					
					List<DataTableColumn> mapKeyPool = new ArrayList<>();
					selectedNodeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
						if(buildGraphFromTwoExistingRecordOperation.getInputNodeDataTableColumnSetAsNodeID().getSet().contains(e.getName())) {
							mapKeyPool.add(e);
						}
					});
					
					List<DataTableColumn> mapValuePool = selectedEdgeRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn();
					
					nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMapBuilder.setPool(mapKeyPool, mapValuePool);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				////
				this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_RECORD_METADATAID.getName().getStringValue()).setValue(
						buildGraphFromTwoExistingRecordOperation.getInputNodeRecordDataMetadataID(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_NODE_ID.getName().getStringValue()).setValue(
						buildGraphFromTwoExistingRecordOperation.getInputNodeDataTableColumnSetAsNodeID().getSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(INPUT_NODE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).setValue(
						buildGraphFromTwoExistingRecordOperation.getinputNodeDataTableColumnSetAsAdditionalFeature().getSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap).setValue(
						new Pair<>(
								buildGraphFromTwoExistingRecordOperation.getNodeIDColumnNameEdgeSourceNodeIDColumnNameMap().getMap(),
								buildGraphFromTwoExistingRecordOperation.getNodeIDColumnNameEdgeSinkNodeIDColumnNameMap().getMap()
								),
						isEmpty);
				this.getChildrenNodeBuilderNameMap().get(TO_ADD_DISCOVERED_VERTEX_FROM_INPUT_EDGE_DATA_TABLE.getName().getStringValue()).setValue(
						buildGraphFromTwoExistingRecordOperation.isToAddDiscoveredVertexFromInputEdgeDataTable(), isEmpty);
				
				/////////////////
				this.checkIfForReproducing(buildGraphFromTwoExistingRecordOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since BuildGraphFromTwoExistingRecordOperation type does not have parameter dependent on input data table content; 
	}
	
	//////////////////////
	@Override
	protected BuildGraphFromTwoExistingRecordOperation build() {
		return new BuildGraphFromTwoExistingRecordOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildBuildGraphFromExistingRecordOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildBuildGraphFromTwoExistingRecordOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder
				);
	}

}
