package ru.mail.aslanisl.mangareader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ru.mail.aslanisl.mangareader.di.component.AppComponent
import ru.mail.aslanisl.mangareader.di.factory.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject protected lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI((application as App).appComponent)
    }

    abstract fun injectDI(appComponent: AppComponent)

    protected inline fun <reified T : ViewModel> initViewModel() = getViewModel<T>(viewModelFactory)
}