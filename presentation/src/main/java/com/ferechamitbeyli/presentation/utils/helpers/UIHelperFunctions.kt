package com.ferechamitbeyli.presentation.utils.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


class UIHelperFunctions {
    companion object {

        /**
         * UI Helper Functions
         */
        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun View.visible(isVisible: Boolean) {
            visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        fun View.enable(enabled: Boolean) {
            isEnabled = enabled
            alpha = if (enabled) 1f else 0.5f
        }

        fun splitBitmap(bitmap: Bitmap, whichHalf: Int): Bitmap? {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inTargetDensity = 1
            bitmap.density = Bitmap.DENSITY_NONE
            var top = 0
            var bottom = 0
            //val targetHeight = 0
            when (whichHalf) {
                1 -> { // return 1st half of image
                    top = 0
                    bottom = bitmap.height / 2
                }
                2 -> { // return 2nd half of image
                    top = bitmap.height / 2 - 10
                    bottom = bitmap.height / 2 - 10
                }
            }
            //val fromHere = (bitmap.height * 0.5).toInt()
            return Bitmap.createBitmap(bitmap, 0, top, bitmap.width, bottom)
        }

        fun getScreenShot(view: View): Bitmap {
            val returnedBitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = view.background
            if (bgDrawable != null) bgDrawable.draw(canvas)
            else canvas.drawColor(Color.WHITE)
            view.draw(canvas)
            return returnedBitmap
        }

        /** Navigation Helper Functions **/

        fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
            Intent(this, activity).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }


    }

}




        