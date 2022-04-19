package viewer;


public interface ViewerFactory<T> {
	/**
	 * 
	 * @param u
	 * @return
	 */
	AbstractViewer<? extends T, ?> build(T u);
	
}
