package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import basic.SimpleName;
import basic.VfNotes;
import basic.lookup.project.type.udt.VisProjectMetadataManager;
import builder.basic.collection.set.nonleaf.HashSetNonLeafNodeBuilder;
import builder.visframe.graphics.property.tree.GraphicsPropertyTreeBuilderFactory;
import core.builder.GenricChildrenNodeBuilderConstraint;
import function.group.CompositionFunctionGroupName;
import function.group.IndependentGraphicsPropertyCFG;
import graphics.property.tree.GraphicsPropertyTree;
import metadata.MetadataID;


public class IndependentGraphicsPropertyCFGBuilder  extends GraphicsPropertyCFGBuilder<IndependentGraphicsPropertyCFG>{
	public static final String NODE_NAME = "IndependentGraphicsPropertyCFG";
	public static final String NODE_DESCRIPTION = "IndependentGraphicsPropertyCFG";
	
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
	public IndependentGraphicsPropertyCFGBuilder(VisProjectMetadataManager hostVisProjectMetadataManager) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectMetadataManager);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	///////////////////////////////
	
	protected static final String targetGraphicsPropertyTreeSet = "targetGraphicsPropertyTreeSet";
	protected static final String targetGraphicsPropertyTreeSet_description = "targetGraphicsPropertyTreeSet";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//targetGraphicsPropertyTreeSet
		GraphicsPropertyTreeBuilderFactory targetGraphicsPropertyTreeBuilderFactory = 
				new GraphicsPropertyTreeBuilderFactory("targetGraphicsPropertyTree", "targetGraphicsPropertyTree", false);
		this.addChildNodeBuilder(new HashSetNonLeafNodeBuilder<GraphicsPropertyTree>(
				targetGraphicsPropertyTreeSet, targetGraphicsPropertyTreeSet_description, false, this,
				
				targetGraphicsPropertyTreeBuilderFactory
				));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//name of trees in targetGraphicsPropertyTreeSet must be unique;
		Set<String> set1 = new HashSet<>();
		set1.add(targetGraphicsPropertyTreeSet);
		
		GenricChildrenNodeBuilderConstraint<IndependentGraphicsPropertyCFG> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "Name of trees in targetGraphicsPropertyTreeSet must be unique!",
				set1,
				e->{
					
					@SuppressWarnings("unchecked")
					HashSetNonLeafNodeBuilder<GraphicsPropertyTree> builder = (HashSetNonLeafNodeBuilder<GraphicsPropertyTree>) e.getChildrenNodeBuilderNameMap().get(targetGraphicsPropertyTreeSet);
					//
					
					Set<SimpleName> treeNameSet = new HashSet<>();
					
					for(GraphicsPropertyTree tree:builder.getCurrentValue()) {
						if(treeNameSet.contains(tree.getName())) {
							return false;
						}
						treeNameSet.add(tree.getName());
					}
					
					return true;
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
	
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
			this.getChildrenNodeBuilderNameMap().get(targetGraphicsPropertyTreeSet).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				IndependentGraphicsPropertyCFG independentGraphicsPropertyCFG = (IndependentGraphicsPropertyCFG)value;
				this.getChildrenNodeBuilderNameMap().get(targetGraphicsPropertyTreeSet).setValue(new HashSet<>(independentGraphicsPropertyCFG.getTargetGraphicsPropertyTreeNameMap().values()), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected IndependentGraphicsPropertyCFG build() {
		CompositionFunctionGroupName nameValue = (CompositionFunctionGroupName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		MetadataID ownerRecordDataMetadataIDValue = (MetadataID) this.getChildrenNodeBuilderNameMap().get(ownerRecordDataMetadataID).getCurrentValue();
		@SuppressWarnings("unchecked")
		Set<GraphicsPropertyTree> targetGraphicsPropertyTreeSetValue = (Set<GraphicsPropertyTree>) this.getChildrenNodeBuilderNameMap().get(targetGraphicsPropertyTreeSet).getCurrentValue();
		
		return new IndependentGraphicsPropertyCFG(nameValue, notesValue, ownerRecordDataMetadataIDValue, targetGraphicsPropertyTreeSetValue);
	}
	
	
}