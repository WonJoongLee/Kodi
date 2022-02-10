# Kodi
초성 검색을 지원하는 한국어 문자열 비교 라이브러리입니다.<br>
Kodi는 Korean Disassembler의 약자입니다.

## 다운로드
추가 예정

## 예시
```kotlin
val stringList = listOf("이철수", "김영희", "이원중", "홍길동", "황괅뒳") 
val userInput = "ㅎㄱㄷ" 
Kodi.insertList(stringList)
val result = Kodi.getMatchStrings(userInput)
// result = ["홍길동", "황괅뒳"]
```
`홍ㄱㄷ`, `ㅎ길ㄷ` 등 초성이 중간에 섞여 있어도 검색이 가능합니다.

## 기타
혹시 문제를 발견하시면 개인적으로 연락을 주셔도 되고, 이슈로 남겨주셔도 됩니다.
감사합니다.<br>
email : realjoong@gmail.com