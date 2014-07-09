package ru.ispras.modis.tm.builder

import ru.ispras.modis.tm.documents.{Document, Alphabet}
import java.util.Random
import ru.ispras.modis.tm.utils.ModelParameters
import ru.ispras.modis.tm.brick.{NoiseParameters, RobustBrick, NonRobustBrick, AbstractPLSABrick}
import ru.ispras.modis.tm.initialapproximationgenerator.{RandomInitialApproximationGenerator, InitialApproximationGenerator}
import ru.ispras.modis.tm.stoppingcriteria.{MaxNumberOfIterationStoppingCriteria, StoppingCriteria}
import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 04.04.14
 * Time: 19:06
 */
class RobustPLSABuilder(numberOfTopics: Int,
                        alphabet: Alphabet,
                        documents: Seq[Document],
                        private val noiseWeight: Float,
                        private val backgroundWeight: Float,
                        private val random: Random = new Random,
                        private val numberOfIteration: Int = 100,
                        attributeWeight: Map[AttributeType, Float])
    extends AbstractPLSABuilder(numberOfTopics, alphabet, documents, attributeWeight) {

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
