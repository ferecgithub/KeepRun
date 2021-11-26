package com.ferechamitbeyli.presentation.utils.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.ferechamitbeyli.presentation.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.snackbar.Snackbar


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

        fun showSnackbar(
            view: View,
            context: Context,
            isPositive: Boolean,
            text: String,
            length: Int
        ): Snackbar {
            val snackbar = Snackbar.make(view, text, length)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(
                if (isPositive) getColor(context, R.color.darkGreen)
                else getColor(context, R.color.red)
            )
            return snackbar
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

        private fun fromVectorToBitmap(id: Int, color: Int, resources: Resources) : BitmapDescriptor {
            val vectorDrawable: Drawable? = ResourcesCompat.getDrawable(resources, id, null)
            if (vectorDrawable == null) {
                Log.d("MapsActivity", "Resource not found.")
                return BitmapDescriptorFactory.defaultMarker()
            }
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            DrawableCompat.setTint(vectorDrawable, color)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
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




        