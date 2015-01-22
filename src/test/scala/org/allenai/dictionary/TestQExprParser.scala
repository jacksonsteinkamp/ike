package org.allenai.dictionary

import org.allenai.common.testkit.UnitSpec
import org.allenai.common.testkit.ScratchDirectory

class TestQExprParser extends UnitSpec with ScratchDirectory {

  val wc = QWildcard
  // A bunch of QExpr shorthand functions
  // scalastyle:off
  def w(s: String) = QWord(s)
  def p(s: String) = QPos(s)
  def c(s: String) = QCluster(s)
  def qs(exprs: QExpr*) = QSeq(exprs)
  def cap(expr: QExpr) = QUnnamed(expr)
  def cap(name: String, expr: QExpr) = QNamed(expr, name)
  def nocap(expr: QExpr) = QNonCap(expr)
  def or(exprs: QExpr*) = QDisj(exprs)
  def star(expr: QExpr) = QStar(expr)
  // scalastyle:on

  def parse(s: String): QExpr = QExprParser.parse(s).get

  "QExprParser" should "parse correctly" in {

    val q1 = "this is a test"
    val e1 = qs(w("this"), w("is"), w("a"), w("test"))
    assert(parse(q1) == e1)

    val q2 = "this is DT NN"
    val e2 = qs(w("this"), w("is"), p("DT"), p("NN"))
    assert(parse(q2) == e2)

    val q3 = "^10 is DT NN"
    val e3 = qs(c("10"), w("is"), p("DT"), p("NN"))
    assert(parse(q3) == e3)

    val q4 = "this is (a test)"
    val e4 = qs(w("this"), w("is"), cap(qs(w("a"), w("test"))))
    assert(parse(q4) == e4)

    val q5 = "this is (?<foo> a test)"
    val e5 = qs(w("this"), w("is"), cap("foo", qs(w("a"), w("test"))))
    assert(parse(q5) == e5)

    val q6 = "this is (?:a test)"
    val e6 = qs(w("this"), w("is"), nocap(qs(w("a"), w("test"))))
    assert(parse(q6) == e6)

    val q7 = "(?:this|that) is a test"
    val e7 = qs(nocap(or(w("this"), w("that"))), w("is"), w("a"), w("test"))
    assert(parse(q7) == e7)

    val q8 = ". is a test"
    val e8 = qs(wc, w("is"), w("a"), w("test"))
    assert(parse(q8) == e8)

    val q9 = ".* is a test"
    val e9 = qs(star(wc), w("is"), w("a"), w("test"))
    assert(parse(q9) == e9)

  }
}
