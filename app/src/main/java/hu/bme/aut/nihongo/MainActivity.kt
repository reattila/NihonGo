package hu.bme.aut.nihongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kanji_listbt.setOnClickListener{
            startActivity(Intent(this, KanjiListActivity::class.java))
        }

        practicebt.setOnClickListener{
            startActivity(Intent(this, PracticeActivity::class.java))
        }

    }
}