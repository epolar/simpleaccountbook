package xyz.eraise.simpleaccountbook.ui.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.ui.tally.add.AddTallyActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, AddTallyActivity::class.java))
        finish()
    }

}
