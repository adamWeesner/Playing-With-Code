package weesner.adam.playingwithcode

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import weesner.adam.playingwithcode.model.genericAlgorithms.AllONesGA
import weesner.adam.playingwithcode.ui.genericAlgorithms.GenericGA
import weesner.adam.playingwithcode.ui.visualizations.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        content_main.removeAllViews()
        when (item.itemId) {
            R.id.nav_nn_all_ones -> {
                GenericGA().create(content_main, AllONesGA(100, .001, .95, 2))
            }
            R.id.nav_circle -> content_main.addView(TouchableCircleCanvas(this))
            R.id.nav_koch_line -> content_main.addView(KochLineCanvas(this))
            R.id.nav_barnsley_fern -> content_main.addView(BarnsleyFernCanvas(this))
            R.id.nav_sunflower_math -> content_main.addView(SunflowerMathCanvas(this))
            R.id.nav_fibonacci_sequence -> content_main.addView(FibonacciSequenceCanvas(this))
            R.id.nav_conways_game_of_life -> ConwaysGameOfLife().create(content_main)
        }

        title = item.title
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
