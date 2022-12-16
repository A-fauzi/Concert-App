package com.example.concert_app.view.main.fragment.home

import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.concert_app.R
import com.example.concert_app.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var chipGroup: ChipGroup

    private lateinit var chipAll: Chip

    private lateinit var searchView: SearchView
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var listView: ListView
    private lateinit var btnSearch: ImageView


    private fun initView() {
        chipGroup = binding.chipGroup
        chipAll = binding.chipAllGenres
        btnSearch = binding.btnSearch
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        replaceFragment(AllGenresFragment())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSearch.setOnClickListener {
            replaceFragment(SearchConcertFragment())
        }

        var state: Boolean = true

        if (state) chipAll.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.blue_soft))


        chipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            for (id in checkedId) {
                val chip = group.findViewById<Chip>(id)
                // Do something with the checked chip's ID
                when (chip.text) {
                    "All genres" -> {
                        state = true
                        if (state) chipAll.chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context!!,
                                R.color.blue_soft
                            )
                        )

                        replaceFragment(AllGenresFragment())

                        Log.d("chip", "Kamu Mengklik All genres")
                    }
                    "Pop" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(PopFragment())

                        Log.d("chip", "Kamu Mengklik Pop")
                    }
                    "Rock" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(RockFragment())

                        Log.d("chip", "Kamu Mengklik Rock")
                    }
                    "Indie" -> {
                        state = false
                        if (!state) chipAll.chipBackgroundColor =
                            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.gray))

                        replaceFragment(IndieFragment())

                        Log.d("chip", "Kamu Mengklik Indie")
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


}