package weesner.adam.playingwithcode.model.genericAlgorithms

import java.util.*

class Population(val populationSize: Int, val chromosomeLength: Int = -1) {
    var population = ArrayList<Individual>(populationSize)
    var populationFitness = -1.0
    var populationAverageFitness = 0.0

    init {
        if (chromosomeLength != -1) {
            (0 until populationSize).forEach {
                val individual = Individual(chromosomeLength = chromosomeLength)
                population.add(it, individual)
            }
        }
    }

    fun fittest(offset: Int): Individual {
        population.sortByDescending { it.fitness }
        population.forEach { populationAverageFitness += it.fitness }
        populationAverageFitness /= populationSize

        return population[offset]
    }

    fun individual(offset: Int, individual: Individual? = null): Individual {
        if (individual != null) {
            population.add(offset, individual)
        }

        return population[offset]
    }
}