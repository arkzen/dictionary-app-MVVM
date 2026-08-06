package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.data.model.Question
import studios.darkzen.dictionaryapp.databinding.ItemReviewBinding

class ReviewAdapter(
    private val questions: List<Question>
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        holder.binding.tvQuestion.text = "${position + 1}. ${question.questionText}"
        holder.binding.tvCorrectAnswer.text = question.options[question.correctAnswerIndex]
        holder.binding.tvExplanation.text = question.explanationBn
    }

    override fun getItemCount() = questions.size
}
