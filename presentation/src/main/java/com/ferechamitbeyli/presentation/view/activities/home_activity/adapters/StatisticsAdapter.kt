package com.ferechamitbeyli.presentation.view.activities.home_activity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ferechamitbeyli.presentation.databinding.ItemStatisticsLayoutBinding
import com.ferechamitbeyli.presentation.uimodels.StatisticsUIModel
import com.ferechamitbeyli.presentation.utils.helpers.TrackingHelperFunctions.calculateTimeFromDatabase

class StatisticsAdapter : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(val binding: ItemStatisticsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<StatisticsUIModel>() {
        override fun areItemsTheSame(
            oldItem: StatisticsUIModel,
            newItem: StatisticsUIModel
        ): Boolean {
            return oldItem.dateRange == newItem.dateRange
        }

        override fun areContentsTheSame(
            oldItem: StatisticsUIModel,
            newItem: StatisticsUIModel
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<StatisticsUIModel>) = differ.submitList(list)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisticsAdapter.StatisticsViewHolder =
        StatisticsViewHolder(
            ItemStatisticsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val statistic = differ.currentList[position]

        holder.binding.apply {
            itemDisplayedDateTitleTv.text = statistic.dateTitle
            itemDisplayedDateRangeTv.text = statistic.dateRange
            itemStatisticsTotalRunTimeValueTv.text =
                calculateTimeFromDatabase(statistic.totalRunTime.toLong())
            itemStatisticsTotalAvgRunSpeedValueTv.text =
                String.format("%.2f km/h", statistic.totalAvgRunSpeed.toFloat())
            itemStatisticsTotalRunDistanceValueTv.text = "${statistic.totalRunDistance} meters"
            itemStatisticsTotalCaloriesBurnedValueTv.text = "${statistic.totalCaloriesBurned} kcal"
            itemStatisticsTotalStepsValueTv.text = statistic.totalSteps
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

}