package ru.ispras.modis.tm.builder

import java.util.Random

import ru.ispras.modis.tm.attribute.AttributeType
import ru.ispras.modis.tm.brick.{NoiseParameters, RobustBrick}
import ru.ispras.modis.tm.documents.{Alphabet, Document}
import ru.ispras.modis.tm.initialapproximationgenerator.RandomInitialApproximationGenerator
import ru.ispras.modis.tm.stoppingcriteria.MaxNumberOfIterationStoppingCriteria
import ru.ispras.modis.tm.utils.ModelParameters

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:06
 */
class RobustPLSABuilder(numberOfTopics: Int,
                        alphabet: Alphabet,
                        documents: Array[Document],
                        private val noiseWeight: Float,
                        private val backgroundWeight: Float,
                        private val random: Random = new Random,
                        private val numberOfIteration: Int = 100,
                        attributeWeight: Map[AttributeType, Float],
                        parallel : Boolean = false)
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents, attributeWeight, parallel) {

    override protected def buildBricks(modelParameters: ModelParameters) = {
        val noiseParameter = new NoiseParameters(noiseWeight, backgroundWeight)
        modelParameters.numberOfWords.map {
            case (attribute, value) => (attribute,
                RobustBrick(regularizer, phiSparsifier, attribute, modelParameters, noiseParameter, documents, attributeWeight.getOrElse(attribute, 1f)))
        }
    }

    stoppingCriteria = new MaxNumberOfIterationStoppingCriteria(numberOfIteration) // TODO if it is possible to avoid messing around with other class fields, AVOID it
    // use setters
    initialApproximationGenerator = new RandomInitialApproximationGenerator(random)
}
