package hu.bme.aut.nihongo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.nihongo.R
import hu.bme.aut.nihongo.data.KanjiItem

class KanjiAdapter(private val listener: KanjiClickListener) :
    RecyclerView.Adapter<KanjiAdapter.KanjiViewHolder>() {

    private val items = mutableListOf<KanjiItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KanjiViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.kanji_list, parent, false)
        return KanjiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KanjiViewHolder, position: Int) {
        val item = items[position]
        holder.kanjiTextView.text = item.kanji
        holder.hiraganaTextView.text = item.hiragana
        holder.meaningTextView.text = item.meaning

        holder.item = item
    }

    fun addItem(item: KanjiItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(kanjiItems: List<KanjiItem>) {
        items.clear()
        items.addAll(kanjiItems)
        notifyDataSetChanged()
    }

    fun delete(kanjiItems: List<KanjiItem>){
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface KanjiClickListener {
        fun onItemChanged(item: KanjiItem)
        fun onItemDeleted(item: KanjiItem)
    }


    inner class KanjiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kanjiTextView: TextView
        val hiraganaTextView: TextView
        val meaningTextView: TextView
        val removeButton: ImageButton

        var item: KanjiItem? = null

        init {
            kanjiTextView = itemView.findViewById(R.id.KanjiTextView)
            hiraganaTextView = itemView.findViewById(R.id.HiraganaTextView)
            meaningTextView = itemView.findViewById(R.id.MeaningTextView)
            removeButton = itemView.findViewById(R.id.KanjiRemoveButton)
            removeButton.setOnClickListener{
                item?.let {
                    val newItem = it.copy()
                    item = newItem
                    listener.onItemDeleted(newItem)
                }
            }
        }
    }
}