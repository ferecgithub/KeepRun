package com.ferechamitbeyli.presentation.utils.helpers

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
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
import java.io.ByteArrayOutputStream


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

        fun divideBitmap(picture: Bitmap): Array<Bitmap?> {
            val halves = arrayOfNulls<Bitmap>(2)
            halves[0] = Bitmap.createBitmap(picture, 0, 0, picture.width, picture.height / 2)
            halves[1] = Bitmap.createBitmap(
                picture,
                0,
                picture.height / 2,
                picture.width,
                picture.height / 2
            )
            return halves
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

        fun fromVectorToBitmap(id: Int, color: Int, resources: Resources): BitmapDescriptor {
            val vectorDrawable: Drawable = ResourcesCompat.getDrawable(resources, id, null)
                ?: return BitmapDescriptorFactory.defaultMarker()
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

        fun toBitmap(bytes: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun fromBitmap(bmp: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        }

        /** Navigation Helper Functions **/

        fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
            Intent(this, activity).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        fun setPendingIntentFlag(): Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

        /** Other Helper Functions **/

        fun capitalizeFirstLetter(
            string: String,
            delimiter: String = " ",
            separator: String = " "
        ): String {
            return string.split(delimiter).joinToString(separator = separator) {
                it.lowercase().replaceFirstChar { char -> char.titlecase() }
            }
        }

    }

}




        