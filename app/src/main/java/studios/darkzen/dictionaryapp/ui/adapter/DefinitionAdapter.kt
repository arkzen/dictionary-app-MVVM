package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.data.model.Definitions
import studios.darkzen.dictionaryapp.databinding.DefinitionListBinding

class DefinitionAdapter(private val definitions: List<Definitions>) :
    RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    class ViewHolder(val binding: DefinitionListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DefinitionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definition = definitions[position]
        with(holder.binding) {
            tvMeaning.text = definition.definition
            
            if (definition.example != null) {
                tvExample.text = "\"${definition.example}\""
                tvExample.visibility = View.VISIBLE
            } else {
                tvExample.visibility = View.GONE
            }

            if (!definition.synonyms.isNullOrEmpty()) {
                synTextItem.text = definition.synonyms.joinToString(", ")
                tvSynonyms.visibility = View.VISIBLE
                synTextItem.visibility = View.VISIBLE
            } else {
                tvSynonyms.visibility = View.GONE
                synTextItem.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = definitions.size
}
