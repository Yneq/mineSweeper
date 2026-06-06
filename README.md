# 💣 經典踩地雷遊戲 (Minesweeper) - Java 視窗版

這是一個使用 Java Swing 框架開發的經典踩地雷遊戲。本專案嚴格遵循 **MVC (Model-View-Controller)** 軟體架構設計，落實物件導向程式設計 (OOP) 核心精神，並優化了多媒體音效反饋與跨平台部署體驗。

## 🌟 核心特色與技術亮點

* **物件導向與 MVC 架構**：
    * `Cell.java` (Model)：定義單一格子的資料狀態（如地雷、插旗、翻開、周圍雷數）。
    * `Board.java` (Model/Controller)：管理 9x9 地圖矩陣，負責地雷隨機分佈與核心邏輯計算。
    * `GameWindow.java` (View/Controller)：負責 Swing UI 畫面渲染、滑鼠事件監聽與排版對齊。
* **高效連鎖擴散演算法**：採用優化的 Flood Fill (遞迴擴散) 演算法。當點擊空白格子 (0) 時，會自動向外連鎖展開，並精準在數字邊界煞車，完美重現經典踩地雷體驗。
* **雙重安全資料同步**：修復了 View 端插旗與 Model 端狀態不同步的經典 BUG，確保遞迴水流能完美避開玩家標記的旗幟。
* **異步非阻塞音效系統**：使用 `javax.sound.sampled` 封裝 `SoundManager`，利用獨立守護執行緒 (Daemon Thread) 異步播放背景音樂、獲勝與失敗音效，不卡死 UI 主執行緒。
* **環境相容與跨平台**：成功打包為 `Runnable JAR`，相容於 Windows 11 (Java 11) 與 macOS (JavaLauncher)，具備「一次編寫，隨處執行」的特性。

## 🎮 遊戲音效清單

本遊戲包含豐富的多媒體音效反饋，執行遊戲時請確保以下音效檔案與 `.jar` 檔置於同一目錄：
* `background.wav`：遊戲進行時的背景音樂（無限循環播放）。
* `lose.wav`：不小心踩到地雷時的爆炸/失敗音效。
* `win.wav`：順利找出所有安全格子時的獲勝通關音效。

## 🚀 如何運行本遊戲

### 方法一：雙擊執行 (推薦)
1. 下載本專案釋出的 `Minesweeper.jar` 以及五個 `.wav` 音效檔案，並放置在**同一個資料夾**內。
2. **Windows**：確保已安裝 Java 11 以上環境，雙擊 `Minesweeper.jar` 即可啟動。（若開啟失敗，可使用 `Jarfix` 工具修復副檔名綁定）。
3. **macOS**：按住 `control` 鍵點擊右鍵選擇「打開」，或在「隱私權與安全性」中允許 `JavaLauncher` 強制打開。

### 方法二：終端機命令列啟動
打開終端機 (Terminal / CMD)，切換至檔案所在目錄，輸入以下指令：
```bash
java -jar Minesweeper.jar