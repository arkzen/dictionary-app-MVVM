package studios.darkzen.dictionaryapp.ui

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentResultBinding

@AndroidEntryPoint
class ResultFragment : CoreBaseFragment<FragmentResultBinding>() {

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

        binding.btnReview.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToReviewFragment(args.packId)
            findNavController().navigate(action)
        }

        binding.btnTryAgain.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToReviewFragment(args.packId) // Reusing navigation logic or popping back
            findNavController().popBackStack() // Go back to Quiz
        }
    }
}
