package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.data.model.QuizPack
import studios.darkzen.dictionaryapp.databinding.ItemQuizPackBinding

class QuizPackAdapter(
    private val packs: List<QuizPack>,
    private val onClick: (QuizPack) -> Unit
) : RecyclerView.Adapter<QuizPackAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemQuizPackBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuizPackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pack = packs[position]
        holder.binding.tvName.text = pack.name
        holder.binding.tvDesc.text = "${pack.questionCount} Questions"
        
        // Icon logic can be based on category or pack type if needed
        holder.binding.ivIcon.setImageResource(R.drawable.ic_book)
        
        holder.itemView.setOnClickListener { onClick(pack) }
    }

    override fun getItemCount() = packs.size
}
