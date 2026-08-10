package studios.darkzen.dictionaryapp.ui

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentResultBinding
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel

@AndroidEntryPoint
class ResultFragment : CoreBaseFragment<FragmentResultBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()
    private val args: ResultFragmentArgs by navArgs()

    override fun getViewBinding() = FragmentResultBinding.inflate(layoutInflater)

    override fun setupUI() {
        val score = args.score
        val total = args.total
        val incorrect = total - score
        val accuracy = if (total > 0) (score * 100) / total else 0

        binding.tvScore.text = "$score/$total"
        binding.tvCorrectCount.text = score.toString()
        binding.tvIncorrectCount.text = incorrect.toString()
        binding.tvAccuracy.text = "$accuracy%"

        // Show random motivational quote
        val quote = viewModel.getRandomCompletionQuote()
        binding.tvResultQuote.text = quote?.en

        binding.btnReview.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToReviewFragment(args.packId)
            findNavController().navigate(action)
        }

        binding.btnTryAgain.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.resetPackProgress(args.packId, score)
                val action = ResultFragmentDirections.actionResultFragmentToQuizFragment(args.packId)
                findNavController().navigate(action)
            }
        }
    }
}
