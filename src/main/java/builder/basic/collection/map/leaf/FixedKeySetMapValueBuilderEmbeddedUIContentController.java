package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import core.builder.NodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FixedKeySetMapValueBuilderEmbeddedUIContentController<K,V> extends LeafNodeBuilderEmbeddedUIContentController<Map<K,V>> {
	public static final String FXML_FILE_DIR = "/builder/basic/collection/map/leaf/FixedKeySetMapValueBuilderEmbeddedUIContent.fxml";
	
	private Map<K,MapEntryHBox> mapKeyEntryHBoxMap;
	
	/**
	 * @return the mapKeyEntryHBoxMap
	 */
	public Map<K, MapEntryHBox> getMapKeyEntryHBoxMap() {
		return mapKeyEntryHBoxMap;
	}
	
	/**
	 * return the current set of NodeBuilders for map values;
	 * @return
	 */
	Set<NodeBuilder<V,?>> getValueNodeBuilderSet(){
		Set<NodeBuilder<V,?>> ret = new LinkedHashSet<>();
		
		mapKeyEntryHBoxMap.values().forEach(v->{
			if(v.valueBuilder!=null)
				ret.add(v.valueBuilder);
		});
		
		return ret;
	}
	
	///////////////////////////////////
	void setMapKeySet(Set<K> keySet) throws SQLException, IOException {
		this.mapKeyEntryHBoxMap.clear();
		this.entryVBox.getChildren().clear();
		
		for(K key:keySet) {
			NodeBuilder<V, ?> mapValueBuilder = this.getOwnerNodeBuilder().getMapValueNodeBuilderFactory().build();
			
			MapEntryHBox entry = new MapEntryHBox(key, mapValueBuilder);
			
			//perform the action to the entry if not null;
			if(this.getOwnerNodeBuilder().getMapValueBuilderAction()!=null)
				this.getOwnerNodeBuilder().getMapValueBuilderAction().accept(key, mapValueBuilder);
			
			
			mapKeyEntryHBoxMap.put(key, entry);
			
			this.entryVBox.getChildren().add(entry.containerHBox);
			
			//whenever the map value node builder status is changed, try to update the status of owner node builder;
			Runnable runnable = ()->{
				FixedKeySetMapValueBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			};
			
			mapValueBuilder.addStatusChangedAction(runnable);
		}
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public FixedKeySetMapValueBuilder<K,V> getOwnerNodeBuilder() {
		return (FixedKeySetMapValueBuilder<K,V>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return contentVBox;
	}
	
	
	@Override
	public Map<K,V> build() {
		Map<K,V> ret = new LinkedHashMap<>();
		
		for(K key:this.mapKeyEntryHBoxMap.keySet()) {
			V value = this.mapKeyEntryHBoxMap.get(key).valueBuilder.getCurrentValue();
			//
			if(value==null) {
				if(!this.getOwnerNodeBuilder().isAllowingNullValue()) {
					throw new VisframeException("at least one map entry's value is null!");
				}
			}
			
			//
			if(ret.values().contains(value) && !this.getOwnerNodeBuilder().isAllowingDuplicateValue())
				throw new VisframeException("duplcate map value is found!");
			
			ret.put(key, value);
		}
		
		return ret;
	}
	
	
	/**
	 * this will set each map value to default empty without change the map key set;
	 * because map key set is fixed;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
		//
		for(MapEntryHBox entry:this.mapKeyEntryHBoxMap.values()) {
			entry.valueBuilder.setToDefaultEmpty();
		}
		
//		this.getOwnerNodeBuilder().setKeySet(new HashSet<>());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(Map<K,V> value) throws SQLException, IOException {
		//do not need to clear, simply set the value for each entry with the given ones
//		//clear all
//		this.mapKeyEntryHBoxMap.clear();
//		this.entryVBox.getChildren().clear();
		
//		this.getOwnerNodeBuilder().setKeySet(value.keySet());
		for(K key:value.keySet()) {
//			MapEntryHBox hbox = this.mapKeyEntryHBoxMap.get(key);
			
			this.mapKeyEntryHBoxMap.get(key).valueBuilder.setValue(value.get(key), false);
		}
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
//		this.getRootParentNode().setMouseTransparent(!modifiable);
		
		for(K key:this.mapKeyEntryHBoxMap.keySet()){
			this.mapKeyEntryHBoxMap.get(key).valueBuilder.setModifiable(modifiable);
		}
	}
	
	
	//////////////////////////

	
	@FXML
	public void initialize() {
		this.mapKeyEntryHBoxMap = new LinkedHashMap<>();
	}
	
	@FXML
	private VBox contentVBox;
	@FXML
	private ScrollPane entryScrollPane;
	@FXML
	private VBox entryVBox;
	
	///////////////////////////
	class MapEntryHBox{
		final K key;
		final NodeBuilder<V,?> valueBuilder;
		
		HBox containerHBox;
		
		TextField keyInforTextField;
		
		HBox valueEmbeddedUIContentContainer;
		
		
		MapEntryHBox(K key, NodeBuilder<V,?> valueBuilder){
			this.key = key;
			this.valueBuilder = valueBuilder;
			
			///////
			this.containerHBox = new HBox();
			
			//
			this.keyInforTextField = new TextField(
					FixedKeySetMapValueBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().getMapKeyToStringRepresentationFunction().apply(this.key));
			this.keyInforTextField.setEditable(false);
			
//			this.keyInforTextField.setOnMouseClicked(e->{
//				System.out.println(this.valueEmbeddedUIContentContainer.getChildren().size());
//			});
			
			
			Tooltip tt = new Tooltip(
					FixedKeySetMapValueBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().getMapKeyToDescriptionFunction().apply(this.key));
			Tooltip.install(this.keyInforTextField, tt);
			
			
			this.valueEmbeddedUIContentContainer = new HBox();
			
			//DEBUG TODO cannot add the UI content?
//			this.valueEmbeddedUIContentContainer.getChildren().add(this.valueBuilder.getEmbeddedUIContentController().getRootParentNode());
			this.valueEmbeddedUIContentContainer.getChildren().add(this.valueBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
			
			////
			this.containerHBox.getChildren().add(keyInforTextField);
			this.containerHBox.getChildren().add(this.valueEmbeddedUIContentContainer);
			
		}
		
	}
}
