package pl.fangcode.sheetydialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    private val sheetyDialog = SheetyDialog.Builder()
//        .setContentLayout(R.layout.view_content)
//        .build()

    private val sheetyDialog = SampleSheetyDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDialogButton.setOnClickListener {
            sheetyDialog.show(supportFragmentManager, "sheety-dialog")
        }

    }
}
