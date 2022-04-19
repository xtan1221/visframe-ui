package viewer.visframe.function.target;

import function.target.CFGTarget;
import viewer.AbstractViewer;

public abstract class CFGTargetViewerBase<T extends CFGTarget<?>, C extends CFGTargetViewerControllerBase<T>> extends AbstractViewer<T, C>{

	protected CFGTargetViewerBase(T value, String FXMLFileDirString) {
		super(value, FXMLFileDirString);
		// TODO Auto-generated constructor stub
	}
	
}
