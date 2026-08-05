package studios.darkzen.dictionaryapp.common.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import studios.darkzen.dictionaryapp.common.customview.LoaderDialogFragment

abstract class CoreBaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB
    private var loaderDialogFragment: LoaderDialogFragment? = null

    abstract fun getViewBinding(): VB
    protected open fun setupObserver() {}
    protected open fun initializeData() {}
    protected abstract fun setupUI()
    protected open fun callInitialApi() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initializeData()
        setupObserver()
        setupUI()
        callInitialApi()
    }

    fun showLoader(show: Boolean, isCancelable: Boolean = false) {
        if (show) {
            if (loaderDialogFragment?.isVisible == true) return
            loaderDialogFragment = LoaderDialogFragment().apply {
                this.isCancelable = isCancelable
            }
            loaderDialogFragment?.show(supportFragmentManager, "LoaderDialogFragment")
        } else {
            loaderDialogFragment?.dismissAllowingStateLoss()
            loaderDialogFragment = null
        }
    }
}
