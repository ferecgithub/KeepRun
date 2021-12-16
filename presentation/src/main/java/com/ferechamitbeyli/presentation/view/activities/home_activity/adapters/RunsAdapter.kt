package com.ferechamitbeyli.presentation.view.activities.home_activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.ferechamitbeyli.presentation.databinding.ItemRunLayoutBinding
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateElapsedTime
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.divideBitmap
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.visible
import java.text.SimpleDateFormat
import java.util.*

class RunsAdapter(val context: Context) : RecyclerView.Adapter<RunsAdapter.RunsViewHolder>() {

    inner class RunsViewHolder(val binding: ItemRunLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<RunUIModel>() {
        override fun areItemsTheSame(oldItem: RunUIModel, newItem: RunUIModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RunUIModel, newItem: RunUIModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RunUIModel>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunsViewHolder =
        RunsViewHolder(
            ItemRunLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RunsViewHolder, position: Int) {

        val run = differ.currentList[position]

        holder.binding.apply {

            if (run.image != null) {
                val firstImagePart = divideBitmap(run.image)[0]
                val secondImagePart = divideBitmap(run.image)[1]

                itemRunRoute1Iv.load(firstImagePart)
                itemRunRoute2Iv.load(secondImagePart)
            } else {

                val imageRequest = ImageRequest.Builder(context)
                    .data(run.imageUrl)
                    .target { drawable ->
                        val bitmap = drawable.toBitmap() // This is the bitmap 🚨
                        val firstImagePart = divideBitmap(bitmap)[0]
                        val secondImagePart = divideBitmap(bitmap)[1]
                        itemRunRoute1Iv.load(firstImagePart)
                        itemRunRoute2Iv.load(secondImagePart)
                    }
                    .build()
                ImageLoader(context).enqueue(imageRequest)

            }


            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            itemDateTv.text = dateFormat.format(calendar.time)

            val time = calculateElapsedTime(run.timeInMillis)
            itemTimeTv.text = time

            val distanceInKm = "${run.distanceInMeters / 1000f}km"
            itemDistanceTv.text = distanceInKm

            // Expanded part
            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            itemSpeedTv.text = avgSpeed

            val caloriesBurned = "${run.caloriesBurned}kcal"
            itemCaloriesTv.text = caloriesBurned

            val steps = "${run.steps} steps"
            itemStepsTv.text = steps

            itemRunRouteGradientIv.setOnClickListener {
                run.isExpanded = !run.isExpanded
                notifyItemChanged(position)
            }

            itemRunRoute1Iv.setOnClickListener {
                run.isExpanded = !run.isExpanded
                notifyItemChanged(position)
            }

            itemRunRoute2Iv.setOnClickListener {
                run.isExpanded = !run.isExpanded
                notifyItemChanged(position)
            }

            itemRunRouteGradientIv.visible(!run.isExpanded)
            itemRunHiddenLayoutCl.visible(run.isExpanded)

        }

    }

    override fun getItemCount(): Int = differ.currentList.size

}