# Bu Dekoratörler Nasıl Birleşir? (The Russian Doll Effect)

Mimarinin en güzel yanı burasıdır. Bu sınıfları iç içe sarmalayarak bir "işlem zinciri" (pipeline) oluşturabiliriz. Bir Use Case'i şöyle ayağa kaldırabiliriz:

````kotlin
// Zinciri oluşturuyoruz: 
// Logging -> Transaction -> Validation -> Actual Business Logic
val createUserUseCase = LoggingDecorator(
    TransactionDecorator(
        ValidationDecorator(
            CreateUserUseCase(userRepository, userMapper) 
        ) { request -> if(request.email.isEmpty()) "Email boş olamaz" else null }
    )
)

// Çalıştırdığımızda sırasıyla:
// 1. Log başlar
// 2. Transaction açılır
// 3. Validasyon yapılır
// 4. (Eğer geçerse) Kullanıcı kaydedilir
// 5. Log biter ve süre hesaplanır
````

## Mentor Analizi: Neden Harika Bir İş Çıkardık?

   1. Single Responsibility: CreateUserUseCase sadece kullanıcı kaydetmeyi bilir. Loglamayı, veritabanı transaction yönetimini veya validasyonu bilmez.

   2. Sürdürülebilirlik: Yarın bir gün "Performans ölçümü yapan bir dekoratör" eklemek istersen, mevcut kodların hiçbirini değiştirmene gerek kalmaz.

   3. Hata Yönetimi: TransactionDecorator sayesinde veritabanında asla "yarım kalmış" bozuk veriler oluşmaz.


## ---------------------------------------------

Artık bir Use Case oluştururken "Pyramid of Doom" (iç içe parantez kabusu) yaşamayacağız:

````kotlin
val finalUseCase = CreateUserUseCase(repo, mapper)
    .decorate()
    .withRules(
        { req -> if (req.email.isBlank()) "Email cannot be blank" else null },
        { req -> if (req.password.length < 8) "Password too short" else null },
        { req -> if (req.age < 18) "Must be older than 18" else null }
    )
    .build()
````
Neden Bu Yapı Çok Güçlü?

   - Readable (Okunabilir): Kod, bir hikaye anlatır gibi okunuyor: "Bu Use Case'i al, logla, transaction içine koy ve valide et."

   - Flexible (Esnek): Sıralamayı istediğin gibi değiştirebilirsin. Örneğin, önce validasyon yapıp sonra transaction açmak performans açısından daha mantıklıdır (Hata varsa boşuna DB bağlantısı meşgul edilmez).

   - Encapsulation (Kapsülleme): Dekoratörlerin nasıl oluşturulduğu detayını (constructor parametreleri vb.) builder içinde gizlemiş olduk.
