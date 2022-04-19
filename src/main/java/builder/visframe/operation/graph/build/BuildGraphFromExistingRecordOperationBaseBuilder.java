package builder.visframe.operation.graph.build;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilder;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.basic.misc.SimpleTypeSelector;
import builder.basic.misc.SimpleTypeSelectorFactory;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.basic.primitive.StringTypeBuilderFactory;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.metadata.graph.type.GraphTypeEnforcerBuilder;
import builder.visframe.operation.AbstractOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import generic.graph.DirectedType;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import metadata.graph.type.GraphTypeEnforcer;
import metadata.record.RecordDataMetadata;
import operation.graph.build.BuildGraphFromExistingRecordOperationBase;
import rdb.table.data.DataTableColumnName;
import utils.CollectionUtils;

import static operation.graph.build.BuildGraphFromExistingRecordOperationBase.*;

/**
 * 
 * @author tanxu
 *
 * @param <T>
 */
public abstract class BuildGraphFromExistingRecordOperationBaseBuilder<T extends BuildGraphFromExistingRecordOperationBase> extends AbstractOperationBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected BuildGraphFromExistingRecordOperationBaseBuilder(
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
		ret.add(DataType.RECORD);
		
		return ret;
	}
	
	protected Collection<DirectedType> getDirectedTypes(){
		Collection<DirectedType> dataTypes = new ArrayList<>();
		for(DirectedType type:DirectedType.values()) {
			dataTypes.add(type);
		}
		return dataTypes;
	}
	
	
	
	/////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	protected Map<SimpleName, Object> buildBuildGraphFromExistingRecordOperationBaseLevelSpecificParameterNameValueObjectMap(){
		
		MetadataID inputEdgeRecordDataMetadataID = (MetadataID) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue()).getCurrentValue();
		//!!
		LinkedHashSet<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeID = (LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue()).getCurrentValue();
		
		//!!
		LinkedHashSet<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeature = (LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).getCurrentValue();
		
		boolean edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets = (boolean) this.getChildrenNodeBuilderNameMap().get(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue()).getCurrentValue();
		
		GraphTypeEnforcer graphTypeEnforcer = (GraphTypeEnforcer) this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).getCurrentValue();
		
		boolean hasDirectedTypeIndicatorColumn = (boolean) this.getChildrenNodeBuilderNameMap().get(HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getName().getStringValue()).getCurrentValue();
		//!!
		DataTableColumnName directedTypeIndicatorColumnName = 
				(DataTableColumnName) this.getChildrenNodeBuilderNameMap().get(DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue()).getCurrentValue();
		
		HashMap<String,DirectedType> directedIndicatorColumnStringValueDirectedTypeMap = (HashMap<String, DirectedType>) this.getChildrenNodeBuilderNameMap().get(DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getName().getStringValue()).getCurrentValue();
		
		DirectedType defaultDirectedType = (DirectedType) this.getChildrenNodeBuilderNameMap().get(DEFAULT_DIRECTED_TYPE.getName().getStringValue()).getCurrentValue();
		
		MetadataName outputGraphDataName = (MetadataName) this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).getCurrentValue();
		
		return BuildGraphFromExistingRecordOperationBase.buildBuildGraphFromExistingRecordOperationBaseLevelSpecificParameterNameValueObjectMap(
				inputEdgeRecordDataMetadataID, inputEdgeDataTableColumnSetAsEdgeID, inputEdgeDataTableColumnSetAsAdditionalFeature, 
				edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets, graphTypeEnforcer, hasDirectedTypeIndicatorColumn, 
				directedTypeIndicatorColumnName, directedIndicatorColumnStringValueDirectedTypeMap, defaultDirectedType, outputGraphDataName);
	}
	
	
	
	/////////////////////////////////////////////
	/**
	 * adding order of child node builders is constrained by the {@link #addStatusChangeEventActionOfChildNodeBuilders()}
	 * 1. INPUT_EDGE_RECORD_METADATAID must be added before
	 * INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID, INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE, DIRECTED_TYPE_INDICATOR_COLUMN_NAME
	 * 
	 * 2. HAS_DIRECTED_TYPE_INDICATOR_COLUMN must be added before 
	 * DIRECTED_TYPE_INDICATOR_COLUMN_NAME, DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP, DEFAULT_DIRECTED_TYPE
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		//INPUT_EDGE_RECORD_METADATAID
		this.addChildNodeBuilder(
				new MetadataIDSelector(
					INPUT_EDGE_RECORD_METADATAID.getName().getStringValue(), INPUT_EDGE_RECORD_METADATAID.getDescriptiveName(), 
					INPUT_EDGE_RECORD_METADATAID.canHaveNullValueObject(this.isForReproducing()), this, 
					this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
					this.getSelectedDataTypeSet(),
					null
				));
		//INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
					INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue(), INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getDescriptiveName(), 
					INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.canHaveNullValueObject(this.isForReproducing()), this,
					e->{return e.getStringValue();}
				));
		
		//INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue(), INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getDescriptiveName(), 
				INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		
		//EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue(),
				EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getDescriptiveName(),
				EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.canHaveNullValueObject(this.isForReproducing()),
				this));
		
		
		///////////////////////////////
		//GRAPH_TYPE_ENFORCER
		this.addChildNodeBuilder(new GraphTypeEnforcerBuilder(
				GRAPH_TYPE_ENFORCER.getName().getStringValue(), GRAPH_TYPE_ENFORCER.getDescriptiveName(),
				GRAPH_TYPE_ENFORCER.canHaveNullValueObject(this.isForReproducing()), this
				));
		
		
		///////////////////////////////
		//HAS_DIRECTED_TYPE_INDICATOR_COLUMN
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getName().getStringValue(), HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getDescriptiveName(),
				HAS_DIRECTED_TYPE_INDICATOR_COLUMN.canHaveNullValueObject(this.isForReproducing()), this));
		
		
		//DIRECTED_TYPE_INDICATOR_COLUMN_NAME
		this.addChildNodeBuilder(new SimpleTypeSelector<DataTableColumnName>(
				DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue(), DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getDescriptiveName(),
				DIRECTED_TYPE_INDICATOR_COLUMN_NAME.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return e.getStringValue();},
				e->{return e.getStringValue();}
				));
		
		
		//DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP
		StringTypeBuilderFactory directedIndicatorColumnStringValueBuilderFactory = new StringTypeBuilderFactory(
				"directedIndicatorColumnStringValue","directedIndicatorColumnStringValue",false);
		SimpleTypeSelectorFactory<DirectedType> directedTypeSelectorFactory = new SimpleTypeSelectorFactory<>(
				"directedType","directedType", false, 
//				getDirectedTypes(),
				e->{return e.toString();}, //typeToStringRepresentationFunction
				e->{return e.name();} //typeToDescriptionFunction 
				);
		directedTypeSelectorFactory.setPool(this.getDirectedTypes());
		this.addChildNodeBuilder(new HashMapNonLeafNodeBuilder<String, DirectedType>(
				DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getName().getStringValue(), DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getDescriptiveName(),
				DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.canHaveNullValueObject(this.isForReproducing()), this,	
				directedIndicatorColumnStringValueBuilderFactory, directedTypeSelectorFactory));
		
		//DEFAULT_DIRECTED_TYPE
		SimpleTypeSelector<DirectedType> defaultDirectedTypeSelector = new SimpleTypeSelector<>(
				DEFAULT_DIRECTED_TYPE.getName().getStringValue(), DEFAULT_DIRECTED_TYPE.getDescriptiveName(),
				DEFAULT_DIRECTED_TYPE.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return e.toString();}, //typeToStringRepresentationFunction
				e->{return e.name();} //typeToDescriptionFunction 
				);
		defaultDirectedTypeSelector.setPool(this.getDirectedTypes());
		this.addChildNodeBuilder(defaultDirectedTypeSelector);
		
		///////////////////////////////////
		//OUTPUT_GRAPH_DATA_NAME
		this.addChildNodeBuilder(new VfNameStringBuilder<MetadataName>(
				OUTPUT_GRAPH_DATA_ID.getName().getStringValue(), OUTPUT_GRAPH_DATA_ID.getDescriptiveName(),
				OUTPUT_GRAPH_DATA_ID.canHaveNullValueObject(this.isForReproducing()), this,
				MetadataName.class
				));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		//inputEdgeDataTableColumnSetAsEdgeID cannot be empty set;
		Set<String> set1 = new HashSet<>();
		set1.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<T> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "inputEdgeDataTableColumnSetAsEdgeID cannot be empty set!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
					
					return !inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue().isEmpty();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE must be disjoint with INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID
		Set<String> set2 = new HashSet<>();
		set2.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
		set2.add(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<T> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE must be disjoint with INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID!",
				set2, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					
					return CollectionUtils.setsAreDisjoint(inputEdgeDataTableColumnSetAsEdgeIDBuilder.getCurrentValue(), inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.getCurrentValue());
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		///prepare involved node builders
		
		MetadataIDSelector inputEdgeRecordDataMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder =
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
		
		BooleanTypeBuilder hasDirectedTypeIndicatorColumnBuilder = (BooleanTypeBuilder) this.getChildrenNodeBuilderNameMap().get(HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		SimpleTypeSelector<DataTableColumnName> directedTypeIndicatorColumnNameSelector = (SimpleTypeSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		HashMapNonLeafNodeBuilder<String, DirectedType> directedIndicatorColumnStringValueDirectedTypeMapBuilder = (HashMapNonLeafNodeBuilder<String, DirectedType>) this.getChildrenNodeBuilderNameMap().get(DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		SimpleTypeSelector<DirectedType> defaultDirectedTypeSelector = (SimpleTypeSelector<DirectedType>) this.getChildrenNodeBuilderNameMap().get(DEFAULT_DIRECTED_TYPE.getName().getStringValue());
		

		//===================================================
		//when INPUT_EDGE_RECORD_METADATAID node builder status is changed
		//INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID, INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE and DIRECTED_TYPE_INDICATOR_COLUMN_NAME(if not null) should be reset
		Runnable inputEdgeRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					inputEdgeDataTableColumnSetAsEdgeIDBuilder.setPool(new ArrayList<>());//set to an empty collection of columns
					inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(new ArrayList<>());
					if(hasDirectedTypeIndicatorColumnBuilder.getCurrentValue()) {
						
						directedTypeIndicatorColumnNameSelector.setPool(new ArrayList<>());
						
					}
				}else if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					//
					try {
						RecordDataMetadata selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputEdgeRecordDataMetadataIDSelector.getCurrentValue());
	
						inputEdgeDataTableColumnSetAsEdgeIDBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
						inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
						
						if(hasDirectedTypeIndicatorColumnBuilder.getCurrentValue()) {
							directedTypeIndicatorColumnNameSelector.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
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
		
		
		//===================================================
		//when HAS_DIRECTED_TYPE_INDICATOR_COLUMN is changed, 
		//DIRECTED_TYPE_INDICATOR_COLUMN_NAME, DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP and DEFAULT_DIRECTED_TYPE should be updated according;
		Runnable hasDirectedTypeIndicatorColumnBuilderStatusChangeEventAction = ()->{
			try {
				if(hasDirectedTypeIndicatorColumnBuilder.getCurrentValue()) {//has indicator column
					directedTypeIndicatorColumnNameSelector.setToNonNull();
					directedIndicatorColumnStringValueDirectedTypeMapBuilder.setToNonNull();
					
					if(inputEdgeRecordDataMetadataIDSelector.getCurrentStatus().hasValidValue()) {//
						RecordDataMetadata selectedRecordDataMetadata;
						//
						
						selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputEdgeRecordDataMetadataIDSelector.getCurrentValue());
						directedTypeIndicatorColumnNameSelector.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
						
					}
					
				}else {
					directedTypeIndicatorColumnNameSelector.setToNull();
					directedIndicatorColumnStringValueDirectedTypeMapBuilder.setToNull();
					//there must be a default directed type if there is no indicator column;
					defaultDirectedTypeSelector.setToNonNull();
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		};
		
		hasDirectedTypeIndicatorColumnBuilder.addStatusChangedAction(
				hasDirectedTypeIndicatorColumnBuilderStatusChangeEventAction);
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(DEFAULT_DIRECTED_TYPE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T buildGraphFromExistingRecordOperationBase = (T)value;
				
				//need to first set the pools
				try {
					//first retrieve the RecordDataMetadata of the input edge record data
					RecordDataMetadata selectedRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().
							lookup(buildGraphFromExistingRecordOperationBase.getInputEdgeRecordDataMetadataID());//!!!!!!!!!!!!
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsEdgeIDBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder =
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue());
					@SuppressWarnings("unchecked")
					SimpleTypeSelector<DataTableColumnName> directedTypeIndicatorColumnNameSelector = (SimpleTypeSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue());
					
					///set pools
					inputEdgeDataTableColumnSetAsEdgeIDBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					inputEdgeDataTableColumnSetAsAdditionalFeatureBuilder.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					directedTypeIndicatorColumnNameSelector.setPool(selectedRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName());
					//
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				
				///////////////////////
				this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_RECORD_METADATAID.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getInputEdgeRecordDataMetadataID(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_EDGE_ID.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getInputEdgeDataTableColumnSetAsEdgeID().getSet(), isEmpty);//!!!!!
				this.getChildrenNodeBuilderNameMap().get(INPUT_EDGE_DATA_TABLE_COLUMN_SET_AS_ADDITIONAL_FEATURE.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getInputEdgeDataTableColumnSetAsAdditionalFeature().getSet(), isEmpty);//!!!!!
				this.getChildrenNodeBuilderNameMap().get(EDGE_ID_COLUMN_SET_DISJOINT_WITH_SOURCE_AND_SINK_NODE_ID_COLUMN_SETS.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.isEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(HAS_DIRECTED_TYPE_INDICATOR_COLUMN.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.hasDirectedTypeIndicatorColumn(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(DIRECTED_TYPE_INDICATOR_COLUMN_NAME.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getDirectedTypeIndicatorColumnName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(DIRECTED_INDICATOR_COLUMN_STRING_VALUE_DIRECTED_TYPE_MAP.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getDirectedIndicatorColumnStringValueDirectedTypeMap().getMap(), isEmpty);//!!!!!
				this.getChildrenNodeBuilderNameMap().get(DEFAULT_DIRECTED_TYPE.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getDefaultDirectedType(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(GRAPH_TYPE_ENFORCER.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getGraphTypeEnforcer(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(OUTPUT_GRAPH_DATA_ID.getName().getStringValue()).setValue(
						buildGraphFromExistingRecordOperationBase.getOutputGraphMetadataID().getName(), isEmpty); //
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected abstract T build();

}
