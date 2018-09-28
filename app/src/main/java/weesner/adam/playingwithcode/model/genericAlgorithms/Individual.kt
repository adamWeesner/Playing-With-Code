package weesner.adam.playingwithcode.model.genericAlgorithms

class Individual(var chromosome: ArrayList<Int> = ArrayList(0), var chromosomeLength: Int = -1) {
    var fitness: Double = -1.0

    init {
        if (chromosomeLength != -1) {
            chromosome = ArrayList(chromosomeLength)
            (0 until chromosomeLength).forEach {
                if (0.5 < Math.random()) chromosome.add(it, 1)
                else chromosome.add(it, 0)
            }
        }
    }

    fun gene(offset: Int, gene: Int = -1): Int {
        if (gene != -1) chromosome[offset] = gene

        return chromosome[offset]
    }

    override fun toString(): String {
        var output = ""
        (0 until chromosomeLength).forEach { output += chromosome[it] }
        return output
    }
}