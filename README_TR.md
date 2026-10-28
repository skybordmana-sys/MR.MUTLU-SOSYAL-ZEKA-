# Premium Content Studio V66.0 — Kotlin / Jetpack Compose başlangıç uygulaması

Bu paket **APK değildir**. GitHub Actions üzerinde derlenmek üzere hazırlanmış kaynak kodudur. APK derlemesi ve cihaz testi henüz yapılmamıştır.

## Gerçekten uygulananlar
- Marka, sosyal medya kullanıcı adı, ad soyad alanları ve onay
- Uygulama içi V66 promptu; oturum verileriyle yer tutucuların değiştirilmesi
- Promptun salt okunur gösterimi ve panoya kopyalanması
- ChatGPT / Copilot / Gemini WebView sekmeleri (sağlayıcı oturum açmayı engelleyebilir)
- Devam / ana menü ve çıkış onayı; aktif kimlik bilgilerinin bellekten temizlenmesi
- Siyah, altın, beyaz arayüz

## Henüz yapılmayanlar
- Kalıcı ve güvenli hesap sistemi, Room, Keystore, kullanıcılar arası sunucu yetkilendirmesi
- Gerçek içerik üretimi, görsel indirme, metin çıktı alanları, Carousel ZIP, bilimsel kaynak doğrulaması
- 45 kabul testi ve 35 kalite kriterinin uygulanması ve cihaz testleri
- Orijinal imza dosyasının yönetimi ve proje veritabanı

## GitHub Actions ile APK
Depoya ZIP'in içindeki **dosyaları ve klasörleri** (ZIP dosyasının kendisini değil) yükleyin. Actions > Android APK > Run workflow. Derleme başarılıysa Artifacts > PremiumContentStudio-debug-apk. Android güvenlik uyarısı üzerinden yalnızca güvendiğiniz APK'yı yükleyin.

Not: `gradle` komutu CI üzerinde setup-gradle tarafından sağlanır. Bu kaynak paketinin başarıyla derlendiği henüz doğrulanmamıştır.
