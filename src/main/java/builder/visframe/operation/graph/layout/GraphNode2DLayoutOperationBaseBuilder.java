package builder.visframe.operation.graph.layout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import basic.SimpleName;
import builder.basic.primitive.IntegerTypeBuilder;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.operation.graph.InputGraphTypeBoundedOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.MetadataName;
import operation.graph.layout.GraphNode2DLayoutOperationBase;

import static operation.graph.layout.GraphNode2DLayoutOperationBase.*;



public abstract class GraphNode2DLayoutOperationBaseBuilder<T extends GraphNode2DLayoutOperationBase> extends InputGraphTypeBoundedOperationBuilder<T>{
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 */
	protected GraphNode2DLayoutOperationBaseBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext,
			boolean resultedFromReproducing
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostVisProjectDBContext, resultedFromReproducing);
		
	}
	
	/////////////////////////////////
	protected Map<SimpleName, Object> buildGraphNode2DLayoutOperationBaseLevelSpecificParameterNameValueObjectMap(){
		int drawingAreaWidth = (int) this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_WIDTH.getName().getStringValue()).getCurrentValue();
		int drawingAreaHeight = (int) this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_HEIGHT.getName().getStringValue()).getCurrentValue();
		MetadataName outputLayoutRecordDataName = (MetadataName) this.getChildrenNodeBuilderNameMap().get(OUTPUT_LAYOUT_RECORD_DATA_ID.getName().getStringValue()).getCurrentValue();
		
		return GraphNode2DLayoutOperationBase.buildGraphNode2DLayoutOperationBaseLevelSpecificParameterNameValueObjectMap(drawingAreaWidth, drawingAreaHeight, outputLayoutRecordDataName);
	}
	
	/////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		//DRAWING_AREA_WIDTH
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				DRAWING_AREA_WIDTH.getName().getStringValue(), DRAWING_AREA_WIDTH.getDescriptiveName(), 
				DRAWING_AREA_WIDTH.canHaveNullValueObject(this.isForReproducing()), this, 
				
				DRAWING_AREA_WIDTH.getNonNullValueAdditionalConstraints(),
				"drawing area width must be positive integer!"
				));
		
		//DRAWING_AREA_HEIGHT
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				DRAWING_AREA_HEIGHT.getName().getStringValue(), DRAWING_AREA_HEIGHT.getDescriptiveName(), 
				DRAWING_AREA_HEIGHT.canHaveNullValueObject(this.isForReproducing()), this, 
				
				DRAWING_AREA_HEIGHT.getNonNullValueAdditionalConstraints(),
				"drawing area height must be positive integer!"
				));
		
		//OUTPUT_LAYOUT_RECORD_DATA_NAME
		this.addChildNodeBuilder(new VfNameStringBuilder<MetadataName>(
				OUTPUT_LAYOUT_RECORD_DATA_ID.getName().getStringValue(), OUTPUT_LAYOUT_RECORD_DATA_ID.getDescriptiveName(),
				OUTPUT_LAYOUT_RECORD_DATA_ID.canHaveNullValueObject(this.isForReproducing()), this,
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
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_WIDTH.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_HEIGHT.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(OUTPUT_LAYOUT_RECORD_DATA_ID.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T graphNode2DLayoutOperationBaseBuilder = (T)value;
				
				this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_WIDTH.getName().getStringValue()).setValue(
						graphNode2DLayoutOperationBaseBuilder.getDrawingAreaWidth(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(DRAWING_AREA_HEIGHT.getName().getStringValue()).setValue(
						graphNode2DLayoutOperationBaseBuilder.getDrawingAreaHeight(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(OUTPUT_LAYOUT_RECORD_DATA_ID.getName().getStringValue()).setValue(
						graphNode2DLayoutOperationBaseBuilder.getOutputLayoutRecordDataID().getName(), isEmpty);
			}
		}
		return changed;
	}
	
	@Override
	protected abstract T build();
}
