# SOCIAL MEDIA APPLICATION

## Yapılacak Listesi 1
1. Dependency Management için gerekli ayarlamalar yapılmalı.
   dependencies.gradle dosyası hazırlanmalı.
   libs kısmında gerekli görülen bağımlılıklar build.gradle(ROOT) içinde tüm projelerde kullanılacak olanlar çağrılmalı.
2. Projede kullanacağımız sürümler önceki proje ile aynı olsun.
3. İçerisine şimdilik sadece AuthService modülü ekleyeceğiz.
4. Auth entity olacak. ( username,password,email,ROLE(enum, ADMIN,USER), activationCode, Status (Enum, Active,Deleted,Pending,Banned) )
5. activationCode: 5 haneli bir kod oluşturup onu kullanıcıya dönmek istiyoruz. Rastgele bir kod üretilmeli.
   UUID oluşturalım. UUID.randomUUID().toString()
   Gelen örnek UUID: e58ed763-928c-4155-bee9-fdbaaadc15f3
   UUID 5 bölümden oluuyor. Her bölümün ilk karakterini alıp bizim activation codumuzu(e94bf) dönecek bir metod yazalım.
   Bu metod util altında CodeGenerator adlı sınıf içinde static bir metod olsun.
   Truncate? -> Sadece tablo içindeki verileri silmek için kullanılıyordu.
6. Exceptionları uygun şekilde kullanalım.
7. Register içinde requestDto-> username,password,repassword,email, responseDto->username, Mapper kullanalım.
8. Validation İşlemleri için ilgili bağımlılık eklenecek.
   Ve Register olurken Validation işlemleri yapılacak.
   username ve password boş geçilemez. min:3 max:20 karakter olabilir.
   email validasyonu da ekleyelim. Email de boş geçilmez.
   @NotNull,       @NotBlank,      @NotEmpty

9. Login işlemleri olacak. request-> email,password, response-> (id,username,email,role,status)

10. Statusu aktif olmayan login yapamamalı.
11. hesabı aktif etmeliyiz. Aktif ederken hangi bilgilere ihtiyacımız var? request->id,activationCode bilgisi alıp,
    gerekli kontrollerden(bu idye ait bir user biz de var mı?, bu activation code bu usera mı ait? değilse hata fırlat.)
    sonra statusu ACTIVE duruma update etmeliyiz. response-> "Aktivasyon Başarılı! Sisteme giriş yapabilirsiniz."

12. Activasyon işlemi STATUS'u sadece PENDING olanlar için geçerli olmalıdır. Diğer durumlarda uygun mesajlar dönülmeli.
13. Bir auth'u silecek endpoint yazınız. Hard Delete-> Gerçekten  kaydı silmek. Soft Delete-> Statusu DELETED yapacağız.
    Dışarıdan endpointe id'si gönderilen auth'un Statusunu DELETED yapacağız.
    PathVariable kullanalım.

## Todo List 2 13.05.2024

1. JWT token mekanizmasını da kullanmalıyız.
   Login olan kullanıcıya token dönmeliyiz. İşlemleri adım adım yapınız.

2. Token oluşturmak için ; JWTTokenManager içersinde:
* id alıp token oluşturan metod,
* id ve role alıp token oluşturan metodu yazalım.

3. decode için getIdFromToken metodunu yazalım.

4. decode için getRoleFromToken metodunu yazalım.

5. secretKey ve issuer bilgileri enviroment variablestan gelsin. Öncesinde application.yml dosyasına çekilmesi gerektiğini unutmayınız.

6. AuthControllerda bu 4 metodu denemek için endpointler açalım.
* getToken endpointi id alacak token yaratacak.
* getRoleToken endpointi id ve role alacak token yaratacak.
* getIdFromToken endpointi token alacak id verecek.
* getRoleFromToken endpointi token alacak role string olarak verecek.

7. AuthService'de login metodu başarılı ise token dönecek. token oluştururken id ve role bilgisi alacak bir metodunuz olmalı.

8. Token üretme aşamasında hatalar için uygun errorlar fırlatalım.

9. UserService modülü ekleyelim. Gerekli packageları alıp gerekli değişikleri yapınız.

10. Entity açalım. UserProfile (id,authId,username,email,phone,photo,address,about,status(enum))

11. application.yml'da ayarlamaları yapalım. db olarak docker içindeki mongoyu kullanalım.

12. Repository katmanını oluşturalım.
13. Service katmanını oluşturalım.
14. Controller katmanını oluşturalım.

15. Controller içinde save metodu olsun. UserSaveRequestDto ile kayıt almalıyız.  (authId,username,email)

16. Dönüşümler için mapper kullanın. UserMapper.

17. Authta kayıt olan user için userservice de save metodu tetiklenmeli. OpenFeign kullanarak bu işlemi gerçekleştiriniz.

18. Araştırma: Eğer auth'a kaydedip user'a kaydedemezse authdaki kaydıda silmesini nasıl sağlayabiliriz?

19. En sonunda projenin tamamını deneyelim. Çalıştığından emin olalım.

## Todo List 3 14.05.2024

