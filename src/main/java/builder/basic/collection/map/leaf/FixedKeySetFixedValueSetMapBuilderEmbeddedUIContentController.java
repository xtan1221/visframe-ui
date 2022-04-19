package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import utils.LayoutCoordinateAndSizeUtils;
import utils.FXUtils;


/**
 * map value radioButton are disabled by default
 * only enabled
 * 
 * map key radioButton are enabled by default;
 * 
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class FixedKeySetFixedValueSetMapBuilderEmbeddedUIContentController<K,V> extends LeafNodeBuilderEmbeddedUIContentController<Map<K,V>> {
	public static final String FXML_FILE_DIR = "/builder/basic/collection/map/leaf/FixedKeySetFixedValueSetMapBuilderEmbeddedUIContent.fxml";
	
	
	///////////////////////////////////
	private Map<K, Label> mapKeyLabelMap = new LinkedHashMap<>();
	private Map<K, MapKeyRadioButtonManager<K>> mapKeyRadioButtonManagerMap = new LinkedHashMap<>();
	private Map<RadioButton, K> radioButtonKeyMap  = new LinkedHashMap<>();
	
	
	private Map<V, Label> mapValueLabelMap = new LinkedHashMap<>();
	private Map<V, MapValueRadioButtonManager<V>> mapValueRadioButtonManagerMap = new LinkedHashMap<>();
	private Map<RadioButton, V> radioButtonValueMap  = new LinkedHashMap<>();
	
	////
	private Map<K,V> linkedMapKeyValueMap = new LinkedHashMap<>();
	/**
	 * map from a map key to the line that links it with a map value;
	 * note that there can only be one or none value for a key to be linked at a time;
	 */
	private Map<K, Line> mapKeyLinkingLineMap = new LinkedHashMap<>();
	
	private K currentlySelectedUnlinkedMapKey;
	/**
	 * update all UI based on 
	 * 
	 * invoked whenever the map key set is changed;
	 * @param keySet
	 */
	void updateMapKeySet() {
		///clear current data
		this.mapKeyGridPane.getChildren().clear();
		
		this.mapKeyLabelMap.clear();
		this.mapKeyRadioButtonManagerMap.clear();
		this.radioButtonKeyMap.clear();
		
		
		this.anchorPane.getChildren().removeAll(mapKeyLinkingLineMap.values());
		this.linkedMapKeyValueMap.clear();
		this.mapKeyLinkingLineMap.clear();
		
		////reset new data
		this.getOwnerNodeBuilder().getKeySet().forEach(key->{
			this.mapKeyLabelMap.put(key, new Label(this.getOwnerNodeBuilder().getMapKeyToStringRepresentationFunction().apply(key)));
			MapKeyRadioButtonManager<K> manager = new MapKeyRadioButtonManager<>(key);
			this.mapKeyRadioButtonManagerMap.put(key, manager);
			this.radioButtonKeyMap.put(manager.radioButton, key);
		});
		
		//add Labels and RadioButtons to grid pane;
		int rowIndex = 0;
		for(K key:this.getOwnerNodeBuilder().getKeySet()){
			GridPane.setConstraints(this.mapKeyLabelMap.get(key), 0, rowIndex); // column, row
			this.mapKeyGridPane.getChildren().add(this.mapKeyLabelMap.get(key));
			this.mapKeyLabelMap.get(key).setOnMouseClicked(e->{
				System.out.println(this.getClass().getSimpleName()+": X:"+this.mapKeyRadioButtonManagerMap.get(key).getRadioButtonCenterCoord(this.anchorPane).getX());
				System.out.println(this.getClass().getSimpleName()+": Y:"+this.mapKeyRadioButtonManagerMap.get(key).getRadioButtonCenterCoord(this.anchorPane).getY());
			});
			
			
			GridPane.setConstraints(this.mapKeyRadioButtonManagerMap.get(key).radioButton, 1, rowIndex); // column, row
			this.mapKeyGridPane.getChildren().add(this.mapKeyRadioButtonManagerMap.get(key).radioButton);
			
			rowIndex++;
		}
		
		//disable all value radiobutton and set to unselected
		this.mapValueRadioButtonManagerMap.forEach((v,m)->{
			FXUtils.set2Disable(m.radioButton, true);
			m.radioButton.setSelected(false);
		});
		
		//enable all key radiobuttona nd set to unselected
		this.mapKeyRadioButtonManagerMap.forEach((k,m)->{
			FXUtils.set2Disable(m.radioButton, false);
			m.radioButton.setSelected(false);
		});
				
				
				
		//set the event listener for each RadioButton of key;
		mapKeyRadioButtonManagerMap.forEach((key,keyRadioButtonManager)->{			
			//
			keyRadioButtonManager.radioButton.setOnMouseClicked(e->{
				if(keyRadioButtonManager.radioButton.isSelected()) {//changed from unselected to selected
					//1
					//find out all unlinked and linkable map values and enable their RadioButtons
					//	need to consider whether duplicate value is allowed!!!!!!TODO
					//disable RadioButtons of other map values; //
					this.mapValueRadioButtonManagerMap.forEach((value,valueRadioButtonManager)->{
						if(this.getOwnerNodeBuilder().getKeyValuePairBiPredicate().test(keyRadioButtonManager.key, value) && 
								!(!this.getOwnerNodeBuilder().isAllowingDuplicateMapValue() && this.linkedMapKeyValueMap.containsValue(value)) //
								) {
							FXUtils.set2Disable(valueRadioButtonManager.radioButton, false);
						}else {
							//skip, since those RadioButtons should have been disabled already
						}
					});
					
					//2. disable the RadioButton of other map keys;
					this.mapKeyRadioButtonManagerMap.forEach((key2,keyRadioButtonManager2)->{
						if(!key.equals(key2)) {
							FXUtils.set2Disable(keyRadioButtonManager2.radioButton, true);
						}
					});
					
					//3. set currentlySelectedUnlinkedMapKey;
					this.currentlySelectedUnlinkedMapKey = key;
					
				}else {//changed from selected to unselected
					//if there is a linked map value,
					//1. remove the line between them; note that there can either none or one value linked to a key at a time!
					//2. set the linked map value's RadioButton to unselected
					if(this.linkedMapKeyValueMap.containsKey(key)) {
						//remove the linking line from anchorpane;
						this.anchorPane.getChildren().remove(this.mapKeyLinkingLineMap.get(key));
						//set the radiobutton of the linked map value to unselected
						this.mapValueRadioButtonManagerMap.get(this.linkedMapKeyValueMap.get(key)).radioButton.setSelected(false);
						//remove the link from maps
						this.mapKeyLinkingLineMap.remove(key);
						this.linkedMapKeyValueMap.remove(key);
					}else {//the key radiobutton was selected to choose a value, but de-selected before it was done;
						this.currentlySelectedUnlinkedMapKey = null;
					}
					
					//disable all value radiobutton
					this.mapValueRadioButtonManagerMap.forEach((v,m)->{
						FXUtils.set2Disable(m.radioButton, true);
					});
					
					//enable RadioButton of other map keys; 
					this.mapKeyRadioButtonManagerMap.forEach((key2,keyRadioButtonManager2)->{
						if(!key.equals(key2)) {
							FXUtils.set2Disable(keyRadioButtonManager2.radioButton, false);
						}
					});
				}
				
				/////////////////////
				//trigger
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
			
			//
		});
		
		
		////////
		this.applyCssAndLayout();
	}
	
	
	void updateMapValueSet() {
		///clear current data
		this.mapValueGridPane.getChildren().clear();
		
		this.mapValueLabelMap.clear();
		this.mapValueRadioButtonManagerMap.clear();
		this.radioButtonValueMap.clear();
		
		
		this.anchorPane.getChildren().removeAll(mapKeyLinkingLineMap.values());
		this.linkedMapKeyValueMap.clear();
		this.mapKeyLinkingLineMap.clear();
		
		////reset new data
		this.getOwnerNodeBuilder().getValueSet().forEach(value->{
			this.mapValueLabelMap.put(value, new Label(this.getOwnerNodeBuilder().getMapValueToStringRepresentationFunction().apply(value)));
			MapValueRadioButtonManager<V> manager = new MapValueRadioButtonManager<>(value);
			this.mapValueRadioButtonManagerMap.put(value, manager);
			this.radioButtonValueMap.put(manager.radioButton, value);
		});
		
		//add Labels and RadioButtons to grid pane;
		int rowIndex = 0;
		for(V value:this.getOwnerNodeBuilder().getValueSet()){
			GridPane.setConstraints(this.mapValueLabelMap.get(value), 1, rowIndex); // column, row
			this.mapValueGridPane.getChildren().add(this.mapValueLabelMap.get(value));
			this.mapValueLabelMap.get(value).setOnMouseClicked(e->{
				System.out.println(this.getClass().getSimpleName()+": X:"+this.mapValueRadioButtonManagerMap.get(value).getRadioButtonCenterCoord(this.anchorPane).getX());
				System.out.println(this.getClass().getSimpleName()+": Y:"+this.mapValueRadioButtonManagerMap.get(value).getRadioButtonCenterCoord(this.anchorPane).getY());
			});
			
			GridPane.setConstraints(this.mapValueRadioButtonManagerMap.get(value).radioButton, 0, rowIndex); // column, row
			this.mapValueGridPane.getChildren().add(this.mapValueRadioButtonManagerMap.get(value).radioButton);
			
			rowIndex++;
		}
		
		//disable all value radiobutton and set to unselected
		this.mapValueRadioButtonManagerMap.forEach((v,m)->{
			FXUtils.set2Disable(m.radioButton, true);
			m.radioButton.setSelected(false);
		});
		
		//enable all key radiobuttona nd set to unselected
		this.mapKeyRadioButtonManagerMap.forEach((k,m)->{
			FXUtils.set2Disable(m.radioButton, false);
			m.radioButton.setSelected(false);
		});
		
		
		//set the event listener for each RadioButton of value;
		mapValueRadioButtonManagerMap.forEach((value,valueRadioButtonManager)->{
			
			//the only event could happen is a enabled RadioButton is selected to create a link to the currentlySelectedUnlinkedMapKey;
			valueRadioButtonManager.radioButton.setOnMouseClicked(e->{
				
//				if(valueRadioButtonManager.radioButton.isSelected()) {//changed from unselected to selected
					//always add link from the this.currentlySelectedUnlinkedMapKey to this value no matter the radio button is selected or not;
				Line line = new Line();
				line.setStartX(this.mapKeyRadioButtonManagerMap.get(this.currentlySelectedUnlinkedMapKey).getRadioButtonCenterCoord(anchorPane).getX());
				line.setStartY(this.mapKeyRadioButtonManagerMap.get(this.currentlySelectedUnlinkedMapKey).getRadioButtonCenterCoord(anchorPane).getY());
				line.setEndX(valueRadioButtonManager.getRadioButtonCenterCoord(anchorPane).getX());
				line.setEndY(valueRadioButtonManager.getRadioButtonCenterCoord(anchorPane).getY());
				
				line.setOnMouseClicked(ev->{
					System.out.println(this.getClass().getSimpleName()+": "+"startX="+line.getStartX());
					System.out.println(this.getClass().getSimpleName()+": "+"startY="+line.getStartY());
					System.out.println(this.getClass().getSimpleName()+": "+"endX="+line.getEndX());
					System.out.println(this.getClass().getSimpleName()+": "+"endY="+line.getEndY());
				});
				
				this.anchorPane.getChildren().add(line);
				
				this.mapKeyLinkingLineMap.put(this.currentlySelectedUnlinkedMapKey, line);
				this.linkedMapKeyValueMap.put(this.currentlySelectedUnlinkedMapKey, value);
				
				//always set to selected
				valueRadioButtonManager.radioButton.setSelected(true);
				
				
				//reset this.currentlySelectedUnlinkedMapKey
				this.currentlySelectedUnlinkedMapKey = null;
				
				//disable all value radioButton
				this.mapValueRadioButtonManagerMap.forEach((v,m)->{
					FXUtils.set2Disable(m.radioButton, true);
				});
				
				//enable all key radiobutton
				this.mapKeyRadioButtonManagerMap.forEach((k,m)->{
					FXUtils.set2Disable(m.radioButton, false);
				});
				
				//trigger
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
			
		});
		
		
		///!
		this.applyCssAndLayout();
	}
	
	/**
	 * whenever the map key set and value set is updated, should invoke this method so that 
	 * 
	 * the updated UI's nodes size and layout will be correctly accessed to facilitate linking line layout calculation before the UI is actually shown in a stage;
	 */
	@SuppressWarnings("unused")
	private void applyCssAndLayout() {
		//first create a dummy scene to hold the scrollpane;
		Group root = new Group();
		Scene scene = new Scene(root);
		//need to remove the anchorpane from the scrollpane before adding it to the dummy scene;
		this.scrollPane.setContent(null);
		
		root.getChildren().add(this.anchorPane);
		//invoke the apply css and layout methods so that the size and layout of all children nodes are accessible;
		root.applyCss();
		root.layout();
		
		//after apply css and layout, remove the anchorpane from the dummy scene;
		root.getChildren().clear();
		
		//add the anchorPane back to the scrollpane
		this.scrollPane.setContent(this.anchorPane);
		
	}
	
	////////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public FixedKeySetFixedValueSetMapBuilder<K,V> getOwnerNodeBuilder() {
		return (FixedKeySetFixedValueSetMapBuilder<K,V>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	
	@Override
	public Map<K,V> build() {
		//if null map value exists, check if allowed
		if(this.linkedMapKeyValueMap.size()<this.getOwnerNodeBuilder().getKeySet().size()) 
			if(!this.getOwnerNodeBuilder().isAllowingNullMapValue()) 
				throw new VisframeException("null map value is not allowed!");
		
		//duplicate map value constraints is implemented in the event listener, no need to explicitly check here;
		
		////////
		Map<K,V> ret = new LinkedHashMap<>();
		
		this.getOwnerNodeBuilder().getKeySet().forEach(k->{
			if(this.linkedMapKeyValueMap.containsKey(k))
				ret.put(k, this.linkedMapKeyValueMap.get(k));
			else
				ret.put(k, null);
			
		});
		
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
		//remove all linking lines 
		this.anchorPane.getChildren().removeAll(this.mapKeyLinkingLineMap.values());
		
		this.linkedMapKeyValueMap.clear();
		this.mapKeyLinkingLineMap.clear();
		
		//disable all value radiobutton and set to unselected
		this.mapValueRadioButtonManagerMap.forEach((v,m)->{
			FXUtils.set2Disable(m.radioButton, true);
			m.radioButton.setSelected(false);
		});
		
		//enable all key radiobuttona nd set to unselected
		this.mapKeyRadioButtonManagerMap.forEach((k,m)->{
			FXUtils.set2Disable(m.radioButton, false);
			m.radioButton.setSelected(false);
		});
		
		this.currentlySelectedUnlinkedMapKey = null;
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(Map<K,V> value) throws SQLException, IOException {
		this.setUIToDefaultEmptyStatus();
		
		value.forEach((k,v)->{
			//create line for each entry
			Line line = new Line();
			line.setStartX(this.mapKeyRadioButtonManagerMap.get(k).getRadioButtonCenterCoord(anchorPane).getX());
			line.setStartY(this.mapKeyRadioButtonManagerMap.get(k).getRadioButtonCenterCoord(anchorPane).getY());
			line.setEndX(this.mapValueRadioButtonManagerMap.get(v).getRadioButtonCenterCoord(anchorPane).getX());
			line.setEndY(this.mapValueRadioButtonManagerMap.get(v).getRadioButtonCenterCoord(anchorPane).getY());
			
			this.anchorPane.getChildren().add(line);
			
			this.mapKeyLinkingLineMap.put(k, line);
			this.linkedMapKeyValueMap.put(k, v);
			
			//set the linked key and value radio button selected
			this.mapKeyRadioButtonManagerMap.get(k).radioButton.setSelected(true);
			this.mapValueRadioButtonManagerMap.get(v).radioButton.setSelected(true);
		});
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//DO NOT invoke super class's method for this controller!!!!
//		super.setModifiable(modifiable);
		//
		this.mapKeyRadioButtonManagerMap.forEach((k,m)->{
			FXUtils.set2Disable(m.radioButton, !modifiable);
		});
		
		FXUtils.set2Disable(this.clearAllLinksButton, !modifiable);
	}
	
	
	//////////////////////////
//	private Scene calibratingScene;
	@FXML
	public void initialize() {
//		this.calibratingScene = new Scene(this.getRootParentNode());
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private VBox mapKeyContainerVBox;
	@FXML
	private GridPane mapKeyGridPane;
	@FXML
	private VBox mapValueContainerVBox;
	@FXML
	private GridPane mapValueGridPane;
	
	@FXML
	private Button clearAllLinksButton;
	
	// Event Listener on Button[#clearAllLinksButton].onAction
	@FXML
	public void clearAllLinksButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.setUIToDefaultEmptyStatus();
	}
	
	////////////////////////////////////////////////////
	static class MapKeyRadioButtonManager<K2>{
		private final K2 key;
		private final RadioButton radioButton;
		
		
		private Point2D radioButtonCenterCoord;
		
		MapKeyRadioButtonManager(K2 key){
			this.key = key;
			this.radioButton = new RadioButton();
		}
		
		Point2D getRadioButtonCenterCoord(AnchorPane anchorPane) {
			if(this.radioButtonCenterCoord==null) {
				Point2D layout = LayoutCoordinateAndSizeUtils.getLayoutPoint2D(anchorPane, radioButton);
				
				this.radioButtonCenterCoord = new Point2D(layout.getX()+10, layout.getY()+10);  //10 is the radius of the RadioButton
			}
			return this.radioButtonCenterCoord;
		}
		
	}
	
	static class MapValueRadioButtonManager<V2>{
		private final V2 value;
		private final RadioButton radioButton;
		
		private Point2D radioButtonCenterCoord;
		
		MapValueRadioButtonManager(V2 value){
			this.value = value;
			this.radioButton = new RadioButton();
		}
		
		Point2D getRadioButtonCenterCoord(AnchorPane anchorPane) {
			if(this.radioButtonCenterCoord==null) {
				Point2D layout = LayoutCoordinateAndSizeUtils.getLayoutPoint2D(anchorPane, radioButton);
				
				this.radioButtonCenterCoord = new Point2D(layout.getX()+10, layout.getY()+10);  //10 is the radius of the RadioButton
			}
			
			return this.radioButtonCenterCoord;
		}
		
	}
}
