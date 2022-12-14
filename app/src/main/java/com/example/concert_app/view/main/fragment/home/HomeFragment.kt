package com.example.concert_app.view.main.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.concert_app.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mTabs: TabLayout
    private lateinit var mIndicator: View
    private lateinit var mViewPager: ViewPager

    private var indicatorWidth = 0


    private fun initView() {
        mTabs = binding.tab
        mIndicator = binding.indicator
        mViewPager = binding.viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter: TabFragmentAdapter = TabFragmentAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(AllGenresFragment(), "All Genders")
        adapter.addFragment(PopFragment(), "Pop")
        adapter.addFragment(RockFragment(), "Rock")
        adapter.addFragment(IndieFragment(), "Indie")
        mViewPager.adapter = adapter
        mTabs.setupWithViewPager(mViewPager)

        mTabs.post {
            indicatorWidth = mTabs.width / mTabs.tabCount

            //Assign new width
            val indicatorParams = mIndicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            mIndicator.layoutParams = indicatorParams
        }

        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
                val params = mIndicator.layoutParams as FrameLayout.LayoutParams

                //Multiply positionOffset with indicatorWidth to get translation
                val translationOffset = (positionOffset + i) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                mIndicator.layoutParams = params
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })

    }
}