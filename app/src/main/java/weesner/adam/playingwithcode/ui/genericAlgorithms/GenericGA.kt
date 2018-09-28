package weesner.adam.playingwithcode.ui.genericAlgorithms

import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import weesner.adam.playingwithcode.model.genericAlgorithms.GenericGA
import weesner.adam.playingwithcode.model.genericAlgorithms.Population

class GenericGA {
    var generation = 1
    var population: Population = Population(0)

    var gensBest = arrayListOf<Entry>()
    var gensAverage = arrayListOf<Entry>()
    var gensWorst = arrayListOf<Entry>()

    lateinit var linearLayout: LinearLayout
    lateinit var textViewBest: TextView

    var graph: LineChart? = null

    lateinit var ga: GenericGA

    fun create(mainView: ConstraintLayout, geneticAlgoritm: GenericGA) {
        ga = geneticAlgoritm
        linearLayout = LinearLayout(mainView.context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)

        val startButton = Button(mainView.context)
        startButton.setPadding(16, 16, 16, 16)
        startButton.text = "Start"
        startButton.setTextColor(Color.BLACK)

        textViewBest = TextView(mainView.context)
        textViewBest.textSize = 24f
        textViewBest.setTextColor(Color.BLACK)
        textViewBest.setPadding(16, 16, 16, 16)

        graph = LineChart(linearLayout.context)
        graph!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        graph!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                graph!!.description.text = gensBest[gensBest.size - 1].y.toString()
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e!!.x.toInt() - 1
                graph!!.description.text =
                        "Generation: ${index + 1}\n" +
                        "Fittest: ${gensBest[index].y * 100}%\n" +
                        "Average: ${gensAverage[index].y * 100}%\n" +
                        "Worst: ${gensWorst[index].y * 100}%"
            }
        })

        graph!!.axisRight.setDrawLabels(false)
        graph!!.axisLeft.axisMaximum = 1f
        graph!!.xAxis.granularity = 1f

        linearLayout.addView(startButton)
        linearLayout.addView(textViewBest)
        linearLayout.addView(graph)

        mainView.addView(linearLayout)

        startButton.setOnClickListener {
            generation = 1
            population = ga.initPopulation(50)
            ga.evaluatePopulation(population)

            gensBest.clear()
            gensAverage.clear()
            gensWorst.clear()
            gensBest.add(Entry(generation.toFloat(), population.fittest(0).fitness.toFloat()))
            gensAverage.add(Entry(generation.toFloat(), population.populationAverageFitness.toFloat()))
            gensWorst.add(Entry(generation.toFloat(), population.fittest(population.populationSize - 1).fitness.toFloat()))
            setupGraph()
            nextGen()
        }

    }

    private fun nextGen() {
        textViewBest.text = "Best: ${population.fittest(0)}"
        launch(UI) {
            withContext(DefaultDispatcher) {
                // apply crossover
                population = ga.crossoverPopulation(population)
                // apply mutation
                population = ga.mutatePopulation(population)
                // evaluate
                ga.evaluatePopulation(population)
                gensBest.add(Entry(generation.toFloat(), population.fittest(0).fitness.toFloat()))
                gensAverage.add(Entry(generation.toFloat(), population.populationAverageFitness.toFloat()))
                gensWorst.add(Entry(generation.toFloat(), population.fittest(population.populationSize - 1).fitness.toFloat()))
                generation++
            }

            setupGraph()

            if (!ga.isTerminationConditionMet(population)) {
                nextGen()
            } else {
                textViewBest.text = "Done in ${generation - 1} generations"
            }
        }
    }

    private fun setupGraph() {
        val lineData = LineData()
        val dataSetBest = LineDataSet(gensBest, "Fittest").apply {
            color = Color.parseColor("#00710F")
            valueTextColor = Color.TRANSPARENT
            setDrawCircles(false)
        }
        val dataSetAverage = LineDataSet(gensAverage, "Average").apply {
            color = Color.parseColor("#4E4C4D")
            valueTextColor = Color.TRANSPARENT
            setDrawCircles(false)
        }
        val dataSetWorst = LineDataSet(gensWorst, "Weakest").apply {
            color = Color.parseColor("#D81B60")
            valueTextColor = Color.TRANSPARENT
            setDrawCircles(false)
        }
        lineData.addDataSet(dataSetBest)
        lineData.addDataSet(dataSetAverage)
        lineData.addDataSet(dataSetWorst)

        graph!!.data = lineData
        graph!!.invalidate()
        graph!!.highlightValue(gensBest[gensBest.size - 1].x, 0)
    }
}