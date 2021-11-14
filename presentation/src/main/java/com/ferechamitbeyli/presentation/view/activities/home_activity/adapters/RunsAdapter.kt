package com.ferechamitbeyli.presentation.view.activities.home_activity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ferechamitbeyli.presentation.databinding.ItemRunLayoutBinding
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.getFormattedStopWatchTime
import com.ferechamitbeyli.presentation.utils.helpers.UIHelperFunctions.Companion.splitBitmap
import java.text.SimpleDateFormat
import java.util.*

class RunsAdapter : RecyclerView.Adapter<RunsAdapter.RunsViewHolder>() {

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

            val firstImagePart = run.image?.let { splitBitmap(it, 0) }
            val secondImagePart = run.image?.let { splitBitmap(it, 1) }

            itemRunRoute1Iv.load(firstImagePart) {
                crossfade(true)
            }
            itemRunRoute2Iv.load(secondImagePart) {
                crossfade(true)
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }

            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            itemDateTv.text = dateFormat.format(calendar.time)

            val time = getFormattedStopWatchTime(run.timeInMillis)
            itemTimeTv.text = time

            val distanceInKm = "${run.distanceInMeters / 1000f}km"
            itemDistanceTv.text = distanceInKm

            // Expended part
            val avgSpeed = "${run.avgSpeedInKMH}km/h"
            itemSpeedTv.text = avgSpeed

            val caloriesBurned = "${run.caloriesBurned}kcal"
            itemCaloriesTv.text = caloriesBurned

            val steps = "${run.steps} steps"
            itemStepsTv.text = steps

            val isExpanded = run.isExpanded
            itemRunRouteGradientIv.visibility = if (isExpanded) View.GONE else View.VISIBLE
            itemRunHiddenLayoutCl.visibility = if (isExpanded) View.VISIBLE else View.GONE

            itemRunRoute1Iv.setOnClickListener {
                run.isExpanded = !run.isExpanded
            }

        }

    }

    override fun getItemCount(): Int = differ.currentList.size
}