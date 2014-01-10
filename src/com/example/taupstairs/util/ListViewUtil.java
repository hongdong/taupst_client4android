package com.example.taupstairs.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtil {

	public static void setListViewHeightBasedOnChildren(ListView listView) {  
	    ListAdapter adapter = listView.getAdapter();   
	    if (adapter == null) {  
	        return;  
	    }  

	    int totalHeight = 0;  
	    for (int i = 0; i < adapter.getCount(); i++) {  
	        View listItem = adapter.getView(i, null, listView);  
	        listItem.measure(0, 0);  
	        totalHeight += listItem.getMeasuredHeight();  
	    }  

	    ViewGroup.LayoutParams params = listView.getLayoutParams();  
	    params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
	    listView.setLayoutParams(params);  
	} 
}
