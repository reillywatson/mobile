package com.vasken.hunkorjunk;

import com.vasken.util.WebRequester;

public abstract class Worker implements WebRequester.RequestCallback{
	
	public abstract HotItem getPageData(String rateId, int i, String imageURL);
}
