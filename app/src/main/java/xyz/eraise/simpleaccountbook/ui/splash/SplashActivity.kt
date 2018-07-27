package xyz.eraise.simpleaccountbook.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.ui.tally.add.AddTallyActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, AddTallyActivity::class.java))
        finish()
    }

}
