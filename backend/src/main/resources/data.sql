INSERT INTO MEMBER (NICKNAME, CREATED_AT, UPDATED_AT, PASSWORD)
VALUES ('dora', '2024-07-15 00:00:00', '2024-07-15 00:00:00', 'PtgtJCnn307FyCBvRprsy+42rX7dg00qVLWkPbl2Ag0='),
       ('poke', '2024-07-15 00:00:00', '2024-07-15 00:00:00', 'XimRQY0Y2avPH6KxGK4ZOXB4+MT3Sfb605ZEPidVNpQ='),
       ('mason', '2024-07-15 00:00:00', '2024-07-15 00:00:00', 'WBmuFn7FSb5jG03SKBB0K7MNk0mNg9FLPoHyTbi4Tl0='),
       ('ever', '2024-07-15 00:00:00', '2024-07-15 00:00:00', 'UhN2JlsRhvNn7XY2WYlfDwI9/d/XoRvr8Ls7tbeYZWg='),
       ('alsong', '2024-07-15 00:00:00', '2024-07-15 00:00:00', '+qY3Pnqyjj9amVGZ1Bu63iJX6cpon7kQiIvqAG0ExkE='),
       ('seogi', '2024-07-15 00:00:00', '2024-07-15 00:00:00', '0CWUdyVQ1TP+GGlI9W2d5Gao/5HgT0MSeIwald0Qcsw='),
       ('chaechae', '2024-07-15 00:00:00', '2024-07-15 00:00:00', 'WCkwnMjy/yW6odwkADguEIcHjFVELq+JLy+WeojvJ88=');

INSERT INTO OFFERING (TOTAL_COUNT, CURRENT_COUNT, TOTAL_PRICE, ORIGIN_PRICE, DISCOUNT_RATE,
                      CREATED_AT, UPDATED_AT, MEMBER_ID, MEETING_DATE, DESCRIPTION, MEETING_ADDRESS,
                      MEETING_ADDRESS_DETAIL,
                      MEETING_ADDRESS_DONG, PRODUCT_URL, THUMBNAIL_URL, TITLE, OFFERING_STATUS, ROOM_STATUS)
