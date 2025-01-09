# 생각할 부분

# 1. Permission 별로 처리

- 버전별로 권한 처리 해주자
    1. READ
        - ~32
            - READ_EXTERNAL_STORAGE
        - 33~
            - READ_EXTERNAL_IMAGE
    2. WRITE
        - ~28
            - WRITE_EXTERNAL_STORAGE
        - 29~
            - MediaStore
- 권한은 해당 기능 사용할때 요청
- 거절 했을때 불이익 알려주자

---

# 2. MVI, Clean Architecture

- BaseViewModel 만들기
- 갤러리
    - DataSource의 Local 부분인거 같다
- SVG FILE
    - 얘도 DataSource의 Local 인가..?
- Cashe
    - 음..얘도 Local인거같은데 Assets folder가 data에 있으면 presentation에서 접근이 안되자나. 그런데 캐시는 app모듈 자체?
- domain을 거쳐야하니까  이미지, Svg, Bitmap 전부 String이나 ByteArray로 바꿔서 보내주자

---

# 3. 이미지, SVG 파일 불러오기

- 불러오면서 Memory, Disk 캐쉬에 저장??
- Coil을 쓰면 안되는건가…
    - 메모리, 디스크 캐쉬에 저장
    - 이미지 불러올때 메모리, 디스크캐쉬 확인 먼저
        - 캐쉬 O - 거기서 가져오기
        - 캐쉬 X - 갤러리 or SvgFolder 직접 접근?
    - LRU알고리즘 직접 구현해야하나? 흠
        - 캐쉬 부족할때 사용안하는거 캐쉬에서 제거하기
    - 비동기 따로 내려주기
    - SVG 불러오는 것도.. 알아서,,?? Coil쓰지말고?
- 이미지 불러올때 UseCase에서 캐쉬에 있나 확인 후 Boolean오면 캐쉬에서 가져오도록?
  - 이미지로더는 우선 UseCase에서 못쓰겠네
- Album, PhotoPicker는 Thumnail 이미지로 보여주자
    - 파일 줄이기
- Photo Picker화면 이미지 6개씩 비동기로..?

---

# 4. 이미지 합성하기

- svg열 - 썸네일 이미지, 클릭해서 합성할때는 원본 띄우기
- 메인 이미지 - 원본으로


---

# 5. 이미지 저장하기

- 저장할때 Cashe에 할지 Gallery에 할지 물어보기?
    - Cashe에 한다하면 사라질수 있다고 알려주기
    - 불러올때 “로컬앨범”으로 앨범 하나 만들어서 Cashe에 있는 앨범들보여주기?
    - 서버 있으면 서버에 저장하기 (용량 정해줘도 될듯? 넘으면 돈내놔~)
- 메모리 캐쉬, 디스크 캐쉬 용량 확인하기?

---
# 자자... 생각해보자
- 앨범 가져오면서 갤러리에있는 사진 전체 메모리 캐시 or 디스크 캐시에 저장?
- 불러올때 UseCase에서 메모리, 디스크 캐쉬 확인후 False(없다)오면 그때 갤러리, AssetFolder 접근?
- 합성할때 빼고 전부 썸네일 이미지로?

메모리, 디스크 캐쉬에 넣는 과정? (앨범 가져올때)
앨범 요청 -> 뷰모델 -> 캐쉬저장유스케이스 -> 앨범레포 -> 앨범Local 데이터소스 -> 갤러리 (이미지 get) -> ... -> 캐쉬저장 UseCase 에서 이미지 가져오는거 성공시 Cashe에 저장 -> 캐쉬레포 -> 캐쉬데이터소스 -> 캐쉬 - 저장 


# 문제
- 이미지 캐쉬에 다 저장하니 앱이 터졌다..?
  - 로드 되는 애들만 저장하게 해야할거같다.
    - 로드 되면서 캐시에 저장하는걸로
    - 저장할때도 압축해서 
- 큰파일 저장할때 ANR 나거나, 너무 오래걸린다
  - ANR - 디스패처를 IO로 바꿔주자
  - 오래걸리는부분 - peresentation에서 compress하고 data부분에서 비트맵변환 후 또 압축하고 있었네;
  - compress할때 시간이 오래걸린다. 이때 "압축중..." 이라는 메세지를 보내주고, 저장할때 "저장중..." 이라고하자
- 아니 근데 cache저장을 
