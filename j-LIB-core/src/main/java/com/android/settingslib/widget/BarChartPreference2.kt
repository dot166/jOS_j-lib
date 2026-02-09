package com.android.settingslib.widget

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.android.settingslib.widget.preference.barchart.R as BCPR
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.github.dot166.jlib.R

class BarChartPreference2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    Preference(context, attrs) {
    private var values: MutableList<Long?> = ArrayList()

    init {
        layoutResource = R.layout.bar_chart_preference2
    }

    fun setValues(values: MutableList<Long?>) {
        this.values = values
        notifyChanged()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val chart = holder.findViewById(R.id.chart) as BarChart
        configureChart(chart)
        updateChartData(chart)
    }

    private fun configureChart(chart: BarChart) {
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        chart.setScaleEnabled(false)
        chart.setTouchEnabled(false)
        chart.setDragEnabled(false)
        chart.setPinchZoom(false)

        chart.axisRight.isEnabled = false

        chart.axisLeft.isEnabled = false
        chart.axisLeft.setAxisMinimum(0f)

        chart.xAxis.isEnabled = false

        chart.setDrawValueAboveBar(false)
        chart.setDrawGridBackground(false)
    }

    private fun updateChartData(chart: BarChart) {
        if (values.size != 7) return

        val entries = ArrayList<BarEntry?>(7)
        for (i in values.indices) {
            entries.add(BarEntry(i.toFloat(), (values[i]!! / 60000f).toFloat()))
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.setDrawValues(false)
        dataSet.setColor(context.getColor(BCPR.color.settings_bar_view_1_color))
        dataSet.highLightAlpha = 0

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f

        chart.setData(barData)
        chart.invalidate()
    }
}
