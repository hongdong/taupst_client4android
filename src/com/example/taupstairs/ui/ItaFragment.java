package com.example.taupstairs.ui;

/*Fragment公用接口*/
public interface ItaFragment {

	/*
	 * 初始化操作
	 */
	void init();
	
	/*
	 * 更新UI
	 */
	void refresh(Object...params);
	
	/*
	 * 退出时最后的操作，由父activity调用
	 */
	void exit();
}
