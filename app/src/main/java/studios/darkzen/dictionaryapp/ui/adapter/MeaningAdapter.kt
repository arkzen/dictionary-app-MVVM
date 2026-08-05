package studios.darkzen.dictionaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studios.darkzen.dictionaryapp.data.model.Meanings
import studios.darkzen.dictionaryapp.databinding.MeaningListBinding

class MeaningAdapter(private val meanings: List<Meanings>) :
    RecyclerView.Adapter<MeaningAdapter.ViewHolder>() {

    class ViewHolder(val binding: MeaningListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MeaningListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meaning = meanings[position]
        with(holder.binding) {
            tvPartsofSpeach.text = meaning.partOfSpeech
            
            rvMeaningDefinition.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DefinitionAdapter(meaning.definitions ?: emptyList())
            }
        }
    }

    override fun getItemCount() = meanings.size
}
