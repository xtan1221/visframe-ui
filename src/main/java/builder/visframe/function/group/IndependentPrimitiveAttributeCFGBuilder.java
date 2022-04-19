package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import basic.SimpleName;
import basic.VfNotes;
import basic.attribute.VfAttribute;
import basic.lookup.project.type.udt.VisProjectMetadataManager;
import builder.basic.collection.set.nonleaf.HashSetNonLeafNodeBuilder;
import builder.visframe.basic.attribute.PrimitiveVfAttributeImplBuilderFactory;
import core.builder.GenricChildrenNodeBuilderConstraint;
import function.group.CompositionFunctionGroupName;
import function.group.IndependentPrimitiveAttributeCFG;
import function.target.IndependentPrimitiveAttributeTarget;
import metadata.MetadataID;


public final class IndependentPrimitiveAttributeCFGBuilder  extends AbstractCompositionFunctionGroupBuilder<IndependentPrimitiveAttributeCFG>{
	public static final String NODE_NAME = "IndependentPrimitiveAttributeCFG";
	public static final String NODE_DESCRIPTION = "IndependentPrimitiveAttributeCFG";
	
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
	public IndependentPrimitiveAttributeCFGBuilder(VisProjectMetadataManager hostVisProjectMetadataManager) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectMetadataManager);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	///////////////////////////////
	
	protected static final String targetIndependentAttributeSet = "targetIndependentAttributeSet";
	protected static final String targetIndependentAttributeSet_description = "targetIndependentAttributeSet";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//targetIndependentAttributeSet
		PrimitiveVfAttributeImplBuilderFactory primitiveVfAttributeImplBuilderFactory = 
				new PrimitiveVfAttributeImplBuilderFactory("targetIndependentAttribute", "targetIndependentAttribute", false);
		this.addChildNodeBuilder(new HashSetNonLeafNodeBuilder<VfAttribute<?>>(
				targetIndependentAttributeSet, targetIndependentAttributeSet_description, false, this,
				primitiveVfAttributeImplBuilderFactory
				));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//name of target attribute in targetIndependentAttributeSet must be unique;
		Set<String> set1 = new HashSet<>();
		set1.add(targetIndependentAttributeSet);
		
		GenricChildrenNodeBuilderConstraint<IndependentPrimitiveAttributeCFG> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "name of target attribute in targetIndependentAttributeSet must be unique!",
				set1,
				e->{
					
					@SuppressWarnings("unchecked")
					HashSetNonLeafNodeBuilder<VfAttribute<?>> builder = 
					(HashSetNonLeafNodeBuilder<VfAttribute<?>>) e.getChildrenNodeBuilderNameMap().get(targetIndependentAttributeSet);
					//
					
					Set<SimpleName> attributeNameSet = new HashSet<>();
					
					for(VfAttribute<?> attribute:builder.getCurrentValue()) {
						if(attributeNameSet.contains(attribute.getName())) {
							return false;
						}
						attributeNameSet.add(attribute.getName());
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
			this.getChildrenNodeBuilderNameMap().get(targetIndependentAttributeSet).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				IndependentPrimitiveAttributeCFG independentPrimitiveAttributeCFG = (IndependentPrimitiveAttributeCFG)value;
				
				Set<VfAttribute<?>> set = new HashSet<>();
				independentPrimitiveAttributeCFG.getTargetNameMap().values().forEach(e->{
					set.add(e.getTargetAttribute());
				});
				this.getChildrenNodeBuilderNameMap().get(targetIndependentAttributeSet).setValue(set, isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected IndependentPrimitiveAttributeCFG build() {
		CompositionFunctionGroupName nameValue = (CompositionFunctionGroupName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		MetadataID ownerRecordDataMetadataIDValue = (MetadataID) this.getChildrenNodeBuilderNameMap().get(ownerRecordDataMetadataID).getCurrentValue();
		
		
		Set<IndependentPrimitiveAttributeTarget<?>> targetIndependentAttributeSetValue = new LinkedHashSet<>();
		@SuppressWarnings("unchecked")
		Set<VfAttribute<?>> vfAttributeSet = (Set<VfAttribute<?>>) this.getChildrenNodeBuilderNameMap().get(targetIndependentAttributeSet).getCurrentValue();
		vfAttributeSet.forEach(e->{
			targetIndependentAttributeSetValue.add(new IndependentPrimitiveAttributeTarget<>(e));
		});
		
		return new IndependentPrimitiveAttributeCFG(nameValue, notesValue, ownerRecordDataMetadataIDValue, targetIndependentAttributeSetValue);
	}
	
	
}