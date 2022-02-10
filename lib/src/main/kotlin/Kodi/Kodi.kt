package Kodi

import KodiInterface

object Kodi : KodiInterface {

    private const val KOREAN_CHAR_START = 44032 // 유니코드에서 '가'의 위치
    private lateinit var disassembledStringList: List<DisassembledString>
    private val choList =
        arrayOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
    private val joongList =
        arrayOf('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
    private val jongList = arrayOf(
        null,
        'ㄱ',
        'ㄲ',
        'ㄳ',
        'ㄴ',
        'ㄵ',
        'ㄶ',
        'ㄷ',
        'ㄹ',
        'ㄺ',
        'ㄽ',
        'ㄻ',
        'ㄼ',
        'ㄾ',
        'ㄿ',
        'ㅀ',
        'ㅁ',
        'ㅂ',
        'ㅄ',
        'ㅅ',
        'ㅆ',
        'ㅇ',
        'ㅈ',
        'ㅊ',
        'ㅋ',
        'ㅌ',
        'ㅍ',
        'ㅎ'
    )

    data class DisassembledString(
        val originString: String, // 원래 String
        val disassembledStringList: List<Char>, // 분해한 한글 리스트
        val choList: List<Char>, // 초성 리스트
        val joongList: List<Char>, // 중성 리스트
        val jongList: List<Char>, // 종성 리스트
        val disassembledCharList: List<KoreanChar>
    )

    data class KoreanChar(
        val cho: Char,
        val joong: Char? = null,
        val jong: Char? = null
    )

    override fun insertList(list: List<String>) {
        disassembledStringList = list.toDisassembledStringList()
    }

    // 초성을 분리해서 match하는 것 모두 보여주기로
    override fun getMatchStrings(inputString: String): List<String> {
        val searchedString = mutableListOf<String>()
        disassembledStringList.forEach { currentDisassembledString ->
            if (checkMatches(currentDisassembledString, inputString.toDisassembledString())) searchedString.add(
                currentDisassembledString.originString
            )
        }
        return searchedString
    }

    /***
     * DisassembledString 타입으로 바꿔주는 함수
     * 홍길동을 ㅎㅗㅇㄱㅣㄹㄷㅗㅇ 으로 나눠주는 작업을 하고 있다
     */
    private fun List<String>.toDisassembledStringList(): List<DisassembledString> {
        val tempDisassembledStringList = mutableListOf<DisassembledString>()
        this.forEach { currentString ->
            tempDisassembledStringList.add(currentString.toDisassembledString())
        }
        return tempDisassembledStringList
    }

    /**
     * current string은 다음과 같은 것들이 될 수 있다.
     * 홍길동을 검색하고 싶을 때, 사용자는 아래와 같은 것들을 입력할 것이다.
     * ㅎ
     * ㅎㄱ
     * ㅎㄱㄷ
     * 홍
     * 홍길
     * 홍길동
     * input current
     * ㅇ원ㅈ 이원중
     * 잉ㅇ종 이원중
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

    private fun String.toDisassembledString(): DisassembledString {
        val currentCharList = mutableListOf<Char>()
        val currentChoList = mutableListOf<Char>()
        val currentJoongList = mutableListOf<Char>()
        val currentJongList = mutableListOf<Char>()
        val koreanCharList = mutableListOf<KoreanChar>()
        this.filterNot { it.isWhitespace() }.forEach { targetChar ->
            val currentHexValue = targetChar.code
            if (currentHexValue >= KOREAN_CHAR_START) { // '가' 이후일 경우
                val diff = currentHexValue - KOREAN_CHAR_START
                val currentCho = choList[(diff / 28 / 21)]
                val currentJoong = joongList[(diff / 28 % 21)]
                val currentJong = jongList[(diff % 28)]
                if (currentJong == null) {
                    currentCharList.apply {
                        add(currentCho)
                        add(currentJoong)
                    }
                    currentChoList.add(currentCho)
                    currentJoongList.add(currentJoong)
                    koreanCharList.add(KoreanChar(currentCho, currentJoong))
                } else {
                    currentCharList.apply {
                        add(currentCho)
                        add(currentJoong)
                        add(currentJong)
                    }
                    currentChoList.add(currentCho)
                    currentJoongList.add(currentJoong)
                    currentJongList.add(currentJong)
                    koreanCharList.add(KoreanChar(currentCho, currentJoong, currentJong))
                }
            } else {
                currentChoList.add(targetChar)
                currentCharList.add(targetChar)
                koreanCharList.add(KoreanChar(targetChar))
            }
        }
        return DisassembledString(
            this,
            currentCharList,
            currentChoList,
            currentJoongList,
            currentJongList,
            koreanCharList
        )
    }

    private fun disassembleCharacter(targetChar: Char): List<Char> {
        val currentHexValue = targetChar.code
        val currentCharList = mutableListOf<Char>()
        if (currentHexValue >= KOREAN_CHAR_START) {
            val diff = currentHexValue - KOREAN_CHAR_START
            val currentCho = choList[(diff / 28 / 21)]
            val currentJoong = joongList[(diff / 28 % 21)]
            val currentJong = jongList[(diff % 28)]
            // isBlank()는 공백만 있을 때 true를 반환
            return if (currentJong == null) {
                currentCharList.apply {
                    add(currentCho)
                    add(currentJoong)
                }
            } else {
                currentCharList.apply {
                    add(currentCho)
                    add(currentJoong)
                    add(currentJong)
                }
            }
        } else {
            return currentCharList.apply {
                add(targetChar)
            }
        }
    }
}