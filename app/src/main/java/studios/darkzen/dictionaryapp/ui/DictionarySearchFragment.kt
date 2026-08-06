package studios.darkzen.dictionaryapp.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import studios.darkzen.dictionaryapp.R
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.common.core.ResultState
import studios.darkzen.dictionaryapp.data.model.RootResponse
import studios.darkzen.dictionaryapp.databinding.FragmentDictionarySearchBinding
import studios.darkzen.dictionaryapp.ui.adapter.MeaningAdapter
import studios.darkzen.dictionaryapp.viewmodel.DictionaryViewModel
import java.io.IOException

@AndroidEntryPoint
class DictionarySearchFragment : CoreBaseFragment<FragmentDictionarySearchBinding>() {

    private val viewModel: DictionaryViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun getViewBinding() = FragmentDictionarySearchBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.rvMeaning.layoutManager = LinearLayoutManager(requireContext())
        
        binding.btnSearch.setOnClickListener {
            val word = binding.etSearch.text.toString().trim()
            if (word.isNotEmpty()) {
                viewModel.getDefinition(word)
                hideKeyboard()
            }
        }
    }

    override fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dictionaryState.collectLatest { state ->
                    when (state) {
                        is ResultState.Idle -> {
                            showLoader(false)
                        }
                        is ResultState.Loading -> {
                            showLoader(true)
                        }
                        is ResultState.Success -> {
                            showLoader(false)
                            updateUI(state.data)
                        }
                        is ResultState.Error -> {
                            showLoader(false)
                            android.widget.Toast.makeText(requireContext(), "Error: ${state.error.message ?: "Unknown error"}", android.widget.Toast.LENGTH_SHORT).show()
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
                    this@DictionarySearchFragment.isPlaying = true
                    binding.btnAudioplay.setImageResource(R.drawable.ic_pause)
                }
                setOnCompletionListener {
                    this@DictionarySearchFragment.isPlaying = false
                    binding.btnAudioplay.setImageResource(R.drawable.ic_playbtn)
                    release()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
    }
}
