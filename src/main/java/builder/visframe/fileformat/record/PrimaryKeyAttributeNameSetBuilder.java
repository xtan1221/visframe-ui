package builder.visframe.fileformat.record;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.set.nonleaf.HashSetNonLeafNodeBuilder;
import builder.visframe.basic.VfNameStringBuilderFactory;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.RecordDataFileFormat.PrimaryKeyAttributeNameSet;

public class PrimaryKeyAttributeNameSetBuilder extends NonLeafNodeBuilder<PrimaryKeyAttributeNameSet>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public PrimaryKeyAttributeNameSetBuilder(String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	//////////////////////////////////////
	protected static final String simpleMandatoryAttributeNameSet = "simpleMandatoryAttributeNameSet";
	protected static final String simpleMandatoryAttributeNameSet_description = "simpleMandatoryAttributeNameSet";
	
	protected static final String tagAttributeNameSet = "tagAttributeNameSet";
	protected static final String tagAttributeNameSet_description = "tagAttributeNameSet";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//Set<SimpleName> simpleMandatoryAttributeNameSet,
		VfNameStringBuilderFactory<SimpleName> simpleMandatoryAttributeNameBuilderFactory = new VfNameStringBuilderFactory<>("simpleMandatoryAttributeName","simpleMandatoryAttributeName", false, SimpleName.class);
        this.addChildNodeBuilder(
        		new HashSetNonLeafNodeBuilder<>(
        				simpleMandatoryAttributeNameSet, simpleMandatoryAttributeNameSet_description, false, this,
        				simpleMandatoryAttributeNameBuilderFactory
        		));
		
		//Set<SimpleName> tagAttributeNameSet
        VfNameStringBuilderFactory<SimpleName> tagAttributeNameBuilderFactory = new VfNameStringBuilderFactory<>("tagAttributeName","tagAttributeName", false, SimpleName.class);
        this.addChildNodeBuilder(
        		new HashSetNonLeafNodeBuilder<>(
        				tagAttributeNameSet, tagAttributeNameSet_description, false, this,
        				tagAttributeNameBuilderFactory
        		));
        
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//simpleMandatoryAttributeNameSet and tagAttributeNameSet cannot both be empty
		Set<String> set1 = new HashSet<>();
		set1.add(simpleMandatoryAttributeNameSet);
		set1.add(tagAttributeNameSet);
		@SuppressWarnings("unchecked")
		GenricChildrenNodeBuilderConstraint<PrimaryKeyAttributeNameSet> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "simpleMandatoryAttributeNameSet and tagAttributeNameSet cannot both be empty!",
				set1, 
				e->{
					Set<SimpleName> simpleMandatoryAttributeNameSet = (Set<SimpleName>) e.getChildrenNodeBuilderNameMap().get(PrimaryKeyAttributeNameSetBuilder.simpleMandatoryAttributeNameSet).getCurrentValue();
					Set<SimpleName> tagAttributeNameSet = (Set<SimpleName>) e.getChildrenNodeBuilderNameMap().get(PrimaryKeyAttributeNameSetBuilder.tagAttributeNameSet).getCurrentValue();
					
					if(simpleMandatoryAttributeNameSet.isEmpty() && tagAttributeNameSet.isEmpty())
						return false;
					else
						return true;
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
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
			this.getChildrenNodeBuilderNameMap().get(simpleMandatoryAttributeNameSet).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(tagAttributeNameSet).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				PrimaryKeyAttributeNameSet primaryKeyAttributeNameSet = (PrimaryKeyAttributeNameSet)value;
				this.getChildrenNodeBuilderNameMap().get(simpleMandatoryAttributeNameSet).setValue(primaryKeyAttributeNameSet.getSimpleMandatoryAttributeNameSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(tagAttributeNameSet).setValue(primaryKeyAttributeNameSet.getTagAttributeNameSet(), isEmpty);
			}
		}
		return changed;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected PrimaryKeyAttributeNameSet build() {
		Set<SimpleName> simpleMandatoryAttributeNameSetValue = (HashSet<SimpleName>) this.getChildrenNodeBuilderNameMap().get(simpleMandatoryAttributeNameSet).getCurrentValue(); 
		Set<SimpleName> tagAttributeNameSetValue = (HashSet<SimpleName>) this.getChildrenNodeBuilderNameMap().get(tagAttributeNameSet).getCurrentValue(); 
		
		return new PrimaryKeyAttributeNameSet(simpleMandatoryAttributeNameSetValue, tagAttributeNameSetValue);
	}
	
	
	
	
}
