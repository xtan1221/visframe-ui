package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;

import basic.VfNotes;
import basic.lookup.project.type.udt.VisProjectMetadataManager;
import builder.basic.misc.SimpleTypeSelector;
import function.group.CompositionFunctionGroupName;
import function.group.ShapeCFG;
import graphics.shape.VfShapeType;
import graphics.shape.VfShapeTypeLookup;
import metadata.MetadataID;


public final class ShapeCFGBuilder extends GraphicsPropertyCFGBuilder<ShapeCFG>{
	public static final String NODE_NAME = "ShapeCFG";
	public static final String NODE_DESCRIPTION = "ShapeCFG";
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectMetadataManager
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public ShapeCFGBuilder(VisProjectMetadataManager hostVisProjectMetadataManager) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectMetadataManager);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	///////////////////////////////
	
	protected static final String shapeType = "shapeType";
	protected static final String shapeType_description = "shapeType";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//shapeType
		SimpleTypeSelector<VfShapeType> shapeTypeSelector  = new SimpleTypeSelector<>(
				shapeType, shapeType_description, false, this,
				//
				e->{return e.getName().getStringValue();}, e->{return e.getName().getStringValue();}
				);
		
		shapeTypeSelector.setPool(VfShapeTypeLookup.getAllVfShapeTypeNameMap().values());
		this.addChildNodeBuilder(shapeTypeSelector);
		//
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(shapeType).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				ShapeCFG shapeCFG = (ShapeCFG)value;
				this.getChildrenNodeBuilderNameMap().get(shapeType).setValue(shapeCFG.getShapeType(), isEmpty);
				
			}
		}
		
		return changed;
	}

	
	@Override
	protected ShapeCFG build() {
		CompositionFunctionGroupName nameValue = (CompositionFunctionGroupName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		MetadataID ownerRecordDataMetadataIDValue = (MetadataID) this.getChildrenNodeBuilderNameMap().get(ownerRecordDataMetadataID).getCurrentValue();
		VfShapeType shapeTypeValue = (VfShapeType) this.getChildrenNodeBuilderNameMap().get(shapeType).getCurrentValue();
		
		return new ShapeCFG(nameValue, notesValue, ownerRecordDataMetadataIDValue, shapeTypeValue);
	}
	
	
	
}
