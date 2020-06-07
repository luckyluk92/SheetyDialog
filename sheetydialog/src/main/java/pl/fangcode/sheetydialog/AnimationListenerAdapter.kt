package pl.fangcode.sheetydialog

import android.view.animation.Animation

abstract class AnimationListenerAdapter : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

    abstract override fun onAnimationEnd(animation: Animation?)

}