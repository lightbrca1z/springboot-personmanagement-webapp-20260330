-- people: email 列の整備・監査列（created_at / updated_at）の追加（MySQL 8 想定）
-- 適用前にバックアップを取得してください。
-- アプリ起動時の Flyway（V1）で同内容を自動適用できるため、手動は任意です。

-- -----------------------------------------------------------------------------
-- 1) 既存 DB（JPA 移行前の古い列名が残っている場合のみ）
-- -----------------------------------------------------------------------------

-- 旧列名から email へ（既に email のみの場合はスキップ）
ALTER TABLE people CHANGE COLUMN mail email VARCHAR(200) NOT NULL;

-- 日時列の追加（既存行は登録実行時刻で初期化）
ALTER TABLE people
  ADD COLUMN created_at DATETIME(6) NOT NULL DEFAULT (CURRENT_TIMESTAMP(6)),
  ADD COLUMN updated_at DATETIME(6) NOT NULL DEFAULT (CURRENT_TIMESTAMP(6));

-- -----------------------------------------------------------------------------
-- 2) 新規 CREATE（参考: app_users 等は別途作成済みであること）
-- -----------------------------------------------------------------------------
/*
CREATE TABLE people (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(200) NOT NULL,
  phone VARCHAR(32) NULL,
  age INT NULL,
  memo VARCHAR(255) NULL,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_people_user FOREIGN KEY (user_id) REFERENCES app_users (id)
);
*/
