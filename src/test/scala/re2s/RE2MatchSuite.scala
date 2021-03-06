package re2s

import org.scalatest.FunSuite

class RE2MatchSuite() extends FunSuite {
  import FindSuite._

  test("Match") {
    for (test <- FIND_TESTS) {
      val re = RE2.compile(test.pat)
      var m  = re.match_(test.text, new Machine(re))
      if (m != (test.matches.length > 0))
        fail(
          "RE2.match failure on %s: %s should be %s"
            .format(test, m, test.matches.length > 0))
      // now try bytes
      m = re.matchUTF8(GoTestUtils.utf8(test.text))
      if (m != (test.matches.length > 0))
        fail(
          "RE2.matchUTF8 failure on %s: %s should be %s"
            .format(test, m, test.matches.length > 0))
    }
  }

  test("MatchFunction") {
    for (test <- FIND_TESTS) {
      val m = RE2.match_(test.pat, test.text)
      if (m != (test.matches.length > 0))
        fail(
          "RE2.match failure on %s: %s should be %s"
            .format(test, m, test.matches.length > 0))
    }
  }
}
