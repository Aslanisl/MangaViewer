package ru.mail.aslanisl.mangareader.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.features.base.BaseFragment
import ru.mail.aslanisl.mangareader.features.dialog.okButton
import ru.mail.aslanisl.mangareader.features.dialog.showDialog

class SettingsFragment : BaseFragment() {

    companion object {
        val TAG = SettingsFragment::class.java.simpleName

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        settingsClearHistory.setOnClickListener {
            contextNotNull.showDialog(R.string.sure_clear_history) {
                okButton { viewModel.clearHistory() }
            }
        }
    }
}