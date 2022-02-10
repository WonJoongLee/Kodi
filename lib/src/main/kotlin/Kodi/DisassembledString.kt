package Kodi

data class DisassembledString(
    val originString: String, // 원래 String
    val disassembledCharList: List<KoreanChar>
)