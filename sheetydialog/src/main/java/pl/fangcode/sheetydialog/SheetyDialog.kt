package pl.fangcode.sheetydialog

import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment

open class SheetyDialog : DialogFragment() {

    private lateinit var backgroundInAnimation: Animation
    private lateinit var backgroundOutAnimation: Animation
    private lateinit var contentInAnimation: Animation
    private lateinit var contentOutAnimation: Animation

    private var hasContentShown = false

    private var dimAmount: Float = 0f

    private lateinit var statusBarAnimation: ValueAnimator

    override fun getTheme(): Int = R.style.SheetyDialogStyles

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dimAmount = ResourcesCompat.getFloat(resources, R.dimen.backgroundDimAmount)

        backgroundInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        backgroundOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        contentInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        contentOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down)

        contentInAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation?) {
                hasContentShown = true
            }
        })

        contentOutAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation?) {
                hasContentShown = false
                dismiss()
            }
        })

        statusBarAnimation = ValueAnimator.ofFloat(0f, dimAmount).apply {
            duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            addUpdateListener {
                updateStatusBarAlphaChannel(it.animatedValue as Float)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_sheety_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.let {
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setOnShowListener {
            dialog.findViewById<View>(R.id.background).startAnimation(backgroundInAnimation)
            dialog.findViewById<View>(R.id.contentContainer).startAnimation(contentInAnimation)
            statusBarAnimation.start()
        }

        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                close()
                true
            } else {
                false
            }
        }

        return dialog
    }

    /**
     * Dismisses dialog after animations.
     * Use this method instead of dismiss()
     */
    open fun close() {
        if (hasContentShown) {
            dialog?.findViewById<View>(R.id.background)?.startAnimation(backgroundOutAnimation)
            dialog?.findViewById<View>(R.id.contentContainer)?.startAnimation(contentOutAnimation)
            statusBarAnimation.reverse()
        }
    }

    private fun updateStatusBarAlphaChannel(alpha: Float) {
        dialog?.window?.statusBarColor = Color.argb(
            (255 * alpha).toInt(),
            0,
            0,
            0
        )
    }

}