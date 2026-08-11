package studios.darkzen.dictionaryapp.ui

import dagger.hilt.android.AndroidEntryPoint
import studios.darkzen.dictionaryapp.common.core.CoreBaseFragment
import studios.darkzen.dictionaryapp.databinding.FragmentPrivacyBinding

@AndroidEntryPoint
class PrivacyFragment : CoreBaseFragment<FragmentPrivacyBinding>() {
    override fun getViewBinding() = FragmentPrivacyBinding.inflate(layoutInflater)

    override fun setupUI() {
        // Privacy Policy page setup
    }
}
