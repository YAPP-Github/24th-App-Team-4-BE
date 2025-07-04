## 🏗 Architecture Overview

### 🔶 Hexagonal Architecture

<br>

![final-pokit](https://github.com/user-attachments/assets/39c29485-1082-40fb-93f3-0d6c325fdf7d)

## 🔗 Module Dependency Flow

|        ->        | **Adapters** | **Application** | **Domain** | **Entry: Web** | **Entry: Batch** |
|------------------|--------------|-----------------|------------|----------------|------------------|
| **Adapters**     | -            | ✅               | ✅          | ❌              | ❌                |
| **Application**  | ❌            | -               | ✅          | ❌              | ❌                |
| **Domain**       | ❌            | ❌               | -          | ❌              | ❌                |
| **Entry: Web**   | ✅            | ✅               | ✅          | -              | ❌                |
| **Entry: Batch** | ✅            | ✅               | ✅          | ❌              | -                |

