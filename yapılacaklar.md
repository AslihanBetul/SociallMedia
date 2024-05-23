SocialMedia projemizdeki activasyon kodunu user'ın emailine gönderelim.

EmailService adında ayrı bir service oluşturunuz.

AuthService-EmailService Aralarındaki iletişim rabbitmq ile olsun. DirectExchange ile yapılabilir.

Şifremi unuttum? e-mail gir, kod üretip gönderebiliriz. o kod ile beraber, email, yeni şifresini girer ve kaydederiz. Eski şifre üzerine yazılır.

findByUsername metodu ile username göre veritabanında arama yapılmaktadır. Yapılan arama sonucunda user nesnesi geri dönen bu metodu cacheleyelim.

(arama yapılırken büyük küçük harf önemsenmesin. ali Ali) Bu maddenin doğru çalışması için veritabanına ali ve Ali diye 2 farklı kayıt açılamamalı.

findByStatus metodu ile statuse göre userlar liste olarak dönmeli. Bunun da cachelenmesini istiyoruz. Birisi statusu güncellendiğinde cachede de güncellenmeli. Yeni bir user register olduğunda Pending durumunda oluyordu. Onun da cachedeki listeye eklenmesi gerekli.

Bu 2 metod içinde gerekli kontrollerle gerekli hataları fırlatalım.

findByStatus metodu içine olmayan bir status gelirse ne olacak? Bunun için ilgili hatayı yakalayıp, globalExceptionHandler içine yeni bir metod ekleyerek handle etmelisiniz.