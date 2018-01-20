package re2s

import org.scalatest.FunSuite

class StrconvSuite() extends FunSuite {
  private def rune(r: Int) = new StringBuffer().appendCodePoint(r).toString

  private val UNQUOTE_TESTS = Array(
    Array("\"\"", ""),
    Array("\"a\"", "a"),
    Array("\"abc\"", "abc"),
    Array("\"☺\"", "☺"),
    Array("\"hello world\"", "hello world"),
    Array("\"\\xFF\"", "\u00FF"),
    Array("\"\\377\"", "\377"),
    Array("\"\\u1234\"", "\u1234"),
    Array("\"\\U00010111\"", rune(0x10111)),
    Array("\"\\U0001011111\"", rune(0x10111) + "11"),
    Array("\"\\a\\b\\f\\n\\r\\t\\v\\\\\\\"\"", "\007\b\f\n\r\t\013\\\""),
    Array("\"'\"", "'"),
    Array("'a'", "a"),
    Array("'☹'", "☹"),
    Array("'\\a'", "\u0007"),
    Array("'\\x10'", "\u0010"),
    Array("'\\377'", "\377"),
    Array("'\\u1234'", "\u1234"),
    Array("'\\U00010111'", rune(0x10111)),
    Array("'\\t'", "\t"),
    Array("' '", " "),
    Array("'\\''", "'"),
    Array("'\"'", "\""),
    Array("``", ""),
    Array("`a`", "a"),
    Array("`abc`", "abc"),
    Array("`☺`", "☺"),
    Array("`hello world`", "hello world"),
    Array("`\\xFF`", "\\xFF"),
    Array("`\\377`", "\\377"),
    Array("`\\`", "\\"),
    Array("`\n`", "\n"),
    Array("`\t`", "\t"),
    Array("` `", " "), // Misquoted strings, should produce an error.
    Array("", null),
    Array("\"", null),
    Array("\"a", null),
    Array("\"'", null),
    Array("b\"", null),
    Array("\"\\\"", null),
    Array("'\\'", null),
    Array("'ab'", null),
    Array("\"\\x1!\"", null),
    Array("\"\\U12345678\"", null),
    Array("\"\\z\"", null),
    Array("`", null),
    Array("`xxx", null),
    Array("`\"", null),
    Array("\"\\'\"", null),
    Array("'\\\"'", null),
    Array("\"\n\"", null),
    Array("\"\\n\n\"", null),
    Array("'\n'", null)
  )

  test("Unquote") {
    for (Array(input, expected) <- UNQUOTE_TESTS) {
      if (expected != null)
        assert(expected == Strconv.unquote(input), "unquote(%s)".format(input))
      else
        try {
          Strconv.unquote(input)
          fail(String.format("unquote(%s) succeeded unexpectedly", input))
        } catch {
          case e: IllegalArgumentException =>
          /* ok */
          case e: StringIndexOutOfBoundsException =>
        }
      // TODO(adonovan): port and run the quote tests too, backward
    }
  }
}
