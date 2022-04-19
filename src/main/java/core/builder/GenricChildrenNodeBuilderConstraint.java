package core.builder;

import java.util.Set;
import java.util.function.Predicate;


/**
 * class for a constraint involving one or more children {@link NodeBuilder}s that should be checked when the parent {@link NodeBuilder} is to built a valid value of target property;
 * 
 * note that this is a stronger relationship than the dependency relationship, thus it is allowed that some of the involved {@link NodeBuilder}s are dependent on others;
 * 
 * @author tanxu
 *
 * @param <T>
 */
public class GenricChildrenNodeBuilderConstraint<T> {
	///////////////
	private final NonLeafNodeBuilder<T> parent;
	private final String description; //description of the constraints;
	private final Set<String> involvedChildrenNodeNameSet;
	private final Predicate<NonLeafNodeBuilder<T>> predicate;
	
	/**
	 * constructor
	 * @param parent the parent node builder of the validated child node builder(s); cannot be null
	 * @param description 
	 * @param involvedChildrenNodeNameSet cannot be null; size must be equal or larger than 2;
	 * @param predicate cannot be null
	 */
	public GenricChildrenNodeBuilderConstraint(
			NonLeafNodeBuilder<T> parent, String description, 
			Set<String> involvedChildrenNodeNameSet, Predicate<NonLeafNodeBuilder<T>> predicate){
		//validations TODO
		
		if(involvedChildrenNodeNameSet==null||involvedChildrenNodeNameSet.isEmpty()) {
			throw new IllegalArgumentException("given involvedSiblingNodeNameSet cannot be null or empty!");
		}
		
		
		////
		this.parent = parent;
		this.description = description;
		this.involvedChildrenNodeNameSet = involvedChildrenNodeNameSet;
		this.predicate = predicate;
	}
	
	
	boolean validate() {
		return this.predicate.test(this.parent);
	}
	
	/**
	 * @return the parent
	 */
	public NonLeafNodeBuilder<T> getParent() {
		return parent;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @return the involvedSiblingNodeNameSet
	 */
	public Set<String> getInvolvedChildrenNodeNameSet() {
		return involvedChildrenNodeNameSet;
	}


	/**
	 * @return the predicate
	 */
	public Predicate<NonLeafNodeBuilder<T>> getPredicate() {
		return predicate;
	}

}
