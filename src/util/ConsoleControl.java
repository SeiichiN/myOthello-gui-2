/**
 * JavaでC,C++のsystem("cls");と同等の機能を実装する
 *   https://qiita.com/Bim/items/c0b5ab527d105bc63d6b
 *
 * Windows の場合
 *   var cc = new ConsoleControl("cmd", "/c", "cls");
 * Linux の場合
 *   var cc = new ConsoleControl("/bin/bash", "-c", "clear");
 */
package util;

import java.io.IOException;

/**
 * 使い方:
 *   このクラスを import して、任意の場所で
 *     ConsoleControl.clearConsole();
 *   とすればよい。
 *   PlatformUtils.java が必要。
 */
public class ConsoleControl {
    private ProcessBuilder pb;

    /**
     * ConsoleControlクラスのコンストラクタです。
     * 指定したコマンドを実行する新しいプロセスを実行する環境を構築します。
     * @param command 実行するコマンド
     */
    public ConsoleControl(String... command) {
        pb = new ProcessBuilder(command);
    }

    /**
     * コマンドプロンプトの画面をクリアするメソッド。
     */
    public void cls() throws IOException, InterruptedException {
        pb.inheritIO().start().waitFor();
        /*
         * // ProcesserBuildのコンストラクタ引数で指定した外部コマンドを
         * // コマンドプロンプトで実行できるように変換
         * ProcessBuilder pbInheritIO = pb.inheritIO();
         * // 外部コマンドで実行
         * Process pro = pbInheritIO.start();
         * // 他のスレッドで動いているプロセスが終わるまで待機
         * pro.waitFor();
         */
    }

    /**
     * 画面クリア
     */
    public static void clearConsole() {
        try {
            ConsoleControl cc = null;
            PlatformUtils pu = new PlatformUtils();
            if (pu.isLinux()) {
                cc = new ConsoleControl("/bin/bash", "-c", "clear");  // for Linux
            } else if (pu.isWindows()) {
                cc = new ConsoleControl("cmd", "/c", "cls");   // for Win
            }
            cc.cls();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (InterruptedException re) {
            re.printStackTrace();
        }
    }    

}
