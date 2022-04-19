package viewer.visframe.function.component;

import function.component.SimpleFunction;
import viewer.visframe.function.composition.CompositionFunctionViewer;

public class SimpleFunctionViewer extends ComponentFunctionViewerBase<SimpleFunction, SimpleFunctionViewerController>{
	
	private ComponentFunctionViewerBase<?,?> nextComponentFunctionViewer;
	
	/**
	 * 
	 * @param value
	 * @param hostCompositionFunctionViewer
	 */
	public SimpleFunctionViewer(
			SimpleFunction value,
			CompositionFunctionViewer hostCompositionFunctionViewer) {
		super(value, SimpleFunctionViewerController.FXML_FILE_DIR_STRING, hostCompositionFunctionViewer);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildDownstreamComponentFunctionViewers() {
		if(this.getValue().getNext()==null) {
			this.nextComponentFunctionViewer = null;
		}else {
			this.nextComponentFunctionViewer = ComponentFunctionViewerFactory.build(this.getValue().getNext(), this.getHostCompositionFunctionViewer());
		}
		
	}
	
	
	@Override
	public void calculateLeafIndex() {
		if(this.nextComponentFunctionViewer==null) { //leaf simple function
			this.leafIndex = CompositionFunctionViewer.LEAF_COUNTER;
			CompositionFunctionViewer.LEAF_COUNTER++;
			
		}else { //non-leaf simple function, leaf index is the same with the next component function
			this.nextComponentFunctionViewer.calculateLeafIndex();
			this.leafIndex = this.nextComponentFunctionViewer.leafIndex;
		}
	}

	@Override
	public void calculateRealSize() {
		super.calculateRealSize();
		
		
		if(this.nextComponentFunctionViewer!=null)
			this.nextComponentFunctionViewer.calculateRealSize();
		
	}

	@Override
	public void setEdgeNumToRoot(int num) {
		this.edgeNumToRoot = num;
		if(this.nextComponentFunctionViewer!=null)
			this.nextComponentFunctionViewer.setEdgeNumToRoot(num+1);
	}
	
	
	@Override
	public void calculateCenterCoordinate() {
		super.calculateCenterCoordinate();
		
		if(this.nextComponentFunctionViewer!=null)
			this.nextComponentFunctionViewer.calculateCenterCoordinate();
	}
	
	@Override
	public void buildLinkingCurveToPreviousComponentFunction() {
		super.buildLinkingCurveToPreviousComponentFunction();
		
		if(this.nextComponentFunctionViewer!=null)
			this.nextComponentFunctionViewer.buildLinkingCurveToPreviousComponentFunction();
	}
}
