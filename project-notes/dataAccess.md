# Adım 2: Veri Erişim Katmanı (Repository Pattern)

Profesyonel bir mimaride, "Veritabanına nasıl bağlanırım?" veya "SQL sorgusunu nasıl yazarım?" soruları iş mantığı (Business Logic) katmanını ilgilendirmez. Bu yüzden Repository Pattern'i kullanarak veri erişimini soyutladık.

## 2.1. Repository<TEntity, ID>: Soyut Sözleşme

Bu arayüz, projemizin veritabanı ile konuşma dilidir.

- __Neden Yazdık?__ Yarın bir gün JetBrains Exposed yerine Hibernate (Spring Data) veya tamamen farklı bir veritabanı teknolojisine geçmek istersek, üst katmanlardaki kodlarımıza dokunmamak için.

- __Ne İşe Yarar?__ `findById`, `findAll`, `insert` gibi standart CRUD işlemlerini jenerik bir yapıda toplar. Bu sayede her yeni tablo için aynı metodları tekrar tekrar tanımlamak zorunda kalmayız (DRY Prensibi).

## 2.2. ExposedRepositoryTemplate: Altyapı ve Template Method Pattern

Burada gerçek bir "Uzman" dokunuşu yaptık ve Template Method Design Pattern uyguladık.

- __Neden Yazdık?__ Exposed ORM ile veritabanı işlemleri yaparken sürekli `transaction { ... }` açmak, nesneleri satırlara (ResultRow) çevirmek gibi tekrarlayan (boilerplate) kodlar oluşur.

- __Nasıl Çalışır?__ Tüm ortak süreçleri (ekleme, silme, güncelleme mantığı) bu şablon sınıfta topladık.

       - Değişen kısımları (rowToEntity, mapToTable) ise abstract (soyut) metodlar olarak bıraktık.

       - Transaction Yönetimi: Her işlem bir transaction bloğu içinde gerçekleşir. Bu, veri güvenliğini sağlar; eğer bir hata olursa veritabanı otomatik olarak eski haline döner (Rollback).

__Neden Bu Yaklaşım "Uzman İşi"?__

Çünkü biz burada Dependency Inversion (Bağımlılıkların Tersine Çevrilmesi) prensibini uyguladık. Üst katmanlar (Servisler), somut bir veritabanı kütüphanesine değil, bizim yazdığımız Repository arayüzüne bağımlıdır.

__Mentor Notu:__ ExposedRepositoryTemplate sayesinde yeni bir Repository yazmak sadece 5 dakikanı alır. Çünkü ağır işlerin (ekleme, silme, bulma) hepsini bu şablon sınıf hallediyor. Sen sadece kolonları eşleştiriyorsun.