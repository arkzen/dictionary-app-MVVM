package studios.darkzen.dictionaryapp.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.databinding.FragmentAllQuizPacksBinding
import studios.darkzen.dictionaryapp.ui.adapter.QuizPackAdapter
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel

@AndroidEntryPoint
class AllQuizPacksFragment : CoreBaseFragment<FragmentAllQuizPacksBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var adapter: QuizPackAdapter
    private var allPacks: List<QuizPack> = emptyList()
    private val displayedPacks = mutableListOf<QuizPack>()
    private val pageSize = 15
    private var currentPage = 0

    override fun getViewBinding() = FragmentAllQuizPacksBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvAllPacks.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = QuizPackAdapter(displayedPacks, viewModel.allProgress.value) { pack ->
            // Navigation will be added to nav_graph.xml
            val action = AllQuizPacksFragmentDirections.actionAllQuizPacksFragmentToQuizFragment(pack.id)
            findNavController().navigate(action)
        }
        binding.rvAllPacks.adapter = adapter

        binding.rvAllPacks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (totalItemCount <= lastVisibleItem + 2 && displayedPacks.size < allPacks.size) {
                    loadNextPage()
                }
            }
        })
    }

    override fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                allPacks = viewModel.getMixedPacks()
                if (displayedPacks.isEmpty()) {
                    loadNextPage()
                }

                viewModel.allProgress.collectLatest { progressList ->
                    adapter.updateData(displayedPacks, progressList)
                }
            }
        }
    }

    private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, allPacks.size)
        
        if (start < allPacks.size) {
            val nextBatch = allPacks.subList(start, end)
            displayedPacks.addAll(nextBatch)
            adapter.updateData(ArrayList(displayedPacks), viewModel.allProgress.value)
            currentPage++
        }
    }
}
