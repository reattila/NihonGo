package hu.bme.aut.nihongo.fragments

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.bme.aut.nihongo.R
import hu.bme.aut.nihongo.data.KanjiItem

class NewKanjiItemDialogFragment : DialogFragment() {
    interface NewKanjiItemDialogListener {
        fun onKanjiItemCreated(newItem: KanjiItem)
    }

    private lateinit var kanjiEditText: EditText
    private lateinit var hiraganaEditText: EditText
    private lateinit var meaningEditText: EditText

    private lateinit var listener: NewKanjiItemDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewKanjiItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewKanjiItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_kanji)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onKanjiItemCreated(getKanjiItem())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun isValid() = kanjiEditText.text.isNotEmpty() && hiraganaEditText.text.isNotEmpty() && meaningEditText.text.isNotEmpty()

    private fun getKanjiItem() = KanjiItem(
        id = null,
        kanji = kanjiEditText.text.toString(),
        hiragana = hiraganaEditText.text.toString(),
        meaning = meaningEditText.text.toString()
    )

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_kanji_item, null)
        kanjiEditText = contentView.findViewById(R.id.KanjiEditText)
        hiraganaEditText = contentView.findViewById(R.id.HiranganaEditText)
        meaningEditText = contentView.findViewById(R.id.MeaningEditText)
        return contentView
    }

    companion object {
        const val TAG = "NewKanjiItemDialogFragment"
    }
}