package dependencygraph.vccl.nodeselection;

public class CopyLink<V> {
	private final V dependingNode;
	private final int dependingNodeCopyIndex;
	private final V dependedNode;
	private final int dependedNodeCopyIndex;
	
	public CopyLink(
			V dependingNode,
			int dependingNodeCopyIndex,
			V dependedNode,
			int dependedNodeCopyIndex
			){
		
		this.dependingNode = dependingNode;
		this.dependingNodeCopyIndex = dependingNodeCopyIndex;
		this.dependedNode = dependedNode;
		this.dependedNodeCopyIndex = dependedNodeCopyIndex;
		
	}

	/**
	 * @return the dependingNode
	 */
	public V getDependingNode() {
		return dependingNode;
	}

	/**
	 * @return the dependingNodeCopyIndex
	 */
	public int getDependingNodeCopyIndex() {
		return dependingNodeCopyIndex;
	}

	/**
	 * @return the dependedNode
	 */
	public V getDependedNode() {
		return dependedNode;
	}

	/**
	 * @return the dependedNodeCopyIndex
	 */
	public int getDependedNodeCopyIndex() {
		return dependedNodeCopyIndex;
	}
}
