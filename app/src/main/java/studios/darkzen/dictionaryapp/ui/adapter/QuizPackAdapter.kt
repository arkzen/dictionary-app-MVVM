package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.data.local.entity.QuizProgressEntity
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.databinding.ItemQuizPackBinding

class QuizPackAdapter(
    private var packs: List<QuizPack>,
    private var progressList: List<QuizProgressEntity> = emptyList(),
    private val onClick: (QuizPack) -> Unit
) : RecyclerView.Adapter<QuizPackAdapter.ViewHolder>() {

    fun updateData(newPacks: List<QuizPack>, newProgressList: List<QuizProgressEntity>) {
        this.packs = newPacks
        this.progressList = newProgressList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemQuizPackBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuizPackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pack = packs[position]
        val progress = progressList.find { it.packId == pack.id }
        
        holder.binding.tvName.text = pack.name
        val totalQuestions = pack.questions.size

        when {
            progress == null || (!progress.isCompleted && progress.answeredCount == 0) -> {
                holder.binding.tvDesc.text = "$totalQuestions Questions"
                holder.binding.tvDesc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.quiz_text_secondary))
            }
            progress.isCompleted -> {
                val accuracy = if (totalQuestions > 0) (progress.lastScore * 100) / totalQuestions else 0
                holder.binding.tvDesc.text = "✓ Completed • ${progress.lastScore}/$totalQuestions • $accuracy%"
                holder.binding.tvDesc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.quiz_correct))
            }
            else -> {
                holder.binding.tvDesc.text = "${progress.answeredCount}/$totalQuestions completed • Continue Quiz"
                holder.binding.tvDesc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.quiz_primary))
            }
        }
        
        // Icon logic based on category ID from pack ID prefix
        val iconRes = when {
            pack.id.startsWith("mistakes") -> R.drawable.ic_error
            pack.id.startsWith("sentences") -> R.drawable.ic_chat
            pack.id.startsWith("vocab") -> R.drawable.ic_book
            else -> R.drawable.ic_book
        }
        holder.binding.ivIcon.setImageResource(iconRes)
        
        holder.itemView.setOnClickListener { onClick(pack) }
    }

    override fun getItemCount() = packs.size
}
