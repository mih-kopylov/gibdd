# ГИБДД бот

Бот для автоматической отправки обращений в ГИБДД при помощи Selenium

## Использование

java -Dwebdriver.chrome.driver="\path\to\chromedriver.exe" -jar bot.jar -dir "path\to\directory\with\photos" -login MAIL.LOGIN -password MAIL.PASSWORD -imap IMAP.HOST -firstName YOUR.NAME -lastName YOUR.SURNAME

### Параметры

* -Dwebdriver.chrome.driver - путь к драйверу Chrome
* -login - логин от почты, куда будет приходить код подтверждения и уведомление от ГИБДД 
* -password - пароль от почты для получения кода подтверждения
* -imap - хост IMAP
* -firstName - имя заявителя
* -lastName - фамилия заявителя