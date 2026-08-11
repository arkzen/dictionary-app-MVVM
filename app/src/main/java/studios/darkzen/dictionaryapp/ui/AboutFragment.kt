package studios.darkzen.dictionaryapp.ui

import android.content.Intent
import android.net.Uri
import dagger.hilt.android.AndroidEntryPoint
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentAboutBinding

@AndroidEntryPoint
class AboutFragment : CoreBaseFragment<FragmentAboutBinding>() {
    override fun getViewBinding() = FragmentAboutBinding.inflate(layoutInflater)

    override fun setupUI() {
        binding.btnYoutube.setOnClickListener {
            openUrl("https://www.youtube.com/@learnwithwordsense")
        }
        binding.btnFacebook.setOnClickListener {
            openUrl("https://www.facebook.com/learnwithwordsense/")
        }
        binding.btnInstagram.setOnClickListener {
            openUrl("https://www.instagram.com/learnwithwordsense/")
        }
        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:studiodarkzen@gmail.com")
            }
            startActivity(intent)
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
