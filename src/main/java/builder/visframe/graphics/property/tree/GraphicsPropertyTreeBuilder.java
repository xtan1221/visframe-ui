package builder.visframe.graphics.property.tree;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.graphics.property.node.GraphicsPropertyNodeSelector;
import core.builder.NonLeafNodeBuilder;
import graphics.property.node.GraphicsPropertyNode;
import graphics.property.tree.GraphicsPropertyTree;

/**
 * 
 * @author tanxu
 *
 */
public final class GraphicsPropertyTreeBuilder extends NonLeafNodeBuilder<GraphicsPropertyTree>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected GraphicsPropertyTreeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	////////////////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String rootNode = "rootNode";
	protected static final String rootNode_description = "rootNode";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		// TODO Auto-generated method stub
		
		//name
		this.addChildNodeBuilder(new VfNameStringBuilder<SimpleName>(
				name, name_description,
				false, this, SimpleName.class
				));
		
		//notes,
		this.addChildNodeBuilder(new VfNotesBuilder(
				notes, notes_description, 
				false, this));
	
		
		//rootNode
		this.addChildNodeBuilder(new GraphicsPropertyNodeSelector(
				rootNode, rootNode_description, false, this));
		
		
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(rootNode).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GraphicsPropertyTree graphicsPropertyTree = (GraphicsPropertyTree)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(graphicsPropertyTree.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(graphicsPropertyTree.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(rootNode).setValue(graphicsPropertyTree.getRootNode(), isEmpty);
				
			}
		}
		return changed;
	}
	
	@Override
	protected GraphicsPropertyTree build() {
		SimpleName nameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		GraphicsPropertyNode rootNodeValue = (GraphicsPropertyNode) this.getChildrenNodeBuilderNameMap().get(rootNode).getCurrentValue();
		
		return new GraphicsPropertyTree(nameValue, rootNodeValue, notesValue);
	}

}
