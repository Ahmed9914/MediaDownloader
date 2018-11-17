package nanodegree.udacity.media_downloader

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {


    lateinit var editTextView:EditText
    private lateinit var executeButton:Button
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:DownloadAdapter
    private lateinit var loadingIndicator:ProgressBar
    private lateinit var errorMessage:TextView

    private var viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views init
        editTextView = findViewById(R.id.edit_text_view)
        executeButton = findViewById(R.id.check_button)
        recyclerView = findViewById(R.id.recyclerview)
        loadingIndicator = findViewById(R.id.loading_indicator)
        errorMessage = findViewById(R.id.error_message_display_tv)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        adapter = DownloadAdapter(this)
        recyclerView.adapter = adapter

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val intent:Intent? = intent
        val action:String? = intent?.action
        val type:String? = intent?.type
        if (Intent.ACTION_SEND == action && "text/plain" == type) {
            editTextView?.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
        }

        val factory = InjectorUtils.provideMainActivityViewModelFactory(this.applicationContext)
        viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel::class.java)
        viewModel!!.availableDownloads.observe(this, Observer {
            if (it != null) {
                errorMessage.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.downloads = it
            } else showErrorMessage()})

        executeButton.setOnClickListener{
            val layout:ConstraintLayout = findViewById(R.id.layout)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(layout.windowToken, 0 )
            if (editTextView.text.toString() != "") {
                errorMessage.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
                val link:String = editTextView.text.toString()
                viewModel!!.retrieveLink(link)
            }
            }
        }

    private fun showErrorMessage(){
        errorMessage.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}