1. NoSQL ve SQL farkı?(idris,salih)
2. NoSQL db tipleri nelerdir? Ve altındaki databaseler nelerdir? (Mina,Aslan)
   Document Type->Mongo
   Key-Value Type->Cassandra
   Arama Type-> ElasticSearch, Solr
   ...
3. CAP teoremi nedir? (Can)
   Consistency(Tutarlılık) , Availability(Erişilebilirlik) , Partition Tolerance(Dağıtık sistem kurulabilir mi?)

4. BASE nedir? (Hasan)
   Basically Availability, Soft State, Eventually Consistency

5. ACID nedir? (Kenan)
   Atomicity, Consistency, Isolation, Durability

5. AuthService de register olduktan sonra activasyon işlemleri yapılırken bu işlem UserProfile tarafına yansıtılmalı. (openfeign) UserProfile tarafında endpointte pathVariable ile authid alabilirsiniz. UserService kapalı veya bir nedenden işlem gerçekleşemezse authservicede de bu işlem geriye alınmalı. (Salih)

6. Login olduktan sonra dönen token ile beraber UserProfile'ını güncelleyebilmeli. (sami)

7. Ama burada sadece username değiştirememeli. Onun dışındaki alanlar değişmeli. Örnek olarak: email bilgisini değiştirirse auth'da da değişmeli.  (Mina)
8. Update ederken eğer bir değer verilmiyorsa, eski değerler kalsın. (sami)
9. Authdaki silme UserProfile tarafına yansıtacak bir mekanizma geliştirin. (Aslıhan)

## Todo List 4 15.05.2024
1. @RestController ile @Controller farkı?
2. Spring Boot Uygulaması ayağa kalkarken neler oluyor?
3. RestApi yazarken nelere dikkat edilmeli?
4. Richardson Maturity Model nedir?
5. @SpringBootApplication anotasyonlu sınıfı spring package içine alırsam proje çalışır mı?
   Hayır!. Peki nasıl çalışır?
5. Sisteme ConfigServer dahil ediniz. (Local ve Git(ayrı bir repo oluşturarak)) (Hicran)
6. BÜtün microserviceler yml dosyalarını bu configserverdan çekecek şekilde ayarlanmalıdır. (Hicran)
7. git config-server'daki bir ymldaki değişiklikten servisin haberdar olması için neler yapılabilir? (Araştırma)
   (WebHook ile yapılabilecek bir yol var gibi.)


## Todo List 5 16.05.2024
1. PostService yazınız. Bir user login olduktan sonra postservice aracılığı ile post atabilmelidir. (Can)
2. User Kendi attığı postları listeleyebilmeleceği bir endpoint olmalı. (Kenan)
3. User Atılan tüm postları da görebileceği bir endpointi olmalı. (idris)
4. User kendi attığı postu silmeli.(idris)
4. User kendi attığı postu güncelleyebilmeli. (ismet)
5. Database siz belirleyin. (Mongo veya PostgreSQL)
   Mongo db'ye user ekleme: (Serkan)
   db.createUser({ user:"bilge",pwd:"bilge!123",roles: ["readWrite","dbAdmin"]})
6. - Optional: ApiGateway ekleyebilirsiniz. (Aslan)
7. Bu kısımda openFeign kullanmaya devam edelim. Rabbitmq dahil etmiyoruz.
8. Araştırma Soruları: Hangi veritabanını seçmek daha uygun olur, MongoDB mi yoksa PostgreSQL mi? Her birinin avantajları ve dezavantajları nelerdir?
9. PostService'in nasıl tasarlanması gerekiyor ve hangi endpointler sunulmalıdır? Bu endpointlerin işlevleri ve kullanım senaryoları nelerdir?
10. Kullanıcıların kendi attıkları postları güncelleyebilmesi ve silmesi için nasıl bir mekanizma sağlanmalıdır? Bu işlemlerin güvenliği ve doğrulaması nasıl yapılmalıdır?
11. PostService için hangi veri modeli ve ilişkiler kullanılmalıdır? Kullanıcılar ve postlar arasındaki ilişki nasıl olmalıdır?
12. PostService'in performansı ve ölçeklenebilirliği nasıl artırılabilir? Büyük ölçekli kullanım senaryolarını desteklemek için neler yapılabilir?

## Todo List 6 17.05.2024
1. Kullanıcı post atarken tokenla atmalıdır. (Önce user login olacak, oradan aldığınız token bilgisini post atarken ekleyeceksiniz.)
2. Diğer user'a özel tüm endpointlerde aynı şekilde token değerini de yollayıp, kontrol ederek işlem yaptırmalısınız.
2. Postun createAt,updateAt,status gibi fieldları olmalıdır. BaseEntityden alınabilir.
3. Database olarak Mongo kullanılmadıysa mongo kullanılmalıdır.

4. RabbitMq'yi de projemize dahil edelim.
5. AuthService'den register olunca UserService'e openFeignle attığımız isteği rabbit ile yapalım. Bunun için registerWithRabbit adlı yeni bir metod ekleyelim. Diğeri de dursun.
6. DirectExchange kullanabilirsiniz.
7. Exchange,Queue,RouteKey bu isimlerini alırken yml dosyasında kendi yazdığınız key-valuelardan çekin.
7. Bİr tane de queue oluşturunuz.
8. Dönüşüm için messageConverter eklenebilir RabbitTemplate için.

