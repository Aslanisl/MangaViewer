package ru.mail.aslanisl.mangareader.features.genre

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.getColorCompat

class FilterTextView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val textSelectedColor by lazy { context.getColorCompat(R.color.white) }
    private val textUnselectedColor by lazy { context.getColorCompat(R.color.orange) }

    
}