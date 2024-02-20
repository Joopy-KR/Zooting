# selenium 라이브러리 받아야 합니다. 명령어는 아래와 같습니다.
# pip install selenium

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import urllib.request
import time

dog = ['에릭남', '박보검', '송중기', '엑소 백현', '배우 이현우']
cat = ['워너원 황민현', '엑소 시우민', '강동원', '이준기', '지코']
bear = ['마동석', '조진웅', '안재홍', '김대명']
dino = ['윤두준', '이민기', '김우빈', '육성재', '샤이니 종현']
rabbit = ['방탄소년단 정국', '워너원 박지훈', 'NCT 도영', '엑소 수호', '투바투 수빈']

animals = [dog, cat]
ani_names = ['dog', 'cat', 'bear', 'dino', 'rabbit']

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