package core.builder;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import utils.AlertUtils;


/**
 * leaf type {@link NodeBuilder} without any children {@link NodeBuilder};
 * 
 * the EmbeddedUIContentController is of {@link LeafNodeBuilderEmbeddedUIContentController} type;
 * 
 * 
 * @author tanxu
 *
 * @param <T>
 * @param <E>
 */
public abstract class LeafNodeBuilder<T, E extends LeafNodeBuilderEmbeddedUIContentController<T>> extends NodeBuilderBase<T, E> {
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param embeddedUIContentFXMLFileDirString
	 */
	protected LeafNodeBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, String embeddedUIContentFXMLFileDirString) {
		super(name, description, canBeNull, parentNodeBuilder, embeddedUIContentFXMLFileDirString);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * directly set the value;
	 * 
	 * this will invoke the {@link LeafNodeBuilderEmbeddedUIContentController#setUIToNonNullValue(Object)} method if the given value is non null;
	 * 
	 * thus should NOT be invoked from {@link LeafNodeBuilderEmbeddedUIContentController} to update valid non null value when an effective input on content UI is made ;
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		
		boolean changed = super.setValue(value, isEmpty);
		if(isEmpty) {
			this.getEmbeddedUIContentController().setUIToDefaultEmptyStatus();
		}else {
			if(value!=null) {
				this.getEmbeddedUIContentController().setUIToNonNullValue((T)value);
			}else {
				this.setToNull();
			}
		}
		
		return changed;
	}
	
	
//	/**
//	 * update the given current non-null value to the current {@link Status} directly from the content UI controller;
//	 * 
//	 * 
//	 * @param value
//	 */
//	public void updateNonNullValueFromContentController(T value) {
//		
//		
//		
//		super.setValue(value, false);
//		
//	}
	
	/**
	 * try to build value based on currently UI data and update it to {@link Status}
	 * 
	 * if any exception is thrown, the update will be halted;
	 * 
	 * also set the UI visual effect accordingly by invoking {@link #setUIVisualEffect(boolean)}
	 * 
	 * ===============================
	 * this is different from the {@link LeafNodeBuilder#setValue(Object, boolean)} method!!!!!!!!!!!
	 * this method does not invoke the {@link LeafNodeBuilderEmbeddedUIContentController#setUIToNonNullValue(Object)} method since it takes the value from it!!!;
	 * 		otherwise, circular invocation may be resulted;
	 * 
	 * specifically, most invocation, if not all, should be from from {@link LeafNodeBuilderEmbeddedUIContentController#setupLogicToCheckEffectiveUIInput()} method when an
	 * effective change on the content UI is detected that results in a valid value of the target property!
	 * @param popupCaughtException whether to pop up a window to show the thrown exception's message or not
	 */
	public void updateNonNullValueFromContentController(boolean popupCaughtException) {
		try{
			T value = this.getEmbeddedUIContentController().build();
			//update to the status
			super.setValue(value, false);
			
			this.setUIVisualEffect(true);
			
		}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
			//skip
			
			if(popupCaughtException) {
				AlertUtils.popAlert("Error found", ex.getMessage());
			}
//			ex.printStackTrace();
			System.out.println("exception thrown, skip update:"+ex.getClass().getSimpleName()+";"+ex.getMessage());
			
			this.setUIVisualEffect(false);
		}
	}



	/**
	 * first invoke super.setToDefaultEmpty() and if it returns true, invoke 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public boolean setToDefaultEmpty() throws SQLException, IOException {
		boolean changed = super.setToDefaultEmpty();
		
//		if(changed) {
//			this.getEmbeddedUIRootContainerNodeController().setUIToDefaultEmptyStatus();
			this.getEmbeddedUIContentController().setUIToDefaultEmptyStatus();
//		}
		
		return changed;
	}
	
	/////////////////////////////////////////

	
}
