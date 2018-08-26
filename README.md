# ГИБДД бот

Бот для автоматической отправки обращений в ГИБДД при помощи Selenium

## Использование

```
java -jar bot.jar -dir "path\to\directory\with\photos" -login MAIL.LOGIN -password MAIL.PASSWORD -imap IMAP.HOST -firstName YOUR.NAME -lastName YOUR.SURNAME
```

### Параметры

* `-login` - логин от почты, куда будет приходить код подтверждения и уведомление от ГИБДД 
* `-password` - пароль от почты для получения кода подтверждения
* `-imap` - хост IMAP
* `-firstName` - имя заявителя
* `-lastName` - фамилия заявителя