9. Bu işlem dışında ayrıca rabbitMQ kullanarak hesabı aktive ediniz. Onun için de ayrı bir kuyruk oluşturmalısınız. Daha önce openFeign kullanmıştık. onu gizleyebilirsiniz. Bu işlemde de DirectExchange kullanabilirsiniz.

10. Araştırma Soruları:
- JWT (JSON Web Token) nedir ve nasıl çalışır? Token oluşturma ve doğrulama süreçlerini açıklayınız.
- JWT ile OAuth2 arasındaki farklar nelerdir? Hangi durumlarda JWT, hangi durumlarda OAuth2 kullanmalısınız?
- RabbitMQ nedir ve temel bileşenleri nelerdir?
- RabbitTemplate nedir ve nasıl kullanılır?
- @RabbitListener anotasyonu ne yapmak için kullanılmaktadır?
- Mesaj dönüşümü (message conversion) nedir ve RabbitMQ'da nasıl uygulanır?
- MongoDB nedir ve SQL tabanlı veritabanlarından farkları nelerdir? MongoDB'nin temel bileşenlerini açıklayınız (koleksiyon, doküman vs..)
- https://www.cloudamqp.com/blog/part1-rabbitmq-for-beginners-what-is-rabbitmq.html




## Todo List 7 22.05.2024
1. Spring ile mail gönderme işlemi nasıl yapılabilir? Araştırınız.
2. Mail olarak lütfen GMAIL kullanınız. Gmail spring gibi diğer dillerde nasıl kullanıma izin veriyor araştırınız.
3. Aşağıdaki uygulamayı yeni bir Spring projesinde gerçekleştiriniz:
```
From: gönderen kısmıdır. burada sizin adresiniz olmalı.
To: kime . Mail göndereceğiniz adres. (barisbilgebirisi@gmail.com)
Subject: Konu. Mailin konusu 
Message: Mail. içeri burada olur.
```
7. Oluşturacağınız end pointe yukarıda belirtilen alanlara değerler girilerek mail gönderebilmelidir.


## Todo List 8 23.05.2024
1. SocialMedia projemizdeki activasyon kodunu user'ın emailine gönderelim.
2. EmailService adında ayrı bir service oluşturunuz.
3. AuthService-EmailService Aralarındaki iletişim rabbitmq ile olsun. DirectExchange ile yapılabilir.
4. Şifremi unuttum? e-mail gir, kod üretip gönderebiliriz.
   o kod ile beraber, email, yeni şifresini girer ve kaydederiz. Eski şifre üzerine yazılır.

## Redis Tekrarı:
5. findByUsername metodu ile username göre veritabanında arama yapılmaktadır. Yapılan arama sonucunda user nesnesi geri dönen bu metodu cacheleyelim.
6. (arama yapılırken büyük küçük harf önemsenmesin. ali Ali) Bu maddenin doğru çalışması için veritabanına ali ve Ali diye 2 farklı kayıt açılamamalı.
7. findByStatus metodu ile statuse göre userlar liste olarak dönmeli. Bunun da cachelenmesini istiyoruz.
   Birisi statusu güncellendiğinde cachede de güncellenmeli.
   Yeni bir user register olduğunda Pending durumunda oluyordu. Onun da cachedeki listeye eklenmesi gerekli.
8. Bu 2 metod içinde gerekli kontrollerle gerekli hataları fırlatalım.
9. findByStatus metodu içine olmayan bir status gelirse ne olacak? Bunun için ilgili hatayı yakalayıp, globalExceptionHandler içine yeni bir metod ekleyerek handle etmelisiniz.

## Todo List 9 27.05.2024
1. ElasticService adında yeni bir microservice ekleyelim.
2. Bu service ayağa kalktığında Postların tamamını elastic-service RabbitMQ ile alalım.
3. Post ekleme,silme,güncelleme işlemleri yapılırsa elastiği de etkilesin. (RabbitMQ)
4. Elastic-service'den sonuçlar dönecek şekilde bazı arama endpointleri hazırlanabilir.
   (PostControllerdaki findAllByToken adlı endpoint elasticten sonuç dönsün. Bu sonuç sayfalama işlemleri ile çalışsın.)

## Todo List 10 5.06.2024
1. Security
2. AuthService'de Register, RegisterWithRabbit , Login,Şifremiunuttum activate code  endpointleri herkesin kullanımına açık olmalı. Diğer endpointler authenticate olmayı istemeli +
3. JWTTOKENFİLTER custom filter'ını oluşturunuz.+
4. AuthControllerda findall endpointine rolü sadece USER olanlar istek atabilsin+
5. AuthControllerda delete endpointine rolü sadece ADMIN olanlar istek atabilsin+
6. UserController için de securtiy kuralım
7.UserService'de swagger ve findall dışındaki istekler kapalı olsun 