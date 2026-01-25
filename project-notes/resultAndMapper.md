# Adım 3: Standardizasyon ve Dönüşüm (Result & Mapper)

Büyük bir projede her fonksiyonun farklı bir tip dönmesi (biri String, diğeri Boolean, öbürü null) kaosa neden olur. Ayrıca, veritabanı nesnelerini (Entity) doğrudan dışarıya göndermek güvenlik riski taşır. İşte bu iki sorunu burada çözüyoruz.

## 3.1. Result Pattern: Uygulamanın Ortak Dili

İşlem başarılı mı? Hata varsa mesajı ne? Veri nerede? Bu soruların cevabını tek bir çatı altında topladık.

- __Neden Yazdık?__ Fonksiyonların başarısızlık durumlarını istisna (exception) fırlatarak değil, bir veri olarak dönmek için. Exception fırlatmak maliyetlidir; oysa Result dönmek hızlı ve güvenlidir.

- __Ne İşe Yarar?__ SuccessDataResult veya ErrorResult gibi sınıflarla, uygulamanın her yerinde aynı dili konuşuruz. Bir when bloğu ile işlemin sonucunu çok rahat bir şekilde kontrol edebiliriz.

## 3.2. Mapper: Veri Tercümanları

Veritabanındaki bir UserEntity ile kullanıcının ekranında gördüğü UserDto aynı şey değildir. Bunların birbirine dönüştürülmesi gerekir.

- __Neden Yazdık?__ "Separation of Concerns" (İlginin Ayrılması) prensibi gereği. İş mantığı katmanı veritabanı detaylarını görmemeli, sunum katmanı ise şifre gibi gizli entity alanlarını bilmemelidir.

- __BaseMapper ve DRY:__ Seninle yazdığımız BaseMapper, liste dönüşümlerini `(List<Entity> -> List<Dto>)` otomatik yapar. Geliştirici sadece tek bir nesnenin nasıl dönüştürüleceğini yazar, gerisini sistem halleder.

__Neden Bu Yaklaşım "Uzman İşi"?__

Çünkü bu yapı sayesinde uygulamamızın katmanları arasına "Gümrük Kapıları" koymuş olduk. Veri bir katmandan diğerine geçerken kontrol edilir (Result) ve kılık değiştirir (Mapper). Bu, kodun kırılganlığını azaltır ve güvenliği en üst seviyeye çıkarır.

__Mentor Notu:__ Bir junior geliştirici doğrudan Entity'yi API'den döner. Bir uzman ise bilir ki; bugün Entity'ye eklediğin masum bir "passwordHash" alanı, Mapper kullanmazsan yarın tüm internete sızabilir. Biz bu riski en başta yok ettik.