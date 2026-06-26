# iOSビルド（ipa生成）対応 完了レポート

CemaAppをiOS（iPhone）で動作させるためのSwiftUI移植コード、プロジェクト設定、およびGitHub Actionsの自動ビルドワークフローの作成がすべて完了しました。
すべての資材は [4ipa](file:///E:/my/kotlin/CemaApp/4ipa) フォルダ内に配置されています。

---

## 作成されたファイル一覧

*   **iOSアプリのコードと設定**
    *   [ContentView.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp/ContentView.swift): Androidの `MainActivity.kt` のロジック（質問データ、スコア計算、特別ペナルティ、判定ロジック）とUI構成をSwiftUIで完全に再現したソースコード。
    *   [CemaApp.swift](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp/CemaApp.swift): iOSアプリを起動するためのメインエントリポイント。
    *   [Info.plist](file:///E:/my/kotlin/CemaApp/4ipa/CemaApp/Info.plist): iOS用のアプリ表示名、バンドルIDなどのメタデータ設定ファイル。
    *   [project.yml](file:///E:/my/kotlin/CemaApp/4ipa/project.yml): XcodeGen用の設定。GitHub Actions（macOSランナー）上でこれをもとに一時的なXcodeプロジェクトファイル（`.xcodeproj`）を自動生成します。コード署名を無効化した状態でビルドできるように設定してあります。

*   **ビルド自動化設定**
    *   [build-ios.yaml](file:///E:/my/kotlin/CemaApp/4ipa/build-ios.yaml): GitHub Actionsで動作するビルド自動化スクリプト。macOSランナー上でXcodeGenを用いてビルドを行い、サイドロード（AltStoreなど）に必要な署名なしの `CemaApp.ipa` をパッケージングしてArtifactsへアップロードします。

---

## 今後の作業手順（GitHub Actionsでのビルドとインストール）

iOSアプリのビルド（`.ipa` の生成）から、お手持ちのiPhone 15 Plusへのインストールまでの具体的な流れは以下の通りです。

### 1. GitHubリポジトリへの資材配置
GitHubに登録する際、ワークフローファイルを正しい位置に配置する必要があります。

1.  プロジェクトのルートディレクトリに `.github/workflows/` ディレクトリを作成します（既に存在する場合はその中）。
2.  `4ipa/build-ios.yaml` を、`.github/workflows/build-ios.yaml` に移動またはコピーして配置します。
3.  `4ipa` フォルダ全体をそのままプロジェクトのルートに配置した状態で、Gitにコミットします。

構成イメージ：
```text
CemaApp (リポジトリルート)
├── .github
│   └── workflows
│       └── build-ios.yaml  <-- ここに配置
├── 4ipa
│   ├── project.yml
│   └── CemaApp
│       ├── CemaApp.swift
│       ├── ContentView.swift
│       └── Info.plist
├── app (Android用フォルダ)
└── ...
```

この状態ですべての変更をリポジトリへ `push` してください。

### 2. GitHub Actionsでビルドを実行
1.  GitHub上のリポジトリを開き、**「Actions」**タブをクリックします。
2.  左側のワークフロー一覧から **「Build iOS IPA」** を選択します。
3.  `workflow_dispatch` が設定されているため、**「Run workflow」**ボタンから手動で実行することもできますし、`main`（または `master`）ブランチへのプッシュで自動的に実行されます。
4.  ビルド（約3〜5分）が完了すると、実行結果のページの下部にある **Artifacts** セクションに **`CemaApp-iOS-ipa`** という名前のファイルが表示されます。これをダウンロードしてください。
5.  ダウンロードした zip ファイルを解凍すると、`CemaApp.ipa` が現れます。

### 3. AltStoreを使ったiPhone 15 Plusへのインストール
1.  AltServerが起動しているPCと、iPhone 15 Plusを同じWi-Fiネットワークに接続します（またはUSBケーブルで接続します）。
2.  ダウンロードして解凍した `CemaApp.ipa` を、iPhoneに送信します（AirDrop、またはiCloud Drive、Google Drive、自身宛てのメールなどでiPhoneに保存してください）。
3.  iPhone上で **AltStore** アプリを起動します。
4.  **「My Apps」**タブを開き、左上の **「＋」** ボタンをタップします。
5.  ファイルアプリが開くので、先ほど保存した `CemaApp.ipa` を選択します。
6.  （初回のみ）Apple IDとパスワードの入力を求められますので、AltStoreのアカウント情報を入力します（これによってAltServerがご自身のApple IDでipaをローカル署名します）。
7.  数分待つとインストールが完了し、ホーム画面に `CemaApp` が表示され、起動可能になります！
