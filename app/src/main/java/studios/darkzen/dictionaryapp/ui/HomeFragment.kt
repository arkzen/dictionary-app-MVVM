package studios.darkzen.dictionaryapp.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.databinding.FragmentHomeBinding
import studios.darkzen.dictionaryapp.ui.adapter.CategoryAdapter
import studios.darkzen.dictionaryapp.ui.adapter.QuizPackAdapter
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment

@AndroidEntryPoint
class HomeFragment : CoreBaseFragment<FragmentHomeBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvQuizPacks.layoutManager = LinearLayoutManager(requireContext())
        
        binding.btnStartTodayQuiz.setOnClickListener {
            // Hardcoded for MVP: Start first pack of first category
            viewModel.categories.value.firstOrNull()?.packs?.firstOrNull()?.let { pack ->
                val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(pack.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest { categories ->
                    if (categories.isNotEmpty()) {
                        binding.rvCategories.adapter = CategoryAdapter(categories) { category ->
                            val action = HomeFragmentDirections.actionHomeFragmentToQuizPackFragment(category.id)
                            findNavController().navigate(action)
                        }
                        
                        val allPacks = categories.flatMap { it.packs }
                        binding.rvQuizPacks.adapter = QuizPackAdapter(allPacks) { pack ->
                            val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(pack.id)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }
}
