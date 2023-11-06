package com.dicoding.githubconnect.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubconnect.adapter.UserAdapter
import com.dicoding.githubconnect.data.remote.response.ItemsItem
import com.dicoding.githubconnect.databinding.FragmentTabBinding
import com.dicoding.githubconnect.vm.FollowViewModel

class TabFragment : Fragment() {
    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = ""

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)!!
        }

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowViewModel::class.java)

        if (position == 1) {

            followViewModel.findFollowers(username)

            followViewModel.followers.observe(viewLifecycleOwner) { followers ->
                setFollowersData(followers)
            }

            followViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else {

            followViewModel.findFollowing(username)

            followViewModel.following.observe(viewLifecycleOwner) { following ->
                setFollowingData(following)
            }

            followViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }

        showRecylerList()
    }

    private fun showRecylerList() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)
    }

    private fun setFollowersData(followers: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(followers)
        binding.rvUsers.adapter = adapter
    }

    private fun setFollowingData(following: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(following)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}