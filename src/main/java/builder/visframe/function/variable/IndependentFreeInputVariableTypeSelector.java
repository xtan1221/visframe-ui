package builder.visframe.function.variable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import builder.visframe.function.variable.input.nonrecordwise.FreeInputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.independent.IndependentFreeInputVariableType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

/**
 * selector of an {@link IndependentFreeInputVariableType} from a host VisProjectDBContext belonging to a pre-existing {@link CompositionFunction};
 * @author tanxu
 *
 */
public class IndependentFreeInputVariableTypeSelector extends LeafNodeBuilder<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeSelectorEmbeddedUIContentController>{
	private final FreeInputVariableBuilder hostFreeInputVariableBuilder;
	
	
	/**
	 * viewer for details of selected IndependentFreeInputVariableType;
	 */
	private IndependentFreeInputVariableTypeBuilder viewerForSelectedType;
	
	private Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	
	private final Predicate<IndependentFreeInputVariableType> filteringCondition;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public IndependentFreeInputVariableTypeSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			FreeInputVariableBuilder hostFreeInputVariableBuilder,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			Predicate<IndependentFreeInputVariableType> filteringCondition) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, IndependentFreeInputVariableTypeSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.hostFreeInputVariableBuilder = hostFreeInputVariableBuilder;
		this.dataTypeConstraints = dataTypeConstraints;
		this.filteringCondition = filteringCondition;
		///
		viewerForSelectedType = new IndependentFreeInputVariableTypeBuilder("","",false,this.getParentNodeBuilder(), null, this.getDataTypeConstraints());//???
		viewerForSelectedType.setModifiable(false);
	}
	
	/**
	 * @return the hostFreeInputVariableBuilder
	 */
	public FreeInputVariableBuilder getHostFreeInputVariableBuilder() {
		return hostFreeInputVariableBuilder;
	}
	
	public VisProjectDBContext getHostVisProjectDBContext() {
		return this.getHostFreeInputVariableBuilder().getHostVisProjectDBContext();
	}
	/**
	 * @return the dataTypeConstraints
	 */
	protected Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}

	public Predicate<IndependentFreeInputVariableType> getFilteringCondition() {
		return filteringCondition;
	}
	
//	/**
//	 * @param dataTypeConstraints the dataTypeConstraints to set
//	 */
//	public void setDataTypeConstraints(Predicate<SQLDataType> dataTypeConstraints) {
//		this.dataTypeConstraints = dataTypeConstraints;
//	}
	
	///////////////////////
//	/**
//	 * @return the viewerForSelectedType
//	 */
//	public IndependentFreeInputVariableTypeBuilder getViewerForSelectedType() {
//		return viewerForSelectedType;
//	}
	
	
//	/**
//	 * set the selection pool for types of the host CompositionFunctionBuilder;
//	 * @param hostCompositionFunctionTypes
//	 */
//	public void setHostCompositionFunctionBuilderTypesPool(Set<IndependentFreeInputVariableType> hostCompositionFunctionTypes) {
//		this.getEmbeddedUIContentController().setHostCompositionFunctionBuilderTypesPool(hostCompositionFunctionTypes);
//	}
}
