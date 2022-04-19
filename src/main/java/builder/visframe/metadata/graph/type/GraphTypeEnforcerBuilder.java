package builder.visframe.metadata.graph.type;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import builder.basic.misc.SimpleTypeSelector;
import builder.basic.primitive.BooleanTypeBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import metadata.graph.type.GraphTypeEnforcer;
import metadata.graph.type.GraphTypeEnforcer.DirectedEnforcingMode;

public final class GraphTypeEnforcerBuilder extends NonLeafNodeBuilder<GraphTypeEnforcer> {
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public GraphTypeEnforcerBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(
//				GraphTypeEnforcer.class, 
				name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////////////////////
	protected static final String toForceDirected = "toForceDirected";
	protected static final String toForceDirected_description = "toForceDirected";
	
	protected static final String directedForcingMode = "directedForcingMode";
	protected static final String directedForcingMode_description = "directedForcingMode";
	
	protected static final String toForceUndirected = "toForceUndirected";
	protected static final String toForceUndirected_description = "toForceUndirected";
	
	protected static final String toForceNoParallelEdges = "toForceNoParallelEdges";
	protected static final String toForceNoParallelEdges_description = "toForceNoParallelEdges";
	
	protected static final String toForceNoSelfLoops = "toForceNoSelfLoops";
	protected static final String toForceNoSelfLoops_description = "toForceNoSelfLoops";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//boolean toForceDirected
		this.addChildNodeBuilder(new BooleanTypeBuilder(toForceDirected, toForceDirected_description, false, this));
		
		//DirectedEnforcingMode directedForcingMode
		Collection<DirectedEnforcingMode> directedForcingModeTypes = new ArrayList<>(); 
		for(DirectedEnforcingMode value:DirectedEnforcingMode.values()) {
			directedForcingModeTypes.add(value);
		}
		Function<DirectedEnforcingMode,String> typeToStringRepresentationFunction = e->{return e.toString();};
		Function<DirectedEnforcingMode,String> typeToDescriptionFunction = e->{return e.getDescription();};
		
		SimpleTypeSelector<DirectedEnforcingMode> directedForcingModeSelector = new SimpleTypeSelector<>(
				directedForcingMode, directedForcingMode_description, true, this, //can be null
				typeToStringRepresentationFunction, typeToDescriptionFunction
				);
		directedForcingModeSelector.setPool(directedForcingModeTypes);
		this.addChildNodeBuilder(directedForcingModeSelector);
		
		//boolean toForceUndirected
		this.addChildNodeBuilder( new BooleanTypeBuilder(toForceUndirected, toForceUndirected_description, false, this));
		
		//boolean toForceNoParallelEdges
		this.addChildNodeBuilder(new BooleanTypeBuilder(toForceNoParallelEdges, toForceNoParallelEdges_description, false, this));

		//boolean toForceNoSelfLoops
		this.addChildNodeBuilder(new BooleanTypeBuilder(toForceNoSelfLoops, toForceNoSelfLoops_description, false, this));
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
//		NonLeafNodeBuilder<T> parent, String description, 
//		Set<String> involvedSiblingNodeNameSet, Predicate<NonLeafNodeBuilder<T>> predicate
		Set<String> involvedSiblingNodeNameSet1 = new HashSet<>();
		involvedSiblingNodeNameSet1.add(toForceDirected);
		involvedSiblingNodeNameSet1.add(toForceUndirected);
		GenricChildrenNodeBuilderConstraint<GraphTypeEnforcer> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "toForceDirectedBuilder and toForceUndirectedBuilder cannot both be true or false!",
				involvedSiblingNodeNameSet1, 
				e->{
					boolean toForceDirected = (boolean) e.getChildrenNodeBuilderNameMap().get(GraphTypeEnforcerBuilder.toForceDirected).getCurrentValue();
					boolean toForceUndirected = (boolean) e.getChildrenNodeBuilderNameMap().get(GraphTypeEnforcerBuilder.toForceUndirected).getCurrentValue();
					
					return !toForceDirected&&toForceUndirected || toForceDirected&&!toForceUndirected;
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		
		////when toForceDirected is changed
		Runnable toForceDirectedBuilderChangeEventAction = ()->{

			try {
				if(this.getChildrenNodeBuilderNameMap().get(toForceDirected).isDefaultEmpty()) {
					
					this.getChildrenNodeBuilderNameMap().get(directedForcingMode).setToDefaultEmpty();
					
				}else {
					boolean toForceDirectedValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toForceDirected).getCurrentValue();
					
					if(toForceDirectedValue) {
						
						this.getChildrenNodeBuilderNameMap().get(directedForcingMode).setToNonNull();
						
					}else {
						this.getChildrenNodeBuilderNameMap().get(directedForcingMode).setToNull();
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		};
		
//		this.getChildrenNodeBuilderNameMap().get(toForceDirected).addDependentSilbingNodeBuilderName(directedForcingMode);
		
		this.getChildrenNodeBuilderNameMap().get(toForceDirected).addStatusChangedAction(
				toForceDirectedBuilderChangeEventAction);
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(toForceDirected).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(directedForcingMode).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toForceUndirected).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toForceNoParallelEdges).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toForceNoSelfLoops).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GraphTypeEnforcer graphTypeEnforcer = (GraphTypeEnforcer)value;
				this.getChildrenNodeBuilderNameMap().get(toForceDirected).setValue(graphTypeEnforcer.isToForceDirected(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(directedForcingMode).setValue(graphTypeEnforcer.getDirectedForcingMode(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toForceUndirected).setValue(graphTypeEnforcer.isToForceUndirected(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toForceNoParallelEdges).setValue(graphTypeEnforcer.isToForceNoParallelEdges(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toForceNoSelfLoops).setValue(graphTypeEnforcer.isToForceNoSelfLoops(), isEmpty);
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected GraphTypeEnforcer build() {
		boolean toForceDirectedValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toForceDirected).getCurrentValue();
		DirectedEnforcingMode directedForcingModeValue = (DirectedEnforcingMode) this.getChildrenNodeBuilderNameMap().get(directedForcingMode).getCurrentValue();
		boolean toForceUndirectedValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toForceUndirected).getCurrentValue();
		boolean toForceNoParallelEdgesValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toForceNoParallelEdges).getCurrentValue();
		boolean toForceNoSelfLoopsValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toForceNoSelfLoops).getCurrentValue();
		
		return new GraphTypeEnforcer(toForceDirectedValue, directedForcingModeValue, toForceUndirectedValue, toForceNoParallelEdgesValue, toForceNoSelfLoopsValue);
		
	}


	
	
	///////////////////////////////////////////////




}
