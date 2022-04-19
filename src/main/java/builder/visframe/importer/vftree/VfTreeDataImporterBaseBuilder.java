package builder.visframe.importer.vftree;

import java.io.IOException;
import java.sql.SQLException;

import builder.basic.primitive.IntegerTypeBuilder;
import builder.visframe.importer.AbstractDataImporterBuilder;
import context.project.VisProjectDBContext;
import importer.vftree.VfTreeDataImporterBase;
import metadata.DataType;

public abstract class VfTreeDataImporterBaseBuilder<T extends VfTreeDataImporterBase> extends AbstractDataImporterBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param hostVisProjectDBContext
	 */
	protected VfTreeDataImporterBaseBuilder(
			String name, String description,
			VisProjectDBContext hostVisProjectDBContext) {
		super(name, description, hostVisProjectDBContext, DataType.vfTREE, false);
		// TODO Auto-generated constructor stub
	}
	
	/////////////////////////////////////////////
	protected static final String bootstrapIteration = "bootstrapIteration";
	protected static final String bootstrapIteration_description = "bootstrapIteration";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//Integer bootstrapIteration
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				bootstrapIteration, bootstrapIteration_description, true, this, 
				e->{return e>0;}, "bootstrap value must be positive integer!"));
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
			this.getChildrenNodeBuilderNameMap().get(bootstrapIteration).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T vfTreeDataImporterBase = (T)value;
				this.getChildrenNodeBuilderNameMap().get(bootstrapIteration).setValue(vfTreeDataImporterBase.getBootstrapIteration(), isEmpty);
			}
		}
		
		return changed;
	}
	
	@Override
	protected abstract T build();
}
