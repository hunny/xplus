-- DROP TABLE IF EXISTS Person;
CREATE TABLE IF NOT EXISTS Person (
  uuid VARCHAR(38) NOT NULL,
  version BIGINT, /* 版本控制 */
  dateCreated DATETIME, /* 创建时间 */
  lastUpdated DATETIME, /* 最后修改时间 */

  name VARCHAR(20), /* 名称 */
  age VARCHAR(20), /* 年龄 */
  nation VARCHAR(20), /* 民族 */
  address VARCHAR(200), /* 地址 */
  UNIQUE KEY uk_1(name),
  PRIMARY KEY (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;