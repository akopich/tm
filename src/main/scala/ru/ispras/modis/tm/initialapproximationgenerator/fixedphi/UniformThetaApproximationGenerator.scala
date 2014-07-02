package ru.ispras.modis.tm.initialapproximationgenerator.fixedphi

import ru.ispras.modis.tm.matrix.{Theta, AttributedPhi}
import ru.ispras.modis.tm.utils.ModelParameters
import ru.ispras.modis.tm.documents.Document
import ru.ispras.modis.tm.attribute.AttributeType

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 07.05.14
 * Time: 15:00
 */
class UniformThetaApproximationGenerator(phi: Map[AttributeType, AttributedPhi]) extends FixedPhiInitialApproximation(phi) {
    /**
     * this method full matrices theta by some initial values.
     * !WARNING! do NOT do dump before return matrices
     * @param parameters model parameters
     * @param documents sequence of documents
     * @param theta matrix with zero values in expectation and stochastic matrix
     */
    protected def fullMatrixTheta(parameters: ModelParameters, documents: Seq[Document], theta: Theta): Unit = {
        0.until(theta.numberOfRows).foreach(row => 0.until(theta.numberOfColumns).foreach(column => theta.addToExpectation(row, column, 1)))
    }

}