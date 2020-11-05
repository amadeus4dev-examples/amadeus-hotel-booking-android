package com.amadeus.android.demo.fragments.search.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.demo.R
import com.amadeus.android.demo.databinding.ItemReposLoadStateFooterBinding
import com.amadeus.android.demo.utils.visibleOrGone

class ReposLoadStateViewHolder(
        private val binding: ItemReposLoadStateFooterBinding,
        retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.visibleOrGone(loadState is LoadState.Loading)
        binding.retryButton.visibleOrGone(loadState !is LoadState.Loading)
        binding.errorMsg.visibleOrGone(loadState !is LoadState.Loading)
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ReposLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_repos_load_state_footer, parent, false)
            val binding = ItemReposLoadStateFooterBinding.bind(view)
            return ReposLoadStateViewHolder(binding, retry)
        }
    }
}