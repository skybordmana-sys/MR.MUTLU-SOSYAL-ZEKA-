package com.premiumcontentstudio.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private val gold = Color(0xFFD6B66B)
private val black = Color(0xFF101010)
private data class Brand(val brand:String, val social:String, val fullName:String)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState:Bundle?) { super.onCreate(savedInstanceState)
  val template = assets.open("v66_master_prompt.txt").bufferedReader().use { it.readText() }
  setContent { MaterialTheme(colorScheme=darkColorScheme(primary=gold,background=black,surface=black,onBackground=Color.White)) {
   Studio(template)
  } }
 }
}

@Composable private fun Studio(template:String) {
 val context= LocalContext.current
 // Deliberately in-memory: logout destroys active personalization, including after app restart.
 var brand by remember { mutableStateOf<Brand?>(null) }
 var draftBrand by remember { mutableStateOf("") }
 var draftSocial by remember { mutableStateOf("") }
 var draftName by remember { mutableStateOf("") }
 var stage by remember { mutableStateOf("entry") }
 var selectedAi by remember { mutableStateOf("ChatGPT") }
 var showLogout by remember { mutableStateOf(false) }
 val normalized = draftSocial.trim().removePrefix("@").replace(" ", "")
 val personal = brand?.let { b -> template
  .replace("{MARKA_ADI}",b.brand)
  .replace("{INSTAGRAM_KULLANICI_ADI}","@${b.social}")
  .replace("{IMZA_ADI}",b.fullName)
  .replace("{AD_SOYAD}",b.fullName)
  .replace("{KULLANICI_ADI}",b.fullName) } ?: template
 fun copy(s:String) { (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("Premium Content Studio",s)) }
 BackHandler(enabled=brand!=null && stage!="home") { stage="home" }
 Column(Modifier.fillMaxSize().background(black).padding(18.dp).verticalScroll(rememberScrollState()), verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("PREMIUM CONTENT STUDIO",color=gold,style=MaterialTheme.typography.headlineSmall)
  Text("V66.0 • Premium Editoryal",color=Color.White)
  when {
   brand==null && stage=="entry" -> {
    OutlinedTextField(draftBrand,{draftBrand=it},label={Text("Marka adı")},singleLine=true)
    OutlinedTextField(draftSocial,{draftSocial=it},label={Text("Sosyal medya kullanıcı adı")},singleLine=true)
    OutlinedTextField(draftName,{draftName=it},label={Text("Ad soyad / imza")},singleLine=true)
    Button(enabled=draftBrand.isNotBlank() && normalized.isNotBlank() && draftName.isNotBlank(),onClick={stage="confirm"}) {Text("BİLGİLERİ KONTROL ET")}
   }
   brand==null && stage=="confirm" -> {
    Text("Marka: $draftBrand\nInstagram: @$normalized\nAd soyad / imza: $draftName",color=Color.White)
    Button(onClick={brand=Brand(draftBrand,normalized,draftName);stage="home"}) {Text("EVET, ONAYLIYORUM")}
    OutlinedButton(onClick={stage="entry"}) {Text("HAYIR, DÜZENLE")}
   }
   else -> {
    Text("${brand?.brand}  •  @${brand?.social}",color=gold)
    if(stage=="home") {
     Button(onClick={stage="prompt"}) {Text("KİŞİSELLEŞTİRİLMİŞ V66.0 PROMPT")}
     Button(onClick={stage="ai"}) {Text("YAPAY ZEKÂ SEKMELERİ")}
     OutlinedButton(onClick={showLogout=true}) {Text("HESAPTAN ÇIK")}
     Text("İçerik üretimi, proje veritabanı ve kalite testleri bu başlangıç sürümünde henüz uygulanmadı.",color=Color.LightGray)
    } else if(stage=="prompt") {
     Button(onClick={copy(personal)}) {Text("TÜM PROMPTU KOPYALA")}
     Text(personal,color=Color.White)
     OutlinedButton(onClick={stage="home"}) {Text("DEVAM / ANA MENÜ")}
    } else if(stage=="ai") {
     Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) {
      listOf("ChatGPT","Copilot","Gemini").forEach { ai ->
       Button(onClick={selectedAi=ai},contentPadding=PaddingValues(horizontal=8.dp)) {Text(ai)}
      }
     }
     Text("Önce promptu kopyalayıp ilgili web sayfasına yapıştırın. WebView oturum açma desteği sağlayıcıya bağlıdır.",color=Color.LightGray)
     Button(onClick={copy(personal)}) {Text("KİŞİSEL PROMPTU KOPYALA")}
     val url=when(selectedAi) {"Copilot"->"https://copilot.microsoft.com/";"Gemini"->"https://gemini.google.com/";else->"https://chatgpt.com/"}
     key(selectedAi) { AndroidView(factory={ c -> WebView(c).apply {
      settings.javaScriptEnabled=true
      settings.domStorageEnabled=true
      webViewClient=WebViewClient()
      loadUrl(url)
     } },modifier=Modifier.fillMaxWidth().height(480.dp)) }
     OutlinedButton(onClick={stage="home"}) {Text("DEVAM / ANA MENÜ")}
    }
    if(showLogout) AlertDialog(onDismissRequest={showLogout=false},title={Text("Hesaptan çıkılsın mı?")},text={Text("Aktif marka, sosyal medya ve ad soyad bilgileri temizlenecek.")},confirmButton={TextButton(onClick={brand=null;draftBrand="";draftSocial="";draftName="";selectedAi="ChatGPT";stage="entry";showLogout=false}){Text("EVET, ÇIK")}},dismissButton={TextButton(onClick={showLogout=false}){Text("HAYIR, DEVAM ET")}})
   }
  }
 }
}
