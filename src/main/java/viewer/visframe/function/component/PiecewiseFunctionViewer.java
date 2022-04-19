package viewer.visframe.function.component;

import java.util.LinkedHashMap;
import java.util.Map;

import function.component.PiecewiseFunction;
import viewer.visframe.function.composition.CompositionFunctionViewer;

public class PiecewiseFunctionViewer extends ComponentFunctionViewerBase<PiecewiseFunction, PiecewiseFunctionViewerController>{
	
	/**
	 * 
	 */
	private ComponentFunctionViewerBase<?,?> defaultNextComponentFunctionViewer;

	/**
	 * 
	 */
	private Map<Integer, ComponentFunctionViewerBase<?,?>> conditionPrecedenceIndexNextComponentFunctionViewerMap;
	
	/**
	 * constructor
	 * @param value
	 * @param hostCompositionFunctionViewer
	 */
	public PiecewiseFunctionViewer(
			PiecewiseFunction value,
			CompositionFunctionViewer hostCompositionFunctionViewer) {
		super(value, SimpleFunctionViewerController.FXML_FILE_DIR_STRING, hostCompositionFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return the defaultNextComponentFunctionViewer
	 */
	public ComponentFunctionViewerBase<?, ?> getDefaultNextComponentFunctionViewer() {
		return defaultNextComponentFunctionViewer;
	}


	/**
	 * @return the conditionPrecedenceIndexNextComponentFunctionViewerMap
	 */
	public Map<Integer, ComponentFunctionViewerBase<?, ?>> getConditionPrecedenceIndexNextComponentFunctionViewerMap() {
		return conditionPrecedenceIndexNextComponentFunctionViewerMap;
	}

	/////////////////////////////////////
	@Override
	protected void buildDownstreamComponentFunctionViewers() {
		this.defaultNextComponentFunctionViewer = 
				ComponentFunctionViewerFactory.build(this.getValue().getDefaultNextFunction(), this.getHostCompositionFunctionViewer());
		
		this.conditionPrecedenceIndexNextComponentFunctionViewerMap = new LinkedHashMap<>();
		this.getValue().getConditionPrecedenceIndexNextFunctionMap().forEach((index, cf)->{
			ComponentFunctionViewerBase<?,?> conditionNext = ComponentFunctionViewerFactory.build(cf, this.getHostCompositionFunctionViewer());
			this.conditionPrecedenceIndexNextComponentFunctionViewerMap.put(
					index,
					conditionNext
					);
		});
	}

	
	@Override
	public void calculateLeafIndex() {
		
		this.defaultNextComponentFunctionViewer.calculateLeafIndex();
		double childLeafIndexSum = this.defaultNextComponentFunctionViewer.leafIndex;
		
		for(int index:this.conditionPrecedenceIndexNextComponentFunctionViewerMap.keySet()) {
			this.conditionPrecedenceIndexNextComponentFunctionViewerMap.get(index).calculateLeafIndex();
			childLeafIndexSum += this.conditionPrecedenceIndexNextComponentFunctionViewerMap.get(index).leafIndex;
		}
		
		this.leafIndex = childLeafIndexSum/(1+this.conditionPrecedenceIndexNextComponentFunctionViewerMap.size());
	}


	@Override
	public void setEdgeNumToRoot(int num) {
		this.edgeNumToRoot = num;
		
		this.defaultNextComponentFunctionViewer.setEdgeNumToRoot(num+1);
		
		this.conditionPrecedenceIndexNextComponentFunctionViewerMap.forEach((index, next)->{
			next.setEdgeNumToRoot(num+1);
		});
		
	}

	@Override
	public void calculateCenterCoordinate() {
		super.calculateCenterCoordinate();
		
		this.defaultNextComponentFunctionViewer.calculateCenterCoordinate();
		this.conditionPrecedenceIndexNextComponentFunctionViewerMap.forEach((index, next)->{
			next.calculateCenterCoordinate();
		});
	}
	
	@Override
	public void calculateRealSize() {
		super.calculateRealSize();
		
		this.defaultNextComponentFunctionViewer.calculateRealSize();
		this.conditionPrecedenceIndexNextComponentFunctionViewerMap.forEach((index, next)->{
			next.calculateRealSize();
		});
	}
		
	@Override
	public void buildLinkingCurveToPreviousComponentFunction() {
		super.buildLinkingCurveToPreviousComponentFunction();
		
		
		this.defaultNextComponentFunctionViewer.buildDownstreamComponentFunctionViewers();
		this.conditionPrecedenceIndexNextComponentFunctionViewerMap.forEach((index,next)->{
			next.buildLinkingCurveToPreviousComponentFunction();
		});
	}
}
