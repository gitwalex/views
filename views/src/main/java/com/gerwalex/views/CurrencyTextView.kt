package com.gerwalex.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.FragmentManager
import com.maltaisn.calcdialog.CalcDialog
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow

/**
 * Zeigt einen Betrag in der jeweiligen Waehrung an. Als Defult wird bei negativen Werten der Text
 * in rot gezeigt.
 */
@Deprecated("Ersetzt durch CurrencyCalculatorView", ReplaceWith("CurrencyCalculatorView"))

class CurrencyTextView : AppCompatTextView {
    private val units = BigDecimal.valueOf(10.0.pow(Currency.getInstance(Locale.getDefault()).defaultFractionDigits.toDouble()))
    private var colorMode = false
    private var defaultColor = 0
    private var mBindingListener: InverseBindingListener? = null
    var value = BigDecimal(0)
        set(value) {
            if (field != value) {
                field = value
                mBindingListener?.onChange()
                if (colorMode and (value < BigDecimal.ZERO))
                    setTextColor(Color.RED) else setTextColor(defaultColor)
                setCurrencyText()
            }
        }
    private val calc = CalcDialog().apply {
        settings.apply {
            numberFormat = NumberFormat.getCurrencyInstance()
            callback = CalcDialog.CalcDialogCallback { _, value ->
                this@CurrencyTextView.value = value ?: BigDecimal(0)
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun getAsLongValue(): Long {
        return value.multiply(units).toLong()
    }

    fun showCalculator(fragmentManager: FragmentManager) {
        calc.settings.initialValue = value
        calc.show(fragmentManager, null)

    }

    fun setValue(amount: Long) {
        value = (BigDecimal(amount).divide(units))
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.CurrencyTextView).use { a ->
            if (isInEditMode) {
                value = BigDecimal(123456789L)
            }
            colorMode = a.getBoolean(R.styleable.CurrencyTextView_colorMode, true)
            defaultColor = currentTextColor
            gravity = Gravity.END
            setEms(7)
            isFocusable = false
            isCursorVisible = false
            setCurrencyText()
        }
    }

    private fun setCurrencyText() {
        text = NumberFormat.getCurrencyInstance().format(value)
    }

    fun postValue(value: Long) {
        post {
            setValue(value)
        }
    }

    companion object {
        @JvmStatic
        @InverseBindingAdapter(attribute = "value")
        fun getBigDecimal(view: CurrencyCalculatorView): BigDecimal {
            return view.value
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "value")
        fun getAsLongValue(view: CurrencyCalculatorView): Long {
            return view.getAsLongValue()
        }

        @JvmStatic
        @BindingAdapter(value = ["value", "valueAttrChanged"], requireAll = false)
        fun setBigDecimal(view: CurrencyCalculatorView, value: BigDecimal, listener: InverseBindingListener?) {
            view.mBindingListener = listener
            view.value = value
        }

        @JvmStatic
        @BindingAdapter(value = ["value", "valueAttrChanged"], requireAll = false)
        fun setValue(view: CurrencyCalculatorView, value: Long, listener: InverseBindingListener?) {
            view.mBindingListener = listener
            view.setValue(value)
        }
    }
}