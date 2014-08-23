package com.example.moment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.widget.ImageButton;
import utils.android.CameraActivity;
import utils.android.PictureSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2014/7/19.
 */
public class UserCenterActivity extends Activity{

	private ViewPager viewPager;//页卡内容
	private List<View> listViews; // Tab页面列表

	private ImageButton button_to_notice;
	private ImageButton button_to_hot;
	private ImageButton button_to_index;
	private ImageButton button_to_contact;
	int firstY; //出现在屏幕上的第一个触点
	int secondY; //出现在屏幕上的第二个触点
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度

	private View face;
	private View notice;
	private View hot;
	private View index;
	private View contact;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.usercenter);

		//initImageView();
		initViewPager();
		initButton(face);
	}
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.view_pager);
//		pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_tab);
		listViews = new ArrayList<View>();

		LayoutInflater mInflater = getLayoutInflater().from(this);

		face = mInflater.inflate(R.layout.face, null);
		notice = mInflater.inflate(R.layout.notice,null);
		hot = mInflater.inflate(R.layout.hot,null);
		index = mInflater.inflate(R.layout.user_index,null);
		contact = mInflater.inflate(R.layout.contact,null);

		listViews.add(notice);
		listViews.add(face);
		listViews.add(contact);

		//设置pagerTabStrip,（滑动小卡）
//		pagerTabStrip.setTabIndicatorColor(Color.RED);
//		pagerTabStrip.setTextColor(Color.RED);
//		pagerTabStrip.setClickable(false);
//		pagerTabStrip.setTextSpacing(100);
//		pagerTabStrip.setBackgroundColor(Color.GREEN);
//		pagerTabStrip.setDrawFullUnderline(true);

		viewPager.setAdapter(new MyPagerAdapter(listViews));
		//设置当前显示的是第二个页面（顺序为add时候的顺序）
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				doSomething(event);
				return false;
			}
		});
	}

	private void initButton(View v){
		button_to_notice = (ImageButton) v.findViewById(R.id.home_to_notice);
		button_to_hot = (ImageButton) v.findViewById(R.id.home_to_hot);
		button_to_index = (ImageButton) v.findViewById(R.id.home_to_index);
		button_to_contact = (ImageButton) v.findViewById(R.id.home_to_contact);

		button_to_notice.setOnClickListener(new MyOnClickListener(0));
		button_to_hot.setOnClickListener(new MyOnClickListener(1));
		button_to_index.setOnClickListener(new MyOnClickListener(2));
		button_to_contact.setOnClickListener(new MyOnClickListener(3));
	}
	private void doSomething(MotionEvent event){
		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_POINTER_1_UP:
				//考虑到用户可能不是两个手指同时离开屏幕
				openCameraOrPicture((int) event.getY(0), (int) event.getY(1));
				break;
			case MotionEvent.ACTION_POINTER_2_DOWN:
				//第一个点的纵坐标
				//先只考虑纵坐标
				firstY = (int) event.getY(0);
				//第二个点的纵坐标
				secondY = (int) event.getY(1);

				break;
			case MotionEvent.ACTION_POINTER_2_UP:
				//或者在第一个手指抬起的时候出发
				openCameraOrPicture((int) event.getY(0), (int) event.getY(1));
				break;
		}
	}

	/**
	 *
	 * @param lastPositionY_1 第一个点最后一次在屏幕上的位置
	 * @param lastPositionY_2 第二个点最后一次在屏幕上的位置
	 */
	private void openCameraOrPicture(int lastPositionY_1, int lastPositionY_2){

		//双指向下滑动
		if (lastPositionY_1 - firstY > 20 && lastPositionY_2 - secondY > 20) {
			//打开照相机
			startActivity(new Intent(this, CameraActivity.class));
		}
		if (firstY - lastPositionY_1 > 20 && secondY - lastPositionY_2 > 20) {
			//打开图库
			startActivity(new Intent(this, PictureSelect.class));
		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		private int index=0;
		public MyOnClickListener(int i){
			index=i;
		}
		public void onClick(View v) {
			//
			switch (index){
				case 0:
					viewPager.setCurrentItem(index);
					break;
				case 1:
					//打开热门
					startActivity(new Intent(UserCenterActivity.this, HotActivity.class));
					overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
					break;
				case 2:
					//打开主页
					startActivity(new Intent(UserCenterActivity.this, UserIndexActivity.class));
					overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
					break;
				case 3:
					viewPager.setCurrentItem(index);
					break;
			}
		}
	}

	public class MyPagerAdapter extends PagerAdapter{
		private List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 	{
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return  mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		//int distance = offset * 2 + bmpW;
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageSelected(int arg0) {
			//标头动画效果
//			Animation animation = new TranslateAnimation(distance * nearlyIndex, distance * arg0, 0, 0);
//			nearlyIndex = arg0;
//			animation.setFillAfter(true);// True:图片停在动画结束位置
//			animation.setDuration(300);
//			cursor.startAnimation(animation);
		}
	}
}