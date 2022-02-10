import org.junit.Test
import kotlin.test.assertEquals
import kodi.Kodi

class KodiTest {
    @Test
    fun `조건에 맞는 문자열 검색 시, 해당 문자열 반환`() {
        // given
        val userInput1 = "홍길동"
        val userInput2 = "홍ㄱㄷ"
        val userInput3 = "ㅎ길ㄷ"
        val userInput4 = "ㅎ길동"
        val userInput5 = "ㅎㄱㄷ"
        val originStringList = listOf("이철수", "김영희", "이원중", "홍길동", "황괅뒳")
        Kodi.insertList(originStringList)

        // when
        val result1 = Kodi.getMatchStrings(userInput1)
        val result2 = Kodi.getMatchStrings(userInput2)
        val result3 = Kodi.getMatchStrings(userInput3)
        val result4 = Kodi.getMatchStrings(userInput4)
        val result5 = Kodi.getMatchStrings(userInput5)

        // then
        assertEquals(result1, listOf("홍길동"))
        assertEquals(result2, listOf("홍길동"))
        assertEquals(result3, listOf("홍길동"))
        assertEquals(result4, listOf("홍길동"))
        assertEquals(result5, listOf("홍길동", "황괅뒳"))
    }
}