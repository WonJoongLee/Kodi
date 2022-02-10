package kodi

object Kodi : KodiInterface {

    private const val KOREAN_CHAR_START = 44032 // 유니코드에서 '가'의 위치
    private lateinit var disassembledStringList: List<DisassembledString>
    private val choList = arrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )
    private val joongList = arrayOf(
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
        'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    )
    private val jongList = arrayOf(
        null, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄽ', 'ㄻ', 'ㄼ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
        'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    /**
     * 비교할 string 함수를 추가하는 함수
     */
    override fun insertList(list: List<String>) {
        disassembledStringList = list.toDisassembledStringList()
    }

    /**
     * 사용자가 입력한 값과 일치하는 string이 있는지 확인하고,
     * 있다면 해당 string들을 반환하는 함수
     */
    override fun getMatchStrings(inputString: String): List<String> {
        val searchedString = mutableListOf<String>()
        disassembledStringList.forEach { currentDisassembledString ->
            if (checkMatches(currentDisassembledString, inputString.toDisassembledString())) searchedString.add(
                currentDisassembledString.originString
            )
        }
        return searchedString
    }

    /**
     * String List -> DisassembledString List 변환 함수
     */
    private fun List<String>.toDisassembledStringList(): List<DisassembledString> {
        val tempDisassembledStringList = mutableListOf<DisassembledString>()
        this.forEach { currentString ->
            tempDisassembledStringList.add(currentString.toDisassembledString())
        }
        return tempDisassembledStringList
    }

    /**
     * 사용자가 찾으려고 한 string이 맞는지 확인하는 함수
     */
    private fun checkMatches(currentString: DisassembledString, inputString: DisassembledString): Boolean {
        inputString.disassembledCharList.zip(currentString.disassembledCharList) { inputKorean, currentKorean ->
            if (inputKorean.cho != currentKorean.cho) return false
            when {
                inputKorean.joong != null && currentKorean.joong == null -> return false
                inputKorean.joong != null && currentKorean.joong != null -> {
                    if (inputKorean.joong != currentKorean.joong) return false
                }
            }
            when {
                inputKorean.jong != null && currentKorean.jong == null -> return false
                inputKorean.jong != null && currentKorean.jong != null -> {
                    if (inputKorean.jong != currentKorean.jong) return false
                }
            }
        }
        return true
    }

    /**
     * String -> DisassembledString 변환 함수
     */
    private fun String.toDisassembledString(): DisassembledString {
        val koreanCharList = mutableListOf<KoreanChar>()
        this.filterNot { it.isWhitespace() }.forEach { targetChar ->
            val currentHexValue = targetChar.code
            if (currentHexValue >= KOREAN_CHAR_START) { // '가' 이후일 경우
                val diff = currentHexValue - KOREAN_CHAR_START
                val currentCho = choList[(diff / 28 / 21)]
                val currentJoong = joongList[(diff / 28 % 21)]
                val currentJong = jongList[(diff % 28)]
                if (currentJong == null) {
                    koreanCharList.add(KoreanChar(currentCho, currentJoong))
                } else {
                    koreanCharList.add(KoreanChar(currentCho, currentJoong, currentJong))
                }
            } else {
                koreanCharList.add(KoreanChar(targetChar))
            }
        }
        return DisassembledString(this, koreanCharList)
    }
}