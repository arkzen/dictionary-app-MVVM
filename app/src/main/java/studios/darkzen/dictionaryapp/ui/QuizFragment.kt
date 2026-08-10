package studios.darkzen.dictionaryapp.ui

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import androidx.lifecycle.lifecycleScope
import studios.darkzen.dictionaryapp.data.local.entity.QuizAnswerEntity
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity
import studios.darkzen.dictionaryapp.data.model.Question
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.databinding.FragmentQuizBinding
import studios.darkzen.dictionaryapp.databinding.ItemOptionBinding
import studios.darkzen.dictionaryapp.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizFragment : CoreBaseFragment<FragmentQuizBinding>() {

    private val viewModel: QuizViewModel by activityViewModels()
    private val args: QuizFragmentArgs by navArgs()

    private var currentPack: QuizPack? = null
    private var currentQuestionIndex = 0
    private var selectedOptionIndex = -1
    private var score = 0
    private var isResuming = false

    override fun getViewBinding() = FragmentQuizBinding.inflate(layoutInflater)

    override fun setupUI() {
        currentPack = viewModel.getPackById(args.packId)
        
        viewLifecycleOwner.lifecycleScope.launch {
            val progress = viewModel.getProgressById(args.packId)
            if (progress != null) {
                if (progress.isCompleted) {
                    navigateToResult(progress.lastScore, currentPack?.questions?.size ?: 0)
                    return@launch
                } else {
                    // Resume from next question
                    currentQuestionIndex = progress.answeredCount
                    score = progress.correctCount
                    isResuming = true
                }
            }
            showQuestion()
        }

        binding.btnSubmit.setOnClickListener {
            if (selectedOptionIndex != -1) {
                checkAnswer()
            }
        }

        binding.btnNext.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < (currentPack?.questions?.size ?: 0)) {
                showQuestion()
            } else {
                finishQuiz()
            }
        }
    }

    private fun finishQuiz() {
        val total = currentPack?.questions?.size ?: 0
        viewLifecycleOwner.lifecycleScope.launch {
            val progress = viewModel.getProgressById(args.packId)
            val bestScore = maxOf(score, progress?.bestScore ?: 0)
            
            viewModel.saveProgress(QuizProgressEntity(
                packId = args.packId,
                currentQuestionIndex = currentQuestionIndex,
                answeredCount = total,
                correctCount = score,
                isCompleted = true,
                lastScore = score,
                bestScore = bestScore
            ))
            
            navigateToResult(score, total)
        }
    }

    private fun navigateToResult(score: Int, total: Int) {
        val action = QuizFragmentDirections.actionQuizFragmentToResultFragment(
            score,
            total,
            args.packId
        )
        findNavController().navigate(action)
    }

    private fun showQuestion() {
        val question = currentPack?.questions?.get(currentQuestionIndex) ?: return
        selectedOptionIndex = -1

        binding.tvQuestionCount.text = "Question ${currentQuestionIndex + 1} of ${currentPack?.questions?.size}"
        binding.progressBar.max = currentPack?.questions?.size ?: 0
        binding.progressBar.progress = currentQuestionIndex + 1
        binding.tvQuestionText.text = question.questionText
        
        if (!question.hookText.isNullOrEmpty()) {
            binding.tvHookText.text = question.hookText
            binding.tvHookText.visibility = View.VISIBLE
        } else {
            binding.tvHookText.visibility = View.GONE
        }

        binding.optionsContainer.removeAllViews()
        question.options.forEachIndexed { index, optionText ->
            val optionBinding = ItemOptionBinding.inflate(LayoutInflater.from(requireContext()), binding.optionsContainer, false)
            optionBinding.tvOptionIndex.text = ('A' + index).toString()
            optionBinding.tvOptionText.text = optionText
            
            optionBinding.root.setOnClickListener {
                if (binding.btnSubmit.visibility == View.VISIBLE) {
                    selectOption(index)
                }
            }
            binding.optionsContainer.addView(optionBinding.root)
        }

        binding.explanationScroll.visibility = View.GONE
        binding.btnSubmit.visibility = View.VISIBLE
        binding.btnNext.visibility = View.GONE
        binding.optionsContainer.visibility = View.VISIBLE
    }

    private fun selectOption(index: Int) {
        selectedOptionIndex = index
        for (i in 0 until binding.optionsContainer.childCount) {
            val child = binding.optionsContainer.getChildAt(i)
            child.findViewById<View>(R.id.container).isSelected = (i == index)
            child.findViewById<TextView>(R.id.tvOptionIndex).isSelected = (i == index)
        }
    }

    private fun checkAnswer() {
        val question = currentPack?.questions?.get(currentQuestionIndex) ?: return
        val isCorrect = selectedOptionIndex == question.correctAnswerIndex
        if (isCorrect) score++

        // Save progress to Room
        viewModel.saveAnswer(QuizAnswerEntity(
            packId = args.packId,
            questionId = question.id,
            selectedAnswerIndex = selectedOptionIndex,
            isCorrect = isCorrect
        ))
        
        viewModel.saveProgress(QuizProgressEntity(
            packId = args.packId,
            currentQuestionIndex = currentQuestionIndex, // Current being answered
            answeredCount = currentQuestionIndex + 1,
            correctCount = score,
            isCompleted = false
        ))

        binding.btnSubmit.visibility = View.GONE
        binding.btnNext.visibility = View.VISIBLE
        binding.explanationScroll.visibility = View.VISIBLE
        binding.optionsContainer.visibility = View.GONE

        // Update Explanation UI
        if (isCorrect) {
            binding.cardResultStatus.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.quiz_accent_green))
            binding.ivResultIcon.setImageResource(R.drawable.ic_check_circle)
            binding.tvResultStatus.text = "Correct!"
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.quiz_correct))
        } else {
            binding.cardResultStatus.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.quiz_accent_red))
            binding.ivResultIcon.setImageResource(R.drawable.ic_sad) // Using ic_sad from existing resources
            binding.tvResultStatus.text = "Incorrect"
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.quiz_incorrect))
        }

        binding.tvCorrectAnswerText.text = question.options[question.correctAnswerIndex]
        binding.tvExplanation.text = question.explanationBn
        binding.tvExampleSentence.text = question.exampleSentence
    }
}
