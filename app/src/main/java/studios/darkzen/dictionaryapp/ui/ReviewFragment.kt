package studios.darkzen.dictionaryapp.ui

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentReviewBinding
import studios.darkzen.dictionaryapp.ui.adapter.ReviewAdapter
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel

@AndroidEntryPoint
class ReviewFragment : CoreBaseFragment<FragmentReviewBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()
    private val args: ReviewFragmentArgs by navArgs()

    override fun getViewBinding() = FragmentReviewBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        
        val pack = viewModel.getPackById(args.packId)
        pack?.let {
            binding.rvReview.adapter = ReviewAdapter(it.questions)
        }
    }
}
