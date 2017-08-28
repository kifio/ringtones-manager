package kifio.ringtones

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        pushFragment(SettingsFragment(), null)
    }

    fun pushFragment(f: Fragment, title: CharSequence?) {
        setTitle(title)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, f)
                .addToBackStack(f.javaClass.simpleName)
                .commit()
    }

}
