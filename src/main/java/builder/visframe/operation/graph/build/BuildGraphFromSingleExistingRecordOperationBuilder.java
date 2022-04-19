package builder.visframe.operation.graph.build;

import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import metadata.record.RecordDataMetadata;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;
import utils.CollectionUtils;
import utils.Pair;

import static operation.graph.build.BuildGraphFromExistingRecordOperationBase.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.misc.PairSetSelector;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.metadata.MetadataIDSelector;


/**
 * builder of a BuildGraphFromSingleExistingRecordOperation
 * 
 * this class employs {@link PairSetSelector} to build the 
 * {@link BuildGraphFromSingleExistingRecordOperation#SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET} and 
 * {@link BuildGraphFromSingleExistingRecordOperation#SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET} together,
 * instead of using one {@link FixedPoolLinkedHashSetSelector} for each of them in {@link BuildGraphFromSingleExistingRecordOperationBuilder_dump};
 * 
 * the advantage of {@link PairSetSelector} is that it can enforce the following constraints at building time:
 * 1. every selected column will be different from others (uniqueness)
 * 2. every pair of selected columns with the same order index in source and sink column set will have same {@link SQLDataType};
 * 
 * @author tanxu
 *
 */
public final class BuildGraphFromSingleExistingRecordOperationBuilder extends BuildGraphFromExistingRecordOperationBaseBuilder<BuildGraphFromSingleExistingRecordOperation>{
	public static final String NODE_NAME = "BuildGraphFromSingleExistingRecordOperation";
	public static final String NODE_DESCRIPTION = "BuildGraphFromSingleExistingRecordOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public BuildGraphFromSingleExistingRecordOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	/////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	private Map<SimpleName, Object> buildBuildGraphFromSingleExistingRecordOperationLevelSpecificParameterNameValueObjectMap(){
		Pair<LinkedHashSet<DataTableColumnName>, LinkedHashSet<DataTableColumnName>> selectedColumns = (Pair<LinkedHashSet<DataTableColumnName>, LinkedHashSet<DataTableColumnName>>) this.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns).getCurrentValue();
		
		LinkedHashSet<DataTableColumnName> sourceVertexIDColumnNameLinkedHashSet = selectedColumns.getFirst();
		
		LinkedHashSet<DataTableColumnName> sinkVertexIDColumnNameLinkedHashSet = selectedColumns.getSecond();
		
		return BuildGraphFromSingleExistingRecordOperation.buildBuildGraphFromSingleExistingRecordOperationLevelSpecificParameterNameValueObjectMap(
				sourceVertexIDColumnNameLinkedHashSet, sinkVertexIDColumnNameLinkedHashSet);
	}
	
	
	/////////////////////////////////////////////
	//
	protected static final String sourceAndSinkVertexIDColumns = "sourceAndSinkVertexIDColumns";
	protected static final String sourceAndSinkVertexIDColumns_description = "sourceAndSinkVertexIDColumns";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		
		//SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET selector
		this.addChildNodeBuilder(new PairSetSelector<DataTableColumn, DataTableColumnName>(
				sourceAndSinkVertexIDColumns, sourceAndSinkVertexIDColumns_description,
				false, this,
				
				e->{return e.getName();},//Function<T,R> returnedTypeFunction, 
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<T,String> toStringFunction,
				e->{return e.getFirst().getSqlDataType().equals(e.getSecond().getSqlDataType());},//Predicate<Pair<T,T>> pairConstraint,
				"column data type must be the same for each pair!"//String pairConstraintDescription
				));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET must be disjoint
		//enforced implicitly by {@link PairSetSelector}
		
		//every selected column with the same order index in source and sink column set must have the same SQLDataType;
		//enforced implicitly by {@link PairSetSelector}
		
		
		
		//INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE must be disjoint with SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET
		Set<String> set1 = new HashSet<>();
		set1.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
		set1.add(sourceAndSinkVertexIDColumns);
		
		GenricChildrenNodeBuilderConstraint<BuildGraphFromSingleExistingRecordOperation> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE must be disjoint with SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET!",
				set1,
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					PairSetSelector<DataTableColumn, DataTableColumnName> sourceAndSinkVertexIDColumnsSelector = 
						(PairSetSelector<DataTableColumn, DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns);
					
					//
					return CollectionUtils.setsAreDisjoint(inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getFirst()) &&
							CollectionUtils.setsAreDisjoint(inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getSecond());
				
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
	
		
		//if EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS is true, INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID must be disjoint with both SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET
		//else, it must be a superset of both SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET
		Set<String> set2 = new HashSet<>();
		set2.add(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue());
		set2.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
		set2.add(sourceAndSinkVertexIDColumns);
		GenricChildrenNodeBuilderConstraint<BuildGraphFromSingleExistingRecordOperation> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS is true, "
						+ "INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID must be disjoint with both SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET; "
						+ "else, it must be a superset of both SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET!",
				set2,
				e->{
					BooleanTypeBuilder edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsBuilder = 
							(BooleanTypeBuilder) e.getChildrenNodeBuilderNameMap().get(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					PairSetSelector<DataTableColumn, DataTableColumnName> sourceAndSinkVertexIDColumnsSelector = 
						(PairSetSelector<DataTableColumn, DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns);
					
					//
					if(edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsBuilder.getCurrentValue()) {
						return CollectionUtils.setsAreDisjoint(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getFirst()) &&
								CollectionUtils.setsAreDisjoint(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getSecond());
						
					}else {//super set
						return CollectionUtils.set1IsSuperSetOfSet2(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getFirst()) && 
								CollectionUtils.set1IsSuperSetOfSet2(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), sourceAndSinkVertexIDColumnsSelector.getCurrentValue().getSecond());

					}
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		
		//
		MetadataIDSelector inputEdgeRecordDataMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue());
		
		
		@SuppressWarnings("unchecked")
		PairSetSelector<DataTableColumn, DataTableColumnName> sourceAndSinkVertexIDColumnsSelector = (PairSetSelector<DataTableColumn, DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns);
		
		//===================================================
		//when INPUT_EDGE_RECORD_METADATAID node builder status is changed
		//SOURCE_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET and SINK_VERTEX_ID_COLUMN_NAME_LINKED_HASH_SET should be reset
		Runnable inputEdgeRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					sourceAndSinkVertexIDColumnsSelector.setPool(new ArrayList<>());
					
					sourceAndSinkVertexIDColumnsSelector.setToDefaultEmpty();
				}else if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					try {
						RecordDataMetadata selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputEdgeRecordDataMetadataIDSelector.getCurrentValue());
						sourceAndSinkVertexIDColumnsSelector.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn());
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
		
		inputEdgeRecordDataMetadataIDSelector.addStatusChangedAction(
				inputEdgeRecordDataMetadataIDSelectorStatusChangeEventAction);
		
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				BuildGraphFromSingleExistingRecordOperation buildGraphFromSingleExistingRecordOperation = (BuildGraphFromSingleExistingRecordOperation)value;
				
				//need to first set the pools
				try {
					RecordDataMetadata selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(buildGraphFromSingleExistingRecordOperation.getInputEdgeRecordDataMetadataID());
					
					@SuppressWarnings("unchecked")
					PairSetSelector<DataTableColumn, DataTableColumnName> sourceAndSinkVertexIDColumnsSelector = 
						(PairSetSelector<DataTableColumn, DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns);
					
					//set pool
					sourceAndSinkVertexIDColumnsSelector.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				//set value
				this.getChildrenNodeBuilderNameMap().get(sourceAndSinkVertexIDColumns).setValue(
						new Pair<>(
							buildGraphFromSingleExistingRecordOperation.getSourceVertexIDColumnNameLinkedHashSet().getSet(),
							buildGraphFromSingleExistingRecordOperation.getSinkVertexIDColumnNameLinkedHashSet().getSet()), 
						isEmpty);
				
				
				//////////////////////////////////////////////
				/////
				this.checkIfForReproducing(buildGraphFromSingleExistingRecordOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	
	/**
	 * 
	 */
	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since BuildGraphFromSingleExistingRecordOperation type does not have parameter dependent on input data table content; 
	}
	
	
	///////////////////////////
	@Override
	protected BuildGraphFromSingleExistingRecordOperation build() {
		return new BuildGraphFromSingleExistingRecordOperation(
//				this.isForReproducing(),//////
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildBuildGraphFromExistingRecordOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildBuildGraphFromSingleExistingRecordOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder
				);
	}
	
}
