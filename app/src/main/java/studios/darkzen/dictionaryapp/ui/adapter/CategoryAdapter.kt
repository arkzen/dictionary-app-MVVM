package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.data.model.QuizCategory
import studios.darkzen.dictionaryapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<QuizCategory>,
    private val onClick: (QuizCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.tvName.text = category.name
        holder.binding.tvDesc.text = category.description
        
        val iconRes = when (category.iconPlaceholder) {
            "ic_error" -> R.drawable.ic_error
            "ic_chat" -> R.drawable.ic_chat
            "ic_book" -> R.drawable.ic_book
            else -> R.drawable.ic_error
        }
        holder.binding.ivIcon.setImageResource(iconRes)
        
        val bgColor = when (category.id) {
            "cat_mistakes" -> R.color.quiz_accent_red
            "cat_sentences" -> R.color.quiz_accent_green
            "cat_vocab" -> R.color.quiz_accent_purple
            else -> R.color.quiz_accent_purple
        }
        holder.binding.container.setBackgroundResource(bgColor)
        
        holder.itemView.setOnClickListener { onClick(category) }
    }

    override fun getItemCount() = categories.size
}
