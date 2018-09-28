package weesner.adam.playingwithcode.model.genericAlgorithms

abstract class GenericGA(open val populationSize: Int, open val mutationRate: Double = 0.05, open val crossoverRate: Double, open val elitismCount: Int) {
    abstract fun initPopulation(chromosomeLength: Int): Population
    abstract fun calculateFitness(individual: Individual): Double
    abstract fun evaluatePopulation(population: Population)
    abstract fun isTerminationConditionMet(population: Population): Boolean
    abstract fun selectParent(population: Population): Individual
    abstract fun crossoverPopulation(population: Population): Population
    abstract fun mutatePopulation(population: Population): Population
}