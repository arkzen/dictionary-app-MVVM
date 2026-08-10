package studios.darkzen.dictionaryapp.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentQuizPackBinding
import studios.darkzen.dictionaryapp.ui.adapter.QuizPackAdapter
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel

@AndroidEntryPoint
class QuizPackFragment : CoreBaseFragment<FragmentQuizPackBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()
    private val args: QuizPackFragmentArgs by navArgs()

    override fun getViewBinding() = FragmentQuizPackBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvPacks.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val category = viewModel.getCategoryById(args.categoryId)
                category?.let { cat ->
                    val adapter = QuizPackAdapter(cat.packs, viewModel.allProgress.value) { pack ->
                        val action = QuizPackFragmentDirections.actionQuizPackFragmentToQuizFragment(pack.id)
                        findNavController().navigate(action)
                    }
                    binding.rvPacks.adapter = adapter

                    viewModel.allProgress.collectLatest { progressList ->
                        adapter.updateData(cat.packs, progressList)
                    }
                }
            }
        }
    }
}