VALUES (8, 6, 10000, 1838, 32, '2024-04-22 00:00:00', '2024-04-22 00:00:00', 1, '2024-08-18 00:00:00',
        '공동 구매 해요  1', '서울특별시 강남구 테헤란로 201', '101동 101호', '봉천동', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '음쓰 봉투 공구하실 분?', 'IMMINENT',
        'DONE'),
       (6, 5, 5000, 1108, 24.8, '2024-04-25 00:00:00', '2024-04-25 00:00:00', 2, '2024-08-25 00:00:00',
        '공동 구매 해요  2', '대전광역시 서구 탄방동 크로바아파트', '탄방동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '흰색 여자 양말 같이 사요', 'IMMINENT',
        'TRADING'),
       (5, 3, 30000, 11040, 45.7, '2024-03-24 00:00:00', '2024-03-24 00:00:00', 3, '2024-08-20 00:00:00',
        '공동 구매 해요  3', '서울특별시 마포구 양화로 45', '신천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '🍀 종합비타민 대량구매 같이 할 사람 구함',
        'IMMINENT', 'TRADING'),
       (6, 6, 25000, 6667, 37.5, '2024-01-31 00:00:00', '2024-01-31 00:00:00', 4, '2024-08-28 00:00:00',
        '공동 구매 해요  4', '대전광역시 서구 탄방동 크로바아파트', '둔산동', '', '',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '수영 모자 공동구매 모집', 'FULL',
        'BUYING'),
       (7, 1, 40000, 6343, 9.9, '2024-03-28 00:00:00', '2024-03-28 00:00:00', 2, '2024-09-06 00:00:00',
        '공동 구매 해요  5', '서울특별시 영등포구 여의대로 24', '여의도동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '뉴진스 굿즈 구입해요', 'AVAILABLE',
        'GROUPING'),
       (10, 3, 15000, 1605, 6.5, '2024-08-04 00:00:00', '2024-08-04 00:00:00', 1, '2024-09-11 00:00:00',
        '공동 구매 해요  6', '서울특별시 송파구 올림픽로 300', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '양말 나눠 사요', 'AVAILABLE',
        'GROUPING'),
       (8, 4, 40000, 9300, 46.2, '2024-08-06 00:00:00', '2024-08-06 00:00:00', 1, '2024-08-16 00:00:00',
        '공동 구매 해요  7', '서울특별시 강북구 도봉로 76', '삼성동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '젤리 같이 구매해요', 'AVAILABLE',
        'TRADING'),
       (10, 6, 45000, 7830, 42.5, '2024-02-15 00:00:00', '2024-02-15 00:00:00', 1, '2024-09-05 00:00:00',
        '공동 구매 해요  8', '서울특별시 동대문구 왕산로 128', '봉천동', '101동 101호', '',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '선풍기 공구 참가', 'AVAILABLE',
        'DONE'),
       (9, 4, 50000, 6501, 14.5, '2024-08-04 00:00:00', '2024-08-04 00:00:00', 1, '2024-09-01 00:00:00',
        '공동 구매 해요  9', '서울특별시 중랑구 봉화산로 224', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '이불 공구해요', 'AVAILABLE',
        'TRADING'),
       (7, 1, 10000, 1843, 22.5, '2024-02-16 00:00:00', '2024-02-16 00:00:00', 6, '2024-09-08 00:00:00',
        '공동 구매 해요  10', '대전광역시 서구 탄방동 크로바아파트', '노은동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '간식 공구 모집', 'AVAILABLE',
        'GROUPING'),
       (7, 1, 5000, 850, 16, '2024-05-15 00:00:00', '2024-05-15 00:00:00', 6, '2024-08-27 00:00:00',
        '공동 구매 해요  11', '서울특별시 광진구 능동로 76', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '고양이 인형 공동구매', 'AVAILABLE',
        'DONE'),
       (5, 2, 40000, 15120, 47.1, '2024-04-21 00:00:00', '2024-04-21 00:00:00', 7, '2024-08-23 00:00:00',
        '공동 구매 해요  12', '서울특별시 동작구 상도로 153', '', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '강아지 인형 나눠요', 'AVAILABLE',
        'BUYING'),
       (9, 7, 10000, 1622, 31.5, '2024-02-20 00:00:00', '2024-02-20 00:00:00', 1, '2024-09-09 00:00:00',
        '공동 구매 해요  13', '서울특별시 서대문구 통일로 484', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '에스파 굿즈 공동구매', 'IMMINENT',
        'GROUPING'),
       (6, 2, 50000, 9750, 14.5, '2024-01-05 00:00:00', '2024-01-05 00:00:00', 7, '2024-09-12 00:00:00',
        '공동 구매 해요  14', '서울특별시 은평구 진흥로 215', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '뉴진스 굿즈 공동 구매', 'AVAILABLE',
        'BUYING'),
       (6, 5, 10000, 1884, 11.5, '2024-05-19 00:00:00', '2024-05-19 00:00:00', 1, '2024-09-07 00:00:00',
        '공동 구매 해요  15', '서울특별시 강서구 화곡로 266', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '양말 같이 사요', 'IMMINENT',
        'BUYING'),
       (6, 2, 5000, 1374, 39.3, '2024-06-11 00:00:00', '2024-06-11 00:00:00', 4, '2024-08-15 00:00:00',
        '공동 구매 해요  16', '서울특별시 구로구 디지털로 288', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '젤리 공구 관심?', 'AVAILABLE',
        'BUYING'),
       (9, 7, 35000, 4278, 9.1, '2024-05-14 00:00:00', '2024-05-14 00:00:00', 2, '2024-09-11 00:00:00',
        '공동 구매 해요  17', '서울특별시 금천구 시흥대로 97', '', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '선풍기 같이 사요', 'IMMINENT',
        'TRADING'),
       (10, 4, 25000, 3750, 33.3, '2024-03-21 00:00:00', '2024-03-21 00:00:00', 4, '2024-09-01 00:00:00',
        '공동 구매 해요  18', '서울특별시 양천구 목동중앙로 132', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '이불 구매 모집', 'AVAILABLE',
        'GROUPING'),
       (5, 3, 20000, 5000, 20, '2024-07-25 00:00:00', '2024-07-25 00:00:00', 5, '2024-09-07 00:00:00',
        '공동 구매 해요  19', '서울특별시 관악구 남부순환로 1419', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '간식 싸게 사요', 'IMMINENT',
        'DONE'),
       (9, 3, 20000, 4044, 45, '2024-05-10 00:00:00', '2024-05-10 00:00:00', 1, '2024-08-31 00:00:00',
        '공동 구매 해요  20', '서울특별시 노원구 동일로 910', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '고양이 인형 구입해요', 'AVAILABLE',
        'DONE'),
       (10, 4, 20000, 2180, 8.3, '2024-04-26 00:00:00', '2024-04-26 00:00:00', 3, '2024-08-18 00:00:00',
        '공동 구매 해요  21', '서울특별시 용산구 한강대로 405', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '강아지 인형 공구', 'AVAILABLE',
        'BUYING'),
       (10, 5, 5000, 760, 34.2, '2024-07-01 00:00:00', '2024-07-01 00:00:00', 5, '2024-08-28 00:00:00',
        '공동 구매 해요  22', '서울특별시 중구 퇴계로 100', '봉천동', '101동 101호', '',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '에스파 굿즈 같이 사요', 'AVAILABLE',
        'BUYING'),
       (8, 7, 15000, 2119, 11.5, '2024-08-12 00:00:00', '2024-08-12 00:00:00', 6, '2024-08-16 00:00:00',
        '공동 구매 해요  23', '서울특별시 종로구 세종대로 175', '', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '뉴진스 굿즈 나눠요', 'IMMINENT',
        'DONE'),
       (9, 9, 10000, 1367, 18.7, '2024-06-21 00:00:00', '2024-06-21 00:00:00', 2, '2024-08-27 00:00:00',
        '공동 구매 해요  24', '서울특별시 강남구 언주로 563', '봉천동', '', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '양말 공구해요', 'FULL', 'DONE'),
       (6, 1, 45000, 12300, 39, '2024-05-30 00:00:00', '2024-05-30 00:00:00', 6, '2024-08-30 00:00:00',
        '공동 구매 해요  25', '서울특별시 송파구 가락로 102', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '젤리 공구 참가해요', 'AVAILABLE',
        'DONE'),
       (5, 5, 15000, 4260, 29.6, '2024-06-03 00:00:00', '2024-06-03 00:00:00', 5, '2024-08-27 00:00:00',
        '공동 구매 해요  26', '서울특별시 서초구 반포대로 158', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '선풍기 공동 구매', 'FULL',
        'BUYING'),
       (7, 6, 15000, 3900, 45.1, '2024-05-09 00:00:00', '2024-05-09 00:00:00', 4, '2024-08-25 00:00:00',
        '공동 구매 해요  27', '서울특별시 영등포구 경인로 846', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '이불 싸게 구입', 'IMMINENT',
        'DONE'),
       (8, 2, 30000, 7238, 48.2, '2024-07-11 00:00:00', '2024-07-11 00:00:00', 5, '2024-09-05 00:00:00',
        '공동 구매 해요  28', '서울특별시 마포구 신촌로 102', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '간식 공구 참가', 'AVAILABLE',
        'BUYING'),
       (8, 5, 10000, 2288, 45.4, '2024-06-23 00:00:00', '2024-06-23 00:00:00', 5, '2024-08-30 00:00:00',
        '공동 구매 해요  29', '서울특별시 동대문구 장한로 164', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '고양이 인형 구매 모집', 'AVAILABLE',
        'TRADING'),
       (10, 3, 30000, 5190, 42.2, '2024-07-14 00:00:00', '2024-07-14 00:00:00', 1, '2024-09-12 00:00:00',
        '공동 구매 해요  30', '서울특별시 성동구 뚝섬로 377', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '강아지 인형 싸게 사요', 'AVAILABLE',
        'TRADING'),
       (5, 2, 25000, 9400, 46.8, '2024-06-20 00:00:00', '2024-06-20 00:00:00', 6, '2024-09-02 00:00:00',
        '공동 구매 해요  31', '서울특별시 강북구 한천로 1060', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '에스파 굿즈 싸게 사요', 'AVAILABLE',
        'GROUPING'),
       (10, 10, 50000, 6450, 22.5, '2024-07-31 00:00:00', '2024-07-31 00:00:00', 2, '2024-09-03 00:00:00',
        '공동 구매 해요  32', '서울특별시 도봉구 노해로 403', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '뉴진스 굿즈 공구 참가', 'FULL',
        'TRADING'),
       (10, 10, 15000, 2835, 47.1, '2024-08-06 00:00:00', '2024-08-06 00:00:00', 1, '2024-08-29 00:00:00',
        '공동 구매 해요  33', '서울특별시 성북구 정릉로 77', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '양말 공동 구매', 'FULL',
        'TRADING'),
       (7, 6, 35000, 8800, 43.2, '2024-05-04 00:00:00', '2024-05-04 00:00:00', 3, '2024-08-22 00:00:00',
        '공동 구매 해요  34', '서울특별시 강동구 천호대로 1017', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '젤리 나눠 사요', 'IMMINENT',
        'TRADING'),
       (6, 4, 40000, 12801, 47.9, '2024-03-02 00:00:00', '2024-03-02 00:00:00', 4, '2024-09-03 00:00:00',
        '공동 구매 해요  35', '서울특별시 서대문구 홍제천로 123', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '선풍기 싸게 사요', 'IMMINENT',
        'DONE'),
       (5, 1, 5000, 1560, 35.9, '2024-04-05 00:00:00', '2024-04-05 00:00:00', 5, '2024-09-07 00:00:00',
        '공동 구매 해요  36', '서울특별시 은평구 불광로 87', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '이불 공동구매 모집', 'AVAILABLE',
        'TRADING'),
       (5, 4, 15000, 4830, 37.9, '2024-08-11 00:00:00', '2024-08-11 00:00:00', 1, '2024-08-19 00:00:00',
        '공동 구매 해요  37', '서울특별시 강서구 공항대로 247', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '간식 구입해요', 'IMMINENT',
        'TRADING'),
       (5, 4, 50000, 12500, 20, '2024-03-16 00:00:00', '2024-03-16 00:00:00', 3, '2024-09-04 00:00:00',
        '공동 구매 해요  38', '서울특별시 구로구 구로동로 148', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '고양이 인형 같이 사요', 'IMMINENT',
        'TRADING'),
       (10, 1, 20000, 2960, 32.4, '2024-05-30 00:00:00', '2024-05-30 00:00:00', 1, '2024-09-06 00:00:00',
        '공동 구매 해요  39', '서울특별시 금천구 금하로 242', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '강아지 인형 공구 모집', 'AVAILABLE',
        'BUYING'),
       (6, 1, 40000, 12134, 45.1, '2024-07-26 00:00:00', '2024-07-26 00:00:00', 1, '2024-08-29 00:00:00',
        '공동 구매 해요  40', '서울특별시 양천구 목동서로 225', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '에스파 굿즈 구입 모집', 'AVAILABLE',
        'BUYING'),
       (10, 6, 30000, 3990, 24.8, '2024-02-26 00:00:00', '2024-02-26 00:00:00', 5, '2024-08-15 00:00:00',
        '공동 구매 해요  41', '서울특별시 관악구 관악로 92', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '뉴진스 굿즈 같이 사요', 'AVAILABLE',
        'DONE'),
       (7, 6, 5000, 885, 19.3, '2024-03-11 00:00:00', '2024-03-11 00:00:00', 4, '2024-08-19 00:00:00',
        '공동 구매 해요  42', '서울특별시 노원구 상계로 138', '봉천동', '101동 101호', '',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '양말 공구 관심?', 'IMMINENT',
        'GROUPING'),
       (6, 1, 45000, 11025, 32, '2024-06-15 00:00:00', '2024-06-15 00:00:00', 2, '2024-08-13 00:00:00',
        '공동 구매 해요  43', '서울특별시 용산구 이태원로 55', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '젤리 공동 구매', 'IMMINENT',
        'DONE'),
       (8, 8, 30000, 7200, 47.9, '2024-02-29 00:00:00', '2024-02-29 00:00:00', 6, '2024-09-07 00:00:00',
        '공동 구매 해요  44', '서울특별시 중구 을지로 130', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '선풍기 공구 모집', 'FULL',
        'TRADING'),
       (9, 1, 20000, 2644, 16, '2024-08-09 00:00:00', '2024-08-09 00:00:00', 5, '2024-09-04 00:00:00',
        '공동 구매 해요  45', '서울특별시 종로구 자하문로 125', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '이불 같이 구매해요', 'AVAILABLE',
        'DONE'),
       (5, 3, 30000, 8040, 25.4, '2024-04-29 00:00:00', '2024-04-29 00:00:00', 1, '2024-09-03 00:00:00',
        '공동 구매 해요  46', '서울특별시 강남구 역삼로 123', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '간식 공동 구매', 'IMMINENT',
        'DONE'),
       (6, 5, 50000, 10000, 16.7, '2024-05-29 00:00:00', '2024-05-29 00:00:00', 7, '2024-09-04 00:00:00',
        '공동 구매 해요  47', '서울특별시 송파구 문정로 140', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '고양이 인형 나눠 사요', 'IMMINENT',
        'GROUPING'),
       (8, 6, 30000, 4463, 16, '2024-04-03 00:00:00', '2024-04-03 00:00:00', 2, '2024-08-23 00:00:00',
        '공동 구매 해요  48', '서울특별시 서초구 바우뫼로 36', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '강아지 인형 구입 모집', 'IMMINENT',
        'GROUPING'),
       (7, 2, 40000, 9314, 38.6, '2024-01-22 00:00:00', '2024-01-22 00:00:00', 3, '2024-08-21 00:00:00',
        '공동 구매 해요  49', '서울특별시 영등포구 도림로 230', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '에스파 굿즈 공동 구매', 'AVAILABLE',
        'GROUPING'),
       (6, 2, 35000, 10499, 44.4, '2024-06-10 00:00:00', '2024-06-10 00:00:00', 1, '2024-08-17 00:00:00',
        '공동 구매 해요  50', '서울특별시 마포구 마포대로 33', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '뉴진스 굿즈 공구 모집', 'AVAILABLE',
        'DONE'),
       (9, 4, 45000, 6300, 20.6, '2024-03-17 00:00:00', '2024-03-17 00:00:00', 2, '2024-09-07 00:00:00',
        '공동 구매 해요  51', '서울특별시 동대문구 무학로 52', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '양말 싸게 구입', 'AVAILABLE',
        'GROUPING'),
       (8, 3, 15000, 2213, 15.3, '2024-02-13 00:00:00', '2024-02-13 00:00:00', 5, '2024-08-14 00:00:00',
        '공동 구매 해요  52', '서울특별시 성동구 왕십리로 34', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '젤리 싸게 사요', 'IMMINENT',
        'TRADING'),
       (10, 4, 45000, 7965, 43.5, '2024-04-29 00:00:00', '2024-04-29 00:00:00', 2, '2024-09-05 00:00:00',
        '공동 구매 해요  53', '서울특별시 강북구 삼양로 234', '봉천동', '101동 101호', '',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '선풍기 공구 관심?', 'AVAILABLE',
        'TRADING'),
       (10, 7, 45000, 8415, 46.5, '2024-02-05 00:00:00', '2024-02-05 00:00:00', 3, '2024-08-25 00:00:00',
        '공동 구매 해요  54', '서울특별시 도봉구 도봉로 678', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '이불 공구 참가해요', 'AVAILABLE',
        'BUYING'),
       (10, 2, 10000, 1490, 32.9, '2024-06-06 00:00:00', '2024-06-06 00:00:00', 5, '2024-09-10 00:00:00',
        '공동 구매 해요  55', '서울특별시 성북구 보문로 114', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '간식 공구해요', 'AVAILABLE',
        'GROUPING'),
       (8, 7, 25000, 3313, 5.7, '2024-03-01 00:00:00', '2024-03-01 00:00:00', 7, '2024-08-23 00:00:00',
        '공동 구매 해요  56', '서울특별시 강동구 양재대로 1576', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '고양이 인형 공구 참가', 'IMMINENT',
        'TRADING'),
       (8, 4, 30000, 5550, 32.4, '2024-01-21 00:00:00', '2024-01-21 00:00:00', 6, '2024-09-05 00:00:00',
        '공동 구매 해요  57', '서울특별시 서대문구 남가좌로 25', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '강아지 인형 공동 구매', 'AVAILABLE',
        'DONE'),
       (6, 2, 10000, 2701, 38.3, '2024-04-04 00:00:00', '2024-04-04 00:00:00', 2, '2024-09-11 00:00:00',
        '공동 구매 해요  58', '서울특별시 은평구 연서로 14', '봉천동', '101동 101호', '',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '에스파 굿즈 나눠요', 'AVAILABLE',
        'GROUPING'),
       (9, 7, 15000, 1750, 4.8, '2024-04-03 00:00:00', '2024-04-03 00:00:00', 2, '2024-08-17 00:00:00',
        '공동 구매 해요  59', '서울특별시 강서구 방화대로 228', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '뉴진스 굿즈 같이 구매', 'IMMINENT',
        'BUYING'),
       (7, 4, 35000, 6200, 19.4, '2024-01-02 00:00:00', '2024-01-02 00:00:00', 6, '2024-08-18 00:00:00',
        '공동 구매 해요  60', '서울특별시 구로구 가마산로 33', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '양말 구입해요', 'AVAILABLE',
        'TRADING'),
       (10, 7, 15000, 1875, 20, '2024-05-14 00:00:00', '2024-05-14 00:00:00', 5, '2024-08-13 00:00:00',
        '공동 구매 해요  61', '서울특별시 금천구 독산로 79', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '젤리 공구 모집', 'IMMINENT',
        'DONE'),
       (7, 5, 20000, 4714, 39.4, '2024-01-01 00:00:00', '2024-01-01 00:00:00', 6, '2024-08-21 00:00:00',
        '공동 구매 해요  62', '서울특별시 양천구 신월로 23', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '선풍기 나눠 사요', 'IMMINENT',
        'BUYING'),
       (8, 3, 30000, 7013, 46.5, '2024-04-28 00:00:00', '2024-04-28 00:00:00', 7, '2024-09-09 00:00:00',
        '공동 구매 해요  63', '서울특별시 관악구 신림로 200', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '이불 공동구매해요', 'AVAILABLE',
        'GROUPING'),
       (5, 3, 5000, 1290, 22.5, '2024-05-12 00:00:00', '2024-05-12 00:00:00', 1, '2024-08-20 00:00:00',
        '공동 구매 해요  64', '서울특별시 노원구 초안산로 25', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '간식 나눠 사요', 'IMMINENT',
        'BUYING'),
       (5, 4, 50000, 11500, 13, '2024-03-15 00:00:00', '2024-03-15 00:00:00', 1, '2024-09-12 00:00:00',
        '공동 구매 해요  65', '서울특별시 용산구 한남대로 130', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '고양이 인형 싸게 구매', 'IMMINENT',
        'BUYING'),
       (8, 2, 25000, 4969, 37.1, '2024-01-09 00:00:00', '2024-01-09 00:00:00', 1, '2024-08-18 00:00:00',
        '공동 구매 해요  66', '서울특별시 중구 다산로 91', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '강아지 인형 공동구매 모집',
        'AVAILABLE', 'DONE'),
       (5, 4, 25000, 7300, 31.5, '2024-01-26 00:00:00', '2024-01-26 00:00:00', 5, '2024-09-04 00:00:00',
        '공동 구매 해요  67', '서울특별시 종로구 통일로 1', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '에스파 굿즈 공구 관심?', 'IMMINENT',
        'BUYING'),
       (9, 8, 50000, 6112, 9.1, '2024-04-13 00:00:00', '2024-04-13 00:00:00', 2, '2024-08-24 00:00:00',
        '공동 구매 해요  68', '서울특별시 강남구 학동로 123', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '뉴진스 굿즈 싸게 사요', 'IMMINENT',
        'TRADING'),
       (5, 4, 10000, 2120, 5.7, '2024-03-31 00:00:00', '2024-03-31 00:00:00', 3, '2024-09-08 00:00:00',
        '공동 구매 해요  69', '서울특별시 송파구 백제고분로 400', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '양말 같이 구매해요', 'IMMINENT',
        'TRADING'),
       (5, 5, 20000, 7120, 43.8, '2024-07-30 00:00:00', '2024-07-30 00:00:00', 4, '2024-09-12 00:00:00',
        '공동 구매 해요  70', '서울특별시 서초구 내방로 19', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '젤리 구입 모집', 'FULL',
        'TRADING'),
       (6, 2, 25000, 5167, 19.4, '2024-07-15 00:00:00', '2024-07-15 00:00:00', 5, '2024-08-16 00:00:00',
        '공동 구매 해요  71', '서울특별시 영등포구 당산로 275', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '선풍기 공구해요', 'AVAILABLE',
        'TRADING'),
       (10, 5, 10000, 1900, 47.4, '2024-02-19 00:00:00', '2024-02-19 00:00:00', 7, '2024-08-27 00:00:00',
        '공동 구매 해요  72', '서울특별시 마포구 월드컵로 240', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '이불 싸게 사요', 'AVAILABLE',
        'DONE'),
       (10, 8, 5000, 975, 48.7, '2024-04-29 00:00:00', '2024-04-29 00:00:00', 4, '2024-09-08 00:00:00',
        '공동 구매 해요  73', '서울특별시 동대문구 고산자로 550', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '간식 공동구매 모집', 'IMMINENT',
        'BUYING'),
       (5, 4, 30000, 9420, 36.3, '2024-02-08 00:00:00', '2024-02-08 00:00:00', 6, '2024-08-18 00:00:00',
        '공동 구매 해요  74', '서울특별시 성동구 성수이로 85', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '고양이 인형 공구 모집', 'IMMINENT',
        'DONE'),
       (10, 9, 15000, 2850, 47.4, '2024-07-27 00:00:00', '2024-07-27 00:00:00', 4, '2024-08-22 00:00:00',
        '공동 구매 해요  75', '서울특별시 강북구 인수봉로 215', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '강아지 인형 같이 구매', 'IMMINENT',
        'GROUPING'),
       (6, 1, 20000, 6233, 46.5, '2024-07-23 00:00:00', '2024-07-23 00:00:00', 3, '2024-09-06 00:00:00',
        '공동 구매 해요  76', '서울특별시 도봉구 방학로 210', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '에스파 굿즈 구입해요', 'AVAILABLE',
        'GROUPING'),
       (9, 4, 10000, 1244, 10.7, '2024-02-29 00:00:00', '2024-02-29 00:00:00', 3, '2024-08-27 00:00:00',
        '공동 구매 해요  77', '서울특별시 성북구 안암로 80', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '뉴진스 굿즈 공구해요', 'AVAILABLE',
        'BUYING'),
       (9, 9, 20000, 4288, 48.2, '2024-04-17 00:00:00', '2024-04-17 00:00:00', 3, '2024-09-01 00:00:00',
        '공동 구매 해요  78', '서울특별시 강동구 구천면로 10', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '양말 공구 참가', 'FULL', 'DONE'),
       (5, 1, 35000, 12950, 45.9, '2024-07-13 00:00:00', '2024-07-13 00:00:00', 6, '2024-09-04 00:00:00',
        '공동 구매 해요  79', '서울특별시 서대문구 명지대길 22', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '젤리 나눠 사요', 'AVAILABLE',
        'DONE'),
       (9, 7, 50000, 7834, 29.1, '2024-02-05 00:00:00', '2024-02-05 00:00:00', 4, '2024-08-16 00:00:00',
        '공동 구매 해요  80', '서울특별시 은평구 응암로 94', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '선풍기 구입 모집', 'IMMINENT',
        'DONE'),
       (5, 5, 10000, 2420, 17.4, '2024-03-20 00:00:00', '2024-03-20 00:00:00', 6, '2024-08-13 00:00:00',
        '공동 구매 해요  81', '서울특별시 강서구 마곡중앙로 87', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '이불 공구 관심?', 'FULL', 'DONE'),
       (6, 2, 15000, 3775, 33.8, '2024-03-25 00:00:00', '2024-03-25 00:00:00', 4, '2024-08-26 00:00:00',
        '공동 구매 해요  82', '서울특별시 구로구 중앙로 15', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '간식 공구 참가해요', 'AVAILABLE',
        'BUYING'),
       (5, 1, 50000, 15700, 36.3, '2024-02-04 00:00:00', '2024-02-04 00:00:00', 1, '2024-08-22 00:00:00',
        '공동 구매 해요  83', '서울특별시 금천구 서부샛길 64', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '고양이 인형 공동구매', 'AVAILABLE',
        'TRADING'),
       (7, 4, 50000, 8143, 12.3, '2024-07-09 00:00:00', '2024-07-09 00:00:00', 7, '2024-09-10 00:00:00',
        '공동 구매 해요  84', '서울특별시 양천구 목동로 54', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '강아지 인형 같이 사요', 'AVAILABLE',
        'TRADING'),
       (5, 4, 45000, 12690, 29.1, '2024-03-12 00:00:00', '2024-03-12 00:00:00', 3, '2024-09-13 00:00:00',
        '공동 구매 해요  85', '서울특별시 관악구 인헌길 67', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '에스파 굿즈 싸게 구입', 'IMMINENT',
        'BUYING'),
       (5, 4, 35000, 8960, 21.9, '2024-03-26 00:00:00', '2024-03-26 00:00:00', 7, '2024-08-30 00:00:00',
        '공동 구매 해요  86', '서울특별시 노원구 중계로 155', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '뉴진스 굿즈 나눠 사요', 'IMMINENT',
        'BUYING'),
       (5, 2, 25000, 7300, 31.5, '2024-05-02 00:00:00', '2024-05-02 00:00:00', 5, '2024-08-16 00:00:00',
        '공동 구매 해요  87', '서울특별시 용산구 후암로 104', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '양말 공동구매 모집', 'AVAILABLE',
        'TRADING'),
       (9, 9, 50000, 9723, 42.9, '2024-04-29 00:00:00', '2024-04-29 00:00:00', 5, '2024-08-29 00:00:00',
        '공동 구매 해요  88', '서울특별시 중구 충무로 57', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '젤리 공구 모집해요', 'FULL',
        'DONE'),
       (10, 9, 20000, 2880, 30.6, '2024-06-05 00:00:00', '2024-06-05 00:00:00', 1, '2024-08-18 00:00:00',
        '공동 구매 해요  89', '서울특별시 종로구 대학로 116', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '선풍기 싸게 구매', 'IMMINENT',
        'TRADING'),
       (8, 3, 40000, 7900, 36.7, '2024-01-15 00:00:00', '2024-01-15 00:00:00', 6, '2024-08-24 00:00:00',
        '공동 구매 해요  90', '서울특별시 강남구 논현로 85', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '이불 같이 사요', 'AVAILABLE',
        'BUYING'),
       (5, 1, 20000, 6240, 35.9, '2024-01-24 00:00:00', '2024-01-24 00:00:00', 1, '2024-09-09 00:00:00',
        '공동 구매 해요  91', '서울특별시 송파구 오금로 200', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '간식 구입 모집', 'AVAILABLE',
        'DONE'),
       (7, 1, 30000, 6729, 36.3, '2024-05-26 00:00:00', '2024-05-26 00:00:00', 2, '2024-08-15 00:00:00',
        '공동 구매 해요  92', '서울특별시 서초구 방배로 56', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/f280868d-bbab-424e-8d2d-aa0f8ad5029a', '고양이 인형 공구 관심?', 'AVAILABLE',
        'TRADING'),
       (8, 1, 45000, 6413, 12.3, '2024-05-26 00:00:00', '2024-05-26 00:00:00', 7, '2024-08-26 00:00:00',
        '공동 구매 해요  93', '서울특별시 영등포구 문래로 180', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '강아지 인형 구입해요', 'AVAILABLE',
        'DONE'),
       (8, 5, 40000, 9350, 46.5, '2024-08-12 00:00:00', '2024-08-12 00:00:00', 3, '2024-09-12 00:00:00',
        '공동 구매 해요  94', '서울특별시 마포구 독막로 29', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '에스파 굿즈 공구 참가', 'AVAILABLE',
        'DONE'),
       (5, 1, 45000, 16470, 45.4, '2024-07-13 00:00:00', '2024-07-13 00:00:00', 5, '2024-09-13 00:00:00',
        '공동 구매 해요  95', '서울특별시 동대문구 답십리로 189', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/87de86ac-b07e-4297-ac29-425b635fbae3', '뉴진스 굿즈 공동 구매', 'AVAILABLE',
        'BUYING'),
       (8, 1, 25000, 4000, 21.9, '2024-03-03 00:00:00', '2024-03-03 00:00:00', 6, '2024-08-16 00:00:00',
        '공동 구매 해요  96', '서울특별시 성동구 독서당로 154', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/897ef4cf-006e-4d88-9e91-d15bf37e9063', '양말 나눠 사요', 'AVAILABLE',
        'TRADING'),
       (6, 2, 50000, 15833, 47.4, '2024-08-12 00:00:00', '2024-08-12 00:00:00', 1, '2024-09-04 00:00:00',
        '공동 구매 해요  97', '서울특별시 강북구 수유로 102', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/c6b07e2f-3c7a-44f9-8d3c-a3431866d8f3', '젤리 싸게 구입', 'AVAILABLE',
        'DONE'),
       (5, 1, 15000, 3990, 24.8, '2024-06-17 00:00:00', '2024-06-17 00:00:00', 5, '2024-08-24 00:00:00',
        '공동 구매 해요  98', '서울특별시 도봉구 쌍문로 28', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '선풍기 공동구매 모집', 'AVAILABLE',
        'GROUPING'),
       (5, 5, 30000, 7140, 16, '2024-06-08 00:00:00', '2024-06-08 00:00:00', 3, '2024-09-04 00:00:00',
        '공동 구매 해요  99', '서울특별시 성북구 길음로 12', '봉천동', '101동 101호', 'www.naver.com',
        'https://github.com/user-attachments/assets/2d7e125a-3254-43f6-99b4-c323f318c58a', '이불 공구 참가', 'FULL',
        'TRADING');

