package com.yogas.api.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yogas.api.databinding.FragmentFolloweringBinding
import com.yogas.api.model.ListUsers
import com.yogas.api.mv.DetailViewModel

class Followering : Fragment() {
    private lateinit var adapter: ListUsers
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: FragmentFolloweringBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListUsers()
        adapter.notifyDataSetChanged()

        binding.followering.layoutManager = LinearLayoutManager(activity)
        binding.followering.adapter = adapter

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME, "")

        if (index == 0) {
            username?.let { detailViewModel.setFollowerUser(it) }
            activity?.let {
                detailViewModel.getFollowerUser().observe(it, { listFollowers ->
                    if (listFollowers != null) {
                        adapter.setData(listFollowers)
                    }
                })
            }
        } else if (index == 1) {
            username?.let { detailViewModel.setFollowingUser(it) }
            activity?.let {
                detailViewModel.getFollowingUser().observe(it, { listFollowing ->
                    if (listFollowing != null) {
                        adapter.setData(listFollowing)
                    }
                })
            }
        }

        activity?.let {
            detailViewModel.statusError.observe(it, { status ->
                if (status != null) {
                    Toast.makeText(activity, status, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFolloweringBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, login: String) =
            Followering().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, login)
                }
            }
    }
}