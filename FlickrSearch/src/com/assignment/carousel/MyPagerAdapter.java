package com.assignment.carousel;

import com.eduard.flickrsearch.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyPagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private MyLinearLayout cur = null;
	private MyLinearLayout next = null;
	private CarouselActivity context;
	private FragmentManager fm;
	private float scale;
	int pCount = 0;

	public MyPagerAdapter(CarouselActivity context, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// make the first pager bigger than others
		try {
			if (position == 0) {
				scale = CarouselActivity.BIG_SCALE;
			} else {
				scale = CarouselActivity.SMALL_SCALE;
			}

			position = position % CarouselActivity.count;

		}

		catch (Exception e) {
			// TODO: handle exception
		}
		return MyFragment.newInstance(context, position, scale);
	}

	@Override
	public int getCount() {
		int count = 0;
		try {
			count = CarouselActivity.count * CarouselActivity.LOOPS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		try {
			if (positionOffset >= 0f && positionOffset <= 1f) {
				cur = getRootView(position);
				next = getRootView(position + 1);

				cur.setScaleBoth(CarouselActivity.BIG_SCALE
						- CarouselActivity.DIFF_SCALE * positionOffset);
				next.setScaleBoth(CarouselActivity.SMALL_SCALE
						+ CarouselActivity.DIFF_SCALE * positionOffset);
				CarouselActivity.first_post = position;
				pCount++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onPageSelected(int position) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private MyLinearLayout getRootView(int position) {

		return (MyLinearLayout) fm
				.findFragmentByTag(this.getFragmentTag(position)).getView()
				.findViewById(R.id.root);

	}

	private String getFragmentTag(int position) {

		return "android:switcher:" + context.pager.getId() + ":" + position;
	}
}