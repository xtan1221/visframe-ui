package viewer.visframe.metadata;

import java.util.HashMap;
import java.util.Map;

import context.VisframeContext;
import metadata.DataType;
import metadata.Metadata;
import metadata.graph.GraphDataMetadata;
import metadata.graph.vftree.VfTreeDataMetadata;
import metadata.record.RecordDataMetadata;
import viewer.ViewerFactory;
import viewer.visframe.metadata.graph.GraphDataMetadataViewer;
import viewer.visframe.metadata.graph.vftree.VfTreeDataMetadataViewer;
import viewer.visframe.metadata.record.RecordDataMetadataViewer;

public class MetadataViewerFactory implements ViewerFactory<Metadata>{
	private static Map<VisframeContext, MetadataViewerFactory> projectSingletonMap;
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static MetadataViewerFactory singleton(VisframeContext context) {
		if(projectSingletonMap==null) {
			projectSingletonMap = new HashMap<>();
		}
		
		if(!projectSingletonMap.containsKey(context)) {
			projectSingletonMap.put(context, new MetadataViewerFactory(context));
		}
		
		return projectSingletonMap.get(context);
	}
	
	////////////////////////////
	private final VisframeContext hostVisframeContext;
	
	/**
	 * 
	 * @param hostVisframeContext
	 */
	public MetadataViewerFactory(VisframeContext hostVisframeContext){
		this.hostVisframeContext = hostVisframeContext;
	}
	
	
	@Override
	public MetadataViewerBase<?, ?> build(Metadata u) {		
		if(u instanceof RecordDataMetadata) {
			RecordDataMetadata r = (RecordDataMetadata)u;
			return new RecordDataMetadataViewer(r, this.hostVisframeContext);
		}else if(u.getDataType().equals(DataType.GRAPH)) {
			GraphDataMetadata g = (GraphDataMetadata)u;
			return new GraphDataMetadataViewer(g, this.hostVisframeContext);
		}else {//vftree
			VfTreeDataMetadata t = (VfTreeDataMetadata)u;
			return new VfTreeDataMetadataViewer(t, this.hostVisframeContext);
		}
	}
	
	
}