INSERT INTO OFFERING_MEMBER (OFFERING_ID, MEMBER_ID, ROLE, CREATED_AT, UPDATED_AT)
VALUES (1, 1, 'PROPOSER', '2024-07-15 00:00:00', '2024-07-15 00:00:00'),
       (1, 2, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (2, 2, 'PROPOSER', '2024-07-15 00:00:00', '2024-07-15 00:00:00'),
       (2, 3, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (3, 3, 'PROPOSER', '2024-07-15 00:00:00', '2024-07-15 00:00:00'),
       (3, 4, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (3, 5, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (4, 4, 'PROPOSER', '2024-07-15 00:00:00', '2024-07-15 00:00:00'),
       (4, 5, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (4, 6, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00'),
       (4, 7, 'PARTICIPANT', '2024-07-16 00:00:00', '2024-07-16 00:00:00');

INSERT INTO COMMENT (OFFERING_ID, MEMBER_ID, CONTENT, CREATED_AT, UPDATED_AT)
VALUES (1, 2, '안녕하세요.', '2024-07-16 09:01:00', '2024-07-16 09:01:00'),
       (1, 1, '안녕하세요.', '2024-07-16 09:01:01', '2024-07-16 09:01:01'),
       (1, 2, '거래 장소 변경 가능할까요?', '2024-07-16 09:01:02', '2024-07-16 09:01:02'),
       (1, 1, '상황봐서요', '2024-07-16 09:01:03', '2024-07-16 09:01:03');

INSERT INTO COMMENT (OFFERING_ID, MEMBER_ID, CONTENT, CREATED_AT, UPDATED_AT)
VALUES (2, 3, '안녕하세요.', '2024-07-16 11:01:00', '2024-07-16 11:01:00'),
       (2, 2, '안녕하세요.', '2024-07-16 11:01:01', '2024-07-16 11:01:01'),
       (2, 3, '거래 장소 변경 가능할까요?', '2024-07-16 11:01:02', '2024-07-16 11:01:02'),
       (2, 2, '그럼요', '2024-07-16 11:01:03', '2024-07-16 11:01:03');

INSERT INTO COMMENT (OFFERING_ID, MEMBER_ID, CONTENT, CREATED_AT, UPDATED_AT)
VALUES (3, 5, '안녕하세요.', '2024-07-16 14:01:00', '2024-07-16 14:01:00'),
       (3, 4, '안녕하세요.', '2024-07-16 14:01:01', '2024-07-16 14:01:01'),
       (3, 3, '안녕하세요.', '2024-07-16 14:01:02', '2024-07-16 14:01:02'),
       (3, 4, '에누리 되나요?', '2024-07-16 14:01:03', '2024-07-16 14:01:03'),
       (3, 3, '겠냐고', '2024-07-16 14:01:04', '2024-07-16 14:01:04');

INSERT INTO COMMENT (OFFERING_ID, MEMBER_ID, CONTENT, CREATED_AT, UPDATED_AT)
VALUES (4, 7, '안녕하세요.', '2024-07-16 17:01:00', '2024-07-16 17:01:00'),
       (4, 6, '안녕하세요.', '2024-07-16 17:01:01', '2024-07-16 17:01:01'),
       (4, 4, '안녕하세요.', '2024-07-16 17:01:02', '2024-07-16 17:01:02'),
       (4, 6, '에누리 되나요', '2024-07-16 17:01:03', '2024-07-16 17:01:03'),
       (4, 4, '절대', '2024-07-16 17:01:04', '2024-07-16 17:01:04'),
       (4, 4, '안됩니다.', '2024-07-16 17:01:05', '2024-07-16 17:01:05');
