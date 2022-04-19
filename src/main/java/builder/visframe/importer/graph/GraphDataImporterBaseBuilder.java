package builder.visframe.importer.graph;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.AbstractDataImporterBuilder;
import builder.visframe.metadata.graph.type.GraphTypeEnforcerBuilder;
import context.project.VisProjectDBContext;
import importer.graph.GraphDataImporterBase;
import metadata.DataType;

public abstract class GraphDataImporterBaseBuilder<T extends GraphDataImporterBase> extends AbstractDataImporterBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param hostVisProjectDBContext
	 */
	protected GraphDataImporterBaseBuilder(
			String name, String description, VisProjectDBContext hostVisProjectDBContext) {
		super(name, description, hostVisProjectDBContext, DataType.GRAPH, false);
		// TODO Auto-generated constructor stub
	}
	
	/////////////////////////////////////////////
	protected static final String graphTypeEnforcer = "graphTypeEnforcer";
	protected static final String graphTypeEnforcer_description = "graphTypeEnforcer";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		//GraphTypeEnforcer graphTypeEnforcer
		this.addChildNodeBuilder(new GraphTypeEnforcerBuilder(
				graphTypeEnforcer, graphTypeEnforcer_description, false, this));
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
			this.getChildrenNodeBuilderNameMap().get(graphTypeEnforcer).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T graphDataImporterBase = (T)value;
				this.getChildrenNodeBuilderNameMap().get(graphTypeEnforcer).setValue(graphDataImporterBase.getGraphTypeEnforcer(), isEmpty);
			}
		}
		
		return changed;
	}
	
	@Override
	protected abstract T build();
	
}
