package core.builder.ui.embedded.content;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.rdb.sqltype.SQLDataTypeBuilderEmbeddedUIContentController;
import core.builder.LeafNodeBuilder;
import core.builder.NodeBuilder;

/**
 * {@link EmbeddedUIContentControllerBase} for {@link LeafNodeBuilder};
 * 
 * 
 * @author tanxu
 *
 * @param <T>
 */
public abstract class LeafNodeBuilderEmbeddedUIContentController<T> extends EmbeddedUIContentControllerBase<T>{
	
	/**
	 * set up the owner {@link NodeBuilder} of this {@link EmbeddedUIContentController}
	 * 
	 * then invoke {@link #setupLogicToCheckEffectiveUIInput()} method;
	 * 
	 * note that this is only applicable for {@link LeafNodeBuilderEmbeddedUIContentController};
	 * on the contrary, for {@link NonLeafNodeBuilderEmbeddedUIContentController}, the logic to detect and save such changes are implemented alternatively in {@link NonLeafNodeBuilderIntegrativeUIController} when the FINISH button is clicked
	 * 
	 * @param ownerNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setOwnerNodeBuilder(NodeBuilder<T,?> ownerNodeBuilder) throws IOException, SQLException {
		super.setOwnerNodeBuilder(ownerNodeBuilder);
		this.setupLogicToCheckEffectiveUIInput();
	}
	
	/**
	 * set up logic to check if any changes on the content UI has resulted in a valid value of the target property of type T, 
	 * if yes, the change is considered effective and invoke the owner  {@link NodeBuilder#setValue(Object, boolean)} with the valid value of type T to save it to the current {@link Status} of the {@link LeafNodeBuilder};
	 * 
	 * should be implemented by sub-type class if it has {@link Node} that can take input and result in effective changes;
	 * 
	 * this is how to realize maintain a real time updated value for {@link LeafNodeBuilder};
	 * 
	 * on the contrary, for {@link NonLeafNodeBuilderEmbeddedUIContentController}, the logic to detect and save such changes are implemented alternatively in {@link NonLeafNodeBuilderIntegrativeUIController} when the FINISH button is clicked
	 * 
	 * ====================
	 * implementation strategy
	 * 		when a change is made such that it is possible a valid non-null value could be fully built from the input data on the content UI, invoke the {@link #build()} method with try-catch clause;
	 * 		if there is no exception throw, it means a valid non-null value is successfully built, then invoke the {@link LeafNodeBuilder#updateNonNullValueFromContentController(Object)} of owner {@link NodeBuilder} with the built value;
	 * 		else, there are some missing/invalid data on the content UI and a non-null value cannot be built yet, do not invoke the {@link LeafNodeBuilder#updateNonNullValueFromContentController(Object)} of owner {@link NodeBuilder} with the built value;
	 * 
	 * see {@link SQLDataTypeBuilderEmbeddedUIContentController#setSqlDataTypeChoiceBox2} and setStringAutoPaddingCheckBox2() methods for example!
	 * @throws SQLException 
	 */
	protected abstract void setupLogicToCheckEffectiveUIInput() throws SQLException;
	
	
	@Override
	public abstract LeafNodeBuilder<T,?> getOwnerNodeBuilder();
	
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.getRootParentNode().setMouseTransparent(!modifiable);
	}
	
	/**
	 * try to build an instance of the target property of type T with the current input data on the content UI;
	 * 
	 * if there are missing data or invalid data to build a valid non null value of the target property of type T, there will be Exception thrown;
	 * 
	 * for the method that invokes this method, they should use try-catch clause to check if a valid non null value is successfully built or not!!!!!
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public abstract T build() throws IOException, SQLException;
	
	/**
	 * simply set the status of all UI component to the default empty status;
	 * NOT responsible for setting any descendant node to the default empty status
	 * 
	 * also should invoke owner builder's {@link LeafNodeBuilder#setUIVisualEffect(boolean)} with parameter = false if the default empty status is a invalid value of the target property;
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public abstract void setUIToDefaultEmptyStatus() throws SQLException, IOException;
	
	/**
	 * set the UI to the given valid non-null value;
	 * 
	 * also should invoke owner builder's {@link LeafNodeBuilder#setUIVisualEffect(boolean)} with parameter = true
	 * 
	 * @param value
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public abstract void setUIToNonNullValue(T value) throws SQLException, IOException;
}
