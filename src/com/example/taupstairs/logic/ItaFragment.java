package com.example.taupstairs.logic;

/*Fragment公用接口*/
public interface ItaFragment {

	/*
	 * 初始化数据
	 */
	void initData();
	
	/*
	 * 初始化UI
	 */
	void initView();
	
	/*
	 * 更新UI
	 */
	void refresh(Object...params);
	
	/*
	 * 退出时最后的操作，由父activity调用
	 */
	void exit();
}
