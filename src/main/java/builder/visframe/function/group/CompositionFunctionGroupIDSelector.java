package builder.visframe.function.group;

import java.util.function.Predicate;

import basic.lookup.project.type.udt.VisProjectCompositionFunctionGroupManager;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;

public class CompositionFunctionGroupIDSelector extends LeafNodeBuilder<CompositionFunctionGroupID, CompositionFunctionGroupIDSelectorEmbeddedUIContentController> {

	private final VisProjectCompositionFunctionGroupManager visProjectCompositionFunctionGroupManager;
	/**
	 * the sql string for filtering condition of selection pool;
	 * if null, no condition;
	 */
	private final String conditionSQLString;
	
	private final Predicate<CompositionFunctionGroup> filteringCondition;
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param visProjectMetadataManager
	 * @param allowedDataTypeSet
	 * @param filteringCondition filtering condition for the queried CompositionFunctionGroup; only those passes this condition will be shown to be selected; can be null;
	 */
	public CompositionFunctionGroupIDSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectCompositionFunctionGroupManager visProjectCompositionFunctionGroupManager,
			String conditionSQLString,
			Predicate<CompositionFunctionGroup> filteringCondition) {
		super(name, description, canBeNull, parentNodeBuilder, CompositionFunctionGroupIDSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.visProjectCompositionFunctionGroupManager = visProjectCompositionFunctionGroupManager;
		this.conditionSQLString = conditionSQLString;
		this.filteringCondition = filteringCondition;
	}
	
	public VisProjectCompositionFunctionGroupManager getVisProjectCompositionFunctionGroupManager() {
		return visProjectCompositionFunctionGroupManager;
	}

	public String getConditionSQLString() {
		return conditionSQLString;
	}

	public Predicate<CompositionFunctionGroup> getFilteringCondition() {
		return filteringCondition;
	}

}
