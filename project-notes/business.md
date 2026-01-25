# Adım 4: İş Mantığı ve Esnek Yapı (Use Cases & Decorators)

Geleneksel projelerde servis sınıfları zamanla binlerce satırlık "çöp kutularına" dönüşür. Biz bu projede bu hataya düşmemek için Use Case (Interactor) yaklaşımını ve Decorator Pattern’i kullandık.

## 4.1. Use Case (İş Senaryosu): Tek Sorumluluk

Mimarimizde her iş birimi (örneğin: "Kullanıcı Kaydet", "Şifre Sıfırla") kendi sınıfına sahiptir.

- __Neden Yazdık?__ Single Responsibility (SRP) prensibini en uç noktada uygulamak için.

- __Ne İşe Yarar?__ İş mantığı kodu çıplaktır. İçinde loglama, validasyon veya veritabanı transaction yönetimi gibi "işin aslıyla ilgisi olmayan" kodlar barındırmaz. Sadece "işini" yapar.

## 4.2. Decorator Pattern: İş Mantığını Giydirmek

İş mantığı (Use Case) "çıplak" dedik. Peki loglamayı veya validasyonu nerede yapıyoruz? İşte burada devreye Decorator (Dekoratör) tasarım deseni giriyor.

- __Neden Yazdık?__ Mevcut iş koduna dokunmadan ona yeni yetenekler eklemek için (Open/Closed Principle).

- __Nasıl Çalışır?__ Bir Use Case'i bir soğanın katmanları gibi sarmalarız. En içte ana iş, dış katmanlarda ise şunlar bulunur:

    - __ValidationDecorator:__ Veri hatalıysa ana işe hiç gitmeden durdurur.

    - __LoggingDecorator:__ İşlemin ne kadar sürdüğünü ve sonucunu otomatik kaydeder.

    - __TransactionDecorator:__ Birden fazla veritabanı işlemini tek bir güvenli zarfa koyar.

## 4.3. UseCaseBuilder: Mimariyi Birleştiren "Fabrika"

Bu kadar çok katmanı (Logging, Transaction, Validation) her seferinde elle iç içe yazmak yorucu olurdu. Bu yüzden bir Builder sınıfı geliştirdik.

- __Neden Yazdık?__ Karmaşık nesne kurulumunu basitleştirmek için.

- __Ne İşe Yarar?__ Bir geliştirici olarak sen sadece .withLogging().withTransaction().withRule(...) diyorsun; arka planda Builder senin için o karmaşık dekoratör zincirini saniyeler içinde kuruyor.

__Neden Bu Yaklaşım "Uzman İşi"?__

Çünkü bu mimaride Cross-Cutting Concerns (Loglama, Güvenlik, Validasyon) iş mantığından tamamen ayrılmıştır. Yarın bir gün "Tüm sistemi Redis cache ile hızlandıralım" derseniz, hiçbir iş mantığına dokunmadan sadece yeni bir CacheDecorator yazar ve Builder'a eklersiniz.

- __Mentor Notu:__ Bu yapı, projenin "değişime direnç göstermemesini" sağlar. Kodun kırılmaz, esnek ve her bir parçası bağımsızca test edilebilir hale gelir. Bu, kurumsal seviyede bir yazılımın imzasıdır.