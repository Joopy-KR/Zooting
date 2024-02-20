# selenium 라이브러리 받아야 합니다. 명령어는 아래와 같습니다.
# pip install selenium

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import urllib.request
import time

dog = ['아이즈원 안유진 셀카', '프로미스나인 장규리 셀카', '위클리 이수진 셀카', '이달의 소녀 츄 셀카']
cat = ['블랙핑크 제니 고양이상', '씨엘씨 장예은 고양이상', '있지 류진 셀카', '있지 예지 셀카', '트와이스 쯔위 셀카']
turtle = ['마마무 솔라 꼬부기상', '하연수 꼬부기상', '브레이브걸스 유정 셀카']
rabbit = ['트와이스 나연 셀카', '오마이걸 효정 셀카', '프로미스나인 박지원 셀카', '스테이씨 수민 셀카', '엘리스 유경 셀카', '우주소녀 보나 셀카', '이달의 소녀 희진 셀카']
deer = ['소녀시대 윤아', '러블리즈 정예인 셀카', '오마이걸 비니', '우주소녀 설아 셀카', '(여자)아이들 미연 셀카', '오마이걸 아린 셀카', '아이즈원 김채원 셀카', '공원소녀 레나 셀카', '설윤 셀카']

animals = [dog, cat, turtle, rabbit, deer]
ani_names = ['dog', 'cat', 'turtle', 'rabbit', 'deer']

idx = -1
for animal in animals:
    idx += 1
    for i in range(len(animal)):

        chrome_options = Options()
        chrome_options.add_experimental_option("detach", True)
        # 크롬 창 안보이게 하는 함수 아래 주석 해제하면 동작하는 동안 크롬창 안뜸
        chrome_options.add_argument("headless")

        driver = webdriver.Chrome(options=chrome_options)
        driver.get("https://www.google.com/imghp?hl=ko&ogbl")
        elem = driver.find_element(By.NAME, "q")
        elem.send_keys(f"{animal[i]}")
        elem.send_keys(Keys.RETURN)

        SCROLL_PAUSE_TIME = 1

        # 현재 스크롤 높이 계산
        last_height = driver.execute_script("return document.body.scrollHeight")

        while True:
            # 스크롤 끝까지 내리기
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

            # 페이지 로딩 기다리기
            time.sleep(SCROLL_PAUSE_TIME)

            # 이미지 더보기 클릭, 없으면 종료
            new_height = driver.execute_script("return document.body.scrollHeight")
            if new_height == last_height:
                try:
                    driver.find_element(By.CLASS_NAME, "LZ4I").click()
                except:
                    break
            last_height = new_height

        time.sleep(2)
        images = driver.find_elements(By.CLASS_NAME, "rg_i.Q4LuWd")
        cnt = 1
        for image in images:
            try:
                image.click()
                time.sleep(2)
                imageUrl = driver.find_element(By.CLASS_NAME, "sFlh5c.pT0Scc.iPVvYb").get_attribute("src")
                urllib.request.urlretrieve(imageUrl, f"{ani_names[idx]}" + f"{animal[i]}" + str(cnt) + ".jpg")
                cnt += 1
                # 사람 한명당 몇개의 이미지를 받아올것인지 결정하는 숫자
                if cnt >= 100:
                    driver.close()
                    break
            except:
                pass