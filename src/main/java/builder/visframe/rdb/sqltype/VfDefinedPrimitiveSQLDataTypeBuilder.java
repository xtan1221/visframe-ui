package builder.visframe.rdb.sqltype;

import java.util.function.Predicate;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public class VfDefinedPrimitiveSQLDataTypeBuilder extends LeafNodeBuilder<VfDefinedPrimitiveSQLDataType, SQLDataTypeBuilderEmbeddedUIContentController> {
	private Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfDefinedPrimitiveSQLDataTypeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		super(name, description, canBeNull, parentNodeBuilder, SQLDataTypeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.dataTypeConstraints = dataTypeConstraints;
	}
	
	
	/**
	 * @return the dataTypeConstraints
	 */
	protected Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}
	
	/**
	 * @param dataTypeConstraints the dataTypeConstraints to set
	 */
	public void setDataTypeConstraints(Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		this.dataTypeConstraints = dataTypeConstraints;
		this.getEmbeddedUIContentController().setDataTypeConstraints(dataTypeConstraints);
	}
}
