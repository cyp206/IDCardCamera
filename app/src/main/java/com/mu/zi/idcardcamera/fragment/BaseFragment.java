package com.mu.zi.idcardcamera.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * Fragment父类
 * 
 * @author hbin
 */
public abstract class BaseFragment extends Fragment {
	/** 子fragment使用v.findViewById 调用布局 */
	protected View 				v;
	private	  String			mPageName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		MobclickAgent.openActivityDurationTrack(false);
		mPageName = getClass().getName();
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(getLayoutResId(), container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI(savedInstanceState);
		initData(savedInstanceState);
		initListener(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (getUserVisibleHint()) {
			onVisibilityChangedToUser(true, false);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (getUserVisibleHint()) {
			onVisibilityChangedToUser(false, false);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isResumed()) {
			onVisibilityChangedToUser(isVisibleToUser, false);
		}
	}
	
	/**
	 * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
	 * @param isVisibleToUser true：用户能看见当前Fragment；false：用户看不见当前Fragment
	 * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
	 */
	private void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
		if (isVisibleToUser) {
		} else {
		}
	}

	/**
	 * 返回View Layout的ID
	 * 
	 * @return
	 */
	protected abstract int getLayoutResId();

	/**
	 * 替换Fragment
	 * 
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack
	 *            是否加入返回栈
	 */
	public void replaceFragment(int resLayId, Fragment fragment, boolean isAddBackStack) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right, R.anim.slide_in_left,
		// R.anim.slide_out_right);
		fragmentTransaction.replace(resLayId, fragment);
		if (isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public void replaceFragment(int resLayId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		// fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right, R.anim.slide_in_left,
		// R.anim.slide_out_right);
		fragmentTransaction.replace(resLayId, fragment);
		fragmentTransaction.commit();
	}
	
	public void addFragment(int resLayId, Fragment fragment){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(resLayId, fragment);
		fragmentTransaction.commit();
	}
	
	public void removeFragment(Fragment fragment){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}

	public void initUI(Bundle savedInstanceState) {

	}

	public void initListener(Bundle savedInstanceState) {

	}

	public void initData(Bundle savedInstanceState) {

	}
	
	public void updateFragment() {
		
	}
	
	// /**
	// * 添加Fragment
	// * @param resLayId
	// * @param fragment
	// * @param isAddBackStack
	// * @param hideFragments 要隐藏的Fragment数组
	// */
	// protected void addFragment(int resLayId,Fragment showFragment,boolean
	// isAnimation,boolean isAddBackStack,Fragment ...hideFragments){
	// getActivity().addFragment(resLayId, showFragment, isAnimation,
	// isAddBackStack, hideFragments);
	// }

	// /**
	// * 显示隐藏Fragment
	// * @param showFragment
	// * @param hideFragments 要隐藏的Fragment数组
	// * @param isAddBackStack 是否加入返回栈
	// */
	// protected void showHideFragment(Fragment showFragment,boolean
	// isAnimation,boolean isAddBackStack,Fragment ...hideFragments){
	// getActivity().showHideFragment(showFragment, isAnimation, isAddBackStack,
	// hideFragments);
	// }

	public int getWidth(){
		DisplayMetrics metric = new DisplayMetrics();
		int width = metric.widthPixels;     // 屏幕宽度（像素）
		return width;
	}

	public int getHeight(){
		DisplayMetrics metric = new DisplayMetrics();
		int height = metric.heightPixels;   // 屏幕高度（像素）
		return height;
	}

}
