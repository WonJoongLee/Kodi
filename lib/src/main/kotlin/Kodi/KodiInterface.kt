// KoreanDisassembler는 이름(다른 string이어도 됨) List가 있을 때, 초성으로 검색하여 원하는 결과를
// 가져올 수 있도록 해주는 프로그램이다.
interface KodiInterface {
    fun insertList(list: List<String>)
    fun getMatchStrings(inputString: String): List<String>
}