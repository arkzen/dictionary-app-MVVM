package studios.darkzen.dictionaryapp.common.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import studios.darkzen.dictionaryapp.common.customview.LoaderDialogFragment

abstract class CoreBaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    private var loaderDialogFragment: LoaderDialogFragment? = null

    abstract fun getViewBinding(): VB
    protected open fun setupObserver() {}
    protected open fun initializeData() {}
    protected abstract fun setupUI()
    protected open fun callInitialApi() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData()
        setupObserver()
        setupUI()
        callInitialApi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoader(show: Boolean, isCancelable: Boolean = false) {
        if (show) {
            if (loaderDialogFragment?.isVisible == true) return
            loaderDialogFragment = LoaderDialogFragment().apply {
                this.isCancelable = isCancelable
            }
            loaderDialogFragment?.show(childFragmentManager, "LoaderDialogFragment")
        } else {
            loaderDialogFragment?.dismissAllowingStateLoss()
            loaderDialogFragment = null
        }
    }
}
