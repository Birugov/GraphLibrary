package com.example.graphs_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColor
import kotlin.properties.Delegates
import kotlin.random.Random

class ChartGraph(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var barPainter:Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#fef08c")
    }
    private var axisPainter:Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#f8cb7a")
        strokeWidth = 5f
    }
    private var guidePainter:Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#f8cb7a")
        strokeWidth = 3f
    }
    private var xLabelPainter:Paint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private var yLabelPainter:Paint = Paint().apply {
        color = Color.parseColor("#3700B3")
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private var padding:Float = 20f
    private var yLabelWidth:Float = 0f
    private var xLabelWidth:Float = 0f
    private var series:ArrayList<BarSeries> = ArrayList()


    init {

        series.add(BarSeries("sgfgdfg", 0.2f))


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val height = height
        val width = width
        val gridBottom = height - padding - xLabelWidth
        val gridRight = width - padding

        drawGuids(canvas, gridBottom, gridRight)
        val gridTopLeft = padding + yLabelWidth + 10f

        drawAxis(canvas, gridTopLeft, gridBottom, gridRight)
        drawBars(canvas, height,  gridTopLeft, gridBottom, gridRight)

    }

    private fun drawBars(canvas: Canvas?, canvasHeight:Int, gridTopLeft: Float, gridBottom: Float, gridRight: Float) {
        val spacing = 10f
        val totalSpace = spacing * series.size
        val width = (gridRight - gridTopLeft - totalSpace) / series.size
        var left = gridTopLeft + spacing
        var right = left + width
        val height = canvasHeight - 2 * padding - xLabelWidth
        for ( i in series){
            val top = padding + height * (1f-i.value)
            canvas?.apply {
                drawRect(left, top, right, gridBottom, barPainter)
                save()
                rotate(-90f)
                drawText(i.label, -height-padding-60f, left+(width + 2 * spacing) / 2, xLabelPainter)
                restore()
            }

            left = right + spacing
            right = left + width
        }
    }

    private fun drawAxis(canvas: Canvas?, gridTopLeft: Float, gridBottom: Float, gridRight: Float) {
        canvas?.drawLine(gridTopLeft, gridBottom, gridTopLeft, padding, axisPainter)
        canvas?.drawLine(gridTopLeft, gridBottom, gridRight, gridBottom, axisPainter)
    }

    private fun drawGuids(canvas: Canvas?, gridBottom: Float, gridRight: Float) {
        val spacing = (gridBottom -padding) / 10f
        var y:Float
        for (i in 0..10){
            var s = 100 - i * 10
            var label = s.toString()
            var width = yLabelPainter.measureText(label)
            if (yLabelWidth < width) yLabelWidth = width
            var bound = Rect()
            yLabelPainter.getTextBounds(label, 0, label.length, bound)

            y = padding + i * spacing
            canvas?.drawLine(padding+yLabelWidth, y , gridRight, y , guidePainter)
            canvas?.drawText(label, padding+yLabelWidth, y+bound.height() / 2, yLabelPainter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setSeries(barSeries: ArrayList<BarSeries>){
        this.series = barSeries
        val label = series.stream().max(Comparator.comparingInt { x->x.label.length }).get().label
        xLabelWidth = xLabelPainter.measureText(label)
    }
}