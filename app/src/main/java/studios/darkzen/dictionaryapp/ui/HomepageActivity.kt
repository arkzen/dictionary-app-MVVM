package studios.darkzen.dictionaryapp.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.common.core.CoreBaseActivity
import studios.darkzen.dictionaryapp.common.core.ResultState
import studios.darkzen.dictionaryapp.data.model.RootResponse
import studios.darkzen.dictionaryapp.databinding.ActivityHomepageBinding
import studios.darkzen.dictionaryapp.ui.adapter.MeaningAdapter
import studios.darkzen.dictionaryapp.viewmodel.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class HomepageActivity : CoreBaseActivity<ActivityHomepageBinding>() {

    private val viewModel: DictionaryViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun getViewBinding() = ActivityHomepageBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvMeaning.layoutManager = LinearLayoutManager(this)
        
        binding.btnSearch.setOnClickListener {
            val word = binding.etSearch.text.toString().trim()
            if (word.isNotEmpty()) {
                viewModel.getDefinition(word)
                hideKeyboard()
            }
        }
        
        val initialWord = intent.getStringExtra("word")
        if (initialWord != null) {
            binding.etSearch.setText(initialWord)
            viewModel.getDefinition(initialWord)
        }
    }

    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dictionaryState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            showLoader(true)
                        }
                        is ResultState.Success -> {
                            showLoader(false)
                            updateUI(state.data)
                        }
                        is ResultState.Error -> {
                            showLoader(false)
                            // Handle error (e.g., show toast)
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(response: RootResponse) {
        binding.tvWord.text = response.word
        binding.tvPhonetics.text = response.phonetics?.firstOrNull { !it.text.isNullOrEmpty() }?.text ?: ""
        
        val meanings = response.meanings ?: emptyList()
        binding.rvMeaning.adapter = MeaningAdapter(meanings)
        
        val audioUrl = response.phonetics?.firstOrNull { !it.audio.isNullOrEmpty() }?.audio
        if (!audioUrl.isNullOrEmpty()) {
            binding.btnAudioplay.visibility = View.VISIBLE
            binding.btnAudioplay.setOnClickListener {
                playAudio(audioUrl)
            }
        } else {
            binding.btnAudioplay.visibility = View.GONE
        }
        
        val source = response.sourceUrls?.firstOrNull()
        if (!source.isNullOrEmpty()) {
            val linkText = "<a href=\"$source\">$source</a>"
            binding.tvSlink.text = HtmlCompat.fromHtml(linkText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvSlink.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun playAudio(url: String) {
        if (isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPlaying = false
            binding.btnAudioplay.setImageResource(R.drawable.ic_playbtn)
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            try {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    this@HomepageActivity.isPlaying = true
                    binding.btnAudioplay.setImageResource(R.drawable.ic_pause)
                }
                setOnCompletionListener {
                    this@HomepageActivity.isPlaying = false
                    binding.btnAudioplay.setImageResource(R.drawable.ic_playbtn)
                    release()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
