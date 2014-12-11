package ru.ispras.modis.tm.utils

/**
 * Created by valerij on 12/11/14.
 */
private[tm] class DenisTrickTupleSeq(private val counters : Array[Short],
                         private val words : Array[Array[Int]])  extends Seq[(Int,Short)] {
    val length = counters.zipWithIndex.map{case(cnt, index) => words(index).sum * cnt}.sum

    private class DenisTrickTupleSeqIterator extends Iterator[(Int, Short)] {
        private var cntIndex = 0
        private var wordsIndex = 0

        override def hasNext: Boolean = (cntIndex < counters.size - 1) || ((cntIndex == counters.size - 1) && (wordsIndex < words(cntIndex).size))

        override def next(): (Int, Short) = {
            var result : (Int,Short) = null

            if (wordsIndex < words(cntIndex).size - 1) {
                result = (words(cntIndex)(wordsIndex), counters(cntIndex))
                wordsIndex += 1
            } else {
                result = (words(cntIndex)(wordsIndex), counters(cntIndex))
                wordsIndex = 0
                cntIndex += 1
            }

            result
        }
    }

    override def apply(idx: Int): (Int, Short) = {
        if (idx >= length) throw new ArrayIndexOutOfBoundsException

        val it = iterator
        var result : (Int, Short) = null
        for (_ <- 0 until idx - 1) result = it.next

        result
    }

    override def iterator: Iterator[(Int, Short)] = new DenisTrickTupleSeqIterator()
}

private[tm] object DenisTrickTupleSeq {
    def apply(pairs : Array[(Int,Short)]) = {
        val grouped = pairs.groupBy(_._2).map{case(cnt, words) => (cnt, words.map(_._1)) }

        val cnts = grouped.keys.toArray
        val words = grouped.values.toArray

        new DenisTrickTupleSeq(cnts, words)
    }
}