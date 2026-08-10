package studios.darkzen.dictionaryapp.ui

import android.view.View
import studios.darkzen.dictionaryapp.R
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
import studios.darkzen.dictionaryapp.viewmodel.TodayQuizState
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment

@AndroidEntryPoint
class HomeFragment : CoreBaseFragment<FragmentHomeBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvQuizPacks.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun navigateToQuiz(packId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(packId)
        findNavController().navigate(action)
    }

    override fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe daily quiz
                launch {
                    viewModel.dailyQuiz.collectLatest { state ->
                        state?.let { updateTodayQuizCard(it) }
                    }
                }

                // Observe categories
                launch {
                    viewModel.categories.collectLatest { categories ->
                        if (categories.isNotEmpty()) {
                            binding.rvCategories.adapter = CategoryAdapter(categories) { category ->
                                val action = HomeFragmentDirections.actionHomeFragmentToQuizPackFragment(category.id)
                                findNavController().navigate(action)
                            }
                            
                            val allPacks = categories.flatMap { it.packs }
                            val adapter = QuizPackAdapter(allPacks, viewModel.allProgress.value) { pack ->
                                navigateToQuiz(pack.id)
                            }
                            binding.rvQuizPacks.adapter = adapter
                            
                            // Observe progress changes for full list
                            launch {
                                viewModel.allProgress.collectLatest { progressList ->
                                    adapter.updateData(allPacks, progressList)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateTodayQuizCard(state: TodayQuizState) {
        val pack = state.pack
        val progress = state.progress
        val totalQuestions = pack.questions.size

        binding.tvTodayQuizTitle.text = pack.name
        
        when {
            progress == null || (!progress.isCompleted && progress.answeredCount == 0) -> {
                binding.tvTodayQuizSet.text = "${state.categoryName} • $totalQuestions Questions"
                binding.btnStartTodayQuiz.text = getString(R.string.start_quiz)
                binding.btnStartTodayQuiz.visibility = View.VISIBLE
            }
            progress.isCompleted -> {
                binding.tvTodayQuizSet.text = getString(R.string.today_quiz_done)
                binding.btnStartTodayQuiz.text = getString(R.string.review_results)
                binding.btnStartTodayQuiz.visibility = View.VISIBLE
            }
            else -> {
                binding.tvTodayQuizSet.text = "${state.categoryName} • ${progress.answeredCount}/$totalQuestions completed"
                binding.btnStartTodayQuiz.text = getString(R.string.continue_quiz)
                binding.btnStartTodayQuiz.visibility = View.VISIBLE
            }
        }

        binding.btnStartTodayQuiz.setOnClickListener {
            navigateToQuiz(pack.id)
        }
    }
}
