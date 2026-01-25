# Adım 1: Temeller ve Veri Standartları (Domain & Core)

Her profesyonel yazılım, önce verinin ne olduğunu ve nasıl kimliklendirileceğini tanımlayarak başlar. Biz de projeye iki temel taşla başladık: Entity ve Dto.

## 1.1. Entity<ID>: Veritabanının Ruhu

Veritabanımızda saklayacağımız her nesnenin bir kimliği olmalıdır. Ancak bu kimlik bazen bir `Long`, bazen bir `String (UUID)` olabilir.

- __Neden Yazdık?__ Tüm tablolarımızda standart bir yapı kurmak için.

- __Ne İşe Yarar?__ İçine koyduğumuz `createdAt` ve `updatedAt` ile her kaydın ne zaman doğduğunu ve ne zaman güncellendiğini otomatik olarak takip edebileceğimiz bir sözleşme (contract) oluşturduk.

## 1.2. Dto: Dış Dünyaya Açılan Pencere

Veritabanı tablolarımızı (Entity) asla doğrudan kullanıcıya göstermeyiz. Bu, evin mahremiyetini (veritabanı şemasını) korumak gibidir.

* __Neden Yazdık?__ Katmanlar arası veri transferini disipline etmek için.

* __Ne İşe Yarar?__  `Dto` arayüzü, bir nesnenin API üzerinden dışarıya veri taşımak için yaratıldığını belirten bir "etiket" (Marker Interface) görevi görür.

__Neden Buradan Başladık?__

Çünkü mimarinin geri kalanı bu iki tip üzerinde yükselecek. Eğer "kimlik" (Entity) ve "mesaj" (Dto) yapısını en baştan standardize etmezsek, projenin ilerleyen safhalarında kod tekrarından kaçamayız.

