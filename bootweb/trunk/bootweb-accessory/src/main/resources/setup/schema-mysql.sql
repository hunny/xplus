/* 公司 */
-- DROP TABLE IF EXISTS Company;
CREATE TABLE IF NOT EXISTS Company (
  id BIGINT NOT NULL AUTO_INCREMENT,
  version BIGINT, /* 版本控制 */
  dateCreated DATETIME, /* 创建时间 */
  lastUpdated DATETIME, /* 最后修改时间 */

  name VARCHAR(254) NOT NULL, /* 公司名称 */
  src VARCHAR(1024), /* 来源地址 */
  UNIQUE KEY uk_1(name),
  INDEX idx_1(name),
  INDEX idx_2(version),
  INDEX idx_3(dateCreated),
  INDEX idx_4(lastUpdated),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 联系方式 */
-- DROP TABLE IF EXISTS Contact;
CREATE TABLE IF NOT EXISTS Contact (
  id BIGINT NOT NULL AUTO_INCREMENT,
  version BIGINT, /* 版本控制 */
  dateCreated DATETIME, /* 创建时间 */
  lastUpdated DATETIME, /* 最后修改时间 */

  companyId BIGINT NOT NULL, /* 公司ID */
  name VARCHAR(254) NOT NULL, /* 联系人 */
  tel VARCHAR(254) NOT NULL, /* 电话 */
  address VARCHAR(254), /* 地址 */
  email VARCHAR(254), /* 邮件地址 */
  dest VARCHAR(254), /* 来源 */
  UNIQUE KEY uk_1(name),
  INDEX idx_1(name),
  INDEX idx_2(version),
  INDEX idx_3(dateCreated),
  INDEX idx_4(lastUpdated),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
