---

```markdown
# ❄️ snowFlakeUtilProject

基於 [Hutool](https://hutool.cn/) 實作的 **雪花片編碼工具專案**，以「建立商品（Product）」為範例，展示如何在 Spring Boot 專案中整合 **Snowflake ID 生成器**。

---

## 📌 專案簡介

此專案示範如何使用 Hutool 提供的 `Snowflake` 工具來生成分散式唯一 ID，並應用於建立商品時的 ID 指派邏輯中。

---

## 🧰 使用技術

- Java 17
- Spring Boot 3.x
- [Hutool](https://hutool.cn/) 工具庫
- Snowflake ID 生成演算法

---

## ❄️ Snowflake ID 是什麼？

Snowflake 是由 Twitter 提出的一種分散式 ID 生成演算法，其特點為：

- 全球唯一、不重複
- 單調遞增（但非嚴格）
- 避免資料庫自增 ID 瓶頸
- 適合微服務與高併發場景

---


## ✨ 使用範例

```java
// 建立 Snowflake 實例（可放在工具類中）
Snowflake snowflake = IdUtil.getSnowflake(1, 1); // workerId, datacenterId

// 生成唯一 ID
long id = snowflake.nextId();

// 使用於建立商品時
Product product = new Product();
product.setId(id);
product.setName("雪花生成測試商品");
````

---

## 🚀 如何啟動

```bash
# 1. clone 專案
git clone https://github.com/kevin771203/snowFlakeUtilProject.git

# 2. 使用 IntelliJ 或其他 IDE 開啟並執行 Spring Boot 專案
```

---


