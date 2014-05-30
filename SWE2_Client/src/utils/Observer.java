package utils;

//The Observers update method is called when the Subject changes

public interface Observer {
	/**
	 * if MWSt changes in the Invoice-View, all the Invoice lines get
	 * notified and call the update-method to update the brutto value
	 * @param newMWSt
	 */
	public void update(double newMWSt);
}
