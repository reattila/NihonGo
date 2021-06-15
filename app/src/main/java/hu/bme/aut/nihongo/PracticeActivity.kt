package hu.bme.aut.nihongo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.nihongo.data.KanjiItem
import hu.bme.aut.nihongo.data.KanjiListDatabase
import kotlinx.android.synthetic.main.activity_practice.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread
import kotlin.random.Random

class PracticeActivity : AppCompatActivity() {

    private lateinit var database: KanjiListDatabase
    private var kanjiList: List<KanjiItem> = emptyList()
    private lateinit var buttons: List<Button>
    private var selectedkanjiindex = 0
    private var randombutton = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        database = Room.databaseBuilder(
            applicationContext,
            KanjiListDatabase::class.java,
            "kanji-list"
        ).build()
        loadItemsInBackground()
        buttons = listOf(kanji1,kanji2,kanji4,kanji3)

        startbt.setOnClickListener{
            if(kanjiList.size < 8){
                Snackbar.make(practiceActivity_layout,R.string.warn_message,Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            currentkanjiTextView.visibility = View.VISIBLE
            frame_image.visibility = View.VISIBLE
            for(button in buttons){
                button.visibility = View.VISIBLE
            }
            startbt.visibility = View.INVISIBLE

            val randomnum = Random.nextInt(kanjiList.size)
            selectedkanjiindex = randomnum
            randombutton = 0;
            selectCorrectKanji()
        }

        //All button init
        for(button in buttons){
            setButtonKanji(button)
        }
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.kanjiItemDao().getAll()
            runOnUiThread {
                kanjiList = items
            }
        }
    }

    //A válasz helyeségére válaszol a button
    private fun setButtonKanji(button: Button){
        button.setOnClickListener{
            if(button.text.equals(kanjiList[selectedkanjiindex].hiragana)){
                button.setBackgroundResource(R.drawable.hiragana_good_button)

                var mediaplayer = MediaPlayer.create(this,R.raw.correct_answer)
                mediaplayer.start()
                Timer("Wait",false).schedule(800){
                    runOnUiThread {
                        button.setBackgroundResource(R.drawable.hiragana_normal_button)
                        selectedkanjiindex++
                        selectCorrectKanji()
                    }
                }
            }else{
                buttons[randombutton].setBackgroundResource(R.drawable.hiragana_good_button)
                button.setBackgroundResource(R.drawable.hiragana_wrong_button)

                Timer("Wait",false).schedule(1000){
                    runOnUiThread {
                        buttons[randombutton].setBackgroundResource(R.drawable.hiragana_normal_button)
                        button.setBackgroundResource(R.drawable.hiragana_normal_button)
                        selectedkanjiindex++
                        selectCorrectKanji()
                    }
                }
            }
        }
    }

    //egy random kanjit választunk ki az kanji listából
    private fun selectCorrectKanji(){
        val randomnum = Random.nextInt(5)
        selectedkanjiindex += randomnum
        if(kanjiList.size <= selectedkanjiindex){
            selectedkanjiindex = 0
            selectedkanjiindex += randomnum
        }

        randombutton = Random.nextInt(4)
        buttons[randombutton].text = kanjiList[selectedkanjiindex].hiragana
        currentkanjiTextView.text = kanjiList[selectedkanjiindex].kanji

        randomButtonText()
    }

    //véletlenszerűen a gombok feliratát megváltoztajuk
    private fun randomButtonText(){
        var rannum = 0
        for (i in 0..3) {
            if(i == randombutton){
                continue
            }
            rannum = randomKanji()

            buttons[i].text = kanjiList[rannum].hiragana
        }
    }

    private fun randomKanji() : Int{
        var randomKanjiindex = 0
        val randomnum = Random.nextInt(7)
        randomKanjiindex += selectedkanjiindex + randomnum + 1
        if(kanjiList.size <= randomKanjiindex){
            randomKanjiindex = 0
            randomKanjiindex += randomnum
            if(randomKanjiindex == selectedkanjiindex){
                if(kanjiList.size >= randomKanjiindex) {
                    randomKanjiindex++
                }else{
                    randomKanjiindex = 0
                }
            }
        }
        return randomKanjiindex
    }

}