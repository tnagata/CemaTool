# Windows 11 / iOSユーザー向け Swift Playgroundsによる CemaApp 実行手順書

本手順書は、Windows 11をお使いのユーザーが、iOS用 SwiftUI アプリである [CemaApp.swiftpm](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm) のソースコードを iPhone / iPad 上の **「Swift Playgrounds（Swiftプレイグラウンド）」** アプリへ転送し、デバイス上で直接ビルド・実行（テスト）するための手順を説明します。

> [!WARNING]
> **重要：「Apple デバイス」アプリ等でフォルダを直接転送しないでください**
> Windowsの「Apple デバイス」アプリやiTunesを使って `.swiftpm` フォルダを直接転送すると、iOS側でパッケージのメタデータやアクセス権限が破損し、Swift Playgrounds起動時に「読み込み中クラッシュ」が発生する原因になります。
> 以下のいずれかの推奨方法で実行してください。

---

## 必要なもの
1. **Windows 11 PC**（ソースコードが配置されているマシン）
2. **iPhone または iPad**（テスト対象の実機。iOS 16 以降推奨）
3. iPhone/iPad にインストールされた **「Swift Playgrounds」アプリ**（App Store から無料でダウンロードできます）

---

## 推奨手順（どちらか一方を選択してください）

### 【方法A】iCloud Drive同期を使う方法（PCでコードを書き換えたい場合）
Windows用のiCloudアプリを使用している場合、同期フォルダを利用することで非常にスマートに開発・テストが行えます。

1. **iPad/iPhoneでの準備**:
   * iOSデバイスの「Swift Playgrounds」アプリを起動します。
   * 「マイプレイグラウンド」画面で「**アプリ**（新規作成）」をタップして、ダミーの新規アプリ（例：`CemaApp`）を作成します。
2. **Windows PCでの書き換え**:
   * しばらくすると、Windows PCの `iCloud Drive\Playgrounds\CemaApp.swiftpm` フォルダが自動的に同期されます。
   * Windows上で、その同期されたフォルダ内にある `ContentView.swift` と、アプリ名が書かれたメインのAppファイル（`App.swift` 等）の中身を、[ContentView.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/ContentView.swift) および [CemaApp.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/CemaApp.swift) のコードで上書き（コピー＆ペースト）します。
3. **デバイスでの実行**:
   * iOSデバイス側に戻ると、自動的にコードが同期されます。
   * 画面上部の「実行（▶）」をタップするだけで、その場でビルド・実行されます。

---

### 【方法B】デバイス側で新規作成し、直接コピペする方法（最も確実）
ファイル転送によるトラブルを防ぐ、最も手軽で確実な方法です。

1. **iPad/iPhoneでの準備**:
   * iOSデバイスの「Swift Playgrounds」アプリを起動し、「**アプリ**（新規作成）」をタップしてダミーの新規アプリを作成します。
2. **ソースコードの送信**:
   * Windows上の [ContentView.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/ContentView.swift) と [CemaApp.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/CemaApp.swift) のコードテキストを、メールやGoogle Drive経由でiOSデバイスへ送信し、クリップボードにコピーできるようにします。
3. **デバイスでのコード上書き**:
   * デバイス上で、作成したダミーアプリの `ContentView.swift` を開き、中身をすべて削除してコピーした [ContentView.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/ContentView.swift) のコードを貼り付けます。
   * メインのAppファイル（`App.swift` 等）も同様に、[CemaApp.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp.swiftpm/CemaApp.swift) のコードに書き換えます。
4. **実行**:
   * 「実行（▶）」をタップしてテストを開始します。

---

### 【方法C】ZIP圧縮して共有する方法
クラウドストレージを使ってパッケージとしてインポートする方法です。

1. **PC側での圧縮**:
   * Windowsの `4ipa` フォルダ内の `CemaApp.swiftpm` フォルダを右クリックし、「**ZIP ファイルに圧縮する**」を選択します。
2. **転送**:
   * クラウドストレージ（Google Driveなど）に `CemaApp.swiftpm.zip` をアップロードします。
3. **デバイスでの解凍と読み込み**:
   * iOSデバイスの「ファイル」アプリでそのZIPファイルをダウンロードしてタップして解凍します。
   * 解凍されたフォルダ（名前の末尾が `.swiftpm` になっているフォルダ）をタップするか、長押しして「Swift Playgrounds」で開きます。
