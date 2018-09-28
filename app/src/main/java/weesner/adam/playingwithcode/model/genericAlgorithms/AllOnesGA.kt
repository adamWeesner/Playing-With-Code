package weesner.adam.playingwithcode.model.genericAlgorithms

class AllONesGA(override val populationSize: Int, override val mutationRate: Double = 0.05, override val crossoverRate: Double, override val elitismCount: Int) :
        GenericGA(populationSize, mutationRate, crossoverRate, elitismCount) {
    override fun initPopulation(chromosomeLength: Int): Population {
        return Population(populationSize, chromosomeLength)
    }

    override fun calculateFitness(individual: Individual): Double {
        var correctGenes = 0.0
        (0 until individual.chromosomeLength).forEach {
            if (individual.gene(it) == 1) correctGenes++
        }

        val fitness = correctGenes / individual.chromosomeLength
        individual.fitness = fitness
        return fitness
    }

    override fun evaluatePopulation(population: Population) {
        var popFitness = 0.0
        for (individual: Individual in population.population)
            popFitness += calculateFitness(individual)

        population.populationFitness = popFitness
    }

    override fun isTerminationConditionMet(population: Population): Boolean {
        for (individual: Individual in population.population)
            if (individual.fitness == 1.0) return true

        return false
    }

    override fun selectParent(population: Population): Individual {
        val individuals = population.population

        // spin roulette wheel
        val popFitness = population.populationFitness
        val rouletteWheelPos = Math.random() * popFitness

        //find parent
        var spinWheel = 0.0
        for (individual: Individual in individuals) {
            spinWheel += individual.fitness
            if (spinWheel >= rouletteWheelPos)
                return individual
        }

        return individuals[population.populationSize - 1]
    }

    override fun crossoverPopulation(population: Population): Population {
        val newPopulation = Population(population.populationSize)

        (0 until population.populationSize).forEach {
            val parent1 = population.fittest(it)

            // apply crossover to this individual?
            if (crossoverRate > Math.random() && it >= elitismCount) {
                // init offspring
                val offspring = Individual(chromosomeLength = parent1.chromosomeLength)
                // find second parent
                val parent2 = selectParent(population)

                // loop over genome
                (0 until parent1.chromosomeLength).forEach { geneIndex ->
                    if (0.5 > Math.random()) offspring.gene(geneIndex, parent1.gene(geneIndex))
                    else offspring.gene(geneIndex, parent2.gene(geneIndex))
                }

                // add offspring to population
                newPopulation.individual(it, offspring)
            } else {
                // add individual to the pop without crossover
                newPopulation.individual(it, parent1)
            }
        }

        return newPopulation
    }

    override fun mutatePopulation(population: Population): Population {
        val newPopulation = Population(population.populationSize)

        (0 until population.populationSize).forEach {
            val individual = population.fittest(it)

            // loop over individuals genes
            (0 until individual.chromosomeLength).forEach { geneIndex ->
                // skip mutation if this is an elite individual
                if (it > elitismCount) {
                    // does gene need mutation?
                    if (mutationRate > Math.random()) {
                        var newGene = 1
                        if (individual.gene(geneIndex) == 1) newGene = 0

                        // mutate gene
                        individual.gene(geneIndex, newGene)
                    }
                }
            }

            // add offspring to population
            newPopulation.individual(it, individual)
        }

        return newPopulation
    }
}