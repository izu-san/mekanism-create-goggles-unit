## Mekanism-Create Goggles Unit Mod 設計メモ

### 1. 目的

- **目的**
  - Create MOD の **エンジニアゴーグル (Engineer's Goggles)** 機能を、Mekanism MOD の **MekaSuit ヘルメット用アップグレードユニット**として実装する。
  - プレイヤーが MekaSuit ヘルメットに本アップグレードを装着しているとき、Create のエンジニアゴーグル相当の情報オーバーレイを表示する。

- **参考情報**
  - Engineer's Goggles 仕様:  
    - `https://github.com/Creators-of-Create/Create/wiki/Engineer%27s-Goggles`
  - Mekasuit とアップグレード一覧:  
    - `https://wiki.aidancbrady.com/wiki/Mekasuit#Upgrades`
  - Mekanism ソースコード:  
    - `https://github.com/mekanism/Mekanism`
  - Create ソースコード（HUD 実装の参考用）:
    - `https://github.com/Creators-of-Create/Create`

---

### 2. 対象バージョンと依存関係（案）

- **Minecraft バージョン**
  - MC 1.21.1（要：Create / Mekanism の対応状況を要確認）

- **MOD ローダー**
  - NeoForge（既存プロジェクトに合わせる）

- **依存 MOD**
  - Mekanism
  - Create

- **依存関係の扱い（方針）**
  - `build.gradle` では Mekanism / Create を `compileOnly` もしくは `implementation` で参照。
  - MOD 側では「ソフト依存」にしておき、Create or Mekanism が無い環境では該当機能を無効化する。
    - 例: `ModList.get().isLoaded("create")` / `ModList.get().isLoaded("mekanism")` でチェック。

---

### 3. 実装したい機能の概要

- **プレイヤー条件**
  - プレイヤーが Mekanism の **MekaSuit ヘルメット**を装備している。
  - そのヘルメットに、自作の **「Goggles Module」(仮称)** が装着されている。
  - Create MOD がロードされている。

- **表示内容（Create ゴーグル互換）**
  - エンジニアゴーグルの仕様に準拠した HUD を表示する:
    - 回転コンポーネントの **回転速度 (RPM)** と色分け（緑 / 青 / 紫）。
    - Speedometer / Stressometer の読み取り:
      - 速度 (RPM)
      - ストレス使用率 (%)
      - 基本ストレス容量 / 残りストレス容量
    - 発電コンポーネント:
      - 基本ストレス容量
      - 現在 RPM におけるストレス供給量
    - 消費コンポーネント:
      - 基本ストレスインパクト
      - 現在 RPM におけるストレスインパクト
  - エンジニアゴーグルと同じく、**ブロックを見ているときにだけ**情報を表示する。

- **その他挙動**
  - Mekanism モジュールの常と同様に、
    - 有効 / 無効のトグル（キーバインド）
    - エネルギー消費（常時少量 / 情報表示時のみなど）
  - などを設定可能にする。

---

### 4. Mekanism 側設計（MekaSuit モジュール）

#### 4.1 モジュール構造（イメージ）

- **新規アイテム**
  - `GogglesUnitItem`（仮称）
    - MekaSuit ヘルメットに装着できるモジュール用アイテム。
    - クラフトレシピで作成。
    - レア度やツールチップに「Create のエンジニアゴーグル機能を MekaSuit に追加する」という説明。

- **モジュールデータクラス**
  - `GogglesModuleData`（仮称）
    - Mekanism のモジュール API に沿った設定クラス。
    - 例:
      - エネルギー消費量
      - 有効 / 無効状態
      - 将来的な拡張用パラメータ（表示距離など）

- **モジュール実装クラス**
  - `ModuleGogglesUnit`（仮称）
    - Mekanism の既存モジュール（Vision Enhancement, Jetpack Unit など）を参考に実装。
    - MekaSuit ヘルメットへの装着専用とする。

#### 4.2 機能判定ヘルパー

- ユーティリティ関数（自作 MOD 内）:
  - `boolean hasGogglesModuleEquipped(Player player)`
    - プレイヤーの `EquipmentSlot.HEAD` から MekaSuit ヘルメットを取得。
    - モジュールコンテナを参照し、`ModuleGogglesUnit` が有効かどうか判定。
  - 他の HUD クラスから共通で利用できるようにしておく。

---

### 5. Create 側連携設計

#### 5.1 Create ゴーグルの仕組み（想定）

- Create 側には以下のような構造が存在する想定：
  - ゴーグルアイテム本体: `GogglesItem`
  - HUD レンダラ: `GoggleOverlayRenderer`（名称はバージョンで変動可）
  - 情報提供インターフェイス:
    - `IHaveGoggleInformation`
    - `IHaveHoveringInformation`
  - 各種 Create ブロックがこれらインターフェイスを実装し、  
    「ゴーグルで見られたときに HUD に出したい情報」を提供する。

- HUD レンダラは概ね以下を行う：
  1. プレイヤーがゴーグルを装備しているか判定。
  2. レイキャストにより、プレイヤーが見ているブロック / ブロックエンティティを取得。
  3. 対象が `IHaveGoggleInformation` なら情報を問い合わせて HUD を描画。

#### 5.2 連携パターン

- **パターン A: Create の HUD レンダラを直接呼び出す**

  - 自作 MOD でクライアント側 HUD イベント（`RenderGuiOverlayEvent` 等）にフック。
  - 条件:
    - Create がロードされている。
    - プレイヤーが MekaSuit ヘルメットを装備しており、`GogglesModule` を有効化している。
  - 条件を満たす場合に、
    - Create の `GoggleOverlayRenderer` 相当を呼び出して、HUD 描画ロジックを再利用する。

  - **メリット**
    - Create 側の HUD ロジック・デザイン変更に追従しやすい。
    - 実装量が少なくて済む。
  - **デメリット**
    - Create の内部クラス／非公開 API に依存する場合、バージョンアップ耐性が弱くなる。

- **パターン B: 仕様互換の HUD を自前実装**

  - Create の `GoggleOverlayRenderer` を参考にしつつ、
    - `IHaveGoggleInformation` などのインターフェイスに対して情報問い合わせを行い、
    - HUD の描画は自前で実装。
  - 色分けや表示内容は [Engineer’s Goggles Wiki](https://github.com/Creators-of-Create/Create/wiki/Engineer%27s-Goggles) の仕様に従う。

  - **メリット**
    - Create の内部構造に依存しない＝壊れにくい。
    - ライセンス的にも「コードのコピー」ではなく仕様準拠の実装でクリーン。
  - **デメリット**
    - HUD 周りの実装量が増える。
    - Create 側で表示形式が変わっても自動追従しない。

- **現時点の方針（案）**
  - まずは **パターン B（自前 HUD 実装）** を基本とする。
    - 理由: 長期的な安定性とライセンス上のわかりやすさ。
  - 必要に応じて、Create の HUD から一部ユーティリティクラスのみ参照する程度に留める。

---

### 6. HUD レンダリングのざっくりフロー

#### 6.1 クライアントイベントフック

- クライアント専用クラス（例: `GogglesModuleHudRenderer`）を用意し、NeoForge/Forge のクライアントイベントに登録。
- 疑似コードイメージ:

public class GogglesModuleHudRenderer {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // Mekanism / Create が無ければ何もしない
        if (!ModList.get().isLoaded("mekanism")) return;
        if (!ModList.get().isLoaded("create")) return;

        // MekaSuit ヘルメット + GogglesModule を装備しているか
        if (!MekHooks.hasGogglesModuleEquipped(player)) return;

        // 実際の HUD 描画処理
        renderCreateStyleGogglesOverlay(mc, event.getGuiGraphics());
    }
}#### 6.2 HUD 描画処理（概要）

1. **視線の先のブロック / BlockEntity を取得**
   - RayTrace / Pick でターゲットを取得。
2. **ターゲットが Create の対象ブロックか判定**
   - BlockEntity が `IHaveGoggleInformation` を実装していれば情報を問い合わせ。
3. **取得した情報を整形し、HUD に描画**
   - テキストやバー、色分け（RPM に対応した色）を描画。
   - 既存 Create HUD のレイアウトを参考にする。

---

### 7. 注意点（ライセンス・互換性・性能）

- **ライセンス**
  - Create / Mekanism 共に OSS だが、ソースコードの直接コピーではなく、
    - 公開 API を利用する、
    - もしくは仕様を参考にした自前実装にする。
  - 各リポジトリの LICENSE を確認し、必要なら README にクレジット表記。

- **バージョン互換性**
  - Mekanism / Create のバージョンアップで API やクラス構造が変わる可能性が高い。
  - `gradle` でバージョンを固定し、更新時は自作 MOD 側も合わせてメンテナンスする前提。

- **性能**
  - 毎フレームのレイキャスト + 情報取得は負荷になり得るので、
    - 一定フレームごとの更新に抑える、
    - 直前の結果をキャッシュする、
  - などの最適化余地を意識しておく。

---

### 8. 今後の実装タスク（ToDo）

- **プロジェクト準備**
  - 新規 NeoForge MOD プロジェクトを `mekanism-create-goggles-unit` として作成。
  - Gradle で Mekanism / Create への依存関係を設定。
  - `mods.toml` / `neoforge.mods.toml` を作成し、MOD ID や依存関係を記述。

- **Mekanism モジュール実装**
  - `GogglesUnitItem` の追加（アイテム登録・モデル・翻訳キー）。
  - `GogglesModuleData` / `ModuleGogglesUnit` を実装し、MekaSuit ヘルメットに装着可能にする。
  - `hasGogglesModuleEquipped(Player)` ヘルパー実装。

- **Create 連携 / HUD**
  - クライアントイベントで HUD レンダラを登録。
  - ターゲットブロックのレイキャスト処理を実装。
  - `IHaveGoggleInformation` 等のインターフェイスを利用して情報取得。
  - Goggles HUD の描画ロジック実装（RPM 色分け・テキスト配置など）。

- **テストと調整**
  - Mekanism + Create + 本 MOD を同時に導入して動作確認。
  - MekaSuit ヘルメット + GogglesModule 装備時のみ HUD が出ることを確認。
  - エネルギー消費やオン／オフ挙動、HUD レイアウトの調整。