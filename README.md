# GriefPreventionSizeLimiter
ワールドごとにGriefPreventionで作成可能な1つの保護の大きさの上限を設定するプラグインです。
保護の作成時および編集時に、上限値を超えたサイズになっている場合はキャンセルされます。

Paper 1.20.1で動作確認 (要 GriefPrevention)

---

## 導入
`plugins`フォルダにjarファイルをコピーした状態でサーバーを立ち上げてください。

設定ファイル`plugins/GriefPreventionSizeLimiter/config.yml`が作成されます。

## 設定変更

設定を書き換えたら、`/reloadGpLimiter`(または`/reloadgpl`)コマンドを用いると再読みされます。

(サーバーを再起動するか、`/reload`コマンドでプラグインを読み直してもよい)

設定例
```yaml
ClaimSizeLimits:
  world: -1
  world_nether: -1
  world_the_end: -1
  test_world: 1000
MessageOfClaimLimit: §d保護のサイズが大きすぎます！このワールドの1つの保護の大きさは最大{0}ブロックです。この保護の大きさ={1}ブロック
IgnoreIfAdminClaim: true
```
+ `ClaimSizeLimits`: ワールドごとの1つの保護の大きさの上限値(ブロック数)を整数で入力します。
    + `-1`だと上限値なしになります。
+ `MessageOfClaimLimit`: 上限値を超えた保護を取ろうとした際に表示されるメッセージです。
  + `{0}`はそのワールドの最大ブロック数、`{1}`は現在取ろうとしている保護のブロック数に置き換えられます。
+ `IgnoreIfAdminClaim`: `true`の場合、Admin保護で保護ブロック数上限を無視します。

## コマンド
+ `/reloadGpLimiter`(`/reloadgpl`): `config.yml`を再読み込みします。 