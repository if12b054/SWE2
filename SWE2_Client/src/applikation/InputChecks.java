package applikation;

import javafxControllers.MainController;

public abstract class InputChecks {
	
	public static String searchKontaktError(String vorname, String nachname, String firma) {
		return null;
	}
	
	/**
	 * Checks if all the input fields are correct (and not null)
	 * @return		false if no error has ocurred, otherwise it sets an error message and returns true
	 */
	public static String searchRechnungError(boolean wrongFromDate, boolean wrongTilDate, String priceFrom, String priceTil, boolean isValidContact) {
		String errorMsg = null;
		if(wrongFromDate) {
			errorMsg = "Wrong date format.";
			return errorMsg;
		}
		if(wrongTilDate) {
			errorMsg = "Wrong date format.";
			return errorMsg;
		}
		if(!isValidContact) {
			errorMsg = "Contact not found in DB";
		}
		
		return errorMsg;
	}
	
	boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public static String saveKontaktError() {
		return null;
	}
	

}
