package rs.sourcecode.loyaltyqr.util;

import java.util.ArrayList;

public class GiftsSingleton {
	
	private static volatile GiftsSingleton instance = null;
	
	public int company_id;
	public ArrayList<Gift> listOfGifts = null;
	public Gift gift;
	
	protected GiftsSingleton() {
		listOfGifts = new ArrayList<Gift>();
	}
	
	public static GiftsSingleton getInstance() {
		if(instance == null) {
			synchronized (GiftsSingleton.class) {
				instance = new GiftsSingleton();
			}
		}
		return instance;
	}
}
