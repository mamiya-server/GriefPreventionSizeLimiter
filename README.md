# GriefPreventionSizeLimiter
ワールドごと、およびワールド内の長方形のエリアごとにGriefPreventionで作成可能な1つの保護の大きさの上限を設定するプラグインです。

保護の作成時および編集時に、上限値を超えたサイズになっている場合はキャンセルされます。

エリアごとに上限を指定した場合は、北西側の端の座標がエリア内にあるかどうかで判別されます。

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

ClaimRuleZone:
  - World: world
    X1: -100
    X2: 100
    Z1: -100
    Z2: 100
    ClaimSizeLimit: 300
    MessageOfClaimLimit: §d保護のサイズが大きすぎます！X,Zが-100～100のエリア内では最大{0}ブロックまでの保護を新規作成可能です。後から拡張が必要になったら保護地を拡げてください。この保護の大きさ={1}ブロック
    DisableOnResized: true
  - World: world
    X1: -100
    X2: 100
    Z1: -100
    Z2: 100
    ClaimSizeLimit: 1000
    MessageOfClaimLimit: §d保護のサイズが大きすぎます！X,Zが-100～100のエリア内では最大{0}ブロックです。この保護の大きさ={1}ブロック
  - World: test_world
    X1: 100
    X2: 400
    Z1: -100
    Z2: -300
    ClaimSizeLimit: 200
    MessageOfClaimLimit: §d保護のサイズが大きすぎます！この区画内の1つの保護の大きさは最大{0}ブロックです。この保護の大きさ={1}ブロック
```
+ `ClaimSizeLimits`: ワールドごとの1つの保護の大きさの上限値(ブロック数)を整数で入力します。
    + `-1`だと上限値なしになります。
+ `MessageOfClaimLimit`: 上限値を超えた保護を取ろうとした際に表示されるメッセージです。
  + `{0}`はそのワールドの最大ブロック数、`{1}`は現在取ろうとしている保護のブロック数に置き換えられます。
+ `IgnoreIfAdminClaim`: `true`の場合、Admin保護で保護ブロック数上限を無視します。
+ `ClaimRuleZone`: ワールド内の一部のエリアで保護の面積上限を設ける場合に設定します。全パラメータが正しく設定されていないと、認識されません。
  + `World`: エリアの属するワールド名(必須)
  + `X1`: 片方の角のx座標(必須)
  + `X2`: 反対側の角のx座標(必須)
  + `Z1`: 片方の角のz座標(必須)
  + `Z2`: 反対側の角のz座標(必須)
  + `ClaimSizeLimit`: このエリア内の1つの保護の大きさの上限値(ブロック数)を整数で指定(必須)
  + `MessageOfClaimLimit`: 上限値を超えた保護を取ろうとした際に表示されるメッセージ(オプション)
    + `{0}`はそのワールドの最大ブロック数、`{1}`は現在取ろうとしている保護のブロック数に置き換えられます。
  + `DisableOnResized`: trueであれば、保護リサイズ時には制限を無効化(オプション)
  + `DisableOnCreated`: trueであれば、新規保護作成時には制限を無効化(オプション)

※ `ClaimRuleZone`で指定したルールは上から順にチェックされ、抵触した時点でそのルールのメッセージとともに保護作成/編集がキャンセルされます。

※ パラメータ名は大文字小文字の設定ミスにご注意ください。

## コマンド
+ `/reloadGpLimiter`(`/reloadgpl`): `config.yml`を再読み込みします。 

