package studios.darkzen.dictionaryapp.ui

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import studios.darkzen.dictionaryapp.common.core.CoreBaseActivity
import studios.darkzen.dictionaryapp.common.core.ResultState
import studios.darkzen.dictionaryapp.databinding.ActivityMainBinding
import studios.darkzen.dictionaryapp.viewmodel.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : CoreBaseActivity<ActivityMainBinding>() {

    private val viewModel: DictionaryViewModel by viewModels()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.btnSearch.setOnClickListener {
            val word = binding.etSearch.text.toString().trim()
            if (word.isNotEmpty()) {
                val intent = Intent(this, HomepageActivity::class.java)
                intent.putExtra("word", word)
                startActivity(intent)
            }
        }
    }

    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dictionaryState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            // Show loading
                        }
                        is ResultState.Success -> {
                            // Handle success
                        }
                        is ResultState.Error -> {
                            // Handle error
                        }
                    }
                }
            }
        }
    }
}
