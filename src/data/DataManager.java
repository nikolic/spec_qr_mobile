package data;


/**
 * @author Nikolic
 *
 */
public class DataManager {

     private static DataManager instance;
     
     private DataManager()
     {
    	 
     }
	
	public static DataManager get() {
		if (instance == null)
		  instance= new	DataManager();
		
		return instance;
	}
		
}